package com.bot.bot.statictic;

import com.bot.bot.Statisticable;
import com.bot.bot.TempStorage;
import com.bot.bot.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class Statistic implements Statisticable {
    @Autowired
    private TempStorage tempStorage;
    @Value("${text.emptyStatisticText}")
    private String emptyStatisticText;
    @Value("${text.startedStatisticText}")
    private String startedStatisticText;
    @Value("${text.winner}")
    private String winner;
    @Value("${text.statisticRules}")
    private String statisticRules;
    @Value("${text.congratulation}")
    private String congratulation;

    @Override
    public void incrementTotalMessages(Update update) {
        if (update.getMessage() == null) {
            return;
        }

        int countMinLength = 3;
        String text = update.getMessage().getText();
        String userName = Utils.getUserName(update);

        if (text == null || text.length() < countMinLength) {
            log.warn("length of message from user " + userName + "less than " + countMinLength + ". Message text: " + text);
            return;
        }

        String chatId = String.valueOf(update.getMessage().getChatId());
        String userId = update.getMessage().getFrom().getId().toString();
        String stringUserData = tempStorage.get(chatId);
        log.info("User data for user " + userName + " was received: " + stringUserData);

        Map<String, StatisticData> usersStatistic;
        if (stringUserData == null) {
            log.info("User data is null, create new user data...");
            Map<String, StatisticData> data = new HashMap<>();
            data.put(userId, new StatisticData(userName, 1, 0, text.length()));
            usersStatistic = data;
        } else {
            usersStatistic = Utils.statisticGroupDeserialize(stringUserData).getUsersStatistic();
            if (usersStatistic.get(userId) == null) {
                log.info("User statistic is null, create new user statistic for user " + userName + "...");
                usersStatistic.put(userId, new StatisticData(userName, 0, 0, text.length()));
            }
        }
        int all = usersStatistic.get(userId).getAllCountMsgs() + 1;
        long allChars = usersStatistic.get(userId).getChars() + text.length();
        usersStatistic.get(userId).setAllCountMsgs(all);
        usersStatistic.get(userId).setChars(allChars);
        StatisticGroup statisticGroup = new StatisticGroup();
        statisticGroup.setUsersStatistic(usersStatistic);
        tempStorage.set(chatId, Utils.statisticGroupSerialize(statisticGroup));

        log.info("Total count message and count of chars for user " + userName + "was updated. Plus 1 message, plus " + text.length() + " chars.");
    }

    @Override
    public void incrementTargetMessages(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        String userId = update.getMessage().getFrom().getId().toString();
        String userName = Utils.getUserName(update);
        String stringUserData = tempStorage.get(chatId);

        Map<String, StatisticData> usersStatistic;
        if (stringUserData == null) {
            log.info("User data is null, create new user data...");
            Map<String, StatisticData> data = new HashMap<>();
            data.put(userId, new StatisticData(userName, 1, 0, 0));
            usersStatistic = data;
        } else {
            usersStatistic = Utils.statisticGroupDeserialize(stringUserData).getUsersStatistic();
            if (usersStatistic.get(userId) == null) {
                log.info("User statistic is null, create new user statistic for user " + userName + "...");
                usersStatistic.put(userId, new StatisticData(userName, 0, 0, 0));
            }
        }
        int numberOfTarget = usersStatistic.get(userId).getNumberOfTarget() + 1;
        usersStatistic.get(userId).setNumberOfTarget(numberOfTarget);
        StatisticGroup statisticGroup = new StatisticGroup();
        statisticGroup.setUsersStatistic(usersStatistic);
        tempStorage.set(chatId, Utils.statisticGroupSerialize(statisticGroup));
        log.info("Count of target message for user " + userName + "was updated. Plus 1 message.");
    }

    @Override
    public String getStatistic(Update update) {
        String chatID = String.valueOf(update.getMessage().getChatId());
        StatisticGroup statisticGroup = Utils.statisticGroupDeserialize(tempStorage.get(chatID));
        StringBuilder stringBuilder = new StringBuilder();

        double maxK = 0;
        String maxName = "";
        for (Map.Entry<String, StatisticData> entry : statisticGroup.getUsersStatistic().entrySet()) {
            StatisticData statisticData = entry.getValue();

            if (statisticData.getNumberOfTarget() > 0) {
                if (stringBuilder.isEmpty()) {
                    stringBuilder.append(startedStatisticText).append(":\n");
                }
                double middleLengthWord = (double) statisticData.getChars() / statisticData.getAllCountMsgs();
                double k = (double) statisticData.getNumberOfTarget() / middleLengthWord;
                stringBuilder.append(statisticData.getName()).append(" - ")
                        .append(statisticData.getNumberOfTarget())
                        .append(" из ").append(statisticData.getAllCountMsgs())
                        .append(" (коэфф: ").append(String.format("%.2f", k)).append(")").append("\n");
                if (k > maxK) {
                    maxK = k;
                    maxName = entry.getValue().getName();
                }
            }
        }
        if (maxK == 0) {
            stringBuilder.append(emptyStatisticText);
        } else {
            stringBuilder.append(winner).append(maxName).append("\n").append(congratulation);
        }
        stringBuilder.append("\n\n*").append(statisticRules);
        return stringBuilder.toString();
    }
}
