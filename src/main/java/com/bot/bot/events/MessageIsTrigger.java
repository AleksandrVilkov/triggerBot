package com.bot.bot.events;

import com.bot.bot.Eventable;
import com.bot.bot.Statisticable;
import com.bot.bot.Utils;
import com.bot.bot.model.DataWrapper;
import com.bot.bot.model.Response;
import com.bot.bot.model.TriggerData;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@Component
@Slf4j
public class MessageIsTrigger implements Eventable {
    @Autowired
    private DataWrapper dataWrapper;
    @Autowired
    private Statisticable statistic;

    @Override
    public Response handle(Update update) {
        String msgId = update.getMessage().getMessageId().toString();
        String chatId = String.valueOf(update.getMessage().getChatId());
        Response response = new Response();
        String text = Utils.getMsgText(update);
        log.info("Message in chat " + chatId + " is trigger word. Message: " + text + "; user: " + Utils.getUserName(update));
        for (TriggerData data : dataWrapper.getData().getFullTrigger()) {
            for (String key : data.getKeys()) {
                if (key.equalsIgnoreCase(text)) {
                    if (data.getGifData() != null && !data.getGifData().isEmpty()) {
                        SendAnimation sendAnimation = new SendAnimation();
                        sendAnimation.setAnimation(Utils.getRandomElement(data.getGifData()));
                        sendAnimation.setChatId(chatId);
                        sendAnimation.setReplyToMessageId(Integer.valueOf(msgId));
                        log.info("gif was picked up for response.");
                        response.setSendAnimation(sendAnimation);

                    } else if (data.getTextData() != null && !data.getTextData().isEmpty()) {
                        String responseText = Utils.getRandomElement(data.getTextData());
                        SendMessage sendMessage = new SendMessage(chatId, responseText);
                        sendMessage.setReplyToMessageId(Integer.valueOf(msgId));

                        log.info("response text was picked up for response: " + responseText);
                        response.setMessage(sendMessage);
                    }
                }
            }
        }
        statistic.incrementTargetMessages(update);
        return response;
    }
}
