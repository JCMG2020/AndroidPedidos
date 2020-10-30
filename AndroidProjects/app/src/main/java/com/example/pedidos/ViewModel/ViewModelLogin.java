package com.example.pedidos.ViewModel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.pedidos.Utils.Validation;

import java.util.HashMap;


public class ViewModelLogin extends ViewModel {


  //  RetrofitClient NextClient;
   // ApiService NextService;

    private MutableLiveData<Boolean> isViewLoading;
    private MutableLiveData<Boolean> intentHome;
    private MutableLiveData<String> email;
    private MutableLiveData<String> password;

    public ViewModelLogin() {
        this.email = new MutableLiveData<>();
        this.password = new MutableLiveData<>();
        this.isViewLoading = new MutableLiveData<>();
        this.intentHome = new MutableLiveData<>();

    }

    public LiveData<String> getEmail(){
        return  email;
    }
    public LiveData<String> getPassword(){
        return  password;
    }
    public LiveData<Boolean> getIsViewLoading(){
        return  isViewLoading;
    }
    public LiveData<Boolean> getintentHome(){
        return  intentHome;
    }


    public  void  onClickLogin(String email,String pass){
        isViewLoading.setValue(true);
        if(validaEmail(email) &&     validaPassword(pass)  ){
            isViewLoading.setValue(false);
        }else {
            peticionLogin(email,pass);
        }
     //  resultado.setValue(Validation.validarEmail(data));
    }

    private boolean validaEmail(String user){
        HashMap<String, Object> meMap= Validation.validarEmail(user);
        boolean verificar= (boolean) meMap.get("bool");
        if (verificar){
            email.setValue((String) meMap.get("mensaje"));
        }
        return verificar;
    }

    private boolean validaPassword(String pass){
        HashMap<String, Object> meMap= Validation.validarPassword(pass);
        boolean verificar= (boolean) meMap.get("bool");
        if (verificar){
            password.setValue((String) meMap.get("mensaje"));
        }
        return verificar;
    }





    private void peticionLogin(String user,String pass){
        intentHome.setValue(true);
       /* RequestLogin requestLogin = new RequestLogin(user, pass);
        Call<ResponseLogin> call = NextService.doLogin(user,pass);
        call.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if(response.isSuccessful()) {

                    Log.e("devolucion",Validation.convertObjToString(response.body()));

                    if(response.body().getData().size()<=0){
                        isViewLoading.setValue(false);
                        Toast.makeText(MyApp.getContext(), "No se encontro usuario", Toast.LENGTH_SHORT).show();
                    }else{
                        Global.miPerfil=response.body().getData().get(0);
                        intentHome.setValue(true);

                    }
                } else {
                    isViewLoading.setValue(false);
                    Toast.makeText(MyApp.getContext(), "Algo fue mal, revise sus datos de acceso", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                isViewLoading.setValue(false);
                Toast.makeText(MyApp.getContext(), "Problemas de conexión. Inténtelo de nuevo", Toast.LENGTH_SHORT).show();
            }
        });*/
    }





}
