package com.penzastreetstudios.currencieslist;

import android.graphics.Bitmap;

public class Valute {
    String code;
    Bitmap flag;
    String name;
    double rate;

    public Valute(String code, String name, Bitmap flag, double rate) {
        this.code = code;
        this.name = name;
        this.flag = flag;
        this.rate = rate;
    }
}
