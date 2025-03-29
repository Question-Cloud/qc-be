package com.eager.questioncloud.core.domain.post.model

class PostContent(
    var title: String,
    var content: String,
    var files: List<PostFile>
) {
    companion object {
        fun create(title: String, content: String, files: List<PostFile>): PostContent {
            return PostContent(title, content, files)
        }
    }
}
