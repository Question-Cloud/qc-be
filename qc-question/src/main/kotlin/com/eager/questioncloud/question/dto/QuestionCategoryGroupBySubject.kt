package com.eager.questioncloud.question.dto

import com.eager.questioncloud.question.enums.Subject
import java.util.stream.Collectors

class QuestionCategoryGroupBySubject(
    val subject: Subject,
    val list: List<MainQuestionCategory>,
) {
    class MainQuestionCategory(
        val title: String,
        val subject: Subject,
        val sub: List<SubQuestionCategory>,
    )

    class SubQuestionCategory(
        val id: Long,
        val title: String
    )

    companion object {
        @JvmStatic
        fun create(mainQuestionCategoryList: List<MainQuestionCategory>): List<QuestionCategoryGroupBySubject> {
            val result: MutableList<QuestionCategoryGroupBySubject> = ArrayList()
            mainQuestionCategoryList
                .stream()
                .collect(Collectors.groupingBy { mainQuestionCategory: MainQuestionCategory -> mainQuestionCategory.subject })
                .forEach { (subject: Subject, items: List<MainQuestionCategory>) ->
                    result.add(QuestionCategoryGroupBySubject(subject, items))
                }

            result.sortWith(Comparator.comparing { data -> data.subject.value })
            return result
        }
    }
}
