package com.app.wawetech.testgrid.repository

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.ANRequest


/**
 * Repository adalah sumber data yang bisa di dapatkan dari network atau lokal
 * Untuk case ini semua data dari network
 */

class ImageSource {

    /**
     *
     * @return ANRequest<out ANRequest<*>>
     */
    fun getAllImage(): ANRequest<out ANRequest<*>> {
        return AndroidNetworking.get("https://api.imgur.com/3/tags")
            .addHeaders("Authorization", "Client-ID 77d71dc75e9339a")
            .build()
    }

    /**
     *
     * @param tag String
     * @return ANRequest<out ANRequest<*>>
     */
    fun getImageByTag(tag: String): ANRequest<out ANRequest<*>> {
        return AndroidNetworking.get("https://api.imgur.com/3/gallery/t/$tag/perPage=40")
            .addHeaders("Authorization", "Client-ID 77d71dc75e9339a")
            .build()
    }

    fun detailImage(id : String): ANRequest<out ANRequest<*>>? {
        return AndroidNetworking.get("https://api.imgur.com/3/image/$id")
            .addHeaders("Authorization","Client-ID 77d71dc75e9339a")
            .build()
    }

}