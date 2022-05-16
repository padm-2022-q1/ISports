package br.edu.ufabc.isports.model.dto

import br.edu.ufabc.isports.model.objects.Jogo
import java.util.Date

data class JogoDTO(
    var id: String? = null,
    var modalidade: String? = null,
    var inicio: Date? = null,
    var fim: Date? = null,
    var local: String? = null,
    var participantes: List<ParticipanteDTO> = emptyList()
) {
    private fun error(property: String): Nothing = throw Exception("Property $property should not be null")

    fun toJogo() = Jogo(
        id = id ?: error("id"),
        modalidade = modalidade ?: error("modalidade"),
        inicio = inicio ?: error("inicio"),
        fim = fim ?: error("fim"),
        local = local ?: error("local"),
        participantes = participantes.map { it.toParticipantes() }
    )
}
