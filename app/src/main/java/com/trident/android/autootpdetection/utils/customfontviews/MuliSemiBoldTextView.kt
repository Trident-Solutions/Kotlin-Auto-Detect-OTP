package com.trident.android.autootpdetection.utils.customfontviews

import android.content.Context
import android.util.AttributeSet
import com.trident.android.autootpdetection.R
import com.trident.android.autootpdetection.utils.customfontviews.FontCache.getTypeface

class MuliSemiBoldTextView : androidx.appcompat.widget.AppCompatTextView {
    constructor(context: Context) : super(context) {}

    private fun applyCustomFont(font: Context) {
        val customFont = getTypeface("Muli-SemiBold.ttf", font)
        setTypeface(customFont)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        applyCustomFont(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        applyCustomFont(context)
    }
}
