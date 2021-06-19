package com.watirna.shop.views.forgotPasswordActivity

import java.io.Serializable

data class ResetPasswordResponse(
    val error: List<Any>,
    val message: String,
    val responseData: ResetPasswordResponseData,
    val statusCode: String,
    val title: String
) : Serializable

data class ResetPasswordResponseData(
    val password: String,
    val otp: String,
    val username: String
) : Serializable