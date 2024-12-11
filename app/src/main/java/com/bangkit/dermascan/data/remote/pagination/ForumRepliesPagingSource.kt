package com.bangkit.dermascan.data.remote.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.dermascan.data.model.response.ForumReply
import com.bangkit.dermascan.data.remote.service.ApiService

class ForumRepliesPagingSource(
    private val forumId: String,
    private val apiService: ApiService // Gantilah dengan nama service Anda
) : PagingSource<Int, ForumReply>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ForumReply> {
        return try {
            val page = params.key ?: 1 // Jika `params.key` null, mulai dari halaman pertama
            val response = apiService.getForumReplies(forumId, page, params.loadSize)
            Log.d("ForumRepliesPagingSource", "Response Data: ${response.data.data}")
            // Menentukan halaman berikutnya
            val nextPage = if (response.data.data.isEmpty()) null else page + 1

            LoadResult.Page(
                data = response.data.data,
                prevKey = null, // Jika tidak ada paginasi mundur
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ForumReply>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
