package com.saurabh.userdetails;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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


public class PasswordFragment extends Fragment{

    private Bundle b;
    private String error = "";
    private EditText passwordTextView;

    public PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = this.getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_password, container, false);
        passwordTextView = v.findViewById(R.id.password);
        Button passwordSubmit = v.findViewById(R.id.passwordSubmit);
        passwordSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("on","click");
                userPassword();
            }
        });
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
            String password = b.getString("password");
            passwordTextView.setText(password);
        }
        catch (Exception e) {
            Log.d("password", "not set");
        }
    }

    public void userPassword() {
        String password = passwordTextView.getText().toString();
        if(passwordValid(password)) {
            b.putString("password", password);
            callNextFragment();
        } else {
            TextView emailInvalid = getView().findViewById(R.id.passwordError);
            emailInvalid.setText(error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                passwordTextView.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
            }
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            passwordTextView.startAnimation(shake);
        }
    }

    private void callNextFragment() {
        NameFragment frag = new NameFragment();
        frag.setArguments(b);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public boolean passwordValid(String password) {
        if(password.length() < 8)
            error = "Password must be 8 characters";
        else if(password.length() > 16)
            error = "Password must be less than 16 characters";
        else if(!password.matches(".*\\d+.*")) //regex check for a number in string
            error = "Password must contain a number";
        else if(password.contains(" "))
            error = "Password cannot contain space";
        else
            return true;
        return false;
    }
}