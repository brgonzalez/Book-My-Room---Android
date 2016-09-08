package com.snaptechnology.bgonzalez.bookmyroomandroid.activity;

/**
 * Created by bgonzalez on 29/08/2016.
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.snaptechnology.bgonzalez.bookmyroomandroid.R;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.AnimationState;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.AnimationStateChangedListener;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.CircleProgressView;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.TextMode;
import com.snaptechnology.bgonzalez.bookmyroomandroid.circleprogress.UnitPosition;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SocketTestFragment extends Fragment {

    private Handler mHandler;
    private TextView text;
    private int i;

    public static int size;

    private final static String TAG = "MainActivity";

    CircleProgressView mCircleView;
    Switch mSwitchSpin;
    Switch mSwitchShowUnit;
    SeekBar mSeekBar;
    SeekBar mSeekBarSpinnerLength;
    Boolean mShowUnit = true;
    Spinner mSpinner;



    private ServerSocket serverSocket;

    Handler updateConversationHandler;

    Thread serverThread = null;


    public static final int SERVERPORT = 6000;

    public SocketTestFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_socket_test, container, false);
        /*int delay = 5000; // delay for 5 sec.
        int period = 1000; // repeat every sec.

        text = (TextView) rootView.findViewById(R.id.socket);

        updateConversationHandler = new Handler();

        this.serverThread = new Thread(new ServerThread());

        this.serverThread.start();*/


        mCircleView = (CircleProgressView) rootView.findViewById(R.id.circleView);
        mCircleView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(float value) {
                Log.d(TAG, "Progress Changed: " + value);
            }
        });

        //value setting
//        mCircleView.setMaxValue(100);
//        mCircleView.setValue(0);
//        mCircleView.setValueAnimated(24);

        //growing/rotating counter-clockwise
//        mCircleView.setDirection(Direction.CCW)

//        //show unit
//        mCircleView.setUnit("%");
//        mCircleView.setUnitVisible(mShowUnit);
//
//        //text sizes
//        mCircleView.setTextSize(50); // text size set, auto text size off
//        mCircleView.setUnitSize(40); // if i set the text size i also have to set the unit size
//        mCircleView.setAutoTextSize(true); // enable auto text size, previous values are overwritten
//        //if you want the calculated text sizes to be bigger/smaller you can do so via
//        mCircleView.setUnitScale(0.9f);
//        mCircleView.setTextScale(0.9f);
//
////        //custom typeface
////        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/ANDROID_ROBOT.ttf");
////        mCircleView.setTextTypeface(font);
////        mCircleView.setUnitTextTypeface(font);
//
//
//        //color
//        //you can use a gradient
//        mCircleView.setBarColor(getResources().getColor(R.color.primary), getResources().getColor(R.color.accent));
//
//        //colors of text and unit can be set via
//        mCircleView.setTextColor(Color.RED);
//        mCircleView.setTextColor(Color.BLUE);
//        //or to use the same color as in the gradient
//        mCircleView.setTextColorAuto(true); //previous set values are ignored
//
//        //text mode
//        mCircleView.setText("Text"); //shows the given text in the circle view
//        mCircleView.setTextMode(TextMode.TEXT); // Set text mode to text to show text
//
//        //in the following text modes, the text is ignored
//        mCircleView.setTextMode(TextMode.VALUE); // Shows the current value
//        mCircleView.setTextMode(TextMode.PERCENT); // Shows current percent of the current value from the max value

        //spinning
