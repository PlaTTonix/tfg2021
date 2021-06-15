package com.tfg.campus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.tfg.campus.databinding.ActivityHomeDesplegableBinding;

public class HomeDesplegable extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeDesplegableBinding binding;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeDesplegableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //  Obtencion del email de la actividad anterior
        email = getIntent().getExtras().getString("email");

        //  Corresponde al boton flotante de ayuda para el usuario
        setSupportActionBar(binding.appBarHomeDesplegable.toolbar);
        binding.appBarHomeDesplegable.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:123456789"));
                startActivity(i);
            }
        });

        //  Menu desplegable
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_orders, R.id.nav_subscriptions, R.id.nav_bills, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_desplegable);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //  Parte superior de menu desplegable, al hacer click
        binding.navView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el Menu, podemos anadir extras al actionbar, ajustes etc...
        //getMenuInflater().inflate(R.menu.home_desplegable, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_desplegable);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Acciones cuando selecciones alguna opcion del menu superior derecho. (Actualmente desactivado)
        switch (item.getItemId()) {
            case R.id.action_dark:
                Toast.makeText(this, "* PROXIMAMENTE *", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDestroy(){
        super.onDestroy();
    }

    public void onRestart ( ) {
        super . onRestart ( ) ;
    }

    public void onPause ( ) {
        super . onPause ( ) ;
    }

    public void onResume ( ) {
        super . onResume ( ) ;
    }

    public void onStop ( ) {
        //  Evita que el usuario al seleccionar Cerrar Sesion regrese al Home
        if (Login.logout){
            finish();
        }
        super . onStop ( ) ;
    }
}