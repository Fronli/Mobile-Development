package com.example.ch09

sealed class Routes(val route: String) {
    data object News : Routes("news")

    data object Detail : Routes("detail/{articleUrl}") {
        fun createRoute(articleUrl: String): String = "detail/${android.net.Uri.encode(articleUrl)}"
    }
}
