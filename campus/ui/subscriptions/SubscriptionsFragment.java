package com.tfg.campus.ui.subscriptions;

import android.content.Intent;
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
import androidx.fragment.app.Fragment;

import com.tfg.campus.Gym;
import com.tfg.campus.Health;
import com.tfg.campus.HomeDesplegable;
import com.tfg.campus.R;
import com.tfg.campus.Shop;
import com.tfg.campus.databinding.FragmentSubscriptionsBinding;
import com.tfg.campus.ui.home.HomeFragment;

public class SubscriptionsFragment extends Fragment {

    LinearLayout gym, health, shop;
    ImageButton goGym, goHealth, goShop, goChat, goAdd;
    TextView gymON, gymOFF, healthON, healthOFF, shopON, shopOFF;
    Drawable finalMyDrawable;

    public static boolean gymSub = true, healthSub = true, shopSub = true;

    private FragmentSubscriptionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Drawable myDrawable = null;
        Resources res = getResources();
        try {
            myDrawable = Drawable.createFromXml(res, res.getXml(R.drawable.btn_subscriptions_unavailable));
        } catch (Exception ex) {
            Log.e("Error", "Exception loading drawable");
        }
        finalMyDrawable = myDrawable;
        goGym = root.findViewById(R.id.img_btn_gym_subs);
        gym = root.findViewById(R.id.LinearLayoutSubscriptionSport);
        gymON = root.findViewById(R.id.textViewSportON);
        gymOFF = root.findViewById(R.id.textViewSportOFF);

        goGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeFragment.gymActive){
                    gym.setGravity(51);// Se ubica en la izquierda
                    HomeFragment.gymActive = false;
                    gymON.setVisibility(View.GONE);
                    gymOFF.setVisibility(View.VISIBLE);
                    gym.setForeground(finalMyDrawable);
                }else{
                    gym.setGravity(53);
                    HomeFragment.gymActive = true;
                    gymON.setVisibility(View.VISIBLE);
                    gymOFF.setVisibility(View.GONE);
                    gym.setForeground(null);
                }
            }
        });

        goHealth = root.findViewById(R.id.img_btn_health_subs);
        health = root.findViewById(R.id.LinearLayoutSubscriptionHealth);
        healthON = root.findViewById(R.id.textViewHealthON);
        healthOFF = root.findViewById(R.id.textViewHealthOFF);
        goHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeFragment.healthActive){
                    health.setGravity(51);// Se ubica en la izquierda
                    HomeFragment.healthActive = false;
                    healthON.setVisibility(View.GONE);
                    healthOFF.setVisibility(View.VISIBLE);
                    health.setForeground(finalMyDrawable);
                }else{
                    health.setGravity(53);
                    HomeFragment.healthActive = true;
                    healthON.setVisibility(View.VISIBLE);
                    healthOFF.setVisibility(View.GONE);
                    health.setForeground(null);
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
                if (HomeFragment.shopActive){
                    shop.setGravity(51);// Se ubica en la izquierda
                    HomeFragment.shopActive = false;
                    shopON.setVisibility(View.GONE);
                    shopOFF.setVisibility(View.VISIBLE);
                    shop.setForeground(finalMyDrawable);
                }else{
                    shop.setGravity(53);
                    HomeFragment.shopActive = true;
                    shopON.setVisibility(View.VISIBLE);
                    shopOFF.setVisibility(View.GONE);
                    shop.setForeground(null);
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
        return root;
    }

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
             //HomeFragment.shopActive = true;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}