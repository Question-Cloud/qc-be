package com.eager.questioncloud.post.api.internal

import com.eager.questioncloud.common.pagination.PagingInformation
import com.eager.questioncloud.post.api.CreatorPostQueryAPIResult

interface PostQueryAPI {
    fun getCreatorPosts(questionIds: List<Long>, pagingInformation: PagingInformation): List<CreatorPostQueryAPIResult>

    fun countByQuestionIdIn(questionIds: List<Long>): Int
}