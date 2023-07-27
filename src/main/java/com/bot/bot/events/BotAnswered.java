package com.bot.bot.events;

import com.bot.bot.ChatBotable;
import com.bot.bot.Eventable;
import com.bot.bot.Translatorable;
import com.bot.bot.Utils;
import com.bot.bot.model.Response;
import com.bot.bot.model.Source;
import com.bot.bot.model.Target;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Slf4j
public class BotAnswered implements Eventable {
    @Autowired
    private Translatorable translator;
    @Autowired
    private ChatBotable chatbot;
    @Value("${text.error}")
    private String textError;

    @Override
    public Response handle(Update update) {
        log.info("processing the response to the bot for user: " + Utils.getUserName(update) + "...");
        String text = update.getMessage().getText();
        log.info("answer is: " + text + ". Translate to EN");
        String translate = translator.getTranslate(Target.EN, Source.RU, text);
        log.info("translation result: " + translate);
        String chat = chatbot.sendRequest(translate);
        log.info("response from chat bot: " + chat);
        String resp = chat == null || chat.isEmpty() ? textError :
                translator.getTranslate(Target.RU, Source.EN, chat);
        log.info("prepared response: " + resp);
        String chatId = String.valueOf(update.getMessage().getChatId());
        String msgId = update.getMessage().getMessageId().toString();
        SendMessage sendMessage = new SendMessage(chatId, resp);
        sendMessage.setReplyToMessageId(Integer.valueOf(msgId));

        Response response = new Response();
        response.setMessage(sendMessage);
        return response;
    }
}
