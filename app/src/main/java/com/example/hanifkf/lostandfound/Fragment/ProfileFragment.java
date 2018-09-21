package com.example.hanifkf.lostandfound.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hanifkf.lostandfound.Activity.EditProfileActivity;
import com.example.hanifkf.lostandfound.Activity.GantiPasswordActivity;
import com.example.hanifkf.lostandfound.Login.MainActivity;
import com.example.hanifkf.lostandfound.R;
import com.google.firebase.auth.FirebaseAuth;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by hanifkf on 24/02/2018.
 */

public class ProfileFragment extends Fragment{

    LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4,linearLayout5;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        mAuth = FirebaseAuth.getInstance();
        linearLayout1 = (LinearLayout)view.findViewById(R.id.linear1);
        linearLayout2 = (LinearLayout)view.findViewById(R.id.linear2);
        linearLayout3 = (LinearLayout)view.findViewById(R.id.linear3);
        linearLayout4 = (LinearLayout)view.findViewById(R.id.linear4);
        linearLayout5= (LinearLayout)view.findViewById(R.id.linear5);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: to Edit Profile");
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("Email"));
                String Send[] = {"contact.teamequinox@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, Send);
                intent.setType("message/text");
                Intent choser = intent.createChooser(intent, "Pilih Email");
                startActivity(choser);
            }
        });

        linearLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getContext(), "Versi 1.0", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        linearLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Log.d(TAG, "onClick: Sign Out");
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), GantiPasswordActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
