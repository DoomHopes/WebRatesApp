package com.doomhopes.webapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private Runnable loadRates;
    private String ratesText;
    private Runnable showRatesText;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv  = findViewById(R.id.textView);
        tv.setMovementMethod(new ScrollingMovementMethod());

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
                runOnUiThread(showRatesText);
            }
        };

        showRatesText=()-> {
            ((TextView)findViewById(R.id.textView)).setText(ratesText);
        };

    }

    public void onClick(View view) {
        (new Thread(loadRates)).start();
    }
}

