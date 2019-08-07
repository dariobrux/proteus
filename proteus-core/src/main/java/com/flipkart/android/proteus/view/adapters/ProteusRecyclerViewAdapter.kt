package com.flipkart.android.proteus.view.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.flipkart.android.proteus.ProteusLayoutInflater
import com.flipkart.android.proteus.value.Layout
import com.flipkart.android.proteus.value.ObjectValue

abstract class ProteusRecyclerViewAdapter<T>(
        private val proteusLayoutInflater: ProteusLayoutInflater,
        private val layout: Layout,
        private val data: ObjectValue,
        private val totalItems: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = proteusLayoutInflater.inflate(layout, data, index)
        index++
        return ProteusViewHolder(view.asView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Do nothing
    }

    override fun getItemCount(): Int {
        return totalItems
    }

    inner class ProteusViewHolder(view: View) : RecyclerView.ViewHolder(view)
}