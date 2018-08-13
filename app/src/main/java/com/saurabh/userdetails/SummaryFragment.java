package com.saurabh.userdetails;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.labo.kaji.fragmentanimations.CubeAnimation;


/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment {

    private Bundle b;

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = this.getArguments();
//        b.putString("email", "saurabh.m1205@gmail.com");
//        b.putString("password", "asdfr45fs");
//
//        b.putString("name", "ewr qwr");
//        b.putString("zip", "79312");
//        b.putString("height", "123.0");
//
//        b.putString("gender", "Male");
//        b.putString("dob", "08-12-1999");
//
//        b.putString("ageMin", "35");
//        b.putString("ageMax", "53");
//        b.putString("interestMale", "1");
//        b.putString("interestFemale", "1");
//
//        b.putString("religion", "Christianity");
//        b.putString("race", "");
//
//        b.putString("imagePath", "/storage/emulated/0/DCIM/Camera/IMG_20180812_071644.jpg");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summary, container, false);
        createSummary(v);
        Button summarySubmit = v.findViewById(R.id.summarySumbit);
        summarySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNextFragment();
            }
        });
        return v;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.LEFT, enter, 750);
    }

    private void createSummary(View v) {
        ((ImageView)v.findViewById(R.id.userImage))
                .setImageBitmap(BitmapFactory.decodeFile(b.getString("imagePath")));
        ((TextView)v.findViewById(R.id.userEmail)).setText(b.getString("email"));
        ((TextView)v.findViewById(R.id.userPassword)).setText(b.getString("password"));
        ((TextView)v.findViewById(R.id.userName)).setText(b.getString("name"));
        ((TextView)v.findViewById(R.id.userZIP)).setText(b.getString("zip"));
        ((TextView)v.findViewById(R.id.userHeight)).setText(b.getString("height"));
        ((TextView)v.findViewById(R.id.userGender)).setText(b.getString("gender"));
        ((TextView)v.findViewById(R.id.userDOB)).setText(b.getString("dob"));
        String ageRange = b.getString("ageMin")
                + " - " + b.getString("ageMax");
        ((TextView)v.findViewById(R.id.userAgeRange)).setText(ageRange);
        String genderInterests = "";
        if(b.getString("interestMale").equals("1"))
            genderInterests = "Males";
        if(b.getString("interestFemale").equals("1")) {
            if(genderInterests.length() > 0) genderInterests += " and ";
            genderInterests += "Females";
        }
        ((TextView)v.findViewById(R.id.userGenderInterests)).setText(genderInterests);
        if(b.get("race").toString().length() > 0) {
            TextView tmp =  v.findViewById(R.id.userRace);
            tmp.setVisibility(View.VISIBLE);
            tmp.setText(b.get("race").toString());
            v.findViewById(R.id.raceTag).setVisibility(View.VISIBLE);
        }
        if(b.get("religion").toString().length() > 0) {
            TextView tmp =  v.findViewById(R.id.userReligion);
            tmp.setVisibility(View.VISIBLE);
            tmp.setText(b.get("religion").toString());
            v.findViewById(R.id.religionTag).setVisibility(View.VISIBLE);
        }
    }

    private void callNextFragment() {
        JsonFragment frag = new JsonFragment();
        frag.setArguments(b);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
