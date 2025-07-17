package com.eager.questioncloud.social

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class KakaoAPI : SocialAPI {
    @Value("\${KAKAO_API_KEY}")
    private lateinit var KAKAO_API_KEY: String
    
    @Value("\${KAKAO_API_SECRET}")
    private lateinit var KAKAO_API_SECRET: String
    
    @Value("\${CLIENT_URL}")
    private lateinit var CLIENT_URL: String
    
    override fun getAccessToken(code: String): String {
        val formBody = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", KAKAO_API_KEY)
            .add("redirect_uri", "$CLIENT_URL/user/social/kakao")
            .add("code", code)
            .add("client_secret", KAKAO_API_SECRET)
            .build()
        
        val request = Request.Builder()
            .url("https://kauth.kakao.com/oauth/token")
            .post(formBody)
            .build()
        
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw FailSocialLoginException()
            return objectMapper.readValue(response.body?.string(), SocialAccessToken::class.java).access_token
        }
    }
    
    override fun getUserInfo(accessToken: String): SocialUserInfo {
        val request = Request.Builder()
            .url("https://kapi.kakao.com/v1/oidc/userinfo")
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()
        
        val kakaoUserInfo = client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw FailSocialLoginException()
            
            objectMapper.readValue(response.body?.string(), KakaoUserInfo::class.java)
        }
        
        return SocialUserInfo(kakaoUserInfo.sub, kakaoUserInfo.email, kakaoUserInfo.nickname, SocialPlatform.KAKAO)
    }
    
    override fun getSocialPlatform(): SocialPlatform {
        return SocialPlatform.KAKAO
    }
    
    private data class KakaoUserInfo(val sub: String, val email: String?, val nickname: String?)
    
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
