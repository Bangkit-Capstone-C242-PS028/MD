package com.bangkit.dermascan.data.remote.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.dermascan.data.model.response.UserData
import com.bangkit.dermascan.data.remote.service.ApiService

class ConsultationPagingSource(
    private val apiService: ApiService
): PagingSource<Int, UserData>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserData> {
//        TODO("Not yet implemented")
        val page = params.key ?: 1
        return try {
            val response = apiService.getAllDoctor(page = page, size = params.loadSize)
            if (response.isSuccessful) {
                val body = response.body()
                val consultations = body?.data?.users ?: emptyList()
                LoadResult.Page(
                    data = consultations,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (consultations.isEmpty()) null else page + 1
                )
            }else{
                LoadResult.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
            }
    }

    override fun getRefreshKey(state: PagingState<Int, UserData>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}