//        mCircleView.spin(); // start spinning
//        mCircleView.stopSpinning(); // stops spinning. Spinner gets shorter until it disappears.
//        mCircleView.setValueAnimated(24); // stops spinning. Spinner spins until on top. Then fills to set value.


        //animation callbacks

        //this example shows how to show a loading text if it is in spinning mode, and the current percent value otherwise.
        mCircleView.setShowTextWhileSpinning(true); // Show/hide text in spinning mode
        mCircleView.setText("Loading...");
        mCircleView.setOnAnimationStateChangedListener(
                new AnimationStateChangedListener() {
                    @Override
                    public void onAnimationStateChanged(AnimationState _animationState) {
                        switch (_animationState) {
                            case IDLE:
                            case ANIMATING:
                            case START_ANIMATING_AFTER_SPINNING:
                                mCircleView.setTextMode(TextMode.PERCENT); // show percent if not spinning
                                mCircleView.setUnitVisible(mShowUnit);
                                break;
                            case SPINNING:
                                mCircleView.setTextMode(TextMode.TEXT); // show text while spinning
                                mCircleView.setUnitVisible(false);
                            case END_SPINNING:
                                break;
                            case END_SPINNING_START_ANIMATING:
                                break;

                        }
                    }
                }
        );


        // region setup other ui elements
        //Setup Switch
        mSwitchSpin = (Switch) rootView.findViewById(R.id.switch1);
        mSwitchSpin.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            mCircleView.spin();
                        } else {
                            mCircleView.stopSpinning();
                        }
                    }
                }

        );

        mSwitchShowUnit = (Switch) rootView.findViewById(R.id.switch2);
        mSwitchShowUnit.setChecked(mShowUnit);
        mSwitchShowUnit.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mCircleView.setUnitVisible(isChecked);
                        mShowUnit = isChecked;
                    }
                }

        );

        //Setup SeekBar
        mSeekBar = (SeekBar) rootView.findViewById(R.id.seekBar);

        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mCircleView.setValueAnimated(seekBar.getProgress(), 1500);
                        mSwitchSpin.setChecked(false);
                    }
                }
        );

        mSeekBarSpinnerLength = (SeekBar) rootView.findViewById(R.id.seekBar2);
        mSeekBarSpinnerLength.setMax(360);
        mSeekBarSpinnerLength.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mCircleView.setSpinningBarLength(seekBar.getProgress());
                    }
                });

        mSpinner = (Spinner) rootView.findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("Left Top");
        list.add("Left Bottom");
        list.add("Right Top");
        list.add("Right Bottom");
        list.add("Top");
        list.add("Bottom");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
        mSpinner.setAdapter(dataAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mCircleView.setUnitPosition(UnitPosition.LEFT_TOP);
                        break;
                    case 1:
                        mCircleView.setUnitPosition(UnitPosition.LEFT_BOTTOM);
                        break;
                    case 2:
                        mCircleView.setUnitPosition(UnitPosition.RIGHT_TOP);
                        break;
                    case 3:
                        mCircleView.setUnitPosition(UnitPosition.RIGHT_BOTTOM);
                        break;
                    case 4:
                        mCircleView.setUnitPosition(UnitPosition.TOP);
                        break;
                    case 5:
                        mCircleView.setUnitPosition(UnitPosition.BOTTOM);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner.setSelection(2);
        //endregion

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

        //mHandler = new Handler();
        //mHandler.post(mUpdate);

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


    private Runnable mUpdate = new Runnable() {
        public void run() {
            Socket mSocket = null;
            String output = "null";
            try
            {
                //tablet
                Socket deviceSocket;
                ServerSocket serverSocket = new ServerSocket(6500);
                while(true) {
                    deviceSocket = serverSocket.accept();
                    InputStream is = deviceSocket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    output = br.readLine();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


            text.setText("My data: " + output);
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    class ServerThread implements Runnable {

        public void run() {
            System.out.println("Run server thread");
            InetAddress addr = null;
            try {
                addr = InetAddress.getByName("10.110.10.149");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            Socket socket;
            try {
                serverSocket = new ServerSocket(6000);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

                try {

                    socket = serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    String read = input.readLine();

                    updateConversationHandler.post(new updateUIThread(read));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class updateUIThread implements Runnable {
        private String msg;

        public updateUIThread(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            text.setText( msg + "\n");
        }
    }

}