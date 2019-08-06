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

package com.flipkart.android.proteus.processor

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.flipkart.android.proteus.ProteusView
import com.flipkart.android.proteus.value.AttributeResource
import com.flipkart.android.proteus.value.Resource
import com.flipkart.android.proteus.value.StyleResource
import com.flipkart.android.proteus.value.Value


/**
 * GravityAttributeProcessor
 *
 * @author aditya.sharat
 */

abstract class ConstraintAttributeProcessor<V : View> : AttributeProcessor<V>() {

    override fun handleValue(view: V, value: Value) {
        setConstraint(view, value.toString())
    }

    override fun handleResource(view: V, resource: Resource) {
        // todo
//        val gravity = resource.getInteger(view.context)
//        setConstraintWithParent(view, gravity ?: android.view.Gravity.NO_GRAVITY)
    }

    override fun handleAttributeResource(view: V, attribute: AttributeResource) {
        val a = attribute.apply(view.context)
        set(view, a)
    }

    override fun handleStyleResource(view: V, style: StyleResource) {
        val a = style.apply(view.context)
        set(view, a)
    }

    private operator fun set(view: V, a: TypedArray) {
        // Todo
//        setConstraintWithParent(view, a.getInt(0, 0))
    }

    abstract fun setConstraint(view: V, resource: String)

    override fun compile(value: Value?, context: Context): Value? {
        return value
    }

    fun setConstraintOnLayoutAdded(view: V, resource: String, startSide: Int, endSide: Int) {
        val viewTreeObserver = view.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    if (view.parent !is ConstraintLayout) {
                        return
                    }
                    val constraintLayout = view.parent as ConstraintLayout
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    var id = constraintLayout.id
                    if (resource != "parent") {
                        repeat(constraintLayout.childCount) {
                            val childAt = constraintLayout.getChildAt(it)
                            if (childAt is ProteusView) {
                                val childName = childAt.viewManager.layout.attributes?.first()?.value.toString()
                                if (resource == childName) {
                                    id = childAt.asView.id
                                }
                            }
                        }
                    }
                    constraintSet.connect(view.id, startSide, id, endSide)
                    constraintSet.applyTo(constraintLayout)
                }
            })
        }
    }

    fun setHorizontalBias(view: View, bias: Float) {
        val viewTreeObserver = view.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val constraintLayout = view.parent as ConstraintLayout
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.setHorizontalBias(view.id, bias)
                    constraintSet.applyTo(constraintLayout)
                }
            })
        }
    }

    fun setVerticalBias(view: View, bias: Float) {
        val viewTreeObserver = view.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val constraintLayout = view.parent as ConstraintLayout
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.setVerticalBias(view.id, bias)
                    constraintSet.applyTo(constraintLayout)
                }
            })
        }
    }
}
