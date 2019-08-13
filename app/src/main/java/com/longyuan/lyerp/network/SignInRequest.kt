package com.longyuan.lyerp.network

import android.content.Context
import com.longyuan.lyerp.Constants
import com.longyuan.lyerp.entities.UserInfo
import com.longyuan.lyerp.entities.params.SigninParam
import com.longyuan.lyerp.entities.response.UserResponse
import com.longyuan.lyerp.network.service.UserService
import io.reactivex.Single

class SignInRequest(context: Context, val username: String, val password: String): BaseRequest<UserService, UserResponse, UserInfo>(context){

    override fun getHeader(): Map<String, String>? {
        val header = HashMap<String, String>()
        header["X-LC-Id"] = "ktl9dNgjXahYHobdqIfm9igv-MdYXbMMI"
        header["Content-Type"] = "application/json"
        header["X-LC-Key"] = "YRClIBtEt4YxFVTjXWC9xjW1"
        return header
    }

    override fun getUrl(): String {
        return Constants.BASE_URL
    }

    override fun getEndpointClass(): Class<UserService> {
        return UserService::class.java
    }

    override fun getEndpoint(endpoint: UserService): Single<UserResponse> {
        val param = SigninParam(username, password)
        return endpoint.signin(param)
    }

    override fun dealWithResponse(response: UserResponse): UserInfo {
        return UserInfo(response.username, response.phone, response.mobilePhoneNumber)
    }
}