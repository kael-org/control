package com.example.control.ui.monthlyView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.control.databinding.FragmentMonthlyBinding;

public class MonthlyViewFragment extends Fragment {

    private MonthlyViewModel monthlyViewModel;
    private FragmentMonthlyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        monthlyViewModel =
                new ViewModelProvider(this).get(MonthlyViewModel.class);

        binding = FragmentMonthlyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMonthly;
        monthlyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}