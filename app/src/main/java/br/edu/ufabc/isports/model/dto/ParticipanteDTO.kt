package br.edu.ufabc.isports.model.dto

import br.edu.ufabc.isports.model.objects.Participante

data class ParticipanteDTO(
    var uid: String? = null,
    var username: String? = null
) {
    private fun error(property: String): Nothing = throw Exception("Property $property should not be null")

    fun toParticipantes() = Participante(
        uid = uid ?: error("uid"),
        username = username ?: error("username")
    )
}
