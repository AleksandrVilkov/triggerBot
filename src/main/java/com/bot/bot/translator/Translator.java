package com.bot.bot.translator;


import com.bot.bot.Translatorable;
import com.bot.bot.model.Source;
import com.bot.bot.model.Target;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
@Slf4j
public class Translator implements Translatorable {
    @Value("${chatBot.key}")
    private String key;

    @Override
    public String getTranslate(Target target, Source source, String text) {
        String SPACE = "%20";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://text-translator2.p.rapidapi.com/translate"))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("X-RapidAPI-Key", key)
                .header("X-RapidAPI-Host", "text-translator2.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString("source_language="
                        + source.toString().toLowerCase() + "&target_language="
                        + target.toString().toLowerCase() + "&text=" + text.replaceAll(" ", SPACE)))
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            log.error(e.getMessage());
            return "";
        }
        JSONObject jsonObject = new JSONObject((response.body()));
        JSONObject data = jsonObject.optJSONObject("data");
        return data.optString("translatedText");
    }
}
