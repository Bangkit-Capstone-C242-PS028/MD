package com.bangkit.dermascan.data.model.response

data class AddToFavoritesResponse(
    val statusCode: Int,
    val message: String,
    val data: FavoriteData?
)

data class FavoriteData(
    val favoriteId: String
)

data class RemoveFromFavoritesResponse(
    val statusCode: Int,
    val message: String
)

