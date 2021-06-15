package com.tfg.campus.desplegable.bills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.tfg.campus.databinding.FragmentBillsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BillsFragment extends Fragment {

    private FragmentBillsBinding binding;

    Spinner list;
    ListView listview;
    TextView code, date, name, dni, amount, description, details;
    LinearLayout lLayDetails;

    FirebaseDatabase database;
    DatabaseReference myRef;

    private List<Bill> listBillsPaid = new ArrayList<Bill>();
    private List<Bill> listBillsPending = new ArrayList<Bill>();
    private ArrayAdapter<Bill> adapterPaid;
    private ArrayAdapter<Bill> adapterPending;
    private boolean exit = false;

    private Bill billSelected;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBillsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //  BBDD
        FirebaseApp.initializeApp(getContext());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        //  Busqueda de datos
        searchAllBillsPending();
        searchAllBillsPaid();

        listview = root.findViewById(R.id.ListViewBills);

        //  Detalles Factura
        code = root.findViewById(R.id.textViewCodeBills);
        date = root.findViewById(R.id.textViewDateBills);
        name = root.findViewById(R.id.textViewNameBills);
        dni = root.findViewById(R.id.textViewDNIBills);
        amount = root.findViewById(R.id.textViewAmountBills);
        description = root.findViewById(R.id.textViewExpensesBills);
        details = root.findViewById(R.id.textViewBillsDetails);
        lLayDetails = root.findViewById(R.id.linearLayoutBillsDetails);

        //  Rellenar Spinner
        list = root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapterList = ArrayAdapter.createFromResource(getActivity(), R.array.list, android.R.layout.simple_list_item_1);
        list.setAdapter(adapterList);

        //  Seleccion Spinner
        list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){ //Pendientes
                    listview.setAdapter(adapterPending);
                }else if (position == 1){   //Pagadas
                    listview.setAdapter(adapterPaid);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //  Seleccion listado
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                billSelected = (Bill) parent.getItemAtPosition(position);
                fillGaps(billSelected.getBid(), billSelected.getDate(), Login.u.getName(), Login.u.getDni(), billSelected.getAmount(), billSelected.getDescription());
            }
        });
        return root;
    }

    private void fillGaps(String code, String date, String name, String dni, String amount, String description){
        this.code.setText("* FACTURA: " + code + " *");
        this.date.setText("- Fecha: " + date);
        this.name.setText("- Residente: " + name);
        this.dni.setText("- DNI: " + dni);
        this.amount.setText("Total: " + amount + "€");
        this.description.setText(description);
        details.setVisibility(View.VISIBLE);
        lLayDetails.setVisibility(View.VISIBLE);
    }

    private void searchAllBillsPending(){
        myRef.child("bills/Pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listBillsPending.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Bill b1 = objSnaptshot.getValue(Bill.class);
                    if (b1.getUid().equals(Login.u.getUid())){  //  La factura corresponde con el usuario logeado
                        listBillsPending.add(b1);
                    }
                }
                if (!exit){ //  Evita actualizar el listado abandonando el fragmento
                    adapterPending = new ArrayAdapter<Bill>(getContext(), android.R.layout.simple_list_item_1, listBillsPending);
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
    }

    private void searchAllBillsPaid(){
        myRef.child("bills/Paid").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listBillsPaid.clear();
                for (DataSnapshot objSnaptshot : snapshot.getChildren()){
                    Bill b1 = objSnaptshot.getValue(Bill.class);
                    if (b1.getUid().equals(Login.u.getUid())){
                        listBillsPaid.add(b1);
                    }
                }
                if (!exit){
                    adapterPaid = new ArrayAdapter<Bill>(getContext(), android.R.layout.simple_list_item_1, listBillsPaid);
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