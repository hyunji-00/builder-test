/*
 * Copyright (C) Inswave Systems, Inc - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Inswave Systems <webmaster@inswave.com>, 2022
 */

package inswave.wmatrix.template

import android.content.DialogInterface
import androidx.annotation.MainThread
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * @author tarkarn
 * @since 2022.04.06
 *
 * Template 에서 사용하기 위한 Dialog.
 * Custom 한 Dialog 를 사용하려는 경우에는 해당 파일을 제거 후
 * TemplateActivity 에서 사용하는 부분을 대체 하면 된다.
 */
object DialogUtils {

    @MainThread
    fun exit(dialog: MaterialAlertDialogBuilder, title: String, message: String, onPositiveButtonClicked: () -> Unit) {
        dialog.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(android.R.string.ok) {_,_ ->
                onPositiveButtonClicked()
            }
            setNegativeButton(android.R.string.cancel, null)
            setCancelable(false)
        }.show().getButton(DialogInterface.BUTTON_NEGATIVE).clearFocus()
    }

    @MainThread
    fun forceExit(dialog: MaterialAlertDialogBuilder, title: String, message: String, onPositiveButtonClicked: () -> Unit) {
        dialog.apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(android.R.string.ok) {_,_ ->
                onPositiveButtonClicked()
            }
            setCancelable(false)
        }.show().getButton(DialogInterface.BUTTON_NEGATIVE).clearFocus()
    }
}