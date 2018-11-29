package br.lorena.ufop.mymoneylopeslorena

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import android.content.Intent
import android.support.v7.widget.RecyclerView

import com.google.firebase.firestore.FirebaseFirestore

class HistoricoRecyclerViewAdapter(

    private val historicoList: MutableList<HistoricoActivity>,
    private val context: Context,
    private val firestoreDB: FirebaseFirestore
) : RecyclerView.Adapter<HistoricoRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.activity_listastyle, p0, false)

        return ViewHolder(view)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        val valor = historicoList[p1]

        if(valor.tipo.toString() == "1"){
            p0.tipo.text = "Alimentação"
            p0.tipo.setTextColor(context.getColor(R.color.red1))
        }
        else if(valor.tipo.toString() == "2"){
            p0.tipo.text = "Cartão"
            p0.tipo.setTextColor(context.getColor(R.color.red1))

        }
        else if(valor.tipo.toString() == "3"){
            p0.tipo.text = "Lazer"
            p0.tipo.setTextColor(context.getColor(R.color.red1))

        }
        else if(valor.tipo.toString() == "4"){
            p0.tipo.text = "Moradia"
            p0.tipo.setTextColor(context.getColor(R.color.red1))

        }
        else if(valor.tipo.toString() == "5"){
            p0.tipo.text = "Saúde"
            p0.tipo.setTextColor(context.getColor(R.color.red1))

        }
        else if(valor.tipo.toString() == "6"){
            p0.tipo.text = "Outros"
            p0.tipo.setTextColor(context.getColor(R.color.red1))

        }
        else if(valor.tipo.toString() == "7"){
            p0.tipo.text = "Salário"
            p0.tipo.setTextColor(context.getColor(R.color.green1))

        }
        else if(valor.tipo.toString() == "8"){
            p0.tipo.text = "Prêmio"
            p0.tipo.setTextColor(context.getColor(R.color.green1))

        }
        else if(valor.tipo.toString().equals("9")){
            p0.tipo.text = "Presente"
            p0.tipo.setTextColor(context.getColor(R.color.green1))

        }
        else{
            p0.tipo.text = "Outros"
            p0.tipo.setTextColor(context.getColor(R.color.green1))


        }


        p0.valor.text = valor.valor.toString()
        p0.dia.text = valor.dia.toString()
        p0.mes.text = valor.mes.toString()
        p0.ano.text = valor.ano.toString()


        if(valor.valor!!>0) {
            p0.itemView.setOnClickListener{updateReceita(valor)}
        }else{
            p0.itemView.setOnClickListener{updateDespesa(valor)}
        }

    }

    override fun getItemCount(): Int {
        return historicoList.size
    }


    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        internal var tipo: TextView
        internal var valor: TextView
        internal var dia: TextView
        internal var mes: TextView
        internal var ano: TextView


        init {
            tipo = view.findViewById(R.id.edit_tipo)
            valor = view.findViewById(R.id.edit_Valor)
            dia = view.findViewById(R.id.edit_Dia)
            mes = view.findViewById(R.id.edit_Mes)
            ano = view.findViewById(R.id.edit_Ano)

        }

    }

    fun updateDespesa(historico: HistoricoActivity) {
        val intent = Intent(context, AlterarExcluirDespesaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateId", historico.id)
        intent.putExtra("UpdateValor", historico.valor.toString())
        intent.putExtra("UpdateDia", historico.dia.toString())
        intent.putExtra("UpdateMes", historico.mes.toString())
        intent.putExtra("UpdateAno", historico.ano.toString())
        intent.putExtra("UpdateDescricao", historico.extra)
        intent.putExtra("UpdateTipo", historico.tipo)
        context.startActivity(intent)
    }

    fun updateReceita(historico: HistoricoActivity) {
        val intent = Intent(context, AlterarExcluirReceitaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("UpdateId", historico.id)
        intent.putExtra("UpdateValor", historico.valor.toString())
        intent.putExtra("UpdateDia", historico.dia.toString())
        intent.putExtra("UpdateMes", historico.mes.toString())
        intent.putExtra("UpdateAno", historico.ano.toString())
        intent.putExtra("UpdateDescricao", historico.extra)
        intent.putExtra("UpdateTipo", historico.tipo)
        context.startActivity(intent)
    }


}
