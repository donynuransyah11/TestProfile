package com.app.wawetech.testgrid.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import com.app.wawetech.testgrid.R

class ImageAdapter(private val listimage: MutableList<String>) : RecyclerView.Adapter<ImageAdapter.FavoritHolder>()  {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FavoritHolder {
        Log.e("adapters",listimage.size.toString())
        return FavoritHolder(
            LayoutInflater.from(p0.context)
                .inflate(R.layout.row_image, p0, false) as LinearLayout
        )
    }

    override fun getItemCount(): Int {
        return listimage.size
    }

    override fun onBindViewHolder(p0: FavoritHolder, p1: Int) {
        p0.UpdateData(listimage[p1])
    }


    class FavoritHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var imageview : ImageView = itemView.findViewById(R.id.imgholder)

        fun UpdateData(image : String){
            Picasso.get()
                .load(image)
                .resize(50, 150)
                .into(imageview)
        }
    }
}