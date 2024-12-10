package com.bangkit.dermascan.data.remote.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.dermascan.data.model.response.SkinLesionItem
import com.bangkit.dermascan.data.remote.service.ApiService

class SkinLesionsPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, SkinLesionItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SkinLesionItem> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getSkinLesions(page = page, limit = 5)
            if (response.isSuccessful) {
                val body = response.body()
                val data = body?.data?.lesions ?: emptyList()
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.isEmpty()) null else page + 1
                )
            } else {
                LoadResult.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SkinLesionItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
