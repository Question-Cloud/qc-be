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
class GoogleAPI : SocialAPI {
    @Value("\${GOOGLE_CLIENT_ID}")
    private lateinit var GOOGLE_CLIENT_ID: String
    
    @Value("\${GOOGLE_CLIENT_SECRET}")
    private lateinit var GOOGLE_CLIENT_SECRET: String
    
    @Value("\${CLIENT_URL}")
    private lateinit var CLIENT_URL: String
    
    override fun getAccessToken(code: String): String {
        val formBody = FormBody.Builder()
            .add("client_id", GOOGLE_CLIENT_ID)
            .add("client_secret", GOOGLE_CLIENT_SECRET)
            .add("code", code)
            .add("grant_type", "authorization_code")
            .add("redirect_uri", "$CLIENT_URL/user/social/kakao")
            .build()
        
        val request = Request.Builder()
            .url("https://oauth2.googleapis.com/token")
            .post(formBody)
            .build()
        
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw FailSocialLoginException();
            return objectMapper.readValue(response.body?.string(), SocialAccessToken::class.java).access_token
        }
    }
    
    override fun getUserInfo(accessToken: String): SocialUserInfo {
        val request = Request.Builder()
            .url("https://www.googleapis.com/oauth2/v1/userinfo?alt=json")
            .addHeader("Authorization", "Bearer $accessToken")
            .get()
            .build()
        
        val googleUserInfo = client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw FailSocialLoginException()
            
            objectMapper.readValue(response.body?.string(), GoogleUserInfo::class.java)
        }
        
        return SocialUserInfo(googleUserInfo.id, googleUserInfo.email, googleUserInfo.name, SocialPlatform.GOOGLE)
    }
    
    override fun getSocialPlatform(): SocialPlatform {
        return SocialPlatform.GOOGLE
    }
    
    internal data class GoogleUserInfo(val id: String, val email: String, val name: String, val picture: String)
    
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
