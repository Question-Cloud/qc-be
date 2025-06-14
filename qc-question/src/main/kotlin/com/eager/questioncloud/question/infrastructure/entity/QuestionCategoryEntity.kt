package com.eager.questioncloud.question.infrastructure.entity

import com.eager.questioncloud.question.domain.QuestionCategory
import com.eager.questioncloud.question.enums.Subject
import jakarta.persistence.*

@Entity
@Table(name = "question_category")
class QuestionCategoryEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Id var id: Long = 0,
    @Column var parentId: Long,
    @Enumerated(EnumType.STRING) @Column var subject: Subject,
    @Column var title: String,
    @Column var isParent: Boolean
) {
    fun toModel(): QuestionCategory {
        return QuestionCategory(id, parentId, subject, title, isParent)
    }
}
