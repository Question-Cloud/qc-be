package com.eager.questioncloud.core.common


class PagingInformation(
    val offset: Int,
    val size: Int,
) {
    val page: Int
        get() = offset / size

    companion object {
        val max = PagingInformation(0, Int.MAX_VALUE)
    }
}
