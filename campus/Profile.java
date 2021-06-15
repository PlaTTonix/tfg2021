package com.tfg.campus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.campus.model.CashCode;
import com.tfg.campus.model.User;

import org.jetbrains.annotations.NotNull;

public class Profile extends AppCompatActivity {

    //  VARIABLES PRINCIPALES
    TextView title, name, surname, dni, email, phone, room, cash;
    TextInputEditText redeemCash, newPass;
    TextInputLayout newPassLay;
    Button btnchangePass;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FirebaseAuth mAuth;
    CashCode cc;
    private boolean firstTry = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //  BBDD
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //  Inicializa la autenticacion en Firebase
        mAuth = FirebaseAuth.getInstance();

        //  TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //  ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        name = findViewById(R.id.textViewProfileName);
        surname = findViewById(R.id.textViewProfileSurname);
        dni = findViewById(R.id.textViewProfileDNI);
        email = findViewById(R.id.textViewProfileEmail);
        phone = findViewById(R.id.textViewProfilePhone);
        room = findViewById(R.id.textViewProfileRoom);
        cash = findViewById(R.id.textViewProfileCash);
        redeemCash = findViewById(R.id.textInputEditTextCashCode);
        newPass = findViewById(R.id.textInputEditTextNewPass);
        newPassLay = findViewById(R.id.textInputLayoutNewPass);
        btnchangePass = findViewById(R.id.buttonPasswordChange);

        updateUser();

        //  TITULO DE LA ACTIVIDAD
        title.setText("Perfil");
    }

    //  Actualiza los datos del usuario
    private void updateUser(){
        myRef.child("users/" + Login.u.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Login.u = snapshot.getValue(User.class);
                name.setText("Nombre: " + Login.u.getName());
                surname.setText("Apellidos: " + Login.u.getSurname());
                dni.setText("DNI: " + Login.u.getDni());
                email.setText("Email: " + Login.u.getEmail());
                phone.setText("Teléfono: " + Login.u.getPhone());
                room.setText("Habitación: " + Login.u.getRoom());
                cash.setText("Saldo: " + Login.u.getCash() + " €");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //  Establece el nuevo saldo al usuario
    private void newCash(){
        Float newCash = Float.parseFloat(cc.getAmount());
        Float oldCash = Float.parseFloat(Login.u.getCash());
        String finalCash = String.valueOf(oldCash + newCash);
        myRef.child("users/" + Login.u.getUid() + "/cash").setValue(finalCash);

        //  Elimina el codigo para que no se vuelva a utilizar
        //myRef.child("cashcodes/" + redeemCash.getText().toString()).removeValue();
    }

    //  Muestra el mensaje al usuario relativa a la cantidad del codigo
    private void message(){
        if (cc != null && cc.getAmount() != null){  //  Si el codigo existe
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Confirmación")
                    .setMessage("El código introducido equivale a: " + cc.getAmount() + "€\n\n¿Efectuar recarga?\n")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            newCash();
                            Toast.makeText(getApplicationContext(), "Saldo añadido", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();
        }else{
            Toast.makeText(this,"Código inválido",Toast.LENGTH_SHORT).show();
        }
    }

    //  Busca el codigo introducido
    private void searchCashCode(String code){
        myRef.child("cashcodes/" + code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                cc = snapshot.getValue(CashCode.class);
                message();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //  Llama a buscar el codigo
    public void redeemCash(View v){
        if (!redeemCash.getText().toString().isEmpty()){
            searchCashCode(redeemCash.getText().toString());
        }else{
            Toast.makeText(this,"Introduzca código",Toast.LENGTH_SHORT).show();
        }
    }

    //  Mensaje del cambio de contrasenia
    public void changePass(View v){
        newPassLay.setVisibility(View.VISIBLE);
        btnchangePass.setText("Continuar ->");

        if (!newPass.getText().toString().isEmpty() && !newPass.getText().toString().equals(" ")){  //  Si el campo no se encuentra vacio
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Reseteo de contraseña")
                    .setMessage("Su nueva contraseña será: " + newPass.getText().toString() + "\n¿Está seguro?\n")
                    .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            mAuth.getCurrentUser().updatePassword(newPass.getText().toString());    //  Actualiza la contraseña para el usuario actual

                            Toast.makeText(getApplicationContext(), "Contraseña Cambiada", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();

            newPassLay.setVisibility(View.GONE);
            btnchangePass.setText("Cambiar Contraseña");
            firstTry = true;
        }else if (!firstTry){
            Toast.makeText(getApplicationContext(), "Rellene el campo", Toast.LENGTH_SHORT).show();
        }else{
            firstTry = false;
        }

    }
}