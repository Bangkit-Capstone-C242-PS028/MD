package com.bangkit.dermascan.data.model.requestBody

import com.google.gson.annotations.SerializedName

data class UserRequest (
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("workplace") val workplace: String? = null, //Doctor only
    @SerializedName("specialization") val specialization: String? = null //Doctor only
)

