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
import androidx.constraintlayout.widget.ConstraintLayout
import com.flipkart.android.proteus.ProteusContext
import com.flipkart.android.proteus.ProteusView
import com.flipkart.android.proteus.ViewTypeParser
import com.flipkart.android.proteus.value.Layout
import com.flipkart.android.proteus.value.ObjectValue
import com.flipkart.android.proteus.view.ProteusConstraintLayout

/**
 * Created by Dario Brux on 02/08/2019.
 */
class ConstraintLayoutParser<T : ConstraintLayout> : ViewTypeParser<T>() {

    override fun getType(): String {
        return "androidx.constraintlayout.widget.ConstraintLayout"
    }

    override fun getParentType(): String? {
        return "ViewGroup"
    }

    override fun createView(context: ProteusContext, layout: Layout, data: ObjectValue, parent: ViewGroup?, dataIndex: Int): ProteusView {
        return ProteusConstraintLayout(context)
    }

    override fun addAttributeProcessors() {
        // Do nothing
    }
}
