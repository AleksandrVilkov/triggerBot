package com.bot.bot.gpt;


import com.bot.bot.ChatBotable;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class ChatBot implements ChatBotable {
    @Value("${chatBot.key}")
    private String key;

    @Override
    public String sendRequest(String text) {
        String SPACE = "%20";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://ai-chatbot.p.rapidapi.com/chat/free?message="
                        + text.replaceAll(" ", SPACE) + "&uid=user1"))
                .header("X-RapidAPI-Key", key)
                .header("X-RapidAPI-Host", "ai-chatbot.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return "";
        }
        JSONObject jsonObject = new JSONObject(response.body());
        if (jsonObject.has("chatbot")) {
            JSONObject chatbot = jsonObject.optJSONObject("chatbot");
            return chatbot.getString("response");
        }
        return "";
    }
}
