package com.example.pedidos.View.Fragments;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedidos.MainActivity;
import com.example.pedidos.R;
import com.example.pedidos.Utils.Global;
import com.example.pedidos.View.Login;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.Proxy;

import static com.example.pedidos.Utils.Global.verificar_vacio;

public class FormPedido extends Fragment {
    View vista;
    Spinner SpnTipoPedido;
    TextInputLayout TIDirOrigen;
    EditText ETDirOrigen;
    TextInputLayout TIDirDest;
    EditText ETDirDest;
    TextInputLayout TIComentario;
    TextView bienvenida;
    EditText ETComentario;
    Button SoliciarPed;

    String DirOrigen, DirDest, Comentario;
    int IdTipo;

    Boolean resultado=false;
    SoapPrimitive resultString;




    public FormPedido() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_form_pedido, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UI();
        CLick();
    }

    private void UI() {
        SpnTipoPedido = vista.findViewById(R.id.SpnTipoPedido);
         TIDirOrigen = vista.findViewById(R.id.TIDirOrigen);
        ETDirOrigen = vista.findViewById(R.id.ETDIrOrigen);
        TIDirDest = vista.findViewById(R.id.TIDirDestino);
        ETDirDest = vista.findViewById(R.id.ETDirDestino);
        TIComentario = vista.findViewById(R.id.TIComentario);
        ETComentario = vista.findViewById(R.id.ETComentario);
        SoliciarPed=vista.findViewById(R.id.ButtonSolicitarPed);
        bienvenida=vista.findViewById(R.id.bienvenida);
        bienvenida.setText("Bienvenido "+Global.nombreGlobal+" " +Global.apellGLobal);
    }


    private void validar_campos(){
        //("VC", "estoy en validar campos ");
        if(verificar_vacio(ETDirOrigen.getText().toString())) {
            ETDirOrigen.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            Log.e("origen", "LLene origen");
        }
        else if(verificar_vacio(ETDirDest.getText().toString())) {
            ETDirDest.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            Log.e("dest", "LLene destino");
        }
        else if (verificar_vacio(ETComentario.getText().toString())) {
            ETComentario.requestFocus();
            Snackbar.make(vista, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show();
            Log.e("comentario", "LLene comertario");
        }else llenarDatos();
    }
    public  void llenarDatos(){
        DirOrigen=ETDirOrigen.getText().toString();
        DirDest=ETDirDest.getText().toString();
        Comentario=ETComentario.getText().toString();
       // IdTipo=1;//Id Tipo se llena en el evento setOnItemSelectedListener de la funcion Click
        Log.e("llenar dt", "los datos se deben llenar");
        siguiente_paantalla();
    }

    private void CLick(){
        SoliciarPed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validar_campos();
            }
        });
        //Rescatar Id de TipoPedido
        SpnTipoPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IdTipo=position+1;
                Log.e("id tipo", ""+IdTipo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Mejora en foco del TexImput
        TIDirOrigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETDirOrigen.requestFocus();
            }
        });
        TIDirDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETDirDest.requestFocus();
            }
        });
        TIComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ETComentario.requestFocus();
            }
        });

        //validaciones para que al seleccionar campo, el texview cambien de color
        ETDirOrigen.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TIDirOrigen.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIDirOrigen.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        ETDirDest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TIDirDest.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIDirDest.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });
        ETComentario.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    TIComentario.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#EE8813")));
                } else {
                    TIComentario.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#CCCCCC")));
                }
            }
        });

        //FIN validaciones para que al seleccionar campo, el texview cambien de color

    }
    private  void siguiente_paantalla(){
        FormPedido.segundoPlano segundo= new FormPedido.segundoPlano();
        segundo.execute();

    }

    private final HttpTransportSE getHttpTransportSE(String MAIN_REQUEST_URL) {
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY,MAIN_REQUEST_URL,60000);
        ht.debug = true;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }


    public Boolean cosumoWs(){
        /*String SOAP_ACTION = "http://www.banguat.gob.gt/variables/ws/TipoCambioDia";
        String METHOD_NAME = "TipoCambioDia";
        String NAMESPACE = "http://190.143.158.229/variables/ws/";
        String URL = "http://190.143.158.229/variables/ws/TipoCambio.asmx";*/
        String SOAP_ACTION = Global.urlServer+":80/Desarrollo-Java-Services/ServicioPedidos/insertarPedido";
        String METHOD_NAME = "insertarPedido";
        String NAMESPACE = "http://servicios.org/";
        String URL = Global.urlServer+"//Desarrollo-Java-Services/ServicioPedidos?wsdl";
        try{
            SoapObject request =  new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("ID_USUARIO",Integer.parseInt(Global.idGlobal));
            request.addProperty("TIPO_PEDIDO",IdTipo);
            request.addProperty("DIR_ORIGEN",DirOrigen);
            request.addProperty("DIR_DESTINO",DirDest);
            request.addProperty("COMENTARIO",Comentario);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = false;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport =  getHttpTransportSE(URL);
            try {
                httpTransport.call(SOAP_ACTION, soapEnvelope);
            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                Log.e("HTTPLOG", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("IOLOG", e.getMessage());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                Log.e("XMLLOG", e.getMessage());
                e.printStackTrace();
            } //send request
            try {
                //String response = soapEnvelope.getResponse().toString();
                SoapPrimitive soapObject = (SoapPrimitive) soapEnvelope.getResponse();
                Log.e("RESPONSE",String.valueOf(soapObject));
                resultado =true;

            } catch (SoapFault e) {
                // TODO Auto-generated catch block
                resultado =false;

                Log.e("SOAPLOG", e.getMessage());
                e.printStackTrace();
            }

        }catch(Exception ex){
            resultado =false;

            Log.e("SOAPLOG","ERROR: " + ex.getMessage());

        }
        return resultado;
    }

    public Boolean cosumoWsACt(){
        /*String SOAP_ACTION = "http://www.banguat.gob.gt/variables/ws/TipoCambioDia";
        String METHOD_NAME = "TipoCambioDia";
        String NAMESPACE = "http://190.143.158.229/variables/ws/";
        String URL = "http://190.143.158.229/variables/ws/TipoCambio.asmx";*/
        String SOAP_ACTION = Global.urlServer+":80/Desarrollo-Java-Services/ServicioPedidos/actualizarPedido";
        String METHOD_NAME = "actualizarPedido";
        String NAMESPACE = "http://servicios.org/";
        String URL = Global.urlServer+"//Desarrollo-Java-Services/ServicioPedidos?wsdl";
        try{
            SoapObject request =  new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("ID_USUARIO",Integer.parseInt(Global.idGlobal));
            request.addProperty("TIPO_PEDIDO",IdTipo);
            request.addProperty("DIR_ORIGEN",DirOrigen);
            request.addProperty("DIR_DESTINO",DirDest);
            request.addProperty("COMENTARIO",Comentario);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = false;
            soapEnvelope.implicitTypes = true;
            soapEnvelope.setOutputSoapObject(request);
            HttpTransportSE httpTransport =  getHttpTransportSE(URL);
            try {
                httpTransport.call(SOAP_ACTION, soapEnvelope);
            } catch (HttpResponseException e) {
                // TODO Auto-generated catch block
                Log.e("HTTPLOG", e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.e("IOLOG", e.getMessage());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                Log.e("XMLLOG", e.getMessage());
                e.printStackTrace();
            } //send request
            try {
                //String response = soapEnvelope.getResponse().toString();
                SoapPrimitive soapObject = (SoapPrimitive) soapEnvelope.getResponse();
                Log.e("RESPONSE",String.valueOf(soapObject));
                resultado =true;

            } catch (SoapFault e) {
                // TODO Auto-generated catch block
                resultado =false;

                Log.e("SOAPLOG", e.getMessage());
                e.printStackTrace();
            }

        }catch(Exception ex){
            resultado =false;

            Log.e("SOAPLOG","ERROR: " + ex.getMessage());

        }
        return resultado;
    }






    private class segundoPlano extends AsyncTask<Boolean,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... params) {
            return cosumoWs();
        }

        @Override
        protected void onPreExecute(){
        }


        @Override
        protected void onPostExecute(Boolean s) {
            //Log.e("result", true);
            if(s){
                Toast.makeText(getActivity(), "Pedido realizado", Toast.LENGTH_LONG).show();
                ((MainActivity) getActivity()).clearFragmentBackStack();
                ((MainActivity) getActivity()).cambiar_tab(1);
            }else{
                Toast.makeText(getActivity(), "No se pudo realizar el pedido", Toast.LENGTH_LONG).show();
            }




        }

    }






}