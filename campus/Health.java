package com.tfg.campus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Health extends AppCompatActivity {

    //VARIABLES PRINCIPALES
    TextView title, doctorName, date, time, address;
    TextInputEditText symptom;
    Button btnRefresh, btnCancel, btnShedule, btnEdit, btnConfirm;

    private boolean haveAppointment = false;
    ListView listview;
    ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        //TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        doctorName = findViewById(R.id.textViewDoctorName);
        date = findViewById(R.id.textViewDate);
        time = findViewById(R.id.textViewTime);
        address = findViewById(R.id.textViewAddress);
        symptom = findViewById(R.id.textInputDateBills);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnCancel = findViewById(R.id.btn_cancelMedicalAppointment);
        btnShedule = findViewById(R.id.btn_scheduleMedicalAppointment);
        btnEdit = findViewById(R.id.btn_editMedicalAppointment);
        btnConfirm = findViewById(R.id.btn_uploadMedicalAppointment);

        listview = (ListView) findViewById(R.id.listView);
        names = new ArrayList<String>();
        names.add("Antonio - 10/6/2021");
        names.add("Pepe - 11/6/2021");
        names.add("Fernando - 12/6/2021");
        names.add("Ernesto - 13/6/2021");
        names.add("Paco - 20/6/2021");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter);

        title.setText("Salud"); //TITULO DE LA ACTIVIDAD

        if (haveAppointment){
            // FALTA
            // SI TIENE UNA CITA ACTUALMENTE, SE INTRODUCIRÁN LOS DATOS. PEDIR A LA BBDD
            //fillGaps();
            symptom.setEnabled(false);
        }
    }

    private void fillGaps(String doctorName, String date, String time, String address, String symptom){
        this.doctorName.setText(doctorName);
        this.date.setText(date);
        this.time.setText(time);
        this.address.setText(address);
        this.symptom.setText(symptom);
    }

    public void refreshList(View v){
        //  ACCESO A BBDD PARA EXTRAER TODO EL LISTADO EN ORDEN
    }

    public void saveAppointment(View v){
        if (!haveAppointment){
            //fillGaps();
            //  ELIMINAR CITA DEL LISTADO DE LA BBDD PORQUE YA ESTA COGIDA, USAR UN METODO
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Consejo")
                    .setMessage("Especificar el/los sintoma/as o la finalidad de la cita ayudará a tener una valoración más eficaz")
                    .setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    })
                    .setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            //  ENVIAR A BBDD TODA LA CITA ASOCIADA AL RESIDENTE
                            symptom.setEnabled(false);
                            haveAppointment = true;
                        }
                    });
            dialog.show();

            //  ACTUALIZAR LISTADO EN APP

        }else{
            Toast.makeText(this,"Actualmente tiene una cita asignada",Toast.LENGTH_SHORT).show();
        }
    }

    public void editSymptom(View v){
        if (haveAppointment){
            symptom.setEnabled(true);
            btnEdit.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.VISIBLE);
        }
    }

    public void uploadSymptoms(View v){
        symptom.setEnabled(false);
        btnEdit.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.GONE);
    }

    private void uploadNewSymptomsBBDD(){
        //  ACTUALIZA EN LA BASE DE DATOS PARA LA CITA LOS SINTOMAS NUEVOS
    }

    public void cancelMedicalAppointment(View v){
        if (haveAppointment){
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Confirmación")
                    .setMessage("¿Desea eliminar la cita programada?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            fillGaps("NOMBRE DOCTOR", "FECHA", "HORA", "LUGAR", "");
                            haveAppointment = false;
                            symptom.setEnabled(true);
                            //  DESASOCIAR LA CITA AL RESIDENTE EN LA BBDD. OPCIONAL= AÑADRILA DE NUEVO AL LISTADO
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    });
            dialog.show();
        }else{
            Toast.makeText(this,"Actualmente no tiene una cita asignada",Toast.LENGTH_SHORT).show();
        }
    }
}