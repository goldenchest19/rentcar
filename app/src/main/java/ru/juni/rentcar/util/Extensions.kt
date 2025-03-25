package ru.juni.rentcar.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Расширение для Fragment, которое показывает Toast сообщение
 */
fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

/**
 * Расширение для Context, которое показывает Toast сообщение
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
} 