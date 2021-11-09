package com.example.control.ui.yearlyView;

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

import com.example.control.databinding.FragmentYearlyBinding;

public class YearlyViewFragment extends Fragment {
    private YearlyViewModel yearlyViewModel;
    private FragmentYearlyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        yearlyViewModel =
                new ViewModelProvider(this).get(YearlyViewModel.class);

        binding = FragmentYearlyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textYearly;
        yearlyViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
