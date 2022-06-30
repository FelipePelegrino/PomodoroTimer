package com.gmail.devpelegrino.ui.settings

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.gmail.devpelegrino.R
import com.gmail.devpelegrino.databinding.DialogSettingsBinding

class SettingsDialogFragment : DialogFragment() {

    private lateinit var binding: DialogSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        loadSettings()
    }

    private fun loadSettings() {
        //TODO: load settings by SQLite Room
        binding.focusLength.setOnClickListener { openDialogNumberPicker() }
    }

    private fun openDialogNumberPicker() {
        val alert = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_number_picker, null)
        alert.setTitle("Title")
        alert.setMessage("Message")
        alert.setView(dialogView)
        val numberPicker = dialogView.findViewById<NumberPicker>(R.id.dialogNumberPicker)
        numberPicker.minValue = 1
        numberPicker.maxValue = 15
        numberPicker.wrapSelectorWheel = false
        numberPicker.setOnValueChangedListener { numberPicker, i, i1 -> println("onValueChange: ") }
        alert.setPositiveButton("Done") { dialogInterface, i ->
            println("onClick: " + numberPicker.value)
        }
        alert.setNegativeButton("Cancel") { dialogInterface, i -> }
        val alertDialog = alert.create()
        alertDialog.show()
    }
}