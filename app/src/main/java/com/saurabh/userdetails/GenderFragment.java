package com.saurabh.userdetails;


import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Calendar;


public class GenderFragment extends Fragment {

    private Bundle b;
    private EditText editText;
    private RadioGroup radioGroup;
    private RadioButton maleCheck, femaleCheck;
    public GenderFragment() {
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
        View v =inflater.inflate(R.layout.fragment_gender, container, false);

        radioGroup = v.findViewById(R.id.radio);
        maleCheck = v.findViewById(R.id.radioMale);
        femaleCheck = v.findViewById(R.id.radioFemale);
        editText = v.findViewById(R.id.dob);

        Button genderSubmit = v.findViewById(R.id.genderSubmit);
        genderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userGender();
                String dob = editText.getText().toString();
                TextView genderError = getView().findViewById(R.id.genderError);
                if(dob.length() > 0) {
                    if(getAge(dob) >= 18) {
                        b.putString("dob", dob);
                        callNextFragment();
                    } else {
                        genderError.setText("Cannot register till you're 18");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                        editText.startAnimation(shake);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            editText.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
                        }
                    }
                }
                else {
                    genderError.setText("Please select your Date of Birth");
                    Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                    editText.startAnimation(shake);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        editText.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
                    }
                }
            }
        });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(editText.getText().toString());
            }
        });

        setOldValue();
        return v;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.LEFT, enter, 750);
    }

    private int getAge(String dob) {
        Calendar now = Calendar.getInstance();

        int datePickerDate, datePickerMonth, datePickerYear;
        datePickerMonth = Integer.parseInt(dob.substring(0,2)) - 1;
        datePickerDate = Integer.parseInt(dob.substring(3,5));
        datePickerYear = Integer.parseInt(dob.substring(6));

        int currentDate, currentMonth, currentYear;
        currentDate= now.get(Calendar.DAY_OF_MONTH);
        currentMonth= now.get(Calendar.MONTH);
        currentYear = now.get(Calendar.YEAR);
        int age = currentYear - datePickerYear;
        if(datePickerMonth > currentMonth) age--;
        else if(currentMonth == datePickerMonth){
            if(datePickerDate > currentDate) age--;
        }
        return age;
    }

    private void setOldValue() {
        try {
            String gender = b.getString("gender");
            if(gender.equalsIgnoreCase("Male")){
                maleCheck.setChecked(true);
            }
            else if(gender.equalsIgnoreCase("Female")) {
                femaleCheck.setChecked(true);
            }
        }
        catch (Exception e){
            Log.d("gender", "old value not set");
        }

        try {
            String dob = b.getString("dob");
            editText.setText(dob);
        }
        catch (Exception e) {
            Log.d("DOB", "old value not set");
        }
    }

    private void datePicker(String date) {
        Calendar now = Calendar.getInstance();
        int datePickerDate, datePickerMonth, datePickerYear;
        if(date.length() > 0) {
            datePickerMonth = Integer.parseInt(date.substring(0,2)) - 1;
            datePickerDate = Integer.parseInt(date.substring(3,5));
            datePickerYear = Integer.parseInt(date.substring(6));

        } else {
            datePickerDate = now.get(Calendar.DAY_OF_MONTH);
            datePickerMonth = now.get(Calendar.MONTH);
            datePickerYear = now.get(Calendar.YEAR);
        }

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                DecimalFormat formatter = new DecimalFormat("00");
                String date = formatter.format(i1+1) + "-" + formatter.format(i2) + "-" + i;
                editText.setText(date);

            }
        };
        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), dateSetListener, datePickerYear, datePickerMonth,
                datePickerDate);
        mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDatePicker.show();
    }

    public void userGender() {
        int selectedID = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = getView().findViewById(selectedID);
        String gender = radioButton.getText().toString();
        b.putString("gender", gender);
    }

    private void callNextFragment() {
        MatchFragment frag = new MatchFragment();
        frag.setArguments(b);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
