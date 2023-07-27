package com.bot.bot;

import com.bot.bot.events.EventFactory;
import com.bot.bot.model.DataWrapper;
import com.bot.bot.model.Response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private BotConfig config;
    @Autowired
    private DataWrapper rhymes;
    @Autowired
    private Statisticable statistic;
    @Autowired
    private EventFactory eventFactory;

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("received new update");
        statistic.incrementTotalMessages(update);
        Eventable event = eventFactory.getAction(update);
        if (event == null) {
            return;
        }
        Response response = event.handle(update);
        try {
            if (response.getMessage() != null) {
                log.info("sending message...");
                execute(response.getMessage());
            }
            if (response.getSendAnimation() != null) {
                log.info("sending gif...");
                execute(response.getSendAnimation());
            }

            if (response.getSticker() != null) {
                log.info("sending sticker...");
                execute(response.getSticker());
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Getter
    @Setter
    @Component
    class BotConfig {
        @Value("${bot.token}")
        private String token;
        @Value("${bot.name}")
        private String name;
    }
}

