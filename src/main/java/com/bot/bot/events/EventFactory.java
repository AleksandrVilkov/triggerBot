package com.bot.bot.events;

import com.bot.bot.Eventable;
import com.bot.bot.Utils;
import com.bot.bot.model.DataWrapper;
import com.bot.bot.model.TriggerData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@Component
@NoArgsConstructor
public class EventFactory {
    @Autowired
    private BotAnswered botAnswered;
    @Autowired
    private MessageIsTrigger messageIsTrigger;
    @Autowired
    private ErrorEvent errorEvent;
    @Autowired
    private SendCommand sendCommand;
    @Autowired
    private BotWasTagged botWasTagged;
    @Autowired
    private ContainsTrigger containsTrigger;
    @Autowired
    private DataWrapper dataWrapper;
    @Value("${bot.name}")
    private String botName;

    public Eventable getAction(Update update) {

        //Боту ответили.
        if (update.getMessage().getReplyToMessage() != null
                && update.getMessage().getReplyToMessage().getFrom().getFirstName().equalsIgnoreCase(botName)) {
            return botAnswered;
        }

        //Сообщение - триггер
        if (textIsTrigger(update)) {
            return messageIsTrigger;
        }
        //бота тегнули
        if (isBotTagged(update)) {
            return botWasTagged;
        }
        //Сообщение содержит триггер
        if (msgContainsTrigger(update)) {
            return containsTrigger;
        }
        //Команда
        if (isCommand(update)) {
            return sendCommand;
        }
        return null;
    }

    private boolean msgContainsTrigger(Update update) {
        String text = Utils.getMsgText(update);
        for (TriggerData data : dataWrapper.getData().getContainsTrigger()) {
            for (String key : data.getKeys()) {
                if (text.toLowerCase().contains(key.toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBotTagged(Update update) {
        return update.hasMessage() && update.getMessage().getText() != null && update.getMessage().hasEntities() &&
                update.getMessage().getEntities().stream().anyMatch(e -> e.getType().equalsIgnoreCase("mention"));
    }

    private boolean isCommand(Update update) {
        return update.hasMessage() && update.getMessage().getText() != null && update.getMessage().hasEntities() &&
                update.getMessage().getEntities().stream().anyMatch(e -> e.getType().equalsIgnoreCase("bot_command"));
    }

    private boolean textIsTrigger(Update update) {
        String text = Utils.getMsgText(update);
        for (TriggerData data : dataWrapper.getData().getFullTrigger()) {
            for (String key : data.getKeys()) {
                if (text.equalsIgnoreCase(key)) {
                    return true;
                }
            }
        }
        return false;
    }
}
