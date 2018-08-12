package com.saurabh.userdetails;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class OptionalFragment extends Fragment {

    private Bundle b;
    private Spinner raceSpinner, religionSpinner;
    private String[] race= new String[]{"Select", "Asian", "White", "African American"};
    private String[] religion= new String[]{"Select", "Christianity", "Hinduism", "Judaism"};
    public OptionalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_optional, container, false);

        ArrayAdapter<String> raceAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, race);
        raceSpinner = v.findViewById(R.id.raceSpinner);
        raceSpinner.setAdapter(raceAdapter);
        ArrayAdapter<String> religionAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, religion);
        religionSpinner = v.findViewById(R.id.religionSpinner);
        religionSpinner.setAdapter(religionAdapter);

        Button optionalSubmit = v.findViewById(R.id.optionalSubmit);
        optionalSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRace();
                getReligion();
                callNextFragment();
            }
        });
        setOldValue();
        return v;
    }

    private void setOldValue() {
        try {
            String x = b.getString("religion");
            Log.d("religion",x);
            if(!x.equals("")) religionSpinner.setSelection(getIndexOf(religion, x));
        }
        catch (Exception e) {
            Log.d("religion","no selection");
        }
        try {
            String x = b.getString("race");
            Log.d("race",x);
            if(!x.equals("")) raceSpinner.setSelection(getIndexOf(race, x));
        }
        catch (Exception e) {
            Log.d("race","no selection");
        }
    }

    private int getIndexOf(String arr[], String n) {
        int index = -1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i].equals(n)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void getReligion() {
        String religion = religionSpinner.getSelectedItem().toString();
        if(!religion.equals("Select")) b.putString("religion", religion);
        else b.putString("religion", "");
    }

    private void getRace() {
        String race = raceSpinner.getSelectedItem().toString();
        if(!race.equals("Select")) b.putString("race", race);
        else b.putString("race", "");
    }

    private void callNextFragment() {
        ImageFragment frag = new ImageFragment();
        frag.setArguments(b);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
