package com.gmail.devpelegrino.ui.settings

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
import com.gmail.devpelegrino.databinding.DialogSettingsBinding
import com.gmail.devpelegrino.util.Constants

class SettingsDialogFragment : DialogFragment() {

    private lateinit var binding: DialogSettingsBinding
    private lateinit var viewModel: SettingsDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSettingsBinding.inflate(inflater, container, false)
        setupSettingsDialog()
        setListeners()
        setObservables()
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
            openDialogNumberPicker(Constants.MAX_FOCUS_MINUTES, focusLength)
        }
        shortBreakLength.setOnClickListener {
            openDialogNumberPicker(Constants.MAX_SHORT_BREAK_MINUTES, shortBreakLength)
        }
        longBreakLength.setOnClickListener {
            openDialogNumberPicker(Constants.MAX_LONG_BREAK_MINUTES, longBreakLength)
        }
        pomodoroUntilLongBreakLength.setOnClickListener {
            openDialogNumberPicker(Constants.MAX_UNTIL_LONG_BREAK, pomodoroUntilLongBreakLength)
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
        englishSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setEnglish(isChecked)
        }
    }

    private fun setObservables() = viewModel.run {
        isCloseDialog.observe(viewLifecycleOwner) { isClose ->
            if(isClose) {
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
            notificationsAction(it)
        }
        isSound.observe(viewLifecycleOwner) {
            binding.soundSwitch.isChecked = it
            soundAction(it)
        }
        isDarkMode.observe(viewLifecycleOwner) {
            binding.darkModeSwitch.isChecked = it
            darkModeAction(it)
        }
        isEnglish.observe(viewLifecycleOwner) {
            binding.englishSwitch.isChecked = it
            englishAction(it)
        }
    }

    private fun close() {
        requireActivity().onBackPressed()
    }

    private fun notificationsAction(isEnable: Boolean) {
        //TODO: Enabled/Disabled notifications
    }

    private fun soundAction(isEnable: Boolean) {
        //TODO: Enabled/Disabled sounds
    }

    private fun darkModeAction(isEnable: Boolean) {
        //TODO: Enabled/Disabled darkMode
    }

    private fun englishAction(isEnable: Boolean) {
        //TODO: Enabled/Disabled english
    }

    private fun setupSettingsDialog() {
        activity?.application?.let { application ->
            viewModel = ViewModelProvider(
                this,
                SettingsDialogViewModel.SettingsDialogViewModelFactory(application)
            )[SettingsDialogViewModel::class.java]
            lifecycle.addObserver(viewModel)
        }
    }

    private fun openDialogNumberPicker(maxValueNumber: Int, textView: TextView) {
        val alert = AlertDialog.Builder(context)
        val dialogView = layoutInflater.inflate(R.layout.dialog_number_picker, null)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.dialogNumberPicker)

        //TODO: config dialog numberpicker
        alert.setTitle("Title")
        alert.setMessage("Message")
        alert.setView(dialogView)
        numberPicker.minValue = 1
        numberPicker.maxValue = maxValueNumber
        numberPicker.wrapSelectorWheel = false
        alert.setPositiveButton(getString(R.string.dialog_number_positive_button)) { _, _ ->
            setNumberPickInField(
                text = numberPicker.value.toString(),
                textView = textView
            )
        }
        alert.setNegativeButton(getString(R.string.dialog_number_negative_button)) { _, _ -> }
        alert.create().show()
    }

    private fun setNumberPickInField(text: String, textView: TextView) {
        //TODO: update sqlRoom
        textView.text = text
    }
}