package com.tfg.campus.desplegable.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tfg.campus.*;
import com.tfg.campus.R;
import com.tfg.campus.categories.Gym;
import com.tfg.campus.categories.Health;
import com.tfg.campus.categories.Shop;
import com.tfg.campus.databinding.FragmentHomeBinding;
import com.tfg.campus.model.UserSubscription;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    //  Controlan las categorias visualizadas
    public static boolean gymActive = false;
    public static boolean healthActive = false;
    public static boolean shopActive = false;

    LinearLayout gymLayout, healthLayout, shopLayout;
    ImageButton goGym, goHealth, goShop;
    TextView noSubscriptions, nameUser;

    //  Parametros para la posicion de las categorias
    ViewGroup.LayoutParams left;
    ViewGroup.LayoutParams center;
    ViewGroup.LayoutParams right;

    UserSubscription mySubscription;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //  Asignacion
        gymLayout = root.findViewById(R.id.gymHomeLayout);
        healthLayout = root.findViewById(R.id.healthHomeLayout);
        shopLayout = root.findViewById(R.id.shopHomeLayout);
        noSubscriptions = root.findViewById(R.id.textViewNoSubscriptions);
        nameUser = root.findViewById(R.id.textViewHomeUser);

        //  Texto principal de home
        nameUser.setText("Bienvenido, " + Login.u.getName() + "!");

        // Guardado de parametros para la alineacion de categorias
        left = gymLayout.getLayoutParams();
        center = healthLayout.getLayoutParams();
        right = shopLayout.getLayoutParams();

        searchUserSubscription();

        //  Acciones de botones principales
        goGym = root.findViewById(R.id.img_btn_gym);
        goGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Gym.class);
                startActivity(intent);
            }
        });

        goHealth = root.findViewById(R.id.img_btn_health);
        goHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Health.class);
                startActivity(intent);
            }
        });

        goShop = root.findViewById(R.id.img_btn_shop);
        goShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Shop.class);
                startActivity(intent);
            }
        });

        return root;
    }

    //  Encargado de ordenar las categorias, gravity y visibilidad
    private void checkActiveCategories(){

        noSubscriptions.setVisibility(View.GONE);
        if (!gymActive){
            gymLayout.setVisibility(View.GONE);
        }else{
            gymLayout.setVisibility(View.VISIBLE);
            gymLayout.setLayoutParams(left);   // IZQUIERDA
        }
        if (!healthActive){
            healthLayout.setVisibility(View.GONE);
        }else{
            healthLayout.setVisibility(View.VISIBLE);
            healthLayout.setLayoutParams(center);    // CENTRO
        }
        if (!shopActive){
            shopLayout.setVisibility(View.GONE);
        }else{
            shopLayout.setVisibility(View.VISIBLE);
            shopLayout.setLayoutParams(right);    // DERECHA
        }

        if (!gymActive && healthActive && shopActive){
            healthLayout.setLayoutParams(left);
        }else if (gymActive && !healthActive && !shopActive){
            gymLayout.setLayoutParams(center);
        }else if (gymActive && healthActive && !shopActive){
            healthLayout.setLayoutParams(right);
        }else if (!gymActive && !healthActive && shopActive){
            shopLayout.setLayoutParams(center);
        }else if (!gymActive && !healthActive && !shopActive){
            noSubscriptions.setVisibility(View.VISIBLE);
        }
    }

    //  Extrae del listado el estado de las categorias del usuario, es decir, si la debe mostrar o no
    private void checkActiveCategoriesBD(){
        if (mySubscription.getSubid1().equals("gym")){
            gymActive = true;
        }else{
            gymActive = false;
        }

        if (mySubscription.getSubid2().equals("health")){
            healthActive = true;
        }else{
            healthActive = false;
        }

        if (mySubscription.getSubid3().equals("shop")){
            shopActive = true;
        }else{
            shopActive = false;
        }

        checkActiveCategories();
    }

    //  Busqueda de la configuracion del usuario para sus categorias
    private void searchUserSubscription(){
        for (UserSubscription usub : Login.listUsersSubscriptions){
            if (usub.getUid().equals(Login.u.getUid())){    //  Encuentra la configuracion correspondiente al usuario logeado
                mySubscription = usub;
            }
        }
        checkActiveCategoriesBD();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}