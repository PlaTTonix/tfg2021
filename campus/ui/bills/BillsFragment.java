package com.tfg.campus.ui.bills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.tfg.campus.R;
import com.tfg.campus.databinding.FragmentBillsBinding;

import java.util.ArrayList;

public class BillsFragment extends Fragment {

    private BillsViewModel billsViewModel;
    private FragmentBillsBinding binding;

    Spinner list;
    ListView listview;
    ArrayList<String> names;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        billsViewModel =
                new ViewModelProvider(this).get(BillsViewModel.class);

        binding = FragmentBillsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        list = root.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.list, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);

        listview = (ListView) root.findViewById(R.id.ListViewBills);
        names = new ArrayList<String>();
        names.add("Enero - 103,50€");
        names.add("Febrero - 50€");
        names.add("Marzo - 104,10€");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, names);
        listview.setAdapter(adapter1);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}