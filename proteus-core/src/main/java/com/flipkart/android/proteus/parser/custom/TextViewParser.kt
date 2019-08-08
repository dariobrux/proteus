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

import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.flipkart.android.proteus.ProteusContext
import com.flipkart.android.proteus.ProteusView
import com.flipkart.android.proteus.ViewTypeParser
import com.flipkart.android.proteus.parser.ParseHelper
import com.flipkart.android.proteus.processor.*
import com.flipkart.android.proteus.toolbox.Attributes
import com.flipkart.android.proteus.value.Layout
import com.flipkart.android.proteus.value.ObjectValue
import com.flipkart.android.proteus.view.ProteusTextView

/**
 * Created by kiran.kumar on 12/05/14.
 */
class TextViewParser<T : TextView> : ViewTypeParser<T>() {

    override fun getType(): String {
        return "TextView"
    }

    override fun getParentType(): String? {
        return "View"
    }

    override fun createView(context: ProteusContext, layout: Layout, data: ObjectValue, parent: ViewGroup?, dataIndex: Int): ProteusView {
        return ProteusTextView(context)
    }

    override fun addAttributeProcessors() {

        addAttributeProcessor(Attributes.TextView.HTML, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.text = Html.fromHtml(value, Html.FROM_HTML_MODE_LEGACY)
                } else {

                    view.text = Html.fromHtml(value)
                }
            }
        })
        addAttributeProcessor(Attributes.TextView.Text, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                view.text = value
            }
        })

        addAttributeProcessor(Attributes.TextView.DrawablePadding, object : DimensionAttributeProcessor<T>() {
            override fun setDimension(view: T, dimension: Float) {
                view.compoundDrawablePadding = dimension.toInt()
            }
        })

        addAttributeProcessor(Attributes.TextView.TextSize, object : DimensionAttributeProcessor<T>() {
            override fun setDimension(view: T, dimension: Float) {
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimension)
            }
        })
        addAttributeProcessor(Attributes.TextView.Gravity, object : GravityAttributeProcessor<T>() {
            override fun setGravity(view: T, @Gravity gravity: Int) {
                view.gravity = gravity
            }
        })

        addAttributeProcessor(Attributes.TextView.TextColor, object : ColorResourceProcessor<T>() {

            override fun setColor(view: T, color: Int) {
                view.setTextColor(color)
            }

            override fun setColor(view: T, colors: ColorStateList) {
                view.setTextColor(colors)
            }
        })

        addAttributeProcessor(Attributes.TextView.TextColorHint, object : ColorResourceProcessor<T>() {

            override fun setColor(view: T, color: Int) {
                view.setHintTextColor(color)
            }

            override fun setColor(view: T, colors: ColorStateList) {
                view.setHintTextColor(colors)
            }
        })

        addAttributeProcessor(Attributes.TextView.TextColorLink, object : ColorResourceProcessor<T>() {

            override fun setColor(view: T, color: Int) {
                view.setLinkTextColor(color)
            }

            override fun setColor(view: T, colors: ColorStateList) {
                view.setLinkTextColor(colors)
            }
        })

        addAttributeProcessor(Attributes.TextView.TextColorHighLight, object : ColorResourceProcessor<T>() {

            override fun setColor(view: T, color: Int) {
                view.highlightColor = color
            }

            override fun setColor(view: T, colors: ColorStateList) {
                //
            }
        })

        addAttributeProcessor(Attributes.TextView.DrawableLeft, object : DrawableResourceProcessor<T>() {
            override fun setDrawable(view: T, drawable: Drawable) {
                val compoundDrawables = view.compoundDrawables
                view.setCompoundDrawablesWithIntrinsicBounds(drawable, compoundDrawables[1], compoundDrawables[2], compoundDrawables[3])
            }
        })
        addAttributeProcessor(Attributes.TextView.DrawableTop, object : DrawableResourceProcessor<T>() {
            override fun setDrawable(view: T, drawable: Drawable) {
                val compoundDrawables = view.compoundDrawables
                view.setCompoundDrawablesWithIntrinsicBounds(compoundDrawables[0], drawable, compoundDrawables[2], compoundDrawables[3])
            }
        })
        addAttributeProcessor(Attributes.TextView.DrawableRight, object : DrawableResourceProcessor<T>() {
            override fun setDrawable(view: T, drawable: Drawable) {
                val compoundDrawables = view.compoundDrawables
                view.setCompoundDrawablesWithIntrinsicBounds(drawable, compoundDrawables[1], drawable, compoundDrawables[3])
            }
        })
        addAttributeProcessor(Attributes.TextView.DrawableBottom, object : DrawableResourceProcessor<T>() {
            override fun setDrawable(view: T, drawable: Drawable) {
                val compoundDrawables = view.compoundDrawables
                view.setCompoundDrawablesWithIntrinsicBounds(drawable, compoundDrawables[1], compoundDrawables[2], drawable)
            }
        })

        addAttributeProcessor(Attributes.TextView.MaxLines, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                view.maxLines = ParseHelper.parseInt(value)
            }
        })

        addAttributeProcessor(Attributes.TextView.Ellipsize, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                val ellipsize = ParseHelper.parseEllipsize(value)
                view.ellipsize = ellipsize as android.text.TextUtils.TruncateAt
            }
        })

        addAttributeProcessor(Attributes.TextView.PaintFlags, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                if (value == "strike") {
                    view.paintFlags = view.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
        })

        addAttributeProcessor(Attributes.TextView.Prefix, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                view.text = value + view.text
            }
        })

        addAttributeProcessor(Attributes.TextView.Suffix, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                view.text = view.text.toString() + value
            }
        })

        addAttributeProcessor(Attributes.TextView.SingleLine, object : BooleanAttributeProcessor<T>() {
            override fun setBoolean(view: T, value: Boolean) {
                view.setSingleLine(value)
            }
        })

        addAttributeProcessor(Attributes.TextView.TextAllCaps, object : BooleanAttributeProcessor<T>() {
            override fun setBoolean(view: T, value: Boolean) {
                view.isAllCaps = value
            }
        })
        addAttributeProcessor(Attributes.TextView.Hint, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                view.hint = value
            }
        })
        addAttributeProcessor(Attributes.TextView.IncludeFontPadding, object : BooleanAttributeProcessor<T>() {
            override fun setBoolean(view: T, value: Boolean) {
                view.includeFontPadding = value
            }
        })
        var fontFamily: String? = null
        var textStyle: String? = null
        addAttributeProcessor(Attributes.TextView.FontFamily, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String?) {
                fontFamily = value
                setFontFamilyStyle(view, fontFamily, textStyle)
            }
        })
        addAttributeProcessor(Attributes.TextView.TextStyle, object : StringAttributeProcessor<T>() {
            override fun setString(view: T, value: String) {
                textStyle = value
                setFontFamilyStyle(view, fontFamily, textStyle)
            }
        })
    }
    
    /**
     * When the fontFamily and textStyle attributes are declared together in layout,
     * the style of the text must be handled considering both.
     */
    private fun setFontFamilyStyle(view: T, fontFamily: String?, textStyle: String?) {
        if (textStyle == null && fontFamily != null) {
            view.typeface = Typeface.create(fontFamily, Typeface.NORMAL)
        } else if (textStyle != null && fontFamily == null) {
            val typeface = ParseHelper.parseTextStyle(textStyle)
            view.typeface = Typeface.defaultFromStyle(typeface)
        } else if (textStyle != null && fontFamily != null) {
            val typeface = ParseHelper.parseTextStyle(textStyle)
            view.typeface = Typeface.create(fontFamily, typeface)
        }
    }
}
