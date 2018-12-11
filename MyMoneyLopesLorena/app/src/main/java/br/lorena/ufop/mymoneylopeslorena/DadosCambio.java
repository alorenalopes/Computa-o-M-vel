package br.lorena.ufop.mymoneylopeslorena;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

public class DadosCambio {


    private Context context;
    private String euro;
    private String dolar;
    private String libras;
    private String iene;

    private boolean encontrou;

    public DadosCambio(Context context) {
        this.context = context;
        this.buscar();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public String getEuro() {
        return euro;
    }

    public void setEuro(String euro) {
        this.euro = euro;
    }

    public String getDolar() {
        return dolar;
    }

    public void setDolar(String dolar) {
        this.dolar = dolar;
    }

    public String getLibras() {
        return libras;
    }

    public void setLibras(String libras) {
        this.libras = libras;
    }

    public String getIene() {
        return iene;
    }

    public void setIene(String iene) {
        this.iene = iene;
    }

    public void setEncontrou(boolean encontrou) {
        this.encontrou = encontrou;
    }

    public boolean isEncontrou() {
        return encontrou;
    }

    public final void buscar() {


        // define a url
        String url = "https://api.exchangeratesapi.io/latest?base=USD";
        String url2 = "https://api.exchangeratesapi.io/latest?base=EUR";
        String url3 = "https://api.exchangeratesapi.io/latest?base=GBP";
        String url4 = "https://api.exchangeratesapi.io/latest?base=JPY";

        Log.d("CSI401", url);


        // define os dados
        JSONObject obj = null;
        JSONObject obj2 = null;
        JSONObject obj3 = null;
        JSONObject obj4 = null;
        try {
            obj = new JSONObject(this.get(url));
            obj2 = new JSONObject(this.get(url2));
            obj3 = new JSONObject(this.get(url3));
            obj4 = new JSONObject(this.get(url4));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!obj.has("erro") && !obj2.has("erro") && !obj3.has("erro") && !obj4.has("erro")) {
            try {

                obj = obj.getJSONObject("rates");
                Log.d("csi401", "" + obj);
                dolar = obj.getString("BRL");
                Log.d("csi401", dolar);

                obj2 = obj2.getJSONObject("rates");
                Log.d("csi401", "" + obj2);
                euro = obj2.getString("BRL");
                Log.d("csi401", euro);

                obj3 = obj3.getJSONObject("rates");
                Log.d("csi401", "" + obj3);
                libras = obj3.getString("BRL");
                Log.d("csi401", libras);

                obj4 = obj4.getJSONObject("rates");
                Log.d("csi401", "" + obj4);
                iene = obj4.getString("BRL");
                Log.d("csi401", iene);

                setEncontrou(true);
                encontrou = true;
            } catch (JSONException e) {

                e.printStackTrace();
            }
        } else {
            encontrou = false;
            Log.d("CSI401", "Não foi possível encontrar o url");
        }
    }

    public final String get(String urlToRead) {
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

        } catch (MalformedURLException | ProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result.toString();
    }
}

