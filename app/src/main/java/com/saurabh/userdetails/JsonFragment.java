package com.saurabh.userdetails;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import com.labo.kaji.fragmentanimations.CubeAnimation;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.Set;


public class JsonFragment extends Fragment {


    Bundle b;
    TextView status;
    public JsonFragment() {
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
        View v = inflater.inflate(R.layout.fragment_json, container, false);
        status = v.findViewById(R.id.status);
        createJson();
        return v;

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return CubeAnimation.create(CubeAnimation.LEFT, enter, 750);
    }

    private String getStringFromBitmap(Bitmap bitmapPicture) {
        final int COMPRESSION_QUALITY = 100;
        String encodedImage;
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private void createJson() {
        JSONObject json = new JSONObject();
        Set<String> keys = b.keySet();
        for (String key : keys) {
            try {
                if(key.equalsIgnoreCase("imagepath")) {
                    String encodedImage = getStringFromBitmap(BitmapFactory.decodeFile(b.getString("imagePath")));
                    json.put("image", JSONObject.wrap(encodedImage));
                } else if(b.get(key).toString().length() > 0)
                    json.put(key, JSONObject.wrap(b.get(key)));
            } catch(JSONException e) {
                Log.e("Error", "in creating json");
            }
        }
        new SendUserDetails(status).execute(json.toString());

    }
}
