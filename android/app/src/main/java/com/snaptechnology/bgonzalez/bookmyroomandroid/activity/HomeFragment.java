package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 24/08/2016.
 */

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.animation.Circle;
import com.snaptechnology.bgonzalez.bookmyroomandroid.animation.CircleAngleAnimation;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.AnimationState;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.AnimationStateChangedListener;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.CircleProgressView;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.TextMode;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.UnitPosition;

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

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        //.
        // 00000Circle circle = (Circle) rootView.findViewById(R.id.progressBar);


        ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        progressBar.bringToFront();
        ObjectAnimator animation = ObjectAnimator.ofInt (progressBar, "progress", 50, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration (5000); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();


//        new LongOperation().execute();
        /*
        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_main_circle);
        final int sizeLinearLayout ;
        linearLayout.post(new Runnable()
        {

            @Override
            public void run()
            {
                //sizeLinearLayout = linearLayout.getWidth();
                //Log.i("TEST", "Layout width : "+  sizeLinearLayout);

            }
        });

        /*final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linear_layout_main_circle);
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // Ensure you call it only once :
                linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                size = (int) (Math.round(linearLayout.getWidth())*0.77);
                Log.i("SQkdjiuw",Integer.toString(size ));

                // Here you can get the size :)
            }

        });



        circle.setSize(size);
        Circle circle2 = circle;

        CircleAngleAnimation animation = new CircleAngleAnimation(circle, 360);
        animation.setDuration(2000);
        circle.startAnimation(animation);*/

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
