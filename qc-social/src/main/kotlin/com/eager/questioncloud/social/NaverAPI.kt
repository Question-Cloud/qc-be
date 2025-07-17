package com.eager.questioncloud.social

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class NaverAPI : SocialAPI {
    @Value("\${NAVER_CLIENT_ID}")
    private lateinit var NAVER_CLIENT_ID: String
    
    @Value("\${NAVER_CLIENT_SECRET}")
    private lateinit var NAVER_CLIENT_SECRET: String
    
    override fun getAccessToken(code: String): String {
        val request = Request.Builder()
            .url(
                "https://nid.naver.com/oauth2.0/token".toHttpUrl()
                    .newBuilder()
                    .addQueryParameter("grant_type", "authorization_code")
                    .addQueryParameter("client_id", NAVER_CLIENT_ID)
                    .addQueryParameter("client_secret", NAVER_CLIENT_SECRET)
                    .addQueryParameter("code", code)
                    .build()
            )
            .get()
            .build()
        
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw FailSocialLoginException()
            
            return objectMapper.readValue(response.body?.string(), SocialAccessToken::class.java).access_token
        }
    }
    
    override fun getUserInfo(accessToken: String): SocialUserInfo {
        val request = Request.Builder()
            .url("https://openapi.naver.com/v1/nid/me")
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()
        
        val naverUserInfo = client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw FailSocialLoginException()
            
            objectMapper.readValue(response.body?.string(), NaverUserInfoAPIResponse::class.java).response
        }
        
        return SocialUserInfo(
            naverUserInfo.id,
            naverUserInfo.email,
            naverUserInfo.nickname,
            SocialPlatform.NAVER
        )
    }
    
    override fun getSocialPlatform(): SocialPlatform {
        return SocialPlatform.NAVER
    }
    
    private data class NaverUserInfoAPIResponse(val response: NaverUserInfo)
    
    private data class NaverUserInfo(
        val id: String,
        val email: String,
        val nickname: String,
        val mobile: String,
        val profile_image: String
    )
    
    companion object {
        private val objectMapper =
            ObjectMapper().registerKotlinModule().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        private val client = OkHttpClient().newBuilder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(3, TimeUnit.SECONDS)
            .callTimeout(5, TimeUnit.SECONDS)
            .build()
    }
}
