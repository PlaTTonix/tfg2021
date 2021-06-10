package com.tfg.campus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile extends AppCompatActivity {
    //VARIABLES PRINCIPALES
    TextView title, name, surname, dni, email, phone, room, cash;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        name = findViewById(R.id.textViewProfileName);
        surname = findViewById(R.id.textViewProfileSurname);
        dni = findViewById(R.id.textViewProfileDNI);
        email = findViewById(R.id.textViewProfileEmail);
        phone = findViewById(R.id.textViewProfilePhone);
        room = findViewById(R.id.textViewProfileRoom);
        cash = findViewById(R.id.textViewProfileCash);

        name.setText("Nombre: " + Login.u.getName());
        surname.setText("Apellidos: " + Login.u.getSurname());
        dni.setText("DNI: " + Login.u.getDni());
        email.setText("Email: " + Login.u.getEmail());
        phone.setText("Teléfono: " + Login.u.getPhone());
        room.setText("Habitación: " + Login.u.getRoom());
        cash.setText("Saldo: " + Login.u.getCash() + " €");


        title.setText("Perfil"); //TITULO DE LA ACTIVIDAD
    }

    public void unavailable (View v){
        Toast.makeText(this, "* PROXIMAMENTE *", Toast.LENGTH_LONG).show();
    }

    public void changePass(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Reseteo de contraseña")
                .setMessage("Esta función restablecerá su contraseña a la predeterminada (123456)\n¿Está seguro?\n")
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        mAuth.getCurrentUser().updatePassword("123456");

                        Toast.makeText(getApplicationContext(), "Contraseña Cambiada", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


}