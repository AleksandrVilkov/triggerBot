package com.bot.bot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Data {
    @JsonProperty("fullTrigger")
    private List<TriggerData> fullTrigger;
    @JsonProperty("containsTrigger")
    private List<TriggerData> containsTrigger;
}
