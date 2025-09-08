package com.eager.questioncloud.post.converter

import com.eager.questioncloud.post.domain.PostFile
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.AttributeConverter
import org.springframework.stereotype.Component
import java.util.*

@Component
class PostFileConverter(
    private val objectMapper: ObjectMapper
) : AttributeConverter<List<PostFile?>?, String> {
    
    override fun convertToDatabaseColumn(attribute: List<PostFile?>?): String? {
        if (attribute.isNullOrEmpty()) {
            return null
        }
        
        return runCatching { objectMapper.writeValueAsString(attribute) }.getOrNull()
    }
    
    override fun convertToEntityAttribute(dbData: String?): List<PostFile> {
        if (dbData.isNullOrEmpty()) {
            return emptyList()
        }
        
        return runCatching {
            objectMapper.readValue(dbData, Array<PostFile>::class.java).toList()
        }.getOrDefault(emptyList())
    }
}
