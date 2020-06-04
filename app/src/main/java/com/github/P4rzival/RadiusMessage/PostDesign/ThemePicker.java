package com.github.P4rzival.RadiusMessage.PostDesign;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//Just a quick and easy color theme picker, can go crazy and expand on it if wanted!
public class ThemePicker {

    //Implement later as a class, something the user can change or pick out
    //current palette https://colorhunt.co/palette/42617
    //could use xml file with a ui for the user to pick their own colors!
    private List<Integer> palette = new ArrayList<Integer>(){{
        add(0xf47c7c);
        add(0xf7f48b);
        add(0xa1de93);
        add(0x70a1d7);
        add(0x58b4ae);
        add(0xffacb7);
        add(0xfec771);
        add(0x79d70f);
        add(0x45046a);
        add(0xffd31d);
    }};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public int getRandomColorFromPalatte(){

        //int randomColorIndex = ThreadLocalRandom.current().nextInt(0, 4);
        Random rNumber = new Random();
        int randomColorIndex = rNumber.nextInt(palette.size());
        int currentColor = palette.get(randomColorIndex);

        int a = 145;
        int r = (currentColor & 0xFF0000) >> 16;
        int g = (currentColor & 0xFF00) >> 8;
        int b = (currentColor & 0xFF);

        int newPostColor = Color.argb(a,r,g,b);
        return newPostColor;

    }

    public int getColorFromPalatte(int colorIndex){

        if (colorIndex >= palette.size()){
            colorIndex -= 1;
        }

        int currentColor = palette.get(colorIndex);

        int a = 145;
        int r = (currentColor & 0xFF0000) >> 16;
        int g = (currentColor & 0xFF00) >> 8;
        int b = (currentColor & 0xFF);

        int newPostColor = Color.argb(a,r,g,b);
        return newPostColor;
    }
}
