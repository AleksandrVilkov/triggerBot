package com.bot.bot;

import com.bot.bot.model.Source;
import com.bot.bot.model.Target;

public interface Translatorable {
    String getTranslate(Target target, Source source, String text);
}
