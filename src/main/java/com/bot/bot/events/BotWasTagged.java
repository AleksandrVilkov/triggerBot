package com.bot.bot.events;

import com.bot.bot.Eventable;
import com.bot.bot.Utils;
import com.bot.bot.model.Response;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@Component
@Slf4j
public class BotWasTagged implements Eventable {
    @Value("${text.tagReaction}")
    private String tagReaction;
    @Value("${bot.tagName}")
    private String botTagName;

    @Override
    public Response handle(Update update) {
        if (update.getMessage().getText().toLowerCase().contains(botTagName.toLowerCase())) {
            log.info("Bot was tagged by " + Utils.getUserName(update));
            String chatId = String.valueOf(update.getMessage().getChatId());
            String msgId = update.getMessage().getMessageId().toString();
            SendMessage sendMessage = new SendMessage(chatId, tagReaction);
            sendMessage.setReplyToMessageId(Integer.valueOf(msgId));

            Response response = new Response();
            response.setMessage(sendMessage);
            return response;
        }
        return new Response();
    }
}
