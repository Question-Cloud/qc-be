package com.eager.questioncloud.core.common

import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import org.springframework.data.domain.Persistable

@MappedSuperclass
abstract class BaseCustomIdEntity<T> : Persistable<T> {
    @Transient
    private var isNewEntity: Boolean = true

    abstract override fun getId(): T

    override fun isNew(): Boolean {
        return isNewEntity
    }

    @PrePersist
    fun unmarkWhenPersist() {
        this.isNewEntity = false
    }

    @PostLoad
    fun unmarkWhenLoad() {
        this.isNewEntity = false
    }
}