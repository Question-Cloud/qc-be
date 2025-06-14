package com.eager.questioncloud.post.domain

class PostContent(
    var title: String,
    var content: String,
    var files: List<PostFile>
) {
    companion object {
        fun create(
            title: String,
            content: String,
            files: List<PostFile>
        ): PostContent {
            return PostContent(title, content, files)
        }
    }
}
