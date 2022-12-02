package com.example.fishfeeder;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Range;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.fishfeeder.bluetooth.BluetoothService;
import com.example.fishfeeder.bluetooth.FeedingFrequencyType;
import com.example.fishfeeder.bluetooth.FeedingProgram;
import com.example.fishfeeder.bluetooth.FeedingQtyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class FeedingSettingsFragment extends Fragment {

    public FeedingSettingsFragment()
    {
        super(R.layout.feeding_settings_fragment);
    }

    private Spinner frequencySpinner,qtySpinner,nrFishesSpinner;

    private void initLayout()
    {
        frequencySpinner = getActivity().findViewById(R.id.feedingFrequency);
        ArrayAdapter<CharSequence> ad1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.feeding_frquency, android.R.layout.simple_spinner_item);
        ad1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(ad1);

        qtySpinner = getActivity().findViewById(R.id.feedingQty);
        ArrayAdapter<CharSequence> ad2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.feeding_qty, android.R.layout.simple_spinner_item);
        ad2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtySpinner.setAdapter(ad2);


        nrFishesSpinner = getActivity().findViewById(R.id.numberOfFishes);

        ArrayAdapter<String> spaa = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spaa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, IntStream.range(1, 21).mapToObj(Integer::toString).collect(Collectors.toList()));
        }
        assert spaa != null;
        spaa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        nrFishesSpinner.setAdapter(spaa);
    }

    private FeedingProgram getCurrentFeedingProgram()
    {
        return new FeedingProgram(FeedingQtyType.valueOf(qtySpinner.getSelectedItemPosition()), FeedingFrequencyType.valueOf(frequencySpinner.getSelectedItemPosition()),nrFishesSpinner.getSelectedItemPosition());
    }

    private void initEventHandlers()
    {
        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                FeedingProgram pg = getCurrentFeedingProgram();
                // send message to the feeder with the new program
                BluetoothService bt = ((MainActivity)getActivity()).getBluetoohService();
                if(bt == null)
                    return;
                bt.sendMessage(pg.getMessage());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        };
        frequencySpinner.setOnItemSelectedListener(listener);
        qtySpinner.setOnItemSelectedListener(listener);
        nrFishesSpinner.setOnItemSelectedListener(listener);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout();
        initEventHandlers();
    }


}