package com.example.pedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.pedidos.Utils.Vista_tabs;
import com.example.pedidos.View.Fragments.ConfPedido;
import com.example.pedidos.View.Fragments.FormPedido;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
   // private ViewModelMain viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IniciarTabs();
        seleccion_tabs();
        tabLayout.getTabAt(0).select();
    }

    private void IniciarTabs(){
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_pedidos_on, R.drawable.ic_pedidos_off, "Crear Pedido")));
        tabLayout.addTab(tabLayout.newTab().setCustomView(new Vista_tabs(this, R.drawable.ic_aceptar_on, R.drawable.ic_aceptar_off, "Confirmar")));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void seleccion_tabs(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                //  tabLayout.getTabAt(position).select();

                elegir(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //("seleccion ","antiguo");

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //  int position = tab.getPosition();
                //("seleccion ","nuevo");
                elegir(tab.getPosition());
            }
        });



    }

    private void elegir(int position){
        // if(position==0)
        clearFragmentBackStack();
        switch (position) {
            case 0:
                getSupportFragmentManager().beginTransaction()

                        .replace(R.id.Contenedor_Fragments, new FormPedido()).commit();
                break;

            case 1:
                //("posicion",""+position);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Contenedor_Fragments, new ConfPedido()).commit();
                break;
        }

    }

    public   void cambiar_tab(int position){
        tabLayout.getTabAt(position).select();

    }



    public void clearFragmentBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        //("cuantos fragments",""+fm.getBackStackEntryCount());
        for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
            fm.popBackStack();
        }
    }
}