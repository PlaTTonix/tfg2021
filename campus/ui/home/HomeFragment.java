package com.tfg.campus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.campus.*;
import com.tfg.campus.R;
import com.tfg.campus.databinding.FragmentHomeBinding;

import java.util.UUID;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public static boolean gymActive = true;
    public static boolean healthActive = true;
    public static boolean shopActive = true;

    LinearLayout gymLayout, healthLayout, shopLayout;
    ImageButton goGym, goHealth, goShop;
    TextView noSubscriptions, nameUser;

    ViewGroup.LayoutParams left;
    ViewGroup.LayoutParams center;
    ViewGroup.LayoutParams right;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        gymLayout = root.findViewById(R.id.gymHomeLayout);
        healthLayout = root.findViewById(R.id.healthHomeLayout);
        shopLayout = root.findViewById(R.id.shopHomeLayout);
        noSubscriptions = root.findViewById(R.id.textViewNoSubscriptions);
        nameUser = root.findViewById(R.id.textViewHomeUser);

        nameUser.setText("Bienvenido, " + Login.u.getName() + "!");

        left = gymLayout.getLayoutParams();
        center = healthLayout.getLayoutParams();
        right = shopLayout.getLayoutParams();

        checkActiveCategories();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}