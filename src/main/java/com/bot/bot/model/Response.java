package com.bot.bot.model;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

@Data
public class Response {
    private SendMessage message;
    private SendAnimation sendAnimation;
    private SendSticker sticker;
}
