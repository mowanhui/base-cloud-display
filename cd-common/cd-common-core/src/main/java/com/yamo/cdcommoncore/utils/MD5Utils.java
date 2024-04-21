package com.yamo.cdcommoncore.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MD5Utils {
    public String convertMd5(String str){
        char[] a=str.toCharArray();
        for(int i=0;i<a.length;i++){
            a[i]=(char) (a[i]^'t');
        }
        String s=new String(a);
        return s;
    }

    public static void main(String[] args) {
    }
}
