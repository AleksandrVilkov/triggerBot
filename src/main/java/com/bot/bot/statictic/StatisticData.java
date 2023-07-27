package com.bot.bot.statictic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticData {
    private String name;
    private int allCountMsgs;
    private int numberOfTarget;
    private long chars;
}
