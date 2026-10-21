package com.example.ch09.data

import com.example.ch09.data.local.ArticleDao
import com.example.ch09.data.local.ArticleEntity
import com.example.ch09.data.model.NewsCategory
import com.example.ch09.testutil.FakeNewsApiService
import com.example.ch09.testutil.articleDto
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class FakeArticleDao : ArticleDao() {
    val storage = mutableListOf<ArticleEntity>()

    override suspend fun getArticles(category: String): List<ArticleEntity> =
        storage.filter { it.category == category }.sortedBy { it.position }

    override suspend fun getArticleByUrl(url: String): ArticleEntity? =
        storage.firstOrNull { it.url == url }

    override suspend fun searchCached(query: String): List<ArticleEntity> =
        storage.filter {
            it.title.contains(query, ignoreCase = true) ||
                it.description.contains(query, ignoreCase = true)
        }.sortedByDescending { it.cachedAt }

    override suspend fun insertAll(articles: List<ArticleEntity>) {
        storage.addAll(articles)
    }

    override suspend fun deleteCategory(category: String) {
        storage.removeAll { it.category == category }
    }
}

class NewsRepositoryImplTest {

    private val api = FakeNewsApiService()
    private val dao = FakeArticleDao()
    private var currentTime = 100_000L
    private val repository = NewsRepositoryImpl(
        api = api,
        dao = dao,
        clock = { currentTime }
    )

    @Test
    fun `berhasil memuat headline menyimpan ke database dan offline null`() = runTest {
        api.articles = listOf(articleDto(1), articleDto(2))

        val result = repository.getTopHeadlines(NewsCategory.General)

        assertTrue(result.isSuccess)
        val feed = result.getOrThrow()
        assertEquals(2, feed.articles.size)
        assertNull(feed.offline)

        val cached = dao.getArticles("general")
        assertEquals(2, cached.size)
        assertEquals("https://contoh.com/1", cached[0].url)
        assertEquals(0, cached[0].position)
        assertEquals(currentTime, cached[0].cachedAt)
    }

    @Test
    fun `gagal memuat headline mengembalikan cache dengan offline info`() = runTest {
        // Simpan cache awal
        api.articles = listOf(articleDto(1))
        repository.getTopHeadlines(NewsCategory.General)

        // Request kedua gagal
        api.error = IOException("Network down")
        val result = repository.getTopHeadlines(NewsCategory.General)

        assertTrue(result.isSuccess)
        val feed = result.getOrThrow()
        assertEquals(1, feed.articles.size)
        assertNotNull(feed.offline)
        assertEquals(currentTime, feed.offline?.cachedAt)
        assertEquals("Tidak ada koneksi internet.", feed.offline?.reason)
    }

    @Test
    fun `gagal memuat headline tanpa cache mengembalikan failure`() = runTest {
        api.error = IOException("Network down")

        val result = repository.getTopHeadlines(NewsCategory.Technology)

        assertTrue(result.isFailure)
    }

    @Test
    fun `pencarian berhasil menyimpan dengan kategori search`() = runTest {
        api.articles = listOf(articleDto(3, title = "Android Kotlin"))

        val result = repository.searchNews("Android")

        assertTrue(result.isSuccess)
        val cached = dao.getArticles("__search__")
        assertEquals(1, cached.size)
        assertEquals("https://contoh.com/3", cached[0].url)
    }

    @Test
    fun `pencarian gagal membaca cache yang cocok`() = runTest {
        // Simpan berita di general
        api.articles = listOf(articleDto(10, title = "Jetpack Compose Guide"))
        repository.getTopHeadlines(NewsCategory.General)

        // Pencarian saat offline
        api.error = IOException("Offline")
        val result = repository.searchNews("Compose")

        assertTrue(result.isSuccess)
        val feed = result.getOrThrow()
        assertEquals(1, feed.articles.size)
        assertEquals("https://contoh.com/10", feed.articles[0].url)
        assertNotNull(feed.offline)
    }

    @Test
    fun `getArticle mengembalikan artikel dari database`() = runTest {
        api.articles = listOf(articleDto(42, title = "Detail Test"))
        repository.getTopHeadlines(NewsCategory.General)

        val article = repository.getArticle("https://contoh.com/42")

        assertNotNull(article)
        assertEquals("Detail Test", article?.title)
        assertEquals("https://contoh.com/42", article?.url)
    }

    @Test(expected = CancellationException::class)
    fun `CancellationException dilempar ulang bukan dianggap offline`() = runTest {
        api.error = CancellationException("Dibatalkan")
        repository.getTopHeadlines(NewsCategory.General)
    }
}
