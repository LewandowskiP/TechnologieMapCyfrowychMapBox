package com.example.arravilar.firstapp;

import java.util.ArrayList;

/**
 * Created by Przemysław on 2016-06-07.
 */
public class MyColor {

    private static MyColor myColor =null;
    private int iterator;
    private ArrayList<String> colors;
    private MyColor()
    {
        iterator =0;
        colors = new ArrayList<String>();
        colors.add("#ddaa00");
        colors.add("#ddaaff");
        colors.add("#d33a00");
        colors.add("#dda110");
        colors.add("#d00000");
        colors.add("#fdaac0");
        colors.add("#4d3a00");
        colors.add("#6daa90");
        colors.add("#00faa0");
        colors.add("#00ad00");
    }
    private int mod(int x, int y)
    {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }
    public static MyColor getInstance()
    {
        if (myColor == null) {
            myColor = new MyColor();
        }
        return myColor;
    }
    public String getColor()
    {
        String toReturn = colors.get(iterator++);
        iterator = MyMath.getInstance().mod(iterator,colors.size());
        return toReturn;
    }
}
