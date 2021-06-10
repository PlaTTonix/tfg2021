package com.tfg.campus;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Login extends AppCompatActivity {

    //VARIABLES PRINCIPALES
    TextView title;
    EditText inputEmail, inputPassword;
    Button help, btnLogin;
    Switch switchRemember;

    private final String CARPETA_RAIZ = "VibeVillage/";
    private final String DOC = "user_login.txt";

    private FirebaseAuth mAuth;

    public static boolean logout = false;
    private boolean userFound = false;
    public static User u = new User();
    private ArrayList<String> listUsers = new ArrayList<String>();

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        help = findViewById(R.id.btnHelp);
        btnLogin = findViewById(R.id.btnLogin);
        switchRemember = findViewById(R.id.switchRemember);

        title.setText("VibeVillage"); //TITULO DE LA ACTIVIDAD

        if (logout){
            deleteUser();
        }else{
            fill(); //RELLENAR USUARIO SI EXISTIERA
        }

        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        searchTotalUsers();
    }

    private void save(){ //GUARDAMOS LOS DATOS DEL USUARIO EN UN ARCHIVO TXT
        File file = new File(this.getFilesDir().toString(), "VibeVillage");
        if (!file.exists()) {
            file.mkdir();
        }
        File fileEvents = new File(this.getFilesDir()+"/VibeVillage/user_login.txt");
        if (fileEvents.exists()){
            fileEvents.delete();
        }
        try {
            File gpxfile = new File(file, "user_login.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(inputEmail.getText().toString() + "\n");
            writer.append(inputPassword.getText().toString() + "\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            System.out.println("ERROR en Escritura Archivo user_login.txt");
        }
    }

    private void deleteUser(){
        File file = new File(this.getFilesDir(), CARPETA_RAIZ+DOC);
        if (file.exists()) {
            file.delete();
        }
    }

    private void fill(){
        File fileEvents = new File(this.getFilesDir()+"/VibeVillage/user_login.txt");
        int cont = 0;
        if (fileEvents.exists()){
            switchRemember.setChecked(true);
            try {
                BufferedReader brs = new BufferedReader(new FileReader(fileEvents));
                String lines;
                while ((lines = brs.readLine()) != null) {
                    cont++;
                    if (cont == 1){
                        inputEmail.setText(lines);
                    } else if (cont == 2){
                        inputPassword.setText(lines);
                    }
                }
                brs.close();
            } catch (IOException e) { }
        }
    }

    public void login(View v){
        if (inputEmail.getText().toString().isEmpty() || inputPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "CORREO/CONTRASEÑA OBLIGATORIO", Toast.LENGTH_LONG).show();
        }else{
            if (switchRemember.isChecked()){
                save();
            }else{
                deleteUser();
            }
            signIn(inputEmail.getText().toString(), inputPassword.getText().toString());
        }
    }

    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            manualUserSearch();
                            System.out.println("INICIO SESION CORRECTO");
                            Intent intent = new Intent (getApplicationContext(), HomeDesplegable.class);
                            intent.putExtra("email",inputEmail.getText().toString());
                            startActivity(intent);
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            Toast.makeText(getApplicationContext(), "INICIO FALLIDO.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void searchTotalUsers(){
        myRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listUsers.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    listUsers.add(objSnaptshot.getKey().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void searchUser(String id){ //  Busca el usuario
        myRef.child("users/" + id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (!userFound){
                    for (DataSnapshot objSnaptshot : snapshot.getChildren()){

                        if (objSnaptshot.getKey().equals("cash")){
                            u.setCash(objSnaptshot.getValue().toString());
                        }
                        if (objSnaptshot.getKey().equals("dni")){
                            u.setDni(objSnaptshot.getValue().toString());
                        }
                        if (objSnaptshot.getKey().equals("email")){
                            u.setEmail(objSnaptshot.getValue().toString());
                        }
                        if (objSnaptshot.getValue().toString().equals(inputEmail.getText().toString())){
                            userFound = true;
                        }
                        if (objSnaptshot.getKey().equals("name")){
                            u.setName(objSnaptshot.getValue().toString());
                        }
                        if (objSnaptshot.getKey().equals("phone")){
                            u.setPhone(objSnaptshot.getValue().toString());
                        }
                        if (objSnaptshot.getKey().equals("room")){
                            u.setRoom(objSnaptshot.getValue().toString());
                        }
                        if (objSnaptshot.getKey().equals("surname")){
                            u.setSurname(objSnaptshot.getValue().toString());
                        }
                        if (objSnaptshot.getKey().equals("uid")){
                            u.setUid(objSnaptshot.getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void manualUserSearch(){

        for (int i = 0; i < listUsers.size(); i++){
            searchUser(listUsers.get(i));
        }

    }

    public void help(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Ayuda")
                .setMessage("Disponemos de dos métodos de contacto: \n")
                .setPositiveButton("Correo electrónico", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Asunto del mensaje");
                        i.putExtra(Intent.EXTRA_TEXT, "Cuerpo del mensaje");
                        i.putExtra(Intent.EXTRA_EMAIL, "emaildeprueba@gmail.com");//PONER EMAIL DE DESTINO
                        startActivity(i);
                    }
                })
                .setNegativeButton("Teléfono", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent i = new Intent(Intent.ACTION_DIAL);
                        i.setData(Uri.parse("tel:123456789"));
                        startActivity(i);
                    }
                });
        dialog.show();
    }

    public void onDestroy(){
        System.out.println("DESTROY LOGIN");
        super.onDestroy();
    }

    public void onRestart ( ) {
        System.out.println("RESTART LOGIN");
        super . onRestart ( ) ;
    }

    public void onPause ( ) {
        System.out.println("PAUSE LOGIN");
        super . onPause ( ) ;
    }

    public void onResume ( ) {
        System.out.println("RESUME LOGIN");
        super . onResume ( ) ;
    }

    public void onStop ( ) {
        System.out.println("STOP LOGIN");
        finish();
        super . onStop ( ) ;
    }
}