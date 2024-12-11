package com.bangkit.dermascan.data.remote.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.dermascan.data.model.response.ArticleItem
import com.bangkit.dermascan.data.model.response.SkinLesionItem
import com.bangkit.dermascan.data.remote.service.ApiService

class ArticlePagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ArticleItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleItem> {
        val page = params.key ?: 1 // Halaman pertama
        return try {
            // Panggil API untuk mengambil data artikel
            val response = apiService.getArticles(page = page, limit = params.loadSize)
            val articles = response.data?.articles ?: emptyList() // Ambil daftar artikel dari respons

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1, // Tidak ada halaman sebelumnya jika ini halaman pertama
                nextKey = if (articles.isEmpty()) null else page + 1 // Tidak ada halaman berikutnya jika data kosong
            )
        } catch (e: Exception) {
            // Tangani error jika ada
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ArticleItem>): Int? {
        // Refresh Key: Kembalikan halaman saat ini yang sedang ditampilkan
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
