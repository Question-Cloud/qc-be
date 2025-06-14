package com.eager.questioncloud.entity

import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import org.springframework.data.domain.Persistable

@MappedSuperclass
abstract class BaseCustomIdEntity<T>(@Transient private var isNewEntity: Boolean) : Persistable<T> {

    abstract override fun getId(): T

    override fun isNew(): Boolean {
        return isNewEntity
    }

    @PostPersist
    @PostLoad
    fun markNotNew() {
        this.isNewEntity = false
    }
}