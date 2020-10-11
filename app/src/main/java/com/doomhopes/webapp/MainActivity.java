package com.doomhopes.webapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Runnable loadRates;
    private String ratesText;
    private Runnable showRatesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadRates=()->{
            try(InputStream resource = new URL("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json").openStream()){
                String response = "";
                int sym;
                while((sym=resource.read())!=-1)
                    response +=(char)sym;
                ratesText=response;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        showRatesText=()-> {
            ((TextView)findViewById(R.id.textView)).setText(ratesText);
        };
    }

    public void onClick(View view) {

    }
}

