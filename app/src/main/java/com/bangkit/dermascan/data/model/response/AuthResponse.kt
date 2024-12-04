package com.bangkit.dermascan.data.model.response

import com.bangkit.dermascan.data.model.response.message.ErrorMessage
import com.bangkit.dermascan.data.model.response.message.SuccessMessage
import com.google.gson.annotations.SerializedName

data class SignupResponse (
    @SerializedName("success") val success: SuccessMessage? = null,  // Optional, karena tidak selalu ada
    @SerializedName("error") val error: ErrorMessage? = null       // Optional, karena tidak selalu ada
)

