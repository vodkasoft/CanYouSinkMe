package com.vodkasoft.canyousinkme.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Main extends FragmentActivity {
    private FacebookAuthFragment authFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            authFragment = new FacebookAuthFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, authFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            authFragment = (FacebookAuthFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
        runLogoAnimation();
    }

    public void runLogoAnimation() {
        final ImageView IM = (ImageView) findViewById(R.id.main_main_logo);
        AlphaAnimation AlphaAnim = new AlphaAnimation(0, 100);
        AlphaAnim.setDuration(40000);
        AlphaAnim.setFillAfter(true);
        final int XFrom = IM.getPaddingLeft();
        final int YFrom = IM.getPaddingTop();
        TranslateAnimation TranslateAnim = new TranslateAnimation(XFrom, XFrom, YFrom, YFrom - 300);
        TranslateAnim.setFillAfter(true);
        TranslateAnim.setFillEnabled(true);
        TranslateAnim.setDuration(1000);
        TranslateAnim.setStartOffset(4000);
        IM.startAnimation(TranslateAnim);
        AnimationSet AnimSet = new AnimationSet(true);
        AnimSet.addAnimation(AlphaAnim);
        AnimSet.addAnimation(TranslateAnim);
        IM.startAnimation(AnimSet);
        AnimSet.setFillAfter(true);
        AnimSet.setFillEnabled(true);
        AnimSet.setFillBefore(true);
        runFacebookAnonymousAnimation();
    }

    public void runFacebookAnonymousAnimation(){
        com.facebook.widget.LoginButton LB = (com.facebook.widget.LoginButton) findViewById(R.id.Facebook_btn);
        AlphaAnimation AlphaAnim = new AlphaAnimation(0, 100);
        AlphaAnim.setDuration(20000);
        AlphaAnim.setFillAfter(true);
        AlphaAnim.setStartOffset(4500);
        LB.startAnimation(AlphaAnim);
        ImageButton IB = (ImageButton) findViewById(R.id.main_guest_btn);
        AlphaAnimation AlphaAnim2 = new AlphaAnimation(0,100);
        AlphaAnim2.setDuration(20000);
        AlphaAnim2.setFillAfter(true);
        AlphaAnim2.setStartOffset(4500);
        IB.startAnimation(AlphaAnim2);
    }

    public void Guest_Event(View view){
        Intent intent = new Intent(this, MenuGuest.class);
        startActivity(intent);
    }

    public void Facebook_Event(View view){
        Intent intent = new Intent(this, MenuFB.class);
        startActivity(intent);
    }
}
