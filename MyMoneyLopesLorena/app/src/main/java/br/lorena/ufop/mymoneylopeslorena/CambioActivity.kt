package br.lorena.ufop.mymoneylopeslorena

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cambio.*

class CambioActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambio)

        val cM = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cM.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected){
            DownloadT().execute()
        }
    }

    inner class DownloadT: AsyncTask<String, Void, DadosCambio>(){
        override fun doInBackground(vararg params: String): DadosCambio? {
            var cot: DadosCambio? = null
            try{
                cot = DadosCambio(this@CambioActivity)
            }finally {
                return cot
            }
        }

        override fun onPostExecute(result: DadosCambio) {
            if(result != null && result!!.isEncontrou) run {
                brD.setText(result!!.dolar)
                brE.setText(result!!.euro)
                brL.setText(result!!.libras)
                brI.setText(result!!.iene)
            }
        }
    }
}
