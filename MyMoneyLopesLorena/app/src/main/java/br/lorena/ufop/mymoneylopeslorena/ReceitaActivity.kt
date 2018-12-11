package br.lorena.ufop.mymoneylopeslorena

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_receita.*
import java.lang.NumberFormatException
import java.util.*

class ReceitaActivity : Activity(), View.OnClickListener {


    private val TAG = "ReceitaActivity"
    private var firestoreDB: FirebaseFirestore? = null
    internal var id: String = ""
    private lateinit var calendar: Calendar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receita)


        firestoreDB = FirebaseFirestore.getInstance()

        button_Salvar.setOnClickListener(this)

        calendar = Calendar.getInstance()

        edit_Dia.setOnClickListener(this)
        edit_Mes.setOnClickListener(this)
        edit_Ano.setOnClickListener(this)


        edit_Dia.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        edit_Mes.text = (calendar.get(Calendar.MONTH) + 1).toString()
        edit_Ano.text = calendar.get(Calendar.YEAR).toString()


    }

    override fun onClick(v: View) {
        val idV = v.id

        if (idV == R.id.button_Salvar) {
            val valor = edit_Valor.text.toString()
            val dia = edit_Dia.text.toString()
            val mes = edit_Mes.text.toString()
            val ano = edit_Ano.text.toString()
            val extra = edit_extra.text.toString()
            var tipo = 0

            if (button_Salario.isChecked) {
                tipo = 7
            } else if (button_Premio.isChecked) {
                tipo = 8
            } else if (button_Presente.isChecked) {
                tipo = 9
            } else {
                tipo = 10
            }

            if (valor.isNotEmpty() && dia.isNotEmpty() && mes.isNotEmpty() && ano.isNotEmpty() && valor != "." &&
                valor.toDouble() >= 0 && dia.toInt() > 0 && dia.toInt() <= 31 && mes.toInt() > 0 && mes.toInt() <= 12 && ano.toInt() >= 2018
                && ano.length == 4 && tipo != 0 && tipo == 10 && extra.isNotEmpty() && extra != "" ) {

                try {
                    add(valor.toDouble(), dia.toInt(), mes.toInt(), ano.toInt(), extra, tipo)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } catch (nfe: NumberFormatException) {
                    Toast.makeText(applicationContext, getString(R.string.app_valoresvalidos), Toast.LENGTH_LONG)
                        .show()
                }
            }

           else if (valor.isNotEmpty() && dia.isNotEmpty() && mes.isNotEmpty() && ano.isNotEmpty() && valor != "." &&
                valor.toDouble() >= 0 && dia.toInt() > 0 && dia.toInt() <= 31 && mes.toInt() > 0 && mes.toInt() <= 12 && ano.toInt() >= 2018
                && ano.length == 4 && tipo != 0 && tipo != 10) {

                try {
                    add(valor.toDouble(), dia.toInt(), mes.toInt(), ano.toInt(), extra, tipo)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } catch (nfe: NumberFormatException) {
                    Toast.makeText(applicationContext, getString(R.string.app_valoresvalidos), Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(applicationContext, getString(R.string.app_validos), Toast.LENGTH_LONG)
                    .show()
            }


        }
        else if (idV == R.id.edit_Dia || idV == R.id.edit_Mes || idV == R.id.edit_Ano) {

            val dateSetListener = DatePickerDialog.OnDateSetListener {
                    view, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                edit_Dia.text = dayOfMonth.toString()
                edit_Mes.text = ((monthOfYear)+1).toString()
                edit_Ano.text = year.toString()
            }

            DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

    }
    private fun add(valor: Double, dia: Int, mes: Int, ano: Int, extra: String, tipo: Int) {
        val valor = HistoricoActivity(valor, dia, mes, ano, extra, tipo).toMap()

        firestoreDB!!.collection("historico")
            .add(valor)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "Documento adicionado com ID: " + documentReference.id)
                Toast.makeText(applicationContext, "Receita adicionada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Erro", e)
                Toast.makeText(applicationContext, "Erro ao adicionar receita", Toast.LENGTH_SHORT).show()
            }
    }
}