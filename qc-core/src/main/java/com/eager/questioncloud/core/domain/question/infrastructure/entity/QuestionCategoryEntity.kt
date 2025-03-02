package com.eager.questioncloud.core.domain.question.infrastructure.entity

import com.eager.questioncloud.core.domain.question.enums.Subject
import com.eager.questioncloud.core.domain.question.model.QuestionCategory
import jakarta.persistence.*

@Entity
@Table(name = "question_category")
class QuestionCategoryEntity private constructor(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long?,
    @Column var parentId: Long,
    @Enumerated(EnumType.STRING) @Column var subject: Subject,
    @Column var title: String,
    @Column var isParent: Boolean
) {
    fun toModel(): QuestionCategory {
        return QuestionCategory(id, parentId, subject, title, isParent)
    }
}
