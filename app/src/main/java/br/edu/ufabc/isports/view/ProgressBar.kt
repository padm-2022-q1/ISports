package br.edu.ufabc.isports.view

import android.view.View
import com.google.android.material.progressindicator.LinearProgressIndicator

class ProgressBar(private val view: LinearProgressIndicator) {
    private var attempts = 0

    fun start() {
        attempts++
        view.visibility = View.VISIBLE
    }

    fun stop() {
        if (--attempts < 1) {
            view.visibility = View.INVISIBLE
        }
    }
}
