package com.eager.questioncloud.storage.question.converter;

import com.eager.questioncloud.core.domain.hub.board.vo.QuestionBoardFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionBoardFileConverter implements AttributeConverter<List<QuestionBoardFile>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<QuestionBoardFile> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public List<QuestionBoardFile> convertToEntityAttribute(String dbData) {
        List<QuestionBoardFile> list = new ArrayList<>();
        if (dbData == null) {
            return list;
        }
        try {
            return Arrays.asList(objectMapper.readValue(dbData, QuestionBoardFile[].class));
        } catch (JsonProcessingException e) {
            return list;
        }
    }
}
