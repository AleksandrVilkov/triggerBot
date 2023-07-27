package com.bot.bot.statictic;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class StatisticGroup {
    private Map<String, StatisticData> usersStatistic;
}
