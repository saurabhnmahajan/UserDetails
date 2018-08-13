package com.saurabh.userdetails;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;


public class NameFragment extends Fragment {

    private Bundle b;
    private String error = "";
    private EditText nameTextView, zipTextView, heightTextView;
    public NameFragment() {
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
        View v = inflater.inflate(R.layout.fragment_name, container, false);
        nameTextView = v.findViewById(R.id.name);
        zipTextView = v.findViewById(R.id.zipcode);
        heightTextView = v.findViewById(R.id.height);
        Button nameSubmit = v.findViewById(R.id.nameSubmit);
        nameSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                userDetails();
            }
        });

        //show keyboard on fragment load
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        setOldValue();
        return v;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.LEFT, enter, 750);
    }

    private void setOldValue() {
        try {
            String name = b.getString("name");
            nameTextView.setText(name);
        }
        catch (Exception e) {
            Log.d("Name bundle", "error");
        }
        try {
            String zip = b.getString("zip");
            zipTextView.setText(zip);
        }
        catch (Exception e) {
            Log.d("zip bundle", "error");
        }
        try {
            float height = Float.parseFloat(b.getString("height"));
            heightTextView.setText(height + "");
        }
        catch (Exception e) {
            Log.d("height bundle", "error");
        }
    }

    public void userDetails() {
        TextView emailInvalid = getView().findViewById(R.id.nameError);
        String name = nameTextView.getText().toString();
        if(validName(name)) {
            int zip = 0;
            try {
                zip = Integer.parseInt(zipTextView.getText().toString());
            }
            catch (Exception e){
                error = "Invalid ZIP code";
            }
            if(validZIP(zip)) {
                float height = 0;
                try {
                    height= Float.parseFloat(heightTextView.getText().toString());
                }
                catch (Exception e) {
                    error = "Invalid height";
                }
                if(validHeight(height)) {
                    b.putString("name", name);
                    b.putString("zip", String.valueOf(zip));
                    b.putString("height", String.valueOf(height));
                    callNextFragment();
                }
            }
        }
        if(error.length() > 0) {
            emailInvalid.setText(error);
        }
    }

    private void callNextFragment() {
        GenderFragment frag = new GenderFragment();
        frag.setArguments(b);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private boolean validHeight(float height) {
        if(height <= 0)
            error = "Height cannot be zero or negative";
        else if(height > 246.5)
            error = "Contact Guinness Book now!";
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                heightTextView.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,0,150,136)));
            }
            return true;
        }
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        heightTextView.startAnimation(shake);
        heightTextView.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            heightTextView.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
        }
        return false;
    }

    private boolean validZIP(int zip) {
        if(zip < 0)
            error = "Invalid ZIP code \nCannot be negative";
        else if(String.valueOf(zip).length() != 5)
            error = "Invalid ZIP code \nPlease enter a US ZIP code";
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                zipTextView.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,0,150,136)));
            }
            return true;
        }
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        zipTextView.startAnimation(shake);
        zipTextView.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            zipTextView.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
        }
        return false;
    }

    private boolean validName(String name) {
        if(!name.trim().contains(" "))
            error = "Enter First and Last name";
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                nameTextView.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,0,150,136)));
            }
            return true;
        }
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        nameTextView.startAnimation(shake);
        nameTextView.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nameTextView.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
        }
        return false;
    }
}
