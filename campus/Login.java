package com.tfg.campus;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.campus.model.User;
import com.tfg.campus.model.UserSubscription;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {

    //VARIABLES PRINCIPALES
    TextView title;
    EditText inputEmail, inputPassword;
    Button help, btnLogin;
    Switch switchRemember;
    LinearLayout logInText;
    ProgressBar loadBarBTN;

    private final String CARPETA_RAIZ = "VibeVillage/";
    private final String DOC = "user_login.txt";

    private FirebaseAuth mAuth;

    public static boolean logout = false;
    private boolean userFound = false;
    public static User u = new User();
    private ArrayList<String> listUsers = new ArrayList<String>();
    public static ArrayList<UserSubscription> listUsersSubscriptions = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  Inicializa la autenticacion de Firebase
        mAuth = FirebaseAuth.getInstance();

        //  BBDD
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //  TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //  ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        help = findViewById(R.id.btnHelp);
        btnLogin = findViewById(R.id.btnLogin);
        switchRemember = findViewById(R.id.switchRemember);
        logInText = findViewById(R.id.LinearLayoutLogInText);
        loadBarBTN = findViewById(R.id.progressBarBTN);

        //  TITULO DE LA ACTIVIDAD
        title.setText("VibeVillage");

        searchTotalUsers();
        searchAllUserSubscriptions();

        //  Elimina los datos del usuario guardado o los rellena
        if (logout){
            deleteUser();
        }else{
            fill();
        }
    }

    //  Encargado de hacer o no visible la carga del inicio de sesion
    private void logInScreen(boolean mode){
        if (mode){
            logInText.setVisibility(View.VISIBLE);
            loadBarBTN.setVisibility(View.VISIBLE);
        }else{
            logInText.setVisibility(View.GONE);
            loadBarBTN.setVisibility(View.GONE);
        }
    }

    //  Cargamos las suscripciones del usuario que inicia sesion
    private void searchAllUserSubscriptions(){
        myRef.child("subscriptions/usersubscriptions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listUsersSubscriptions.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    UserSubscription usub = objSnaptshot.getValue(UserSubscription.class);
                    listUsersSubscriptions.add(usub);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //  Guardado de datos del usuario en .txt
    private void save(){
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

    //  Elimina el archivo que contiene la informacion de inicio de sesion del usuario
    private void deleteUser(){
        File file = new File(this.getFilesDir(), CARPETA_RAIZ+DOC);
        if (file.exists()) {
            file.delete();
        }
    }

    //  Rellena los campos de inicio de sesion
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

                //  Deshabilita los campos mientras carga el inicio de sesion
                inputPassword.setEnabled(false);
                inputEmail.setEnabled(false);
                switchRemember.setEnabled(false);
                btnLogin.setEnabled(false);

                brs.close();
            } catch (IOException e) { }

            logInScreen(true);

            //  Damos una pausa para que pueda cargar todos los archivos correctamente
            TimerTask tarea = new TimerTask() {
                @Override
                public void run() {
                    signIn(inputEmail.getText().toString(), inputPassword.getText().toString());
                }
            };
            Timer tiempo = new Timer();
            tiempo.schedule(tarea, 3000);
        }
    }

    //  Dirige a iniciar la sesion, guardar o elimnar los datos del usuario y comprobar que los campos de inicio de sesion estan rellenos
    public void login(View v){
        if (inputEmail.getText().toString().isEmpty() || inputPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "CORREO/CONTRASEÑA OBLIGATORIO", Toast.LENGTH_LONG).show();
        }else{
            if (switchRemember.isChecked()){
                save();
            }else{
                deleteUser();
            }
            logInScreen(true);
            signIn(inputEmail.getText().toString(), inputPassword.getText().toString());
        }
    }

    //  Comprueba el inicio de sesion en la BBDD de Firebase para determinar si son correctos
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            manualUserSearch();
                            logout = false;
                            Intent intent = new Intent (getApplicationContext(), HomeDesplegable.class);
                            intent.putExtra("email",inputEmail.getText().toString());
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Inicio fallido",
                                    Toast.LENGTH_SHORT).show();
                            inputEmail.setEnabled(true);
                            inputPassword.setEnabled(true);
                            switchRemember.setEnabled(true);
                            btnLogin.setEnabled(true);
                            logInScreen(false);
                        }
                    }
                });
    }

    //  Busca los usuarios
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

    //  Busca un usuario
    private void searchUser(String id){
        myRef.child("users/" + id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (!userFound){
                    for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                        //  Forma Manual
                        //  Se puede sacar dato por dato, sin embargo como en Health, podemos obtener un objeto completo
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

    //  Busca cada usuario (manual 1-1)
    private void manualUserSearch(){
        for (int i = 0; i < listUsers.size(); i++){
            searchUser(listUsers.get(i));
        }
    }

    //  Accion del boton de ayuda
    public void help(View v){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Ayuda")
                .setMessage("Disponemos de dos métodos de contacto: \n")
                .setPositiveButton("Correo electrónico", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Ayuda APP CAMPUS");
                        i.putExtra(Intent.EXTRA_TEXT, "\n*Mensaje generado desde la APP CAMPUS*\n\n");
                        i.putExtra(Intent.EXTRA_EMAIL, "vibevillagetfg@gmail.com");
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
        finish();
        super . onStop ( ) ;
    }
}