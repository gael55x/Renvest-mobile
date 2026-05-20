package com.business.renvest.utils

import android.content.DialogInterface
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.business.renvest.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

fun AppCompatActivity.showValidatedFormDialog(
    @StringRes titleRes: Int,
    dialogView: View,
    vararg requiredFields: TextInputLayout,
    onSave: () -> Unit,
): AlertDialog {
    val dialog = MaterialAlertDialogBuilder(this)
        .setTitle(titleRes)
        .setView(dialogView)
        .setNegativeButton(android.R.string.cancel, null)
        .setPositiveButton(R.string.action_save) { _, _ -> onSave() }
        .create()
    dialog.enableSaveWhenRequiredFieldsFilled(*requiredFields)
    dialog.show()
    return dialog
}

/**
 * Keeps the positive action disabled until every [requiredFields] layout has non-blank text.
 */
fun AlertDialog.enableSaveWhenRequiredFieldsFilled(vararg requiredFields: TextInputLayout) {
    setOnShowListener {
        val saveButton = getButton(DialogInterface.BUTTON_POSITIVE) ?: return@setOnShowListener
        val validate = {
            val allFilled = requiredFields.all { layout ->
                !layout.editText?.text?.toString().orEmpty().trim().isEmpty()
            }
            saveButton.isEnabled = allFilled
        }
        requiredFields.forEach { layout ->
            layout.editText?.doAfterTextChanged { validate() }
        }
        validate()
    }
}

fun requiredFieldsValid(vararg requiredFields: TextInputLayout): Boolean =
    requiredFields.all { !it.editText?.text?.toString().orEmpty().trim().isEmpty() }
