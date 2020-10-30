package com.example.pedidos.View.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.pedidos.MainActivity;
import com.example.pedidos.R;
import com.example.pedidos.Utils.Global;

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

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ConfPedido extends Fragment {

    View vista;
    Button BtnConfirmar, BtnCancelarPedido;
    SweetAlertDialog pDialog;

    Boolean resultado=false;
    SoapPrimitive resultString;

    public ConfPedido() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_conf_pedido, container, false);
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        animacion_cargando();
        pDialog.show();
        ConfPedido.segundoPlano segundo= new ConfPedido.segundoPlano();
        segundo.execute();


        UI();
        BtnConfirmar.setEnabled(false);
        BtnCancelarPedido.setEnabled(false);
        CLick();
    }

    private void UI() {
        BtnConfirmar = vista.findViewById(R.id.BtnConfirmar);
        BtnCancelarPedido = vista.findViewById(R.id.BtnCancelPed);
    }
    private void CLick() {
        BtnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.idTipoAct=2;
                pDialog.show();
                pDialog.setTitle("Actualizando");
                ConfPedido.segundoPlanoActualizar segundoAct= new ConfPedido.segundoPlanoActualizar();
                segundoAct.execute();
            }
        });
        BtnCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.idTipoAct=3;
                pDialog.show();
                pDialog.setTitle("Actualizando");
                ConfPedido.segundoPlanoActualizar segundoAct= new ConfPedido.segundoPlanoActualizar();
                segundoAct.execute();
            }
        });
    }

    private void animacion_cargando(){
        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.col_naranja))));
        pDialog.setTitleText("Cargando");
        pDialog.setCancelable(false);
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
        String SOAP_ACTION = Global.urlServer+":80/Desarrollo-Java-Services/ServicioPedidos/ObtenerUltimoPedido";
        String METHOD_NAME = "ObtenerUltimoPedido";
        String NAMESPACE = "http://servicios.org/";
        String URL = Global.urlServer+"//Desarrollo-Java-Services/ServicioPedidos?wsdl";
        try{
            SoapObject request =  new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("usuario",Integer.parseInt(Global.idGlobal));
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
                SoapObject soapObject = (SoapObject) soapEnvelope.getResponse();

                Global.idUltimpPedidoGlo = Integer.parseInt(soapObject.getProperty("idPedido").toString());

                Log.e("RESPONSE",String.valueOf(soapObject));
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
                pDialog.dismiss();
                BtnConfirmar.setEnabled(true);
                BtnCancelarPedido.setEnabled(true);

            }else{
                BtnConfirmar.setEnabled(false);
                BtnCancelarPedido.setEnabled(false);
                Toast.makeText(getActivity(), "No se pudo rconsultar el ultimo pedido", Toast.LENGTH_LONG).show();
            }




        }

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
            request.addProperty("IdPedido", Global.idUltimpPedidoGlo);
            request.addProperty("Idestado",Global.idTipoAct);

            Log.e("pedidoid",""+ Global.idUltimpPedidoGlo);

            Log.e("tipoActualiza",""+Global.idTipoAct);

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


    private class segundoPlanoActualizar extends AsyncTask<Boolean,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... params) {
            return cosumoWsACt();
        }

        @Override
        protected void onPreExecute(){
        }


        @Override
        protected void onPostExecute(Boolean s) {
            //Log.e("result", true);
            if(s){
                if (Global.idTipoAct==2) Toast.makeText(getActivity(), "El pedido se ha confirmado", Toast.LENGTH_LONG).show();
                if (Global.idTipoAct==3) Toast.makeText(getActivity(), "El pedido se ha cancelado", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "No se pudo Actualizar el pedido", Toast.LENGTH_LONG).show();
            }
            pDialog.dismiss();




        }

    }

}