package com.bot.bot.events;

import com.bot.bot.Eventable;
import com.bot.bot.Statisticable;
import com.bot.bot.TempStorage;
import com.bot.bot.Utils;
import com.bot.bot.model.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@Component
@Slf4j
public class SendCommand implements Eventable {
    @Autowired
    private TempStorage tempStorage;
    @Autowired
    private Statisticable statistic;
    @Value("${text.hello}")
    private String helloText;

    @Override
    public Response handle(Update update) {
        Response response = new Response();

        if (update.getMessage().hasEntities()) {
            update.getMessage().getEntities().forEach(messageEntity -> {
                String command = messageEntity.getText();
                SendMessage sendMessage = null;
                switch (command) {
                    case "/stat@igorPetuch_bot", "/stat" -> sendMessage = sendStatistic(update);
                    case "/start@igorPetuch_bot", "/start" -> sendMessage = sendStart(update);
                }
                response.setMessage(sendMessage);
            });
        }
        return response;
    }

    private SendMessage sendStatistic(Update update) {
        String text = statistic.getStatistic(update);
        String chatID = String.valueOf(update.getMessage().getChatId());
        log.info("user " + Utils.getUserName(update) + "start statistic command in chat: " + chatID);
        log.info("statistic will be send: " + text);
        return new SendMessage(chatID, text);
    }

    private SendMessage sendStart(Update update) {
        String chat = String.valueOf(update.getMessage().getChatId());
        log.info("user " + Utils.getUserName(update) + "start start command in chat: " + chat);
        return new SendMessage(chat, helloText);
    }
}
