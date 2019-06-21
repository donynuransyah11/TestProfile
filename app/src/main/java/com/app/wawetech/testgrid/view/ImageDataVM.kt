package com.app.wawetech.testgrid.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.app.wawetech.testgrid.repository.ImageSource
import org.json.JSONObject


/**
 *
 * @property source ImageSource
 * @property allImage MutableLiveData<JSONObject>
 * @property tagImage MutableLiveData<JSONObject>
 * @property message MutableLiveData<String>
 *
 * Observe sumber data menggunakan RX lalu simpan pada mutablelivedata<T> untuk nantinya akan di Observe oleh Activity
 *
 * */
class ImageDataVM : ViewModel() {

    /**
     * repository
     */
    var source = ImageSource()

    var allImage: MutableLiveData<JSONObject> = MutableLiveData()
    var tagImage: MutableLiveData<JSONObject> = MutableLiveData()
    var message: MutableLiveData<String> = MutableLiveData()

    fun getListImage() {
        source.getAllImage()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    allImage.value = response
                }

                override fun onError(anError: ANError?) {
                    anError?.stackTrace
                    message.postValue(anError?.message)
                }
            })
    }

    fun getImageByTag(tag: String) {
        source.getImageByTag(tag)
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    tagImage.value = response
                }

                override fun onError(anError: ANError?) {
                    message.postValue(anError?.message)
                }

            })

    }

}