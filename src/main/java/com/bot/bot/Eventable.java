package com.bot.bot;

import com.bot.bot.model.Response;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Eventable {
    Response handle(Update update);
}
