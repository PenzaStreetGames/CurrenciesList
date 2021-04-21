package com.penzastreetstudios.currencieslist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.loader.ResourcesLoader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    RecyclerView currenciesList;
    CurrencyAdapter adapter;
    ArrayList<Valute> valutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currenciesList = findViewById(R.id.currencyList);
        adapter = new CurrencyAdapter();
        currenciesList.setAdapter(adapter);
        currenciesList.setLayoutManager(new LinearLayoutManager(this));

        Gson gson = new Gson();
        StringBuilder jsonRaw = new StringBuilder();
        Scanner in = new Scanner(this.getResources().openRawResource(R.raw.currencies));
        while (in.hasNextLine()) {
            jsonRaw.append(in.nextLine());
        }

        HashMap<String, Bitmap> flags = new HashMap<String, Bitmap>();
        flags.put("USD", BitmapFactory.decodeResource(getResources(), R.drawable.flag_united_states));
        flags.put("GBP", BitmapFactory.decodeResource(getResources(), R.drawable.flag_united_kingdom));
        flags.put("EUR", BitmapFactory.decodeResource(getResources(), R.drawable.flag_european_union));
        flags.put("JPY", BitmapFactory.decodeResource(getResources(), R.drawable.flag_japan));
        flags.put("UAH", BitmapFactory.decodeResource(getResources(), R.drawable.flag_ukraine));
        flags.put("CNY", BitmapFactory.decodeResource(getResources(), R.drawable.flag_china));

        String json = jsonRaw.toString();
        Type type = new TypeToken<Map<String, Map<String, String>>>(){}.getType();
        Map<String, Map<String, String>> read = gson.fromJson(json, type);
        for (String code : read.keySet()) {
            String name = read.get(code).get("name");
            Bitmap flag = flags.get(code);
            Valute valute = new Valute(code, name, flag, 0);
            valutes.add(valute);
        }
        adapter.setItems(valutes);

        registerReceiver(receiver, new IntentFilter(CurrencyService.CHANNEL));
        Intent intent = new Intent(this, CurrencyService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, CurrencyService.class);
        stopService(intent);
    }

    protected BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                JSONObject json = new JSONObject(intent.getStringExtra(CurrencyService.INFO));
                JSONObject rates = json.getJSONObject("rates");

                for (Valute valute : valutes) {
                    valute.rate = 1 / rates.getDouble(valute.code);
                }
                adapter.clearItems();
                adapter.setItems(valutes);
                /*
                //Type type = new Collection<Weather>();

                Type listOfWeather = new TypeToken<ArrayList<Weather>>() {}.getType();

                Gson gson = new Gson();
                List<Weather> list = gson.fromJson(gisArray.toString(), listOfWeather);

                ArrayList<WeatherFragment> fragments = new ArrayList<>();
                fragments.add(new WeatherFragment(list.get(0)));
                for (int i = 0; i < 4; i++) {
                    fragments.add(new WeatherFragment(list.get(i)));
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.fullDayFragment, fragments.get(0));
                transaction.replace(R.id.morningFragment, fragments.get(1));
                transaction.replace(R.id.dayFragment, fragments.get(2));
                transaction.replace(R.id.eveningFragment, fragments.get(3));
                transaction.replace(R.id.nightFragment, fragments.get(4));

                transaction.commit();
                */
            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, "Wring JSON format", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    };
}