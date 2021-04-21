package com.penzastreetstudios.currencieslist;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class CurrencyService extends Service {

    public static final String CHANNEL = "GIS_SERVICE";
    public static final String INFO = "INFO";

    @Override
    public void onCreate() {
        // сообщение о создании службы
        //Toast.makeText(this, "Service created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // сообщение о запуске службы
        //Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();

        // создаем объект нашего AsyncTask (необходимо для работы с сетью)
        GisAsyncTask t = new GisAsyncTask();
        t.execute(); // запускаем AsyncTask

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //сообщение об остановке службы
        //Toast.makeText(this, "Service stoped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //поток работы с сетью
    private class GisAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String aVoid) {
            Intent i = new Intent(CHANNEL); // интент для отправки ответа
            i.putExtra(INFO, aVoid); // добавляем в интент данные
            sendBroadcast(i); // рассылаем
        }

        @Override
        protected String doInBackground(Void... voids) {
            String result;
            try {
                //загружаем данные
                URL url = new URL("https://www.cbr-xml-daily.ru/latest.js");

                // ”оборачиваем” для удобства чтения


                // читаем и добавляем имя JSON массива
                StringBuilder jsonRaw = new StringBuilder();
                Scanner in = new Scanner((InputStream) url.getContent());
                while (in.hasNextLine()) {
                    jsonRaw.append(in.nextLine());
                }
                result = jsonRaw.toString();
            } catch (Exception e) {
                result = e.toString();
            }
            return result;
        }
    }
}

