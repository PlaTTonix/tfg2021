package com.tfg.campus.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.campus.Login;
import com.tfg.campus.R;
import com.tfg.campus.model.Sport;
import com.tfg.campus.model.UserSession;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Gym extends AppCompatActivity {

    //VARIABLES PRINCIPALES
    TextView title, newActSelWeekday, newActSel, myActWeekday, myAct, myActDescription;
    TextView actMonday, actTuesday, actWednesday, actThursday, actFriday, actSaturday, actSunday;
    RadioButton rBtnMonday, rBtnTuesday, rBtnWednesday, rBtnThursday, rBtnFriday, rBtnSaturday, rBtnSunday;
    Button btnClean, btnDelete, btnAssign;

    private boolean haveSession = false;
    private int actSelected;
    private boolean first = true;
    
    private Sport mySession;
    private List<Sport> listSports = new ArrayList<Sport>();
    private List<Sport> listSportsOrganized = new ArrayList<Sport>();
    private List<UserSession> listSessions = new ArrayList<UserSession>();

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        //  BBDD
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        myActWeekday = findViewById(R.id.textViewMyActWeekday);
        myAct = findViewById(R.id.textViewMyAct);
        myActDescription = findViewById(R.id.textViewMyActDescription);

        actMonday = findViewById(R.id.textViewActMonday);
        actTuesday = findViewById(R.id.textViewActTuesday);
        actWednesday = findViewById(R.id.textViewActWednesday);
        actThursday = findViewById(R.id.textViewActThursday);
        actFriday = findViewById(R.id.textViewActFriday);
        actSaturday = findViewById(R.id.textViewActSaturday);
        actSunday = findViewById(R.id.textViewActSunday);

        rBtnMonday = findViewById(R.id.radioButtonMonday);
        rBtnTuesday = findViewById(R.id.radioButtonTuesday);
        rBtnWednesday = findViewById(R.id.radioButtonWednesday);
        rBtnThursday = findViewById(R.id.radioButtonThursday);
        rBtnFriday = findViewById(R.id.radioButtonFriday);
        rBtnSaturday = findViewById(R.id.radioButtonSaturday);
        rBtnSunday = findViewById(R.id.radioButtonSunday);

        btnClean = findViewById(R.id.btnClean);
        btnDelete = findViewById(R.id.btnDelete);
        btnAssign = findViewById(R.id.btnAssign);

        //   TITULO DE LA ACTIVIDAD
        title.setText("Deportes");

        //  Seleccion del RadioButton
        rBtnMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusWeeklyOptions(false);
                actSelected = 1;
            }
        });
        rBtnTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusWeeklyOptions(false);
                actSelected = 2;
            }
        });
        rBtnWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusWeeklyOptions(false);
                actSelected = 3;
            }
        });
        rBtnThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusWeeklyOptions(false);
                actSelected = 4;
            }
        });
        rBtnFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusWeeklyOptions(false);
                actSelected = 5;
            }
        });
        rBtnSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusWeeklyOptions(false);
                actSelected = 6;
            }
        });
        rBtnSunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusWeeklyOptions(false);
                actSelected = 7;
            }
        });

        //  Texto inicial sin reserva
        if (!haveSession){
            myActWeekday.setText("Sin sesión reservada");
            myAct.setVisibility(View.GONE);
            myActDescription.setVisibility(View.GONE);
        }

        searchAllSports();
        searchAllSessions();
    }

    private void fillGaps(String weekday, String name, String description){
        myActWeekday.setText(weekday);
        myAct.setText(name);
        myActDescription.setText(description);
    }

    //  Establece el deporte en cada dia
    private void assignSportToWeek(){
        listSportsOrganized.clear();
        for (Sport s : listSports){
            switch (s.getWeekday()){
                case "Lunes": actMonday.setText(s.getName()); listSportsOrganized.add(0,s); break;
                case "Martes": actTuesday.setText(s.getName());listSportsOrganized.add(1,s); break;
                case "Miercoles": actWednesday.setText(s.getName());listSportsOrganized.add(2,s); break;
                case "Jueves": actThursday.setText(s.getName());listSportsOrganized.add(3,s); break;
                case "Viernes": actFriday.setText(s.getName());listSportsOrganized.add(4,s); break;
                case "Sabado": actSaturday.setText(s.getName());listSportsOrganized.add(5,s); break;
                case "Domingo": actSunday.setText(s.getName());listSportsOrganized.add(6,s); break;
            }
        }
    }

    //  Busca en BBDD todos los deportes
    private void searchAllSports(){
        myRef.child("gym/sports").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listSports.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Sport s1 = objSnaptshot.getValue(Sport.class);
                    listSports.add(s1);
                }
                assignSportToWeek();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //  Des/Habilita los RadioButton
    private void statusWeeklyOptions(boolean x){
        rBtnMonday.setEnabled(x);
        rBtnTuesday.setEnabled(x);
        rBtnWednesday.setEnabled(x);
        rBtnThursday.setEnabled(x);
        rBtnFriday.setEnabled(x);
        rBtnSaturday.setEnabled(x);
        rBtnSunday.setEnabled(x);
    }

    //  Limpia la seleccion
    private void cleanWeeklyOptions(){
        rBtnMonday.setChecked(false);
        rBtnTuesday.setChecked(false);
        rBtnWednesday.setChecked(false);
        rBtnThursday.setChecked(false);
        rBtnFriday.setChecked(false);
        rBtnSaturday.setChecked(false);
        rBtnSunday.setChecked(false);
    }

    public void clean(View v){
        if (!haveSession){
            cleanWeeklyOptions();
            statusWeeklyOptions(true);
        }else{
            Toast.makeText(this,"Actualmente tiene una sesión asignada",Toast.LENGTH_SHORT).show();
        }

    }

    //  Envia la sesion del usuario a la BBDD
    private void createSession(UserSession us){
        myRef.child("gym/usersSessions").child(us.getUid()).setValue(us);
    }

    //  Guarda la sesion del usuario
    public void saveSession(View v){
        if (!haveSession && actSelected != 0){
            switch (actSelected){   //  Establece los datos dependiendo de la eleccion del usuario
                case 1: newActSelWeekday = findViewById(R.id.textViewMonday); newActSel = findViewById(R.id.textViewActMonday); break;
                case 2: newActSelWeekday = findViewById(R.id.textViewTuesday); newActSel = findViewById(R.id.textViewActTuesday); break;
                case 3: newActSelWeekday = findViewById(R.id.textViewWednesday); newActSel = findViewById(R.id.textViewActWednesday); break;
                case 4: newActSelWeekday = findViewById(R.id.textViewThursday); newActSel = findViewById(R.id.textViewActThursday); break;
                case 5: newActSelWeekday = findViewById(R.id.textViewFriday); newActSel = findViewById(R.id.textViewActFriday); break;
                case 6: newActSelWeekday = findViewById(R.id.textViewSaturday); newActSel = findViewById(R.id.textViewActSaturday); break;
                case 7: newActSelWeekday = findViewById(R.id.textViewSunday); newActSel = findViewById(R.id.textViewActSunday); break;
            }

            if (listSportsOrganized.size() != 0){   //  Cuando no es capaz de leer los datos el listado estara vacio, suele darse en la perdida de internet
                UserSession us = new UserSession(listSportsOrganized.get(actSelected-1).getSid(), Login.u.getUid());
                createSession(us); //   Envia la peticion a la BBDD

                myActWeekday.setText(newActSelWeekday.getText().toString());
                myAct.setText(newActSel.getText().toString());
                myAct.setVisibility(View.VISIBLE);
                myActDescription.setVisibility(View.VISIBLE);
            }

        }else{
            Toast.makeText(this,"Sesión asignada/Seleccione sesión",Toast.LENGTH_SHORT).show();
        }
    }

    //  Mensaje visualizado al tener una sesion reservada
    private void sportReservedMessage(){

        if (first){
            first = false;
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Atención!")
                    .setMessage("Sesión reservada.\nPor motivos de organización rogamos a los residentes que eviten cambios innecesarios.\n\nGracias!")
                    .setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            fillGaps(mySession.getWeekday(), mySession.getName(), mySession.getDescription());
                            myAct.setVisibility(View.VISIBLE);
                            myActDescription.setVisibility(View.VISIBLE);
                        }
                    }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    myAct.setVisibility(View.VISIBLE);
                    myActDescription.setVisibility(View.VISIBLE);
                }
            });
            dialog.show();
        }
    }

    //  Busca los datos del deporte
    private void searchSport(String sid){ //  Busca el Deporte
        myRef.child("gym/sports/" + sid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mySession = snapshot.getValue(Sport.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        sportReservedMessage();
    }

    //  Busca todas las sesiones del usuario
    private void searchAllSessions(){
        myRef.child("gym/usersSessions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listSessions.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    UserSession us = objSnaptshot.getValue(UserSession.class);
                    listSessions.add(us);
                    if (us.getUid().equals(Login.u.getUid())){ // La sesion corresponde al usuario logeado
                        haveSession = true;
                        statusWeeklyOptions(false);
                        searchSport(us.getSid());
                    }else{
                        myActWeekday.setText("Sin sesión reservada");
                        myAct.setVisibility(View.GONE);
                        myActDescription.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    //  Eliminar la sesion reservada
    public void deleteSession(View v){
        if (!haveSession){
            Toast.makeText(this,"No tiene una sesión asignada",Toast.LENGTH_SHORT).show();
        }else{
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Confirmación")
                    .setMessage("¿Desea eliminar la sesión reservada?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            haveSession = false;
                            actSelected = 0;
                            myActWeekday.setText("Sin sesión reservada");
                            myAct.setVisibility(View.GONE);
                            myActDescription.setVisibility(View.GONE);
                            cleanWeeklyOptions();
                            statusWeeklyOptions(true);
                            myRef.child("gym/usersSessions").child(Login.u.getUid()).removeValue(); //  Elimina de la BBDD
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();
        }
    }

}