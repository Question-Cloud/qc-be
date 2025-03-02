package com.eager.questioncloud.core.domain.post.infrastructure.converter

import com.eager.questioncloud.core.domain.post.model.PostFile
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import jakarta.persistence.AttributeConverter
import java.util.*

class PostFileConverter : AttributeConverter<List<PostFile?>?, String> {
    private val objectMapper = ObjectMapper().registerKotlinModule()

    override fun convertToDatabaseColumn(attribute: List<PostFile?>?): String? {
        if (attribute.isNullOrEmpty()) {
            return null
        }
        return try {
            objectMapper.writeValueAsString(attribute)
        } catch (e: JsonProcessingException) {
            null
        }
    }

    override fun convertToEntityAttribute(dbData: String?): List<PostFile> {
        return dbData?.let {
            try {
                objectMapper.readValue(dbData, Array<PostFile>::class.java).toList()
            } catch (e: JsonProcessingException) {
                emptyList()
            }
        } ?: emptyList()
    }
}
