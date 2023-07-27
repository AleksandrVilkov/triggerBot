package com.bot.bot.events;

import com.bot.bot.Eventable;
import com.bot.bot.model.Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@Component
public class ErrorEvent implements Eventable {
    @Value("${text.error}")
    private String textError;

    @Override
    public Response handle(Update update) {
        String chatId = String.valueOf(update.getMessage().getChatId());
        String msgId = update.getMessage().getMessageId().toString();
        SendMessage sendMessage = new SendMessage(chatId, textError);
        sendMessage.setReplyToMessageId(Integer.valueOf(msgId));

        Response response = new Response();
        response.setMessage(sendMessage);
        return response;
    }
}
