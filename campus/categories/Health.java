package com.tfg.campus.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.campus.Login;
import com.tfg.campus.R;
import com.tfg.campus.model.Doctor;
import com.tfg.campus.model.UserAppointment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Health extends AppCompatActivity {

    //VARIABLES PRINCIPALES
    TextView title, doctorName, date, time, place, textMyAppointment;
    TextInputEditText symptom;
    Button btnRefresh, btnCancel, btnShedule, btnEdit, btnConfirm;
    Space edit, update;
    LinearLayout myAppointmentLayout;

    private boolean haveAppointment = false;
    //private boolean doctorFound = false;
    private boolean optionSelected = false;
    private boolean first = true;
    private String appointmentSymptoms; //  Guardamos los sintomas de la cita agendada
    ListView listview;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private Doctor doctorSelected;
    private List<Doctor> listDoctors = new ArrayList<Doctor>();
    private List<UserAppointment> listAppointments = new ArrayList<UserAppointment>();
    private ArrayAdapter<Doctor> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        //  BBDD
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //  TITULO DEL ACTIONBAR
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        //  ASIGNACION VARIABLES
        title = findViewById(R.id.tvTitle);
        doctorName = findViewById(R.id.textViewDoctorName);
        date = findViewById(R.id.textViewDate);
        time = findViewById(R.id.textViewTime);
        place = findViewById(R.id.textViewPlace);
        symptom = findViewById(R.id.textInputHealthSymptom);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnCancel = findViewById(R.id.btn_cancelMedicalAppointment);
        btnShedule = findViewById(R.id.btn_scheduleMedicalAppointment);
        btnEdit = findViewById(R.id.btn_editMedicalAppointment);
        btnConfirm = findViewById(R.id.btn_uploadMedicalAppointment);
        listview = findViewById(R.id.listViewHealthDoctors);
        edit = findViewById(R.id.spaceedit);
        update = findViewById(R.id.spaceupdate);
        textMyAppointment = findViewById(R.id.textViewMyAppointment);
        myAppointmentLayout = findViewById(R.id.linearLayoutAppointmentDetails);

        //  TITULO DE LA ACTIVIDAD
        title.setText("Salud");

        searchAllDoctors();
        searchAllAppointments();

        //  Seleccion de la cita en el listado
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!haveAppointment){  //  Podra seleccionar solo si no tiene una cita asignada
                    optionSelected = true;
                    doctorSelected = (Doctor) parent.getItemAtPosition(position);
                    fillGaps(doctorSelected.getName(), doctorSelected.getDate(), doctorSelected.getTime(), doctorSelected.getPlace(), "");
                    showMyAppointmentDetails(true);
                }
            }
        });
    }

    //  Control de la visualizacion de Mi Cita
    private void showMyAppointmentDetails(boolean visibility){
        if (visibility){
            textMyAppointment.setVisibility(View.VISIBLE);
            myAppointmentLayout.setVisibility(View.VISIBLE);

        }else{
            textMyAppointment.setVisibility(View.GONE);
            myAppointmentLayout.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }
    }

    //  Buscamos todos los doctores
    private void searchAllDoctors(){
        myRef.child("appointments/doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listDoctors.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Doctor d1 = objSnaptshot.getValue(Doctor.class);
                    listDoctors.add(d1);

                    //  Listamos los doctores
                    adapter = new ArrayAdapter<Doctor>(getApplicationContext(), android.R.layout.simple_list_item_1, listDoctors);
                    listview.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //  Mensaje de la cita reservada
    private void appointmentReservedMessage(){
        if (haveAppointment && first){
            first = false;
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Atención!")
                    .setMessage("Cita reservada.\nPor motivos de organización rogamos a los pacientes que eviten cambios innecesarios en las fechas.\n\nGracias!")
                    .setPositiveButton("De acuerdo", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            fillGaps(doctorSelected.getName(), doctorSelected.getDate(),doctorSelected.getTime(), doctorSelected.getPlace(), appointmentSymptoms);
                        }
                    }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    fillGaps(doctorSelected.getName(), doctorSelected.getDate(),doctorSelected.getTime(), doctorSelected.getPlace(), appointmentSymptoms);
                }
            });
            dialog.show();

            showMyAppointmentDetails(true);
            btnEdit.setVisibility(View.VISIBLE);    //  El boton de editar los sintomas solo se hace visible cuando hay una cita asignada
            symptom.setEnabled(false);
        }
    }

    // Busqueda de un doctor
    private void searchDoctorReserved(String did){ //  Busca el Doctor
        myRef.child("appointments/reserved/" + did).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                doctorSelected = snapshot.getValue(Doctor.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        appointmentReservedMessage();
    }

    private void fillGaps(String doctorName, String date, String time, String place, String symptom){
        this.doctorName.setText("Doctor: " + doctorName);
        this.date.setText("Fecha:  " + date);
        this.time.setText("Hora:    " + time);
        this.place.setText("Lugar:   " + place);
        this.symptom.setText(symptom);
    }

    public void refreshList(View v){
        searchAllDoctors();
    }

    //  Asigna la cita al usuario logeado, el doctor pasa a estar reservado y se elimina de la lista
    private void createAppointment(){
        UserAppointment uap = new UserAppointment();
        uap.setdID(doctorSelected.getDid());
        uap.setSymptoms(symptom.getText().toString());
        uap.setuID(Login.u.getUid());
        myRef.child("appointments/usersAppointments").child(Login.u.getUid()).setValue(uap);

        myRef.child("appointments/reserved").child(doctorSelected.getDid()).setValue(doctorSelected);

        myRef.child("appointments/doctors").child(doctorSelected.getDid()).removeValue();
    }

    //  Mensaje antes de asignar la cita
    public void saveAppointment(View v){
        if (!haveAppointment && optionSelected){    //  No debe tener cita asignada previamente y debe haber seleccionado una opcion
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Consejo")
                    .setMessage("Especificar el/los sintoma/as o la finalidad de la cita ayudará a tener una valoración más eficaz.")
                    .setPositiveButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        }
                    })
                    .setNegativeButton("Continuar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            createAppointment();
                            btnEdit.setVisibility(View.VISIBLE);

                            Toast.makeText(getApplicationContext(), "Cita agendada", Toast.LENGTH_LONG).show();
                            symptom.setEnabled(false);
                        }
                    });
            dialog.show();
        }else{
            Toast.makeText(this,"Cita asignada/Escoga cita",Toast.LENGTH_SHORT).show();
        }
    }

    //  Busca una cita
    private void searchAppointment(String uid){
        myRef.child("appointments/usersAppointments/" + uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                UserAppointment ua = snapshot.getValue(UserAppointment.class);
                if (ua != null){
                    searchDoctorReserved(ua.getdID());
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //  Busca si el usuario tiene una cita
    private void searchAllAppointments(){
        myRef.child("appointments/usersAppointments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listAppointments.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    UserAppointment ua = objSnaptshot.getValue(UserAppointment.class);
                    listAppointments.add(ua);
                    if (ua.getuID().equals(Login.u.getUid())){
                        haveAppointment = true;
                        searchAppointment(ua.getuID());
                        appointmentSymptoms = ua.getSymptoms();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    //  Accion del boton Editar sintomas
    public void editSymptom(View v){
        symptom.setEnabled(true);
        btnEdit.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.VISIBLE);
        update.setVisibility(View.VISIBLE);
    }

    //  Accion del boton actualizar sintomas
    public void uploadSymptoms(View v){
        appointmentSymptoms = symptom.getText().toString();
        symptom.setEnabled(false);
        btnEdit.setVisibility(View.VISIBLE);
        edit.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.GONE);
        update.setVisibility(View.GONE);

        createAppointment();
        Toast.makeText(this, "Cita actualizada", Toast.LENGTH_LONG).show();

    }

    //  Eliminacion de la cita en la BBDD
    private void deleteAppointmentBD(){
        myRef.child("appointments/usersAppointments").child(Login.u.getUid()).removeValue();
        Toast.makeText(this, "Cita eliminada", Toast.LENGTH_LONG).show();

        //  Se encarga de eliminar la cita de Reservado
        myRef.child("appointments/reserved").child(doctorSelected.getDid()).removeValue();

        //  Las citas eliminadas pasan a revision en Cancelled
        myRef.child("appointments/cancelled").child(doctorSelected.getDid()).setValue(doctorSelected);

        //  Volvemos a introducir el doctor para no vaciar las lista, por lo general las citas quedarian en Cancelled pendientes de revision.
        myRef.child("appointments/doctors").child(doctorSelected.getDid()).setValue(doctorSelected);
    }

    //  Cancelar cita asignada
    public void cancelMedicalAppointment(View v){
        if (haveAppointment){
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Confirmación")
                    .setMessage("¿Desea eliminar la cita programada?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            haveAppointment = false;
                            optionSelected = false;
                            symptom.setEnabled(true);
                            deleteAppointmentBD();
                            fillGaps("","","","","");
                            showMyAppointmentDetails(false);
                            //btnEdit.setVisibility(View.GONE);   //  Al no tener cita el boton de editar desaparece
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