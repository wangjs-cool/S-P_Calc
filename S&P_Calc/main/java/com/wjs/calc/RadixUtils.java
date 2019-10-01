package com.wjs.calc;

import android.content.Context;

import java.math.BigInteger;

import static java.lang.Math.floor;

public class RadixUtils {
    //单位dp转化为dx
    public static int dp2px(float dp, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale+0.5f);
    }
    //单位sp转化为dx
    public static int sp2px(float sp, Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale+0.5f);
    }

    public static boolean isNum(char[] c){
        if(c[0]=='0'||c[0]=='1'||c[0]=='2'||c[0]=='3'||c[0]=='4'||c[0]=='5'||c[0]=='6'||c[0]=='7'||c[0]=='8'||c[0]=='9'||c[0]=='.'){
            return true;
        }
        return  false;
    }
    public static String double2angle(String temp){
        double num = Double.valueOf(temp);
        long Degree = (long)floor(num);
        double Rem = (num - Degree);
        long Minute = (long)floor(Double.valueOf(String.format("%.2f",Rem*60)));
        Rem = (Double.valueOf(String.format("%.2f",Rem*60)) - Minute);
        Rem=Double.valueOf(String.format("%.2f",Rem));
        double Second = Double.valueOf(String.format("%.2f",Rem*60));
        return Degree + "°" + Minute + "′" + Second + "″";
    }
    public static String angle2double(String temp){
        double num=0.0;
        if(temp.contains("°")){
            num=Double.valueOf(temp.substring(0,temp.indexOf("°",0)));
        }
        if(temp.contains("′")){
            num=num+Double.valueOf(temp.substring(temp.indexOf("°",0)+1,temp.indexOf("′",0)))/60;
        }
        if(temp.contains("″")){
            num=num+Double.valueOf(temp.substring(temp.indexOf("′",0)+1,temp.indexOf("″",0)))/3600;
        }
        num=Double.valueOf(String.format("%.9f",num));
        return num+"";
    }
    public static String angle2red(String data_r){
        for(;data_r.contains("°");){
            int start,end;
            String data_temp;
            start=data_r.indexOf("°",0);
            for(int i=0;isNum(data_r.substring(data_r.indexOf("°",0)-1-i,data_r.indexOf("°",0)-i).toCharArray());i++){
                start--;
                if(data_r.indexOf("°",0)-1-i==0){
                    break;
                }
            }
            if(data_r.contains("″")){
                end=data_r.indexOf("″",0)+1;
            }else if(data_r.contains("′")){
                end=data_r.indexOf("′",0)+1;
            }else {
                end=data_r.indexOf("°",0)+1;
            }
            data_temp=data_r.substring(start,end);
            data_r = data_r.replace(data_temp,Double.valueOf(angle2double(data_temp))/180*Math.PI+"");
        }
        return data_r;
    }
    public static String rad2angle(String s){
        s=s.replace("rad","");
        double num=Double.valueOf(s);
        return String.format("%.2f",num*180/Math.PI)+"°";
    }
    public static String bin2oct(String str){
        str=Integer.valueOf(str,2).toString();
        str=Integer.toOctalString(Integer.parseInt(str));
        return str;
    }
    public static String bin2dec(String str){
        str=Integer.valueOf(str,2).toString();
        return str;
    }
    public static String bin2hex(String str){
        str=Integer.valueOf(str,2).toString();
        str=Integer.toHexString(Integer.parseInt(str));
        return str;
    }
    public static String oct2bin(String str){
        str=Integer.valueOf(str,8).toString();
        str=Integer.toBinaryString(Integer.parseInt(str));
        return str;
    }
    public static String oct2dec(String str){
        str=Integer.valueOf(str,8).toString();
        return str;
    }
    public static String oct2hex(String str){
        str=Integer.valueOf(str,8).toString();
        str=Integer.toHexString(Integer.parseInt(str));
        return str;
    }
    public static String dec2bin(String str){
        str=Integer.toBinaryString(Integer.parseInt(str));
        return str;
    }
    public static String dec2oct(String str){
        str=Integer.toOctalString(Integer.parseInt(str));
        return str;
    }
    public static String dec2hex(String str){
        str=Integer.toHexString(Integer.parseInt(str));
        return str;
    }
    public static String hex2bin(String str){
        str=Integer.valueOf(str,16).toString();
        str=Integer.toBinaryString(Integer.parseInt(str));
        return str;
    }
    public static String hex2oct(String str){
        str=Integer.valueOf(str,16).toString();
        str=Integer.toOctalString(Integer.parseInt(str));
        return str;
    }
    public static String hex2dec(String str){
        str=Integer.valueOf(str,16).toString();
        return str;
    }
    public static String all2dec(String str,int mode){
        switch (mode){
            case 16:
                str=Integer.valueOf(str,16).toString();
                break;
            case 10:
                break;
            case 8:
                str=Integer.valueOf(str,8).toString();
                break;
            case 2:
                str=Integer.valueOf(str,2).toString();
                break;
        }
        return str;
    }
    public static String radixConv(String str, int radix_in, int radix_out){
        BigInteger tempnum=new BigInteger(str, radix_in);
        StringBuilder strbuff=new StringBuilder(tempnum.toString(radix_out));
        if(tempnum.compareTo(new BigInteger("0"))>0) {
            if (radix_out == 16) {
                int N = strbuff.length();
                int N_int = N / 4;
                int N_fra = N % 4;
                for (int i = 1; i < (N_fra == 0 ? N_int : N_int + 1); i++) {
                    strbuff.insert(N - 4 * i, " ");
                }
            }
            if (radix_out == 10) {
                int N = strbuff.length();
                int N_int = N / 3;
                int N_fra = N % 3;
                for (int i = 1; i < (N_fra == 0 ? N_int : N_int + 1); i++) {
                    strbuff.insert(N - 3 * i, ",");
                }
            }
            if (radix_out == 8) {
                int N = strbuff.length();
                int N_int = N / 3;
                int N_fra = N % 3;
                for (int i = 1; i < (N_fra == 0 ? N_int : N_int + 1); i++) {
                    strbuff.insert(N - 3 * i, " ");
                }
            }
            if (radix_out == 2) {
                int N = strbuff.length();
                int N_int = N / 4;
                for (; N % 4 != 0; ) {
                    strbuff.insert(0, "0");
                    N = strbuff.length();
                    N_int = N / 4;
                }
                for (int i = 1; i < N_int; i++) {
                    strbuff.insert(N - 4 * i, " ");
                }
            }
        }
        return strbuff.toString();
    }
}
