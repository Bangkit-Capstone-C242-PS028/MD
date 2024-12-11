package com.bangkit.dermascan.data.remote.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.dermascan.data.model.response.ArticleItem
import com.bangkit.dermascan.data.model.response.ForumItem
import com.bangkit.dermascan.data.remote.service.ApiService

class ForumPagingSource(
    private val apiService: ApiService
) :  PagingSource<Int, ForumItem>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ForumItem> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getForums(page = page, limit = params.loadSize)
            val forums = response.data?.forums ?: emptyList()
            LoadResult.Page(
                data = forums,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (forums.isEmpty()) null else page + 1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ForumItem>): Int? {
        val anchorPosition = state.anchorPosition ?: return null

        // Temukan halaman terdekat dengan anchor position
        val closestPage = state.closestPageToPosition(anchorPosition)

        // Jika halaman terdekat ada, kembalikan key yang sesuai
        return closestPage?.prevKey ?: closestPage?.nextKey
    }
}

