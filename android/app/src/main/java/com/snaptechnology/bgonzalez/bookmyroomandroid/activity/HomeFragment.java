package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 24/08/2016.
 */

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.animation.Circle;
import com.snaptechnology.bgonzalez.bookmyroomandroid.animation.CircleAngleAnimation;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.AnimationState;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.AnimationStateChangedListener;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.CircleProgressView;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.TextMode;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.UnitPosition;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.Transition;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.extra.Scale;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    public static int size;

    private final static String TAG = "MainActivity";

    CircleProgressView mCircleView;
    Switch mSwitchSpin;
    Switch mSwitchShowUnit;
    SeekBar mSeekBar;
    SeekBar mSeekBarSpinnerLength;
    Boolean mShowUnit = true;
    Spinner mSpinner;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);




        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ImageView image_unavalaible = (ImageView) rootView.findViewById(R.id.image_unavailable);
        ImageView image_available = (ImageView) rootView.findViewById(R.id.image_available);



        final ViewGroup transitionsContainer = (ViewGroup) rootView.findViewById(R.id.transitions_container);


        if(Math.random()< 0.5){
            //unavailable
            progressBar.bringToFront();
            progressBar.setVisibility(View.VISIBLE);
            ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 50, 500); // see this max value coming back here, we animale towards that value
            animation.setDuration (60000); //in milliseconds
            animation.setInterpolator (new DecelerateInterpolator());
            image_unavalaible.setVisibility(View.VISIBLE);
            image_unavalaible.bringToFront();
            animation.start ();

            final TextView finished = (TextView) transitionsContainer.findViewById(R.id.circle_finished);

            transitionsContainer.findViewById(R.id.image_unavailable).setOnClickListener(new VisibleToggleClickListener() {

                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                    finished.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);

                }


            });

        }else{
            //available
            image_available.setVisibility(View.VISIBLE);
            image_available.bringToFront();




            final TextView minutes1 = (TextView) transitionsContainer.findViewById(R.id.minutes1);
            final TextView minutes2 = (TextView) transitionsContainer.findViewById(R.id.minutes2);
            final TextView minutes3 = (TextView) transitionsContainer.findViewById(R.id.minutes3);
            final TextView minutes4 = (TextView) transitionsContainer.findViewById(R.id.minutes4);


            transitionsContainer.findViewById(R.id.image_available).setOnClickListener(new VisibleToggleClickListener() {

                @Override
                protected void changeVisibility(boolean visible) {
                    TransitionManager.beginDelayedTransition(transitionsContainer, new Scale());
                    minutes1.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    minutes2.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    minutes3.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                    minutes4.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }


            });
        }




        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
