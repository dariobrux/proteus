/*
 * Copyright 2019 Flipkart Internet Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.android.proteus.parser.custom


import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flipkart.android.proteus.ProteusContext
import com.flipkart.android.proteus.ProteusView
import com.flipkart.android.proteus.ViewTypeParser
import com.flipkart.android.proteus.processor.NumberAttributeProcessor
import com.flipkart.android.proteus.processor.StringAttributeProcessor
import com.flipkart.android.proteus.toolbox.Attributes
import com.flipkart.android.proteus.value.Layout
import com.flipkart.android.proteus.value.ObjectValue
import com.flipkart.android.proteus.view.ProteusRecyclerView
import com.flipkart.android.proteus.view.adapters.ProteusRecyclerViewAdapter

/**
 * Created by Dario Brux on 02/08/2019.
 */
class RecyclerViewParser<T : RecyclerView>(private val onRecyclerViewCallback: OnRecyclerCallback?) : ViewTypeParser<T>() {

    interface OnRecyclerCallback {
        fun getRecyclerViewAdapter(itemCount: Int): ProteusRecyclerViewAdapter<*>?
    }

    override fun getType(): String {
        return "androidx.recyclerview.widget.RecyclerView"
    }

    override fun getParentType(): String? {
        return "ViewGroup"
    }

    override fun createView(context: ProteusContext, layout: Layout, data: ObjectValue, parent: ViewGroup?, dataIndex: Int): ProteusView {
        return ProteusRecyclerView(context)
    }

    override fun addAttributeProcessors() {
        var layoutManager: RecyclerView.LayoutManager? = null
        var orientation: Int? = null
        var itemCount: Int? = null
        addAttributeProcessor(Attributes.RecyclerView.OverScrollMode, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                view.overScrollMode = when (value) {
                    "never" -> RecyclerView.OVER_SCROLL_NEVER
                    "always" -> RecyclerView.OVER_SCROLL_ALWAYS
                    else -> RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS
                }
            }
        })
        addAttributeProcessor(Attributes.RecyclerView.LayoutManager, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                if ("androidx.recyclerview.widget.LinearLayoutManager" == value) {
                    layoutManager = LinearLayoutManager(view.context)
                }
                setRecyclerAdapterIfNotNull(view, layoutManager, orientation, itemCount)
            }
        })
        addAttributeProcessor(Attributes.RecyclerView.Orientation, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                orientation = if (value == "horizontal") {
                    RecyclerView.HORIZONTAL
                } else {
                    RecyclerView.VERTICAL
                }
                setRecyclerAdapterIfNotNull(view, layoutManager, orientation, itemCount)
            }
        })
        addAttributeProcessor(Attributes.RecyclerView.ItemCount, object : NumberAttributeProcessor<T>() {
            override fun setNumber(view: T, value: Number) {
                itemCount = value.toInt()
                setRecyclerAdapterIfNotNull(view, layoutManager, orientation, itemCount)
            }
        })
    }

    private fun setRecyclerAdapterIfNotNull(view: T, layoutManager: RecyclerView.LayoutManager?, orientation: Int?, itemCount: Int?) {
        if (layoutManager != null && orientation != null && itemCount != null) {
            view.layoutManager = layoutManager
            if (view.layoutManager is LinearLayoutManager) {
                (view.layoutManager as LinearLayoutManager).orientation = orientation
            }
            view.adapter = onRecyclerViewCallback?.getRecyclerViewAdapter(itemCount)
        }
    }
}
