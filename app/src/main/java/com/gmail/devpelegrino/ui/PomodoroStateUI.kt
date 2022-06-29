package com.gmail.devpelegrino.ui

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

class PomodoroStateUI(
    val name: String,
    @ColorInt val primaryColor: Int,
    @ColorInt val secondaryColor: Int,
    @ColorInt val tertiaryColor: Int,
    val iconSrc: Drawable
)