package com.bot.bot;

import com.bot.bot.statictic.StatisticGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class Utils {

    public static String statisticGroupSerialize(StatisticGroup statisticGroup) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(statisticGroup);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static StatisticGroup statisticGroupDeserialize(String jsonInput) {
        if (jsonInput == null) {
            return new StatisticGroup();
        }
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<StatisticGroup> typeRef = new TypeReference<>() {};
        try {
            return mapper.readValue(jsonInput, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUserName(Update update) {
        return update.getMessage().getFrom().getUserName() != null ? update.getMessage().getFrom().getUserName() :
                update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();
    }
    public static <T> T getRandomElement(List<T> list) {
        Random random = new Random();
        int randomIndex = random.nextInt(list.size());
        return list.get(randomIndex);
    }
    public static String getMsgText(Update update) {
        String text = update.getMessage().getText().toLowerCase();
        Pattern pattern = Pattern.compile("[^А-Яа-яa-zA-Z\\s]", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        text = pattern.matcher(text).replaceAll("");
        return text;
    }
}
