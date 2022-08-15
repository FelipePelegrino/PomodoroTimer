package com.gmail.devpelegrino.pomodorotimer.ui.pomodoro

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.gmail.devpelegrino.R

open class PomodoroStateUI(
    val name: String,
    @ColorInt val primaryColor: Int,
    @ColorInt val secondaryColor: Int,
    @ColorInt val tertiaryColor: Int,
    val iconSrc: Drawable
)

data class FocusPomodoroStateUI(
    private val context: Context
    ): PomodoroStateUI(
    name = context.getString(R.string.focus_name),
    primaryColor = context.resources.getColor(R.color.colorFocus, context.theme),
    secondaryColor = context.resources.getColor(
        R.color.colorFocusSecondary,
        context.theme
    ),
    tertiaryColor = context.resources.getColor(
        R.color.colorFocusTertiary,
        context.theme
    ),
    iconSrc = context.resources.getDrawable(R.drawable.ic_focus, context.theme)
)

data class ShortBreakPomodoroStateUI(
    private val context: Context
): PomodoroStateUI(
    name = context.getString(R.string.short_break_name),
    primaryColor = context.resources.getColor(R.color.colorShortBreak, context.theme),
    secondaryColor = context.resources.getColor(
        R.color.colorShortBreakSecondary,
        context.theme
    ),
    tertiaryColor = context.resources.getColor(
        R.color.colorShortBreakTertiary,
        context.theme
    ),
    iconSrc = context.resources.getDrawable(R.drawable.ic_coffee, context.theme)
)

data class LongBreakPomodoroStateUI(
    private val context: Context
): PomodoroStateUI(
    name = context.getString(R.string.long_break_name),
    primaryColor = context.resources.getColor(R.color.colorLongBreak, context.theme),
    secondaryColor = context.resources.getColor(
        R.color.colorLongBreakSecondary,
        context.theme
    ),
    tertiaryColor = context.resources.getColor(
        R.color.colorLongBreakTertiary,
        context.theme
    ),
    iconSrc = context.resources.getDrawable(R.drawable.ic_coffee, context.theme)
)