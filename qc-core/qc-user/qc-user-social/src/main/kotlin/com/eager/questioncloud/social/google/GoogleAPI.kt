package com.eager.questioncloud.social.google

import com.eager.questioncloud.common.http.ContentType
import com.eager.questioncloud.common.http.HttpClient
import com.eager.questioncloud.common.http.HttpRequest
import com.eager.questioncloud.social.SocialAPI
import com.eager.questioncloud.social.SocialAccessToken
import com.eager.questioncloud.social.SocialPlatform
import com.eager.questioncloud.social.SocialUserInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GoogleAPI(
    private val httpClient: HttpClient
) : SocialAPI {
    @Value("\${GOOGLE_CLIENT_ID}")
    private lateinit var GOOGLE_CLIENT_ID: String
    
    @Value("\${GOOGLE_CLIENT_SECRET}")
    private lateinit var GOOGLE_CLIENT_SECRET: String
    
    @Value("\${CLIENT_URL}")
    private lateinit var CLIENT_URL: String
    
    override fun getAccessToken(code: String): String {
        val form = mutableMapOf<String, String>()
        form["client_id"] = GOOGLE_CLIENT_ID
        form["client_secret"] = GOOGLE_CLIENT_SECRET
        form["code"] = code
        form["grant_type"] = "authorization_code"
        form["redirect_uri"] = "$CLIENT_URL/user/social/kakao"
        
        val request = HttpRequest(url = "https://oauth2.googleapis.com/token", form = form, contentType = ContentType.FORM)
        val response = httpClient.post(request, SocialAccessToken::class.java)
        return response.access_token
    }
    
    override fun getUserInfo(accessToken: String): SocialUserInfo {
        val headers = mapOf("Authorization" to "Bearer $accessToken")
        val request = HttpRequest(url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json", headers = headers)
        val response = httpClient.get(request, GoogleUserInfo::class.java)
        return SocialUserInfo(response.id, response.email, response.name, SocialPlatform.GOOGLE)
    }
    
    override fun getSocialPlatform(): SocialPlatform {
        return SocialPlatform.GOOGLE
    }
}

private data class GoogleUserInfo(val id: String, val email: String, val name: String, val picture: String)
