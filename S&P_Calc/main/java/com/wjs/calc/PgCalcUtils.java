package com.wjs.calc;

import java.math.BigInteger;

import static java.lang.Math.max;

public class PgCalcUtils {

    public static String add(String str1, String str2, int radix, int bit){
        String result=new BigInteger(str1,radix).add(new BigInteger(str2,radix)).toString(2);
        if (result.length()>bit){
            result=null;
        }else{
            result=new BigInteger(result,2).toString(radix);
        }
        return result;
    }
    public static String sub(String str1, String str2, int radix, int bit){
        BigInteger tempResult=new BigInteger(str1,radix).subtract(new BigInteger(str2,radix));
        String result="";
        if (tempResult.compareTo(new BigInteger("0"))<0){
            result=null;
        }else{
            result=tempResult.toString(radix);
        }
        return result;
    }
    public static String mul(String str1, String str2, int radix, int bit){
        String result=new BigInteger(str1,radix).multiply(new BigInteger(str2,radix)).toString(2);
        if (result.length()>bit){
            result=null;
        }else{
            result=new BigInteger(result,2).toString(radix);
        }
        return result;
    }
    public static String div(String str1, String str2, int radix, int bit){
        String result="";
        if(new BigInteger(str2,radix).equals(new BigInteger("0"))){
            result=null;
        }else{
            result=new BigInteger(str1,radix).divide(new BigInteger(str2,radix)).toString(radix);
        }
        return result;
    }

    public static String And(String str1, String str2, int radix, int bit){
        StringBuilder result=new StringBuilder();
        str1 = new BigInteger(str1, radix).toString(2);
        str2 = new BigInteger(str2, radix).toString(2);
        int str1Len=str1.length(), str2Len=str2.length();
        if(str1Len<str2Len) {
            for (int i = str1Len; i < str2Len; i++) {
                str1 = "0" + str1;
            }
        }
        if(str1Len>str2Len) {
            for (int i = str2Len; i < str1Len; i++) {
                str2 = "0" + str2;
            }
        }
        char[] arr1=str1.toCharArray(), arr2=str2.toCharArray();
        for(int i = 0; i < max(str1Len,str2Len); i++){
            if(arr1[i]=='0' || arr2[i]=='0'){
                result.append("0");
            }else{
                result.append("1");
            }
        }
        result.replace(0,result.length(),new BigInteger(result.toString(),2).toString(radix));
        return result.toString();
    }

    public static String Or(String str1, String str2, int radix, int bit){
        StringBuilder result=new StringBuilder();
        str1 = new BigInteger(str1, radix).toString(2);
        str2 = new BigInteger(str2, radix).toString(2);
        int str1Len=str1.length(), str2Len=str2.length();
        if(str1Len<str2Len) {
            for (int i = str1Len; i < str2Len; i++) {
                str1 = "0" + str1;
            }
        }
        if(str1Len>str2Len) {
            for (int i = str2Len; i < str1Len; i++) {
                str2 = "0" + str2;
            }
        }
        char[] arr1=str1.toCharArray(), arr2=str2.toCharArray();
        for(int i = 0; i < max(str1Len,str2Len); i++){
            if(arr1[i]=='1' || arr2[i]=='1'){
                result.append("1");
            }else{
                result.append("0");
            }
        }
        result.replace(0,result.length(),new BigInteger(result.toString(),2).toString(radix));
        return result.toString();
    }

    public static String Xor(String str1, String str2, int radix, int bit){
        StringBuilder result=new StringBuilder();
        str1 = new BigInteger(str1, radix).toString(2);
        str2 = new BigInteger(str2, radix).toString(2);
        int str1Len=str1.length(), str2Len=str2.length();
        if(str1Len<str2Len) {
            for (int i = str1Len; i < str2Len; i++) {
                str1 = "0" + str1;
            }
        }
        if(str1Len>str2Len) {
            for (int i = str2Len; i < str1Len; i++) {
                str2 = "0" + str2;
            }
        }
        char[] arr1=str1.toCharArray(), arr2=str2.toCharArray();
        for(int i = 0; i < max(str1Len,str2Len); i++){
            if(arr1[i]==arr2[i]){
                result.append("0");
            }else{
                result.append("1");
            }
        }
        result.replace(0,result.length(),new BigInteger(result.toString(),2).toString(radix));
        return result.toString();
    }

    public static String Not(String str, int radix, int bit){
        StringBuilder result=new StringBuilder();
        str = new BigInteger(str, radix).toString(2);
        for(int i=str.length();i<bit;i++){
            str="0"+str;
        }
        char[] arr = str.toCharArray();
        for (int i = 0; i < bit; i++) {
            if (arr[i] == '1') {
                result.append("0");
            } else {
                result.append("1");
            }
        }
        str = new BigInteger(result.toString(),2).toString(radix);
        return str;
    }

}
