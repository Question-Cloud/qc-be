package com.eager.questioncloud.test.utils

import jakarta.annotation.PostConstruct
import jakarta.persistence.Entity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.Table
import jakarta.persistence.metamodel.EntityType
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DBCleaner(
    @PersistenceContext private val entityManager: EntityManager
) {
    private lateinit var tableNames: List<String>
    
    @PostConstruct
    fun resolveTableNames() {
        val entities: Set<EntityType<*>> = entityManager.metamodel.entities
        
        tableNames = entities
            .filter { entityType ->
                entityType.javaType?.getAnnotation(Entity::class.java) != null
            }
            .mapNotNull { entityType ->
                val tableAnnotation = entityType.javaType?.getAnnotation(Table::class.java)
                val explicitTableName = tableAnnotation?.name?.trim()
                if (explicitTableName.isNullOrBlank()) {
                    null
                } else {
                    explicitTableName
                }
            }
            .toList()
    }
    
    @Transactional
    fun cleanUp() {
        entityManager.flush()
        tableNames.forEach { tableName ->
            if (tableName != "question_category") {
                val truncateSql = "TRUNCATE TABLE `$tableName`"
                entityManager.createNativeQuery(truncateSql).executeUpdate()
            }
        }
    }
}