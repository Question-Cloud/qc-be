package com.eager.questioncloud.application.utils

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

    @Transactional // 정리 작업은 원자적으로 실행
    fun cleanUp() {
        entityManager.flush()
        tableNames.forEach { tableName ->
            val truncateSql = "TRUNCATE TABLE `$tableName`"
            try {
                entityManager.createNativeQuery(truncateSql).executeUpdate()
            } catch (e: Exception) {

            }
        }
    }
}