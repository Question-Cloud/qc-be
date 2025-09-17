package com.eager.questioncloud.post.entity

import com.eager.questioncloud.post.converter.PostFileConverter
import com.eager.questioncloud.post.domain.PostContent
import com.eager.questioncloud.post.domain.PostFile
import jakarta.persistence.Convert
import jakarta.persistence.Embeddable

@Embeddable
class PostContentEntity private constructor(
    var title: String,
    var content: String,
    @Convert(converter = PostFileConverter::class)
    var files: List<PostFile>
) {
    fun toModel(): PostContent {
        return PostContent(title, content, files)
    }
    
    companion object {
        fun from(postContent: PostContent): PostContentEntity {
            return PostContentEntity(postContent.title, postContent.content, postContent.files)
        }
    }
}
