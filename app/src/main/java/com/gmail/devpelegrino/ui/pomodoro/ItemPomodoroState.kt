package com.gmail.devpelegrino.ui.pomodoro

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import com.gmail.devpelegrino.R
import com.gmail.devpelegrino.databinding.ItemPomodoroStateBinding
import com.gmail.devpelegrino.ui.PomodoroStateUI

class ItemPomodoroState @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    @ColorInt private var strokeColor: Int? = null
    @ColorInt private var backgroundColor: Int? = null
    private var iconSrc: Drawable? = null
    @ColorInt private var iconColor: Int? = null
    private var text: String? = null
    @ColorInt private var textColor: Int? = null

    private val binding = ItemPomodoroStateBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    init {
        setLayout(attrs)
    }

    fun setStateType(type: PomodoroStateUI) {
        strokeColor = type.primaryColor
        backgroundColor = type.secondaryColor
        iconSrc = type.iconSrc
        iconColor = type.primaryColor
        text = type.name
        textColor = type.primaryColor
        refreshState()
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let { attributeSet ->
            val attributes = context.obtainStyledAttributes(
                attributeSet,
                R.styleable.ItemPomodoroState
            )

            strokeColor = attributes.getColor(R.styleable.ItemPomodoroState_state_stroke_color, 0)
            backgroundColor = attributes.getColor(R.styleable.ItemPomodoroState_state_background_color, 0)
            iconSrc = attributes.getDrawable(R.styleable.ItemPomodoroState_state_icon_src)
            iconColor = attributes.getColor(R.styleable.ItemPomodoroState_state_icon_color, 0)
            text = attributes.getString(R.styleable.ItemPomodoroState_state_text)
            textColor = attributes.getColor(R.styleable.ItemPomodoroState_state_text_color, 0)

            attributes.recycle()
        }
    }

    private fun refreshState() {
        strokeColor?.let { binding.cardState.strokeColor = it }
        backgroundColor?.let { binding.cardState.setCardBackgroundColor(it) }
        iconSrc?.let { binding.iconState.setImageDrawable(it) }
        iconColor?.let { binding.iconState.setColorFilter(it) }
        text?.let { binding.textState.text = it }
        textColor?.let { binding.textState.setTextColor(it) }

        refreshDrawableState()
    }
}