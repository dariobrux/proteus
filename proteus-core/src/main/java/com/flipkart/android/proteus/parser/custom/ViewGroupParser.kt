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

import android.os.Build
import android.view.View
import android.view.ViewGroup
import com.flipkart.android.proteus.ProteusConstants
import com.flipkart.android.proteus.ProteusContext
import com.flipkart.android.proteus.ProteusView
import com.flipkart.android.proteus.ViewTypeParser
import com.flipkart.android.proteus.exceptions.ProteusInflateException
import com.flipkart.android.proteus.managers.ViewGroupManager
import com.flipkart.android.proteus.processor.AttributeProcessor
import com.flipkart.android.proteus.processor.BooleanAttributeProcessor
import com.flipkart.android.proteus.processor.StringAttributeProcessor
import com.flipkart.android.proteus.toolbox.Attributes
import com.flipkart.android.proteus.value.*
import com.flipkart.android.proteus.view.ProteusAspectRatioFrameLayout

class ViewGroupParser<T : ViewGroup> : ViewTypeParser<T>() {

    override fun getType(): String {
        return "ViewGroup"
    }

    override fun getParentType(): String? {
        return "View"
    }

    override fun createView(context: ProteusContext, layout: Layout, data: ObjectValue, parent: ViewGroup?, dataIndex: Int): ProteusView {
        return ProteusAspectRatioFrameLayout(context)
    }

    override fun createViewManager(context: ProteusContext, view: ProteusView, layout: Layout, data: ObjectValue, caller: ViewTypeParser<*>?, parent: ViewGroup?, dataIndex: Int): ProteusView.Manager {
        val dataContext = createDataContext(context, layout, data, parent, dataIndex)
        return ViewGroupManager(context, caller ?: this, view.asView, layout, dataContext)
    }

    override fun addAttributeProcessors() {

        addAttributeProcessor(Attributes.ViewGroup.ClipChildren, object : BooleanAttributeProcessor<T>() {
            override fun setBoolean(view: T, value: Boolean) {
                view.clipChildren = value
            }
        })

        addAttributeProcessor(Attributes.ViewGroup.ClipToPadding, object : BooleanAttributeProcessor<T>() {
            override fun setBoolean(view: T, value: Boolean) {
                view.clipToPadding = value
            }
        })

        addAttributeProcessor(Attributes.ViewGroup.LayoutMode, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if (LAYOUT_MODE_CLIP_BOUNDS == value) {
                        view.layoutMode = ViewGroup.LAYOUT_MODE_CLIP_BOUNDS
                    } else if (LAYOUT_MODE_OPTICAL_BOUNDS == value) {
                        view.layoutMode = ViewGroup.LAYOUT_MODE_OPTICAL_BOUNDS
                    }
                }
            }
        })

        addAttributeProcessor(Attributes.ViewGroup.SplitMotionEvents, object : BooleanAttributeProcessor<T>() {
            override fun setBoolean(view: T, value: Boolean) {
                view.isMotionEventSplittingEnabled = value
            }
        })

        addAttributeProcessor(Attributes.ViewGroup.Children, object : AttributeProcessor<T>() {
            override fun handleBinding(view: T, value: Binding) {
                handleDataBoundChildren(view, value)
            }

            override fun handleValue(view: T, value: Value) {
                handleChildren(view, value)
            }

            override fun handleResource(view: T, resource: Resource) {
                throw IllegalArgumentException("children cannot be a resource")
            }

            override fun handleAttributeResource(view: T, attribute: AttributeResource) {
                throw IllegalArgumentException("children cannot be a resource")
            }

            override fun handleStyleResource(view: T, style: StyleResource) {
                throw IllegalArgumentException("children cannot be a style attribute")
            }
        })
    }

    override fun handleChildren(view: T, children: Value): Boolean {
        val proteusView = view as ProteusView
        val viewManager = proteusView.viewManager
        val layoutInflater = viewManager.context.inflater
        val data = viewManager.dataContext.data
        val dataIndex = viewManager.dataContext.index

        if (children.isArray) {
            var child: ProteusView
            val iterator = children.asArray.iterator()
            var element: Value
            while (iterator.hasNext()) {
                element = iterator.next()
                if (!element.isLayout) {
                    throw ProteusInflateException("attribute  'children' must be an array of 'Layout' objects")
                }
                child = layoutInflater.inflate(element.asLayout, data, view, dataIndex)
                addView(proteusView, child)
            }
        }

        return true
    }

    protected fun handleDataBoundChildren(view: T, value: Binding) {
        val parent = view as ProteusView
        val manager = parent.viewManager as ViewGroupManager
        val dataContext = manager.dataContext
        val config = (value as NestedBinding).value.asObject

        val collection = config.getAsBinding(ProteusConstants.COLLECTION)
        val layout = config.getAsLayout(ProteusConstants.LAYOUT)

        manager.hasDataBoundChildren = true

        if (null == layout || null == collection) {
            throw ProteusInflateException("'collection' and 'layout' are mandatory for attribute:'children'")
        }

        val dataset = collection.asBinding.evaluate(view.context, dataContext.data, dataContext.index)
        if (dataset.isNull) {
            return
        }

        if (!dataset.isArray) {
            throw ProteusInflateException("'collection' in attribute:'children' must be NULL or Array")
        }

        val length = dataset.asArray.size()
        var count = view.childCount
        val data = dataContext.data
        val inflater = manager.context.inflater
        var child: ProteusView
        var temp: View

        if (count > length) {
            while (count > length) {
                count--
                view.removeViewAt(count)
            }
        }

        for (index in 0 until length) {
            if (index < count) {
                temp = view.getChildAt(index)
                if (temp is ProteusView) {
                    (temp as ProteusView).viewManager.update(data)
                }
            } else {

                child = inflater.inflate(layout, data, view, index)
                addView(parent, child)
            }
        }
    }

    override fun addView(parent: ProteusView, view: ProteusView): Boolean {
        if (parent is ViewGroup) {
            (parent as ViewGroup).addView(view.asView)
            return true
        }
        return false
    }

    companion object {

        private val LAYOUT_MODE_CLIP_BOUNDS = "clipBounds"
        private val LAYOUT_MODE_OPTICAL_BOUNDS = "opticalBounds"
    }
}
