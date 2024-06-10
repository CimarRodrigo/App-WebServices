package com.example.web_services;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //
    //https://192.168.18.44/inf325/listado.php
    public static String myUrl = "https://proferey.com/325/ws/listado.php";
    private EditText edtCodigo;
    private EditText edtDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        //new LoadAsync().execute(myUrl);
    }

    public void listar(View v){
        new LoadAsync().execute(myUrl);
    }

    public void initComponents(){
        edtCodigo = findViewById(R.id.edtCod);
        edtDesc = findViewById(R.id.edtDesc);
    }

    public void uploadInfo(View v) throws MalformedURLException {
        //TODO
        // https://proferey.com/325/ws/reg.php?cod=352&msj=funciona

        String request = "https://proferey.com/325/ws/reg.php?cod="+edtCodigo.getText().toString() +"&msj="+edtDesc.getText().toString();

        URL site = new URL(request);
        new LoadAsync().execute(request);
    }

    public String downloadInfo(String url) {
        InputStream is = null;
        String result = "";
        try {
            URL site = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) site.openConnection();
            is = conn.getInputStream();
            if(is != null) {

                result = convertToString(is);

            }
            else{
                result = "No se pudo establecer la conexión";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int countSemiColon = 0;
        String res = "";
        boolean sw = false;
        for(int i = 0; i < result.length(); i++){
            if(result.charAt(i) == '>'){
                sw = true;
                continue;
            }
            if(result.charAt(i) == '<'){
                sw = false;
            }
            if(sw){
                if (result.charAt(i) == ';'){
                    countSemiColon++;
                    if(countSemiColon == 2){
                        res += "\n";
                        countSemiColon = 0;
                    }else{
                        res += " ";
                    }
                }

                else{
                    res += result.charAt(i);
                }

            }
        }

        return res;
    }

    public String convertToString(InputStream is) throws IOException {
        //java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        String result = "";
        while((line = reader.readLine()) != null){

            result += line + "\n";

        }
        reader.close();
        return result;
        //return s.hasNext() ? s.next() : "";
    }

    class LoadAsync extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            System.out.println(Arrays.toString(strings) + "AJJAK");
            return downloadInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            TextView tv = findViewById(R.id.TextView1);
            tv.setText(s);
        }
    }

}