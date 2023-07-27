package com.bot.bot.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@Getter
@NoArgsConstructor
public class DataWrapper {
    private Data data;

    @Autowired
    public DataWrapper(@Value("${data}") String data) {
        String content;
        try {
            content = new String(Files.readAllBytes(Paths.get(data)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.data = dataDeserialize(content);
    }

    private Data dataDeserialize(String jsonInput) {
        if (jsonInput == null) {
            return new Data();
        }
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<Data> typeRef = new TypeReference<>() {
        };
        try {
            return mapper.readValue(jsonInput, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
