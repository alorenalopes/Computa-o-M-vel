package br.lorena.ufop.mymoneylopeslorena

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_listar.*

class ListarActivity : Activity(), View.OnClickListener {

    private val TAG = "ListarActivity"

    private var mAdapter: HistoricoRecyclerViewAdapter? = null
    private var firestoreDB: FirebaseFirestore? = null
    private var firestoreListener: ListenerRegistration? = null
    var mes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar)

        button_Filtro.setOnClickListener(this)

        firestoreDB = FirebaseFirestore.getInstance()

        loadList()
        criar()
    }

    override fun onDestroy() {
        super.onDestroy()

        firestoreListener!!.remove()
    }


    private fun loadList() {


        firestoreDB!!.collection("historico")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val historicoList = mutableListOf<HistoricoActivity>()

                    for (doc in task.result!!) {
                        val historico = doc.toObject<HistoricoActivity>(HistoricoActivity::class.java)
                        historico.id = doc.id
                        historicoList.add(historico)
                    }

                    mAdapter = HistoricoRecyclerViewAdapter(historicoList, applicationContext, firestoreDB!!)
                    val mLayoutManager = LinearLayoutManager(applicationContext)
                    RecyclerView.layoutManager = mLayoutManager
                    RecyclerView.itemAnimator = DefaultItemAnimator()
                    RecyclerView.adapter = mAdapter

                } else {
                    Log.d(TAG, "Erro", task.exception)
                }
            }
    }

    override fun onClick(v: View) {
      if(filtro.text.isEmpty() || filtro.text.toString().toInt() < 1 || filtro.text.toString().toInt() > 12){
          Toast.makeText(this,"Digite um mês válido",Toast.LENGTH_LONG).show()
      }else{
          mes = filtro.text.toString().toInt()
          filtrar()
      }
        }

    private fun criar(){
        firestoreListener = firestoreDB!!.collection("historico").orderBy("mes").orderBy("dia")
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

                mAdapter = HistoricoRecyclerViewAdapter(historicoList, applicationContext, firestoreDB!!)
                RecyclerView.adapter = mAdapter

            })
    }

    private fun filtrar(){
        firestoreListener = firestoreDB!!.collection("historico").whereEqualTo("mes", mes).orderBy("dia")
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

                mAdapter = HistoricoRecyclerViewAdapter(historicoList, applicationContext, firestoreDB!!)
                RecyclerView.adapter = mAdapter

            })
    }


}