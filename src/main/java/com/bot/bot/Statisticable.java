package com.bot.bot;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Statisticable  {
    void incrementTotalMessages(Update update);
    void incrementTargetMessages(Update update);
    String getStatistic(Update update);
}
