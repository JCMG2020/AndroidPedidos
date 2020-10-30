package com.example.pedidos.Utils;

import android.text.TextUtils;
import android.util.Patterns;



import java.util.HashMap;
import java.util.regex.Pattern;

public class Validation {

    public static  HashMap<String, Object>  validarEmail(String data){
        HashMap<String, Object> meMap=new HashMap<String, Object>();
        if(verificar_vacio(data)) {
            meMap.put("bool", true);
            meMap.put("mensaje", "El campo email es requerido");
            return meMap;
        }

        if (!verificarEmail(data)){
            meMap.put("bool", true);
            meMap.put("mensaje", "El campo no es un  email valido");
            return meMap;
        }

            meMap.put("bool", false);
            return meMap;

    }

    public static  HashMap<String, Object>  validarPassword(String data){
        HashMap<String, Object> meMap=new HashMap<String, Object>();
        if(verificar_vacio(data)) {
            meMap.put("bool", true);
            meMap.put("mensaje", "La contraseña  es requerida");
            return meMap;

        }
        if(data.length()<=2){
            meMap.put("bool", true);
            meMap.put("mensaje", "La contraseña debe tener min 3 caracteres");
            return meMap;

        }
            meMap.put("bool", false);

        return meMap;

    }





    public static Boolean verificar_vacio(String texto) {
        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
            return true;
        }
        return false;
    }

    public static boolean verificarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


    public static Double formatearDecimales(Double numero, Integer numeroDecimales) {
        return Math.round(numero * Math.pow(10, numeroDecimales)) / Math.pow(10, numeroDecimales);
    }


}
