package com.gmail.devpelegrino.util

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

class PomodoroState(
    val name: String,
    @ColorInt val primaryColor: Int,
    @ColorInt val secondaryColor: Int,
    @ColorInt val tertiaryColor: Int,
    val iconSrc: Drawable
)