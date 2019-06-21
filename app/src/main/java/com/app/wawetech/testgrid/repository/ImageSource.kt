package com.app.wawetech.testgrid.repository

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest


/**
 * Repository adalah sumber data yang bisa di dapatkan dari network atau lokal
 * Untuk case ini semua data dari network
 */

class ImageSource {
    fun getAllImage(): ANRequest<out ANRequest<*>> {
        return AndroidNetworking.get("https://api.imgur.com/3/tags")
            .addHeaders("Authorization", "Client-ID 77d71dc75e9339a")
            .build()
    }

    fun getImageByTag(tag: String): ANRequest<out ANRequest<*>> {
        return AndroidNetworking.get("https://api.imgur.com/3/gallery/t/$tag")
            .addHeaders("Authorization", "Client-ID 77d71dc75e9339a")
            .build()
    }
}