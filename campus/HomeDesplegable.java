package com.tfg.campus;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
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

    TextView nameDesplegable, cashDesplegable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeDesplegableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = getIntent().getExtras().getString("email");

        setSupportActionBar(binding.appBarHomeDesplegable.toolbar);
        binding.appBarHomeDesplegable.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:123456789"));
                startActivity(i);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_subscriptions, R.id.nav_bills, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_desplegable);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        binding.navView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ESTO ES CUANDO TOCAN EL LUGAR DEL PERFIL DEL RESIDENTE
                Intent intent = new Intent (getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });
/*
        nameDesplegable = findViewById(R.id.nameDesplegable);
        cashDesplegable = findViewById(R.id.cashDesplegable);

        if (Login.u != null){
            nameDesplegable.setText("");
            cashDesplegable.setText("€");
        }else{
            System.out.println("PARECE QUE U ES NULO");
        }
*/
    }

/*    private void crear(){
        User p = new User();
        p.setUid(UUID.randomUUID().toString());
        p.setCash("nombre");
        p.setDni("app");
        p.setEmail("correo");
        p.setName("password");
        p.setRoom("sdf");
        p.setPhone("sfdgs");
        myRef.child("users").child(p.getUid()).setValue(p);
        Toast.makeText(this, "Agregado", Toast.LENGTH_LONG).show();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_desplegable, menu);
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_dark:
                Toast.makeText(this, "* PROXIMAMENTE *", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDestroy(){
        System.out.println("DESTROY HOMEDESPLEGABLE");
        super.onDestroy();
    }

    public void onRestart ( ) {
        System.out.println("RESTART HOMEDESPLEGABLE");
        super . onRestart ( ) ;
    }

    public void onPause ( ) {
        System.out.println("PAUSE HOMEDESPLEGABLE");
        super . onPause ( ) ;
    }

    public void onResume ( ) {
        System.out.println("RESUME HOMEDESPLEGABLE");
        super . onResume ( ) ;
    }

    public void onStop ( ) {
        System.out.println("STOP HOMEDESPLEGABLE");
        if (Login.logout){
            finish();
        }

        super . onStop ( ) ;
    }


}