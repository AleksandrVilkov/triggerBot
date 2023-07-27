package com.bot.bot;

public interface TempStorage {
    String get(String key);

    void set(String key, String data);
}
