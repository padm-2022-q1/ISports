package br.edu.ufabc.isports.model

import java.util.Date

data class JogoFirestore(
    var modalidade: String,
    var inicio: Date,
    var fim: Date,
    var local: String,
    var participantes: List<Participantes>
)