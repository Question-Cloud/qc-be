package com.eager.questioncloud.core.common


class PagingInformation(
    val offset: Int,
    val size: Int,
) {
    val page: Int
        get() = offset / size
}
