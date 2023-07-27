package com.bot.bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TriggerData {
    @JsonProperty("keys")
    private List<String> keys;
    @JsonProperty("text")
    private List<String> textData;
    @JsonProperty("gif")
    private List<InputFile> gifData;
    @JsonProperty("photo")
    private List<String> photoData;
    @JsonProperty("sticker")
    private List<String> stickerData;

}