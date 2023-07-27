package com.bot;

import com.bot.bot.Bot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@Slf4j
public class Main {

    public static void main(String[] args) {

        log.info("Let's run this shitcode....");
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        log.info("Spring app was run.");
        Bot bot = (Bot) context.getBean("bot");
        log.info("Got a bot from the context. Great!");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
            log.info("The bot has been successfully registered, let's start working!");
        } catch (TelegramApiException e) {
            log.error("SHIT! " + e);
        }
    }
}