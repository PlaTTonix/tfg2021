package com.tfg.campus.desplegable.subscriptions;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.campus.model.Bill;
import com.tfg.campus.Login;
import com.tfg.campus.R;
import com.tfg.campus.databinding.FragmentSubscriptionsBinding;
import com.tfg.campus.desplegable.home.HomeFragment;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SubscriptionsFragment extends Fragment {

    LinearLayout gym, health, shop;
    ImageButton goGym, goHealth, goShop, goChat, goAdd;
    TextView gymON, gymOFF, healthON, healthOFF, shopON, shopOFF;
    Drawable finalMyDrawable;

    private String costMonthGym;
    private boolean overwritten = false;

    //  Usado para buscar la factura correspondiente al mes actual y anadirle el concepto
    Date date = new Date();
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    String actualMonth = String.valueOf(localDate.getMonthValue());

    FirebaseDatabase database;
    DatabaseReference myRef;

    private FragmentSubscriptionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // BBDD
        FirebaseApp.initializeApp(getContext());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //  Imagen para actualizar el boton a apagado
        Drawable myDrawable = null;
        Resources res = getResources();
        try {
            myDrawable = Drawable.createFromXml(res, res.getXml(R.drawable.btn_subscriptions_unavailable));
        } catch (Exception ex) {
            Log.e("Error", "Exception loading drawable");
        }
        finalMyDrawable = myDrawable;

        //  Asignacion
        goGym = root.findViewById(R.id.img_btn_gym_subs);
        gym = root.findViewById(R.id.LinearLayoutSubscriptionSport);
        gymON = root.findViewById(R.id.textViewSportON);
        gymOFF = root.findViewById(R.id.textViewSportOFF);

        goGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeFragment.gymActive || costMonthGym == null){    // Si al hacer click estaba encendido pasa a apagado. costMonthGym es null cuando no lo encuentra en la BBDD (sin internet o no existe)
                    if (costMonthGym == null){
                        Toast.makeText(getContext(), "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                    //  Cambia el boton
                    gym.setGravity(51);// Se ubica en la izquierda
                    HomeFragment.gymActive = false;
                    gymON.setVisibility(View.GONE);
                    gymOFF.setVisibility(View.VISIBLE);
                    gym.setForeground(finalMyDrawable);
                    //  Cambia el estado de la visualizacion en la BBDD
                    myRef.child("subscriptions/usersubscriptions/" + Login.u.getUid() + "/subid1").setValue("0");
                }else{
                    if (!overwritten){  //  Evitamos que el mensaje salga repetido
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                        dialog.setTitle("Suscripción a Servicio")
                                .setMessage("Proceso de alta en el servicio de Deportes\nEl servicio tiene un coste mensual de: " + costMonthGym + "€")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        //  Cambia el boton
                                        gym.setGravity(53); //  Se ubica en la derecha
                                        HomeFragment.gymActive = true;
                                        gymON.setVisibility(View.VISIBLE);
                                        gymOFF.setVisibility(View.GONE);
                                        gym.setForeground(null);
                                        myRef.child("subscriptions/usersubscriptions/" + Login.u.getUid() + "/subid1").setValue("gym");
                                        searchBillPending();
                                        Toast.makeText(getContext(), "Suscripción realizada", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                                    }
                                });
                        dialog.show();
                    }else{  //  Si el proceso ya se habia hecho antes, se saltan los pasos
                        gym.setGravity(53);
                        HomeFragment.gymActive = true;
                        gymON.setVisibility(View.VISIBLE);
                        gymOFF.setVisibility(View.GONE);
                        gym.setForeground(null);
                        myRef.child("subscriptions/usersubscriptions/" + Login.u.getUid() + "/subid1").setValue("gym");
                    }

                }
            }
        });

        //  Misma funcionalidad al anterior, sin embargo estos otros servicios no son opcionales
        goHealth = root.findViewById(R.id.img_btn_health_subs);
        health = root.findViewById(R.id.LinearLayoutSubscriptionHealth);
        healthON = root.findViewById(R.id.textViewHealthON);
        healthOFF = root.findViewById(R.id.textViewHealthOFF);
        goHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeFragment.healthActive || costMonthGym == null){
                    if (costMonthGym == null){
                        Toast.makeText(getContext(), "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                    health.setGravity(51);// Se ubica en la izquierda
                    HomeFragment.healthActive = false;
                    healthON.setVisibility(View.GONE);
                    healthOFF.setVisibility(View.VISIBLE);
                    health.setForeground(finalMyDrawable);
                    myRef.child("subscriptions/usersubscriptions/" + Login.u.getUid() + "/subid2").setValue("0");
                }else{
                    health.setGravity(53);
                    HomeFragment.healthActive = true;
                    healthON.setVisibility(View.VISIBLE);
                    healthOFF.setVisibility(View.GONE);
                    health.setForeground(null);
                    myRef.child("subscriptions/usersubscriptions/" + Login.u.getUid() + "/subid2").setValue("health");
                }
            }
        });

        goShop = root.findViewById(R.id.img_btn_shop_subs);
        shop = root.findViewById(R.id.LinearLayoutSubscriptionShop);
        shopON = root.findViewById(R.id.textViewShopON);
        shopOFF = root.findViewById(R.id.textViewShopOFF);
        goShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeFragment.shopActive || costMonthGym == null){
                    if (costMonthGym == null){
                        Toast.makeText(getContext(), "Sin conexión", Toast.LENGTH_SHORT).show();
                    }
                    shop.setGravity(51);// Se ubica en la izquierda
                    HomeFragment.shopActive = false;
                    shopON.setVisibility(View.GONE);
                    shopOFF.setVisibility(View.VISIBLE);
                    shop.setForeground(finalMyDrawable);
                    myRef.child("subscriptions/usersubscriptions/" + Login.u.getUid() + "/subid3").setValue("0");
                }else{
                    shop.setGravity(53);
                    HomeFragment.shopActive = true;
                    shopON.setVisibility(View.VISIBLE);
                    shopOFF.setVisibility(View.GONE);
                    shop.setForeground(null);
                    myRef.child("subscriptions/usersubscriptions/" + Login.u.getUid() + "/subid3").setValue("shop");
                }
            }
        });

        goChat = root.findViewById(R.id.img_btn_chat_subs);
        goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "* PROXIMAMENTE *", Toast.LENGTH_LONG).show();
            }
        });

        goAdd = root.findViewById(R.id.img_btn_add_subs);
        goAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "* PROXIMAMENTE *", Toast.LENGTH_LONG).show();
            }
        });

        checkCategoriesActive();
        checkPrice();
        checkSubscribed();

        return root;
    }

    //  Busca el precio del Deporte
    private void checkPrice(){
        myRef.child("subscriptions/gym/cost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                costMonthGym = snapshot.getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    //  Comprueba si ya estaba suscrito al Deporte
    private void checkSubscribed(){
        myRef.child("subscriptions/subscribed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.getValue().toString().equals("true")){
                    overwritten = true;
                }else{
                    overwritten = false;
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }

    //  Establece el estado de los botones al iniciar
    private void checkCategoriesActive(){
         if (HomeFragment.gymActive){
             gym.setGravity(53);
             gymON.setVisibility(View.VISIBLE);
             gymOFF.setVisibility(View.GONE);
             gym.setForeground(null);
         }else{
             gym.setGravity(51);// Se ubica en la izquierda
             gymON.setVisibility(View.GONE);
             gymOFF.setVisibility(View.VISIBLE);
             gym.setForeground(finalMyDrawable);
         }
         if (HomeFragment.healthActive){
             health.setGravity(53);
             healthON.setVisibility(View.VISIBLE);
             healthOFF.setVisibility(View.GONE);
             health.setForeground(null);
         }else{
             health.setGravity(51);// Se ubica en la izquierda
             healthON.setVisibility(View.GONE);
             healthOFF.setVisibility(View.VISIBLE);
             health.setForeground(finalMyDrawable);
         }
         if (HomeFragment.shopActive){
             shop.setGravity(53);

             shopON.setVisibility(View.VISIBLE);
             shopOFF.setVisibility(View.GONE);
             shop.setForeground(null);
         }else{
             shop.setGravity(51);// Se ubica en la izquierda
             shopON.setVisibility(View.GONE);
             shopOFF.setVisibility(View.VISIBLE);
             shop.setForeground(finalMyDrawable);
         }
    }

    // Busqueda en BBDD de la factura pendiente correspondiente al mes actual
    private void searchBillPending(){
        myRef.child("bills/Pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Bill b1 = objSnaptshot.getValue(Bill.class);
                    if (b1.getUid().equals(Login.u.getUid())){
                        if (b1.getMonthnumber().equals(actualMonth) && !overwritten){   //  La factura es la de este mes y no ha sido escrita anteriormente
                            overwritten = true;
                            myRef.child("bills/Pending/" + b1.getBid() + "/description").setValue("- Suscripción gimnasio: " + costMonthGym + "€\n");
                            myRef.child("bills/Pending/" + b1.getBid() + "/amount").setValue(String.valueOf(Float.parseFloat(b1.getAmount()) + Float.parseFloat(costMonthGym)));
                            myRef.child("subscriptions/subscribed").setValue(true);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}