package com.saurabh.userdetails;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;


public class MatchFragment extends Fragment {

    private Bundle b;
    private TextView ageText;
    private CrystalRangeSeekbar ageRange;
    private CheckBox checkMale, checkFemale;
    private int flag;
    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = this.getArguments();
        flag = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_match, container, false);
        checkMale = v.findViewById(R.id.checkMale);
        checkFemale = v.findViewById(R.id.checkFemale);
        Button matchSubmit = v.findViewById(R.id.matchSubmit);
        matchSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getGenderInterests();
                b.putString("ageMin", String.valueOf(ageRange.getSelectedMinValue()));
                b.putString("ageMax", String.valueOf(ageRange.getSelectedMaxValue()));
                if(flag == 1) callNextFragment();
            }
        });

        ageRange = v.findViewById(R.id.ageRange);
        ageText = v.findViewById(R.id.ageText);
        ageRange.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                Log.d("listener", String.valueOf(minValue));
                String age = minValue + " - " + maxValue;
                ageText.setText(age);
            }
        });

        setOldValue();
        return v;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.LEFT, enter, 750);
    }

    private void setOldValue() {
        try {
            if(b.getString("interestMale").equals("1"))
                checkMale.setChecked(true);
        }
        catch (Exception e){
            Log.d("Male Interests", "not set");
        }
        try {
            if(b.getString("interestFemale").equals("1"))
                checkFemale.setChecked(true);
        }
        catch (Exception e){
            Log.d("Female Interests", "not set");
        }
        try {
            int ageMin = Integer.parseInt(b.getString("ageMin"));
            if(ageMin > 0) {
                Log.d("agemin", ageMin+"");
                ageRange.setMinStartValue(ageMin);
                Log.d("agemin", ageMin+"");
            }
            int ageMax = Integer.parseInt(b.getString("ageMax"));
            if(ageMax > 0) {
                ageRange.setMaxStartValue(ageMax);
            }
            ageRange.apply();
        }
        catch (Exception e) {
            Log.d("Age Range", "not set");
        }
    }

    private void getGenderInterests() {
        flag = 0;
        TextView matchError = getView().findViewById(R.id.matchError);
        if(checkMale.isChecked()) {
            b.putString("interestMale", "1");
            flag = 1;
        }
        else b.putString("interestMale", "0");
        if(checkFemale.isChecked()) {
            b.putString("interestFemale", "1");
            flag = 1;
        }
        else b.putString("interestFemale", "0");
        //no gender interest selected
        if(flag == 0) {
            matchError.setText("Select one gender interest");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                checkFemale.setButtonTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
                checkMale.setButtonTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
            }
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            checkMale.startAnimation(shake);
            checkFemale.startAnimation(shake);
        }
    }

    private void callNextFragment() {
        OptionalFragment frag = new OptionalFragment();
        frag.setArguments(b);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
