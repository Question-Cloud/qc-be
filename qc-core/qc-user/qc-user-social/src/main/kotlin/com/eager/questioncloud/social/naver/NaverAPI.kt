package com.eager.questioncloud.social.naver

import com.eager.questioncloud.common.http.HttpClient
import com.eager.questioncloud.common.http.HttpRequest
import com.eager.questioncloud.social.SocialAPI
import com.eager.questioncloud.social.SocialAccessToken
import com.eager.questioncloud.social.SocialPlatform
import com.eager.questioncloud.social.SocialUserInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NaverAPI(
    private val objectMapper: ObjectMapper,
    private val httpClient: HttpClient,
) : SocialAPI {
    @Value("\${NAVER_CLIENT_ID}")
    private lateinit var NAVER_CLIENT_ID: String
    
    @Value("\${NAVER_CLIENT_SECRET}")
    private lateinit var NAVER_CLIENT_SECRET: String
    
    override fun getAccessToken(code: String): String {
        val query = mutableMapOf<String, String>()
        query["grant_type"] = "authorization_code"
        query["client_id"] = NAVER_CLIENT_ID
        query["client_secret"] = NAVER_CLIENT_SECRET
        query["code"] = code
        
        val request = HttpRequest(url = "https://nid.naver.com/oauth2.0/token", query = query)
        val response = httpClient.get(request, SocialAccessToken::class.java)
        return response.access_token
    }
    
    override fun getUserInfo(accessToken: String): SocialUserInfo {
        val headers = mapOf("Authorization" to "Bearer $accessToken")
        val request = HttpRequest("https://openapi.naver.com/v1/nid/me", headers = headers)
        val response = httpClient.get(request, NaverUserInfoAPIResponse::class.java).response
        
        return SocialUserInfo(
            response.id,
            response.email,
            response.nickname,
            SocialPlatform.NAVER
        )
    }
    
    override fun getSocialPlatform(): SocialPlatform {
        return SocialPlatform.NAVER
    }
}

private data class NaverUserInfoAPIResponse(val response: NaverUserInfo)

private data class NaverUserInfo(
    val id: String,
    val email: String,
    val nickname: String,
    val mobile: String,
    val profile_image: String
)
