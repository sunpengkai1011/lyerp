package com.longyuan.lyerp.network.service

import com.longyuan.lyerp.entities.params.SigninParam
import com.longyuan.lyerp.entities.response.UserResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/1.1/login")
    fun signin(@Body signinParam: SigninParam): Single<UserResponse>

}