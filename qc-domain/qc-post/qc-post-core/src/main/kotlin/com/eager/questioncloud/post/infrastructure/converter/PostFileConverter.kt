package com.eager.questioncloud.post.infrastructure.converter

import com.eager.questioncloud.post.domain.PostFile
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
