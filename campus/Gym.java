package com.tfg.campus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class Gym extends AppCompatActivity {

    //VARIABLES PRINCIPALES
    TextView title, newActSelWeekday, newActSel, myActWeekday, myAct, myActDescription;
    RadioButton rBtnMonday, rBtnTuesday, rBtnWednesday, rBtnThursday, rBtnFriday, rBtnSaturday, rBtnSunday;
    Button btnClean, btnDelete, btnAssign;

    private boolean haveSession = false;
    private int actSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        //TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        myActWeekday = findViewById(R.id.textViewMyActWeekday);
        myAct = findViewById(R.id.textViewMyAct);
        myActDescription = findViewById(R.id.textViewMyActDescription);

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

        title.setText("Deportes"); //TITULO DE LA ACTIVIDAD

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

        if (haveSession){
            statusWeeklyOptions(false);

            /* EXTRAER DE BBDD LA SESION QUE YA TIENE myActWeekday.setText("");
            myAct.setVisibility(View.VISIBLE);
            myActDescription.setVisibility(View.VISIBLE);*/
        }else{
            myActWeekday.setText("Sin sesión reservada");
            myAct.setVisibility(View.GONE);
            myActDescription.setVisibility(View.GONE);
        }
    }

    private void statusWeeklyOptions(boolean x){
        rBtnMonday.setEnabled(x);
        rBtnTuesday.setEnabled(x);
        rBtnWednesday.setEnabled(x);
        rBtnThursday.setEnabled(x);
        rBtnFriday.setEnabled(x);
        rBtnSaturday.setEnabled(x);
        rBtnSunday.setEnabled(x);
    }

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

    public void saveSession(View v){
        if (!haveSession){
            haveSession = true;
            switch (actSelected){
                case 1: newActSelWeekday = findViewById(R.id.textViewMonday); newActSel = findViewById(R.id.textViewActMonday); break;
                case 2: newActSelWeekday = findViewById(R.id.textViewTuesday); newActSel = findViewById(R.id.textViewActTuesday); break;
                case 3: newActSelWeekday = findViewById(R.id.textViewWednesday); newActSel = findViewById(R.id.textViewActWednesday); break;
                case 4: newActSelWeekday = findViewById(R.id.textViewThursday); newActSel = findViewById(R.id.textViewActThursday); break;
                case 5: newActSelWeekday = findViewById(R.id.textViewFriday); newActSel = findViewById(R.id.textViewActFriday); break;
                case 6: newActSelWeekday = findViewById(R.id.textViewSaturday); newActSel = findViewById(R.id.textViewActSaturday); break;
                case 7: newActSelWeekday = findViewById(R.id.textViewSunday); newActSel = findViewById(R.id.textViewActSunday); break;
            }
            myAct.setVisibility(View.VISIBLE);
            myActDescription.setVisibility(View.VISIBLE);

            myActWeekday.setText(newActSelWeekday.getText().toString());
            myAct.setText(newActSel.getText().toString());
            //FALTA EXTRAER LA DESCRIPCION DE LA ACTIVIDAD DE LA BBDD
        }else{
            Toast.makeText(this,"Actualmente tiene una sesión asignada",Toast.LENGTH_SHORT).show();
        }
    }

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
                            myActWeekday.setText("Sin sesión reservada");
                            myAct.setVisibility(View.GONE);
                            myActDescription.setVisibility(View.GONE);
                            cleanWeeklyOptions();
                            statusWeeklyOptions(true);
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