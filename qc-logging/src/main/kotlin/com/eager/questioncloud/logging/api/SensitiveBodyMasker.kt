package com.eager.questioncloud.logging.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

class SensitiveBodyMasker {
    companion object {
        private val sensitiveKeys: Set<String> = setOf("password", "newpassword", "accesstoken", "refreshtoken")
        private val maskValue: String = "***************"
        
        private val objectMapper: ObjectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        
        fun mask(body: String): String {
            if (body.isBlank()) {
                return body
            }
            
            return runCatching {
                val rootNode: JsonNode = objectMapper.readTree(body)
                
                if (rootNode.isObject) {
                    maskJsonNodeRecursively(rootNode)
                } else if (rootNode.isArray) {
                    maskJsonNodeRecursively(rootNode)
                }
                
                objectMapper.writeValueAsString(rootNode)
            }.getOrDefault("")
        }
        
        private fun maskJsonNodeRecursively(node: JsonNode) {
            when {
                node.isObject -> {
                    val objectNode = node as ObjectNode
                    val fieldNames = mutableListOf<String>()
                    objectNode.fieldNames().forEachRemaining { fieldNames.add(it) }
                    
                    for (fieldName in fieldNames) {
                        val fieldValue = objectNode.get(fieldName)
                        if (sensitiveKeys.contains(fieldName.lowercase())) {
                            objectNode.set<ObjectNode>(fieldName, TextNode(maskValue))
                        } else {
                            maskJsonNodeRecursively(fieldValue)
                        }
                    }
                }
                
                node.isArray -> {
                    for (arrayElement in node) {
                        maskJsonNodeRecursively(arrayElement)
                    }
                }
            }
        }
    }
}