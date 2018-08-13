package com.saurabh.userdetails;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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

public class EmailFragment extends Fragment {

    private Bundle b;
    private EditText emailTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = this.getArguments();
    }

    private void setOldValue() {
        try {
            String email = b.getString("email");
            emailTextView.setText(email);
        }
        catch (Exception e) {
            Log.d("email", "not set");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_email, container, false);
        emailTextView = v.findViewById(R.id.email);
        Button emailSubmit = v.findViewById(R.id.emailSubmit);
        emailSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userEmail();
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

    public void userEmail() {
        String email = emailTextView.getText().toString();
        if(validEmail(email)) {
            b.putString("email", email);
            callNextFragment(email);
        } else {
            TextView emailInvalid = getView().findViewById(R.id.emailError);
            emailInvalid.setText("Invalid Email Address");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                emailTextView.setBackgroundTintList(ColorStateList.valueOf(Color.argb(255,255,68,68)));
            }
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            emailTextView.startAnimation(shake);
        }
    }

    private void callNextFragment(String email) {
        PasswordFragment frag = new PasswordFragment();
        frag.setArguments(b);
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(getFragmentManager().getFragments().get(0));
        fragmentTransaction.replace(R.id.container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public boolean validEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}

