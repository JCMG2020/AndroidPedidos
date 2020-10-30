package com.example.pedidos.Utils;

import android.text.TextUtils;

public class
Global {

   public static String idGlobal, nombreGlobal, apellGLobal;
   public static int idUltimpPedidoGlo;
   public static int idTipoAct;//2 conf, 3 cancelar


   public static String urlServer="http://01ef6f796431.ngrok.io";



    public static Boolean verificar_vacio(String texto) {
        //Todo retorno true si esta vacio
        if (TextUtils.isEmpty(texto)) {
            return true;
        }
        return false;
    }

}
