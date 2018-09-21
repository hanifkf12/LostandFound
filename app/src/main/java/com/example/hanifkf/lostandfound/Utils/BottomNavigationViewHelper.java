package com.example.hanifkf.lostandfound.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.hanifkf.lostandfound.Activity.HomeActivity;
import com.example.hanifkf.lostandfound.Activity.NotifActivity;
import com.example.hanifkf.lostandfound.Activity.PostActivity;
import com.example.hanifkf.lostandfound.Activity.ProfileActivity;
import com.example.hanifkf.lostandfound.R;
import com.example.hanifkf.lostandfound.Activity.SugestActivity;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by hanifkf on 23/02/2018.
 */

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";

    public static  void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNav");
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home:
                        Intent home=new Intent(context, HomeActivity.class);
                        context.startActivity(home);
                        Log.d(TAG, "onNavigationItemSelected: To Home");
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        callingActivity.finish();
                        break;
                    case R.id.ic_sugest:
                        Intent sugest=new Intent(context, SugestActivity.class);
                        context.startActivity(sugest);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        callingActivity.finish();
                        break;
                    case R.id.ic_add:
                        Intent add=new Intent(context,PostActivity.class);
                        context.startActivity(add);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        callingActivity.finish();
                        break;
                    case R.id.ic_notif:
                        Intent notif=new Intent(context, NotifActivity.class);
                        context.startActivity(notif);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        callingActivity.finish();
                        break;
                    case R.id.ic_profile:
                        Intent profile=new Intent(context, ProfileActivity.class);
                        context.startActivity(profile);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        callingActivity.finish();
                        break;
                }

                return false;
            }
        });
    }

}
