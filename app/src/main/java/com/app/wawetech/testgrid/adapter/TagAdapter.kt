package com.app.wawetech.testgrid.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.app.wawetech.testgrid.R

class TagAdapter(private val listtag: MutableList<String>,var listener : ItemListener) : RecyclerView.Adapter<TagAdapter.FavoritHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FavoritHolder {
        return FavoritHolder(
            LayoutInflater.from(p0.context)
                .inflate(R.layout.row_tag, p0, false) as LinearLayout
        )
    }

    override fun getItemCount(): Int {
        return listtag.size
    }

    override fun onBindViewHolder(p0: FavoritHolder, p1: Int) {
        p0.UpdateData(listtag[p1])
    }

    interface ItemListener{
        fun onClick(tag : String)
    }

    inner class FavoritHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var btntag = itemView.findViewById<Button>(R.id.btntag)
        fun UpdateData(tag: String) {
            btntag.text = tag
            btntag.setOnClickListener { listener.onClick(tag) }
        }
    }
}