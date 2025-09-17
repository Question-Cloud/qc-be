package com.eager.questioncloud.social.kakao

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
class KakaoAPI(
    private val httpClient: HttpClient
) : SocialAPI {
    @Value("\${KAKAO_API_KEY}")
    private lateinit var KAKAO_API_KEY: String
    
    @Value("\${KAKAO_API_SECRET}")
    private lateinit var KAKAO_API_SECRET: String
    
    @Value("\${CLIENT_URL}")
    private lateinit var CLIENT_URL: String
    
    override fun getAccessToken(code: String): String {
        val form = mutableMapOf<String, String>()
        form["grant_type"] = "authorization_code"
        form["client_id"] = KAKAO_API_KEY
        form["redirect_uri"] = "$CLIENT_URL/user/social/kakao"
        form["code"] = code
        form["client_secret"] = KAKAO_API_SECRET
        
        val request = HttpRequest(url = "https://kauth.kakao.com/oauth/token", form = form, contentType = ContentType.FORM)
        val response = httpClient.post(request, SocialAccessToken::class.java)
        return response.access_token
    }
    
    override fun getUserInfo(accessToken: String): SocialUserInfo {
        val headers = mapOf("Authorization" to "Bearer $accessToken")
        val request = HttpRequest(url = "https://kapi.kakao.com/v1/oidc/userinfo", headers = headers)
        val response = httpClient.get(request, KakaoUserInfo::class.java)
        return SocialUserInfo(response.sub, response.email, response.nickname, SocialPlatform.KAKAO)
    }
    
    override fun getSocialPlatform(): SocialPlatform {
        return SocialPlatform.KAKAO
    }
}

private data class KakaoUserInfo(val sub: String, val email: String?, val nickname: String?)
