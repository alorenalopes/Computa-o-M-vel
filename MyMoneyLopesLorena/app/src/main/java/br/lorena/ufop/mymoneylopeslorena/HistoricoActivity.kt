package br.lorena.ufop.mymoneylopeslorena

import java.util.HashMap

class HistoricoActivity {

    var valor: Double? = null
    var dia: Int? = null
    var mes: Int? = null
    var ano: Int? = null
    var extra: String? = null
    var id: String? = null
    var tipo: Int? = null

    constructor()
    constructor(id: String, valor: Double, dia: Int, mes: Int, ano: Int, extra: String, tipo: Int) {
        this.id = id
        this.valor = valor
        this.dia = dia
        this.mes = mes
        this.ano = ano
        this.extra = extra
        this.tipo = tipo
    }
    constructor(valor: Double, dia: Int, mes: Int, ano: Int, extra: String, tipo: Int) {
        this.valor = valor
        this.dia = dia
        this.mes = mes
        this.ano = ano
        this.extra = extra
        this.tipo = tipo
    }

    fun toMap(): Map<String, Any> {

        val historic = HashMap<String, Any>()
        historic.put("valor", valor!!)
        historic.put("dia", dia!!)
        historic.put("mes", mes!!)
        historic.put("ano", ano!!)
        historic.put("extra", extra!!)
        historic.put("tipo", tipo!!)


        return historic
    }
}