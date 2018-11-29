package br.lorena.ufop.mymoneylopeslorena

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : Activity(), View.OnClickListener {

    private val TAG = "MainActivity"
    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""
    private var firestoreListener: ListenerRegistration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        app_add.setOnClickListener(this)
        app_neg.setOnClickListener(this)
        app_listar.setOnClickListener(this)

        firestoreDB = FirebaseFirestore.getInstance()

        somar()
    }

    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.app_add) {
            addReceita()
        } else if (id == R.id.app_neg) {
            addDespesa()
        } else if (id == R.id.app_listar) {
            historico()
        }
    }

    private fun addReceita() {
        val intent = Intent(this, ReceitaActivity::class.java)
        startActivity(intent)

    }

    private fun addDespesa() {
        val intent = Intent(this, DespesaActivity::class.java)
        startActivity(intent)
    }

    private fun historico() {
        val intent = Intent(this, ListarActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("ResourceAsColor")
    private fun somar() {

        firestoreListener = firestoreDB!!.collection("historico")
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

                var soma: Double = 0.0

                for(i  in historicoList){
                    soma += i.valor!!
                }
                edit_Saldo.text = soma.toString()

                if(soma>0){
                    edit_Situacao.text = "Sobrando dinheiro pra breja"
                    edit_Situacao.setBackgroundColor(resources.getColor(R.color.green))
                }else{
                    edit_Situacao.text = "Vou ter que pedir dinheiro pra vovó"
                    edit_Situacao.setBackgroundColor(resources.getColor(R.color.red))
                }
            })
    }
}