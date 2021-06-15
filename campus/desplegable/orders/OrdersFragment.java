package com.tfg.campus.desplegable.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tfg.campus.model.Order;
import com.tfg.campus.R;
import com.tfg.campus.databinding.FragmentOrdersBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private FragmentOrdersBinding binding;

    FirebaseDatabase database;
    DatabaseReference myRef;

    ListView listview;
    TextView code, date, amount, content, statusOrder, textDetails;
    ImageView statusimg;
    LinearLayout orderDetailsLayout;

    private List<Order> listOrders = new ArrayList<>();
    private ArrayAdapter<Order> adapter;
    private Order orderSelected;
    private boolean exit = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //  Inicio BBDD
        FirebaseApp.initializeApp(getContext());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //  Asignacion
        listview = root.findViewById(R.id.ListViewOrders);
        code = root.findViewById(R.id.textViewCodeOrder);
        date = root.findViewById(R.id.textViewDateOrder);
        amount = root.findViewById(R.id.textViewAmountOrder);
        content = root.findViewById(R.id.textViewContentOrder);
        statusOrder = root.findViewById(R.id.textViewStatusOrder);
        orderDetailsLayout = root.findViewById(R.id.linearLayoutOrdersDetails);
        statusimg = root.findViewById(R.id.imageViewOrderStatus);
        textDetails = root.findViewById(R.id.textViewOrderDetails);

        //  Seleccion en listado
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderSelected = (Order) parent.getItemAtPosition(position);
                fillGaps(orderSelected.getOid(), orderSelected.getDate(), orderSelected.getAmount(), orderSelected.getContent());
                imgStatusSelection();
                orderDetailsLayout.setVisibility(View.VISIBLE);
                textDetails.setVisibility(View.VISIBLE);
            }
        });

        searchAllOrders();

        return root;
    }

    //  Imagen del estado del pedido
    private void imgStatusSelection(){
        if (orderSelected.getStatus().equals("Entregado")){
            statusimg.setImageResource(R.drawable.ic_received);
        }else if (orderSelected.getStatus().equals("Enviado")){
            statusimg.setImageResource(R.drawable.ic_sent);
        }else{  //  Todos los pedidos estaran Pagados como necesidad minima
            statusimg.setImageResource(R.drawable.ic_paid);
        }
        statusOrder.setText("* " + orderSelected.getStatus() + " *");
    }

    private void fillGaps(String code, String date, String amount, String content){
        this.code.setText("Código: " + code);
        this.date.setText("Fecha: " + date);
        this.amount.setText("Total: " + amount + "€");
        this.content.setText(content);
    }

    private void searchAllOrders(){
        myRef.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listOrders.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Order o = objSnaptshot.getValue(Order.class);
                    listOrders.add(o);
                    if (!exit){ //  Evita actualizar el listado en caso de haber abandonado el fragmento
                        adapter = new ArrayAdapter<Order>(getContext(), android.R.layout.simple_list_item_1, listOrders);
                        listview.setAdapter(adapter);
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
        exit = true;
        super.onDestroyView();
        binding = null;
    }
}