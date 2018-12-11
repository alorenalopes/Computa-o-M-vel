package br.lorena.ufop.mymoneylopeslorena

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_graficofiltro.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.view.PieChartView


class GraficofiltroActivity : Activity(), View.OnClickListener {

    private val TAG = "GraficoFiltroActivity"
    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""
    private var firestoreListener: ListenerRegistration? = null
    var mes = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graficofiltro)

        button_Filtro.setOnClickListener(this)

        firestoreDB = FirebaseFirestore.getInstance()

        val bundle = intent.extras
        if (bundle != null){
            mes = bundle.getInt("mes")
        }

        firestoreListener = firestoreDB!!.collection("historico").whereEqualTo("mes", mes)
            .addSnapshotListener(EventListener { documentSnapshots, e ->
                if (e != null) {
                    Log.e(TAG, "Erro", e)
                    return@EventListener
                }

                val historicoList = mutableListOf<HistoricoActivity>()

                for (doc in documentSnapshots!!) {
                    val historico = doc.toObject(HistoricoActivity::class.java)
                    historico.id = doc.id
                    historicoList.add(historico)
                }

                var despesa = mutableListOf<SliceValue>()
                var receita = mutableListOf<SliceValue>()
                var valoralimentacao = 0.0
                var valorcartao = 0.0
                var valorlazer = 0.0
                var valormoradia = 0.0
                var valorsaude = 0.0
                var valoroutrosD = 0.0
                var valorsalario = 0.0
                var valorpremio = 0.0
                var valorpresente = 0.0
                var valoroutrosR = 0.0

                for (i in historicoList) {
                    if (i.valor!! < 0 && i.tipo == 1) {
                        valoralimentacao += i.valor!!
                    }
                    if (i.valor!! < 0 && i.tipo == 2) {
                        valorcartao += i.valor!!
                    }
                    if (i.valor!! < 0 && i.tipo == 3) {
                        valorlazer += i.valor!!
                    }
                    if (i.valor!! < 0 && i.tipo == 4) {
                        valormoradia += i.valor!!
                    }
                    if (i.valor!! < 0 && i.tipo == 5) {
                        valorsaude += i.valor!!
                    }
                    if (i.valor!! < 0 && i.tipo == 6) {
                        valoroutrosD += i.valor!!
                    }
                    if (i.valor!! > 0 && i.tipo == 7) {
                        valorsalario += i.valor!!
                    }
                    if (i.valor!! > 0 && i.tipo == 8) {
                        valorpremio += i.valor!!
                    }
                    if (i.valor!! > 0 && i.tipo == 9) {
                        valorpresente += i.valor!!
                    }
                    if (i.valor!! > 0 && i.tipo == 10) {
                        valoroutrosR += i.valor!!
                    }
                }

                despesa.add(
                    SliceValue(
                        valoralimentacao.toFloat(),
                        this.getColor(R.color.red)
                    ).setLabel("Alimentação " + valoralimentacao)
                )
                despesa.add(
                    SliceValue(
                        valorcartao.toFloat(),
                        this.getColor(R.color.red)
                    ).setLabel("Cartão " + valorcartao)
                )
                despesa.add(
                    SliceValue(
                        valorlazer.toFloat(),
                        this.getColor(R.color.red)
                    ).setLabel("Lazer " + valorlazer)
                )
                despesa.add(
                    SliceValue(
                        valormoradia.toFloat(),
                        this.getColor(R.color.red)
                    ).setLabel("Moradia " + valormoradia)
                )
                despesa.add(
                    SliceValue(
                        valorsaude.toFloat(),
                        this.getColor(R.color.red)
                    ).setLabel("Saúde " + valorsaude)
                )
                despesa.add(
                    SliceValue(
                        valoroutrosD.toFloat(),
                        this.getColor(R.color.red)
                    ).setLabel("Outros " + valoroutrosD)
                )
                receita.add(
                    SliceValue(
                        valorsalario.toFloat(),
                        this.getColor(R.color.green)
                    ).setLabel("Salário " + valorsalario)
                )
                receita.add(
                    SliceValue(
                        valorpremio.toFloat(),
                        this.getColor(R.color.green)
                    ).setLabel("Prêmio " + valorpremio)
                )
                receita.add(
                    SliceValue(
                        valorpresente.toFloat(),
                        this.getColor(R.color.green)
                    ).setLabel("Presente " + valorpresente)
                )
                receita.add(
                    SliceValue(
                        valoroutrosR.toFloat(),
                        this.getColor(R.color.green)
                    ).setLabel("Outros " + valoroutrosR)
                )


                var line = PieChartData(despesa).setHasLabelsOnlyForSelected(true)
                var g = PieChartView(this)
                g.setPieChartData(line)
                grafico_id.addView(g)

                var line2 = PieChartData(receita).setHasLabelsOnlyForSelected(true)
                var g2 = PieChartView(this)
                g2.setPieChartData(line2)
                graficoR_id.addView(g2)
            })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(v: View) {
        if(filtro.text.isEmpty()){
            val intent = Intent(this, GraficoActivity::class.java)
            startActivity(intent)
            finish()
        }
        else if(filtro.text.toString().toInt() < 1 || filtro.text.toString().toInt() > 12){
            Toast.makeText(this,"Digite um mês válido", Toast.LENGTH_LONG).show()
        }else{
            mes = filtro.text.toString().toInt()
            val intent = Intent(this, GraficofiltroActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("mes",mes)
            startActivity(intent)
            finish()
        }
    }
}