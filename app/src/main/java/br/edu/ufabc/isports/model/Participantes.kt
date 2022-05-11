package br.edu.ufabc.isports.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Participantes(
    var uid: String,
    var username: String
) : Parcelable
