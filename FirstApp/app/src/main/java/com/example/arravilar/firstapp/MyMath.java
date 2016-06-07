package com.example.arravilar.firstapp;

/**
 * Created by Przemysław on 2016-06-07.
 */
public class MyMath {
    private int precision  = 10000;
    private static MyMath myMath = null;
    private MyMath()
    {
    }
    public int mod(int x, int y)
    {
        int result = x % y;
        if (result < 0)
            result += y;
        return result;
    }
    public double round(double d) {
        double result = d * precision;
        result = Math.round(result);
        result = result / precision;
    return result;
    }
    public double round(double d,int precision) {
        double result = d * precision;
        result = Math.round(result);
        result = result / precision;
        return result;
    }
    public static MyMath getInstance()
    {
        if (myMath == null) {
            myMath = new MyMath();
        }
        return myMath;
    }

}
