package com.doomhopes.webapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private Runnable loadRates;
    private String ratesText;
    private Runnable showRatesText;
    private Runnable showJson;
    private Runnable showUSDJson;
    private Runnable showEURJson;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv  = findViewById(R.id.textView);
        tv.setMovementMethod(new ScrollingMovementMethod());

        TextView tvDollar = findViewById(R.id.textView2);
        TextView tvEvro = findViewById(R.id.textView5);


        loadRates=()->{
            try(InputStream resource = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json").openStream()){
                String response = "";
                int sym;
                while((sym=resource.read())!=-1)
                    response +=(char)sym;
                ratesText = new String(response.getBytes(StandardCharsets.ISO_8859_1) , StandardCharsets.UTF_8);

            } catch (MalformedURLException e) {
                ratesText = e.getMessage();
            } catch (IOException e) {
                ratesText = e.getMessage();
            }
            finally {
                runOnUiThread(showUSDJson);
                runOnUiThread(showEURJson);
                runOnUiThread(showJson);
            }
        };

        showRatesText=()-> {
            tv.setText(ratesText);
        };

        showJson=()->{

            StringBuilder txt = new StringBuilder();

            try {
                JSONArray rates = new JSONArray(ratesText);

                for(int i=0;i<rates.length();++i){
                    JSONObject rate = rates.getJSONObject(i);
                    txt.append(rate.getString("txt")).append(" ").append(rate.getDouble("rate")).append("\n");
                }
                tv.setText(txt.toString());
            } catch (JSONException e) {
                tv.setText(e.getMessage());
            }
        };

        showUSDJson=()->{

            StringBuilder txt = new StringBuilder();

            try {
                JSONArray rates = new JSONArray(ratesText);

                for(int i=0;i<rates.length();++i){
                    JSONObject rate = rates.getJSONObject(i);
                    if(rate.getString("cc").equals("USD")) {
                        txt.append(rate.getDouble("rate"));
                    }
                }
                tvDollar.setText(txt.toString());
            } catch (JSONException e) {
                tvDollar.setText(e.getMessage());
            }
        };

        showEURJson=()->{

            StringBuilder txt = new StringBuilder();

            try {
                JSONArray rates = new JSONArray(ratesText);

                for(int i=0;i<rates.length();++i){
                    JSONObject rate = rates.getJSONObject(i);
                    if(rate.getString("cc").equals("EUR")) {
                        txt.append(rate.getDouble("rate"));
                    }
                }
                tvEvro.setText(txt.toString());
            } catch (JSONException e) {
                tvEvro.setText(e.getMessage());
            }
        };
    }

    public void onClick(View view) {
        (new Thread(loadRates)).start();
    }
}

