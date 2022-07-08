package com.gmail.devpelegrino.pomodorotimer.ui.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.gmail.devpelegrino.R
import com.gmail.devpelegrino.pomodorotimer.data.database.AppDatabase
import com.gmail.devpelegrino.pomodorotimer.data.repository.SettingsDataSource
import com.gmail.devpelegrino.databinding.DialogSettingsBinding
import com.gmail.devpelegrino.pomodorotimer.util.Constants
import com.gmail.devpelegrino.pomodorotimer.util.ThemeUtils

class SettingsDialogFragment : DialogFragment() {

    private lateinit var binding: DialogSettingsBinding
    private lateinit var viewModel: SettingsDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSettingsBinding.inflate(inflater, container, false)
        setUpSettingsDialog()
        setListeners()
        setObservers()
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    private fun setListeners() = binding.run {
        exitButton.setOnClickListener {
            viewModel.closeSettingsDialog()
        }
        focusLength.setOnClickListener {
            openDialogNumberPicker(focusLength)
        }
        shortBreakLength.setOnClickListener {
            openDialogNumberPicker(shortBreakLength)
        }
        longBreakLength.setOnClickListener {
            openDialogNumberPicker(longBreakLength)
        }
        pomodoroUntilLongBreakLength.setOnClickListener {
            openDialogNumberPicker(pomodoroUntilLongBreakLength)
        }
        autoResumeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAutoResume(isChecked)
        }
        notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setNotifications(isChecked)
        }
        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setSound(isChecked)
        }
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkMode(isChecked)
        }
    }

    private fun setObservers() = viewModel.run {
        isCloseDialog.observe(viewLifecycleOwner) { isClose ->
            if (isClose) {
                close()
            }
        }
        focusLength.observe(viewLifecycleOwner) {
            binding.focusLength.text = it.toString()
        }
        shortBreakLength.observe(viewLifecycleOwner) {
            binding.shortBreakLength.text = it.toString()
        }
        longBreakLength.observe(viewLifecycleOwner) {
            binding.longBreakLength.text = it.toString()
        }
        untilLongBreak.observe(viewLifecycleOwner) {
            binding.pomodoroUntilLongBreakLength.text = it.toString()
        }
        isAutoResume.observe(viewLifecycleOwner) {
            binding.autoResumeSwitch.isChecked = it
        }
        isNotification.observe(viewLifecycleOwner) {
            binding.notificationsSwitch.isChecked = it
        }
        isSound.observe(viewLifecycleOwner) {
            binding.soundSwitch.isChecked = it
        }
        isDarkMode.observe(viewLifecycleOwner) {
            binding.darkModeSwitch.isChecked = it
            darkModeAction(it)
        }
    }

    private fun close() {
        requireActivity().onBackPressed()
    }

    private fun darkModeAction(isEnable: Boolean) {
        ThemeUtils.changeAppTheme(isEnable)
    }

    private fun setUpSettingsDialog() {
        activity?.application?.let { application ->
            val database = AppDatabase.getDatabase(application.applicationContext)
            viewModel = ViewModelProvider(
                this,
                SettingsDialogViewModel.SettingsDialogViewModelFactory(
                    application = application,
                    settingsRepository = SettingsDataSource(database.settingsDao())
                )
            )[SettingsDialogViewModel::class.java]
            lifecycle.addObserver(viewModel)
        }
    }

    private fun openDialogNumberPicker(textView: TextView) {
        val alert = AlertDialog.Builder(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_number_picker, null)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.dialogNumberPicker)

        alert.setTitle(getString(R.string.dialog_number_title))
        alert.setView(dialogView)
        numberPicker.minValue = 1
        alert.setPositiveButton(getString(R.string.dialog_number_positive_button)) { _, _ ->
            setNumberPickInField(
                text = numberPicker.value.toString(),
                textView = textView
            )
        }
        alert.setNegativeButton(getString(R.string.dialog_number_negative_button)) { _, _ -> }

        when (textView) {
            binding.focusLength -> {
                alert.setMessage(getString(R.string.dialog_number_message_focus))
                numberPicker.maxValue = Constants.MAX_FOCUS_MINUTES
                numberPicker.value = binding.focusLength.text.toString().toInt()
            }
            binding.shortBreakLength -> {
                alert.setMessage(getString(R.string.dialog_number_message_short_break))
                numberPicker.maxValue = Constants.MAX_SHORT_BREAK_MINUTES
                numberPicker.value = binding.shortBreakLength.text.toString().toInt()
            }
            binding.longBreakLength -> {
                alert.setMessage(getString(R.string.dialog_number_message_long_break))
                numberPicker.maxValue = Constants.MAX_LONG_BREAK_MINUTES
                numberPicker.value = binding.longBreakLength.text.toString().toInt()
            }
            binding.pomodoroUntilLongBreakLength -> {
                alert.setMessage(getString(R.string.dialog_number_message_until_long))
                numberPicker.maxValue = Constants.MAX_UNTIL_LONG_BREAK
                numberPicker.value = binding.pomodoroUntilLongBreakLength.text.toString().toInt()
            }
        }
        alert.create().show()
    }

    private fun setNumberPickInField(text: String, textView: TextView) {
        textView.text = text
        when (textView) {
            binding.focusLength -> {
                viewModel.setFocusLength(text.toInt())
            }
            binding.shortBreakLength -> {
                viewModel.setShortBreakLength(text.toInt())
            }
            binding.longBreakLength -> {
                viewModel.setLongBreakLength(text.toInt())
            }
            binding.pomodoroUntilLongBreakLength -> {
                viewModel.setUntilLongBreak(text.toInt())
            }
        }
    }
}