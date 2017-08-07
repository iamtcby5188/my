package com.sumscope.bab.quote.commons.util;

public final class Utils {
    private  static final String[] SPECIAL_CHARACTERS = new String[]{"'","‘","select","having","ltrim","(",")","=","|",":",";","and"};

    private Utils(){}

    public static String validateStr(String param){
        String newParam = param;
        if(newParam == null){
            return null;
        }
        for(int i=0;i<SPECIAL_CHARACTERS.length;i++){
            if(newParam.contains(SPECIAL_CHARACTERS[i])){
                newParam= newParam.replace(SPECIAL_CHARACTERS[i],"");
            }
        }
        return newParam;
    }

    public static int compareUtils(int param){
       if(param > 0){
            return 1;
       }else if(param <= 0){
            return 0;
        }else{
            return -1;
        }
    }
}
