package com.example.pedidos.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedidos.MainActivity;
import com.example.pedidos.R;
import com.example.pedidos.Utils.Global;
import com.example.pedidos.ViewModel.ViewModelLogin;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.Proxy;

public class Login extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Button ingresar;
    private ViewModelLogin viewModel;
    private ConstraintLayout ContainerLogin;
    private View layoutLoading;
    private TextView registrase;

    Boolean resultado=false;
    SoapPrimitive resultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UI();
        observer();



    }
    private void UI() {
        user = findViewById(R.id.EditLoginUser);
        password = findViewById(R.id.EditLoginPass);
        ingresar = findViewById(R.id.ButtonLoginIngresar);

        layoutLoading = findViewById(R.id.layoutLoading);
        ContainerLogin = findViewById(R.id.ContainerLogin);
        viewModel = ViewModelProviders.of(this).get(ViewModelLogin.class);


        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Click", "login");
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                viewModel.onClickLogin(user.getText().toString(), password.getText().toString());
            }
        });

    }



    private void observer() {

        final Observer<String> observerEmail = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                user.setError(s);
            }
        };
        final Observer<String> observerPassword = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                password.setError(s);
            }
        };

        final Observer<Boolean> observerLoading = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {

                if(result){
                    layoutLoading.setVisibility(View.VISIBLE);
                    ContainerLogin.setVisibility(View.GONE);
                }else{
                    layoutLoading.setVisibility(View.GONE);
                    ContainerLogin.setVisibility(View.VISIBLE);

                }

            }
        };


        final Observer<Boolean> observerHome = new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                if(result){
                    goToSignUp();

                }else{

                }

            }
        };

        viewModel.getEmail().observe(this, observerEmail);
        viewModel.getPassword().observe(this, observerPassword);
        viewModel.getIsViewLoading().observe(this, observerLoading);

        viewModel.getintentHome().observe(this, observerHome);

    }

    private void goToSignUp() {
        //cosumoWs();

        segundoPlano segundo= new segundoPlano();
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
        String SOAP_ACTION = Global.urlServer+"//Desarrollo-Java-Services/ServiciosUsuario/LoginUsuario";
        String METHOD_NAME = "LoginUsuario";
        String NAMESPACE = "http://servicios.org/";
        String URL = Global.urlServer+"//Desarrollo-Java-Services/ServiciosUsuario?wsdl";
        try{
            SoapObject request =  new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("email",user.getText().toString());
            request.addProperty("contrasena",password.getText().toString().trim());
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
                SoapObject soapObject = (SoapObject)soapEnvelope.getResponse();
                Global.idGlobal = soapObject.getProperty("idUsuario").toString();
                Global.nombreGlobal = soapObject.getProperty("nombre").toString();
                Global.apellGLobal = soapObject.getProperty("apellido").toString();
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
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Usuario o Contraseña incorrecta",Toast.LENGTH_LONG).show();
                layoutLoading.setVisibility(View.GONE);
                ContainerLogin.setVisibility(View.VISIBLE);
            }

        }

    }




}