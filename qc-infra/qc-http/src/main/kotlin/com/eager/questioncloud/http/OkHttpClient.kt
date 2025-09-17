package com.eager.questioncloud.http

import com.eager.questioncloud.common.http.ContentType
import com.eager.questioncloud.common.http.HttpClient
import com.eager.questioncloud.common.http.HttpClientException
import com.eager.questioncloud.common.http.HttpRequest
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.FormBody
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class OkHttpClient(
    private val objectMapper: ObjectMapper,
) : HttpClient {
    private val client = OkHttpClient().newBuilder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .callTimeout(5, TimeUnit.SECONDS)
        .build()
    
    override fun <T> get(req: HttpRequest, valueType: Class<T>): T {
        val okHttpReqBuilder = Request.Builder()
        setHttpUrl(req, okHttpReqBuilder)
        setHttpHeader(req, okHttpReqBuilder)
        
        client.newCall(okHttpReqBuilder.get().build()).execute().use { response ->
            if (response.code in 400..499) {
                throw HttpClientException.Response4xxException()
            }
            
            if (response.code in 500..599) {
                throw HttpClientException.Response5xxException()
            }
            
            return objectMapper.readValue(response.body?.string(), valueType)
        }
    }
    
    override fun <T> post(req: HttpRequest, valueType: Class<T>): T {
        val okHttpReqBuilder = Request.Builder()
        setHttpUrl(req, okHttpReqBuilder)
        setHttpHeader(req, okHttpReqBuilder)
        setBody(req, okHttpReqBuilder)
        
        client.newCall(okHttpReqBuilder.build()).execute().use { response ->
            if (response.code in 400..499) {
                throw HttpClientException.Response4xxException()
            }
            
            if (response.code in 500..599) {
                throw HttpClientException.Response5xxException()
            }
            
            return objectMapper.readValue(response.body?.string(), valueType)
        }
    }
    
    override fun post(req: HttpRequest) {
        val okHttpReqBuilder = Request.Builder()
        setHttpUrl(req, okHttpReqBuilder)
        setHttpHeader(req, okHttpReqBuilder)
        setBody(req, okHttpReqBuilder)
        
        client.newCall(okHttpReqBuilder.build()).execute().use { response ->
            if (response.code in 400..499) {
                throw HttpClientException.Response4xxException()
            }
            
            if (response.code in 500..599) {
                throw HttpClientException.Response5xxException()
            }
        }
    }
    
    private fun setHttpUrl(req: HttpRequest, builder: Request.Builder) {
        builder.url(
            req.url.toHttpUrl()
                .newBuilder()
                .apply { req.query.forEach { (k, v) -> addQueryParameter(k, v) } }
                .build()
        )
    }
    
    private fun setHttpHeader(req: HttpRequest, builder: Request.Builder) {
        req.headers.forEach { (k, v) -> builder.addHeader(k, v) }
    }
    
    private fun setBody(req: HttpRequest, builder: Request.Builder) {
        val body = req.body
        val form = req.form
        val contentType = req.contentType
        
        if ((body == null && form.isEmpty()) || contentType == null) return
        
        when (contentType) {
            ContentType.JSON -> {
                builder.post(objectMapper.writeValueAsString(req.body).toRequestBody("application/json".toMediaType()))
            }
            
            ContentType.FORM -> {
                val formBody = FormBody.Builder()
                req.form.forEach { (k, v) -> formBody.add(k, v) }
                builder.post(formBody.build())
            }
        }
    }
}