package com.bangkit.dermascan.data.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

//data class ForumResponse(
//    @SerializedName("statusCode") val statusCode: Int,
//    @SerializedName("message") val message: String,
//    @SerializedName("dataArticles") val data: ForumDataList
//)

//data class ForumDataList(
//    @SerializedName("dataArticles") val forums: List<ForumData>,
//    @SerializedName("meta") val meta: Meta
//)

//data class ForumCreatedResponse(
//    @SerializedName("forumId") val forumId: String
//)

//data class ForumData(
//    @SerializedName("id") val id: String,
//    @SerializedName("title") val title: String,
//    @SerializedName("content") val content: String,
//    @SerializedName("status") val status: String,
//    @SerializedName("created_at") val createdAt: String,
//    @SerializedName("updated_at") val updatedAt: String,
//    @SerializedName("patient") val patient: PatientData?,
//    @SerializedName("doctor") val doctor: DoctorData?
//)

data class BaseResponse<T>(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("dataArticles") val data: T
)
@Parcelize
data class Meta(
    @SerializedName("total") val total: Int,
    @SerializedName("page") val page: String,
    @SerializedName("lastPage") val lastPage: Int
):Parcelable


