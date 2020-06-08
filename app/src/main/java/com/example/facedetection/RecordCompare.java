package com.example.facedetection;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecordCompare extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener  {

    private int mCameraIndexCount;
    private Button changeRecordCamera;

    private MatOfRect faces;

    private static final String    TAG                 = "OCVSample::Activity";
    private static final Scalar FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);
    public static final int        JAVA_DETECTOR       = 0;
    public static final int        NATIVE_DETECTOR     = 1;

    private Mat mface;
    final Compare cpr = new Compare();
    private Mat                    mRgba;
    private Mat                    mGray;
    private File mCascadeFile;
    private CascadeClassifier mJavaDetector;
    private DetectionBasedTracker  mNativeDetector;

    private int                    mDetectorType       = NATIVE_DETECTOR;
    private String[]               mDetectorName;

    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;

    private CameraBridgeViewBase mOpenCvCameraView;

//    public native void rotateImage(long matAddr);

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");

                    System.loadLibrary("detection_based_tracker");
                    System.loadLibrary("rotate");

                    try {
                        // load cascade file from application resources
                        InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                        mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                        FileOutputStream os = new FileOutputStream(mCascadeFile);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                        }
                        is.close();
                        os.close();

                        mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                        if (mJavaDetector.empty()) {
                            Log.e(TAG, "Failed to load cascade classifier");
                            mJavaDetector = null;
                        } else
                            Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                        mNativeDetector = new DetectionBasedTracker(mCascadeFile.getAbsolutePath(), 0);

                        cascadeDir.delete();

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
                    }

                    mOpenCvCameraView.enableView();     //开启相机链接
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public RecordCompare() {
        mDetectorName = new String[2];
        mDetectorName[JAVA_DETECTOR] = "Java";
        mDetectorName[NATIVE_DETECTOR] = "Native (tracking)";

        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_record_compare);

        mCameraIndexCount = getSharedPreferences("CameraCount", MODE_PRIVATE).getInt("Camera", 0);

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.recordcomparecamera);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        mOpenCvCameraView.setCameraIndex(mCameraIndexCount);

        changeRecordCamera = findViewById(R.id.recordChangeCamera);
        changeRecordCamera.setOnClickListener(this);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
    }

    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        if(mCameraIndexCount==1) {
            rotateImage(mRgba.getNativeObjAddr());
            rotateImage(mGray.getNativeObjAddr());
        }

        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
            mNativeDetector.setMinFaceSize(mAbsoluteFaceSize);
        }

        faces = new MatOfRect();

        if (mNativeDetector != null)
            mNativeDetector.detect(mGray, faces);
        else
            Log.e(TAG, "Detection method is not selected!");

        Rect[] facesArray = faces.toArray();

        for (int i = 0; i < facesArray.length; i++)
            Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);

        doCompare();

        return mRgba;
    }

    private native void rotateImage(long addr);

    public String doCompare(){
        try{
            mface = cpr.detectFace(mGray,faces).clone();
        }
        catch (Exception e){
            return null;
        }
        String msg = cpr.doCompare(mface);

        if(msg != null) {
            System.out.println(msg);
            Intent g = new Intent(this,Gallery.class);
            Bundle bundle = new Bundle();
            bundle.putString("str",msg);
            g.putExtras(bundle);
            startActivity(g);
            return msg;
        }
        return null;
    }

    int getCameraIndexCount(){
        SharedPreferences pref = getSharedPreferences("CameraCount", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (mCameraIndexCount>=1) {
            Toast.makeText(this,"后置摄像头",Toast.LENGTH_SHORT).show();
            mCameraIndexCount = 0;
        }
        else {
            mCameraIndexCount = 1;
            Toast.makeText(this,"前置摄像头",Toast.LENGTH_SHORT).show();
        }
        editor.putInt("Camera",mCameraIndexCount);
        editor.commit();
        return mCameraIndexCount;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.recordChangeCamera:
                mOpenCvCameraView.disableView();
                mOpenCvCameraView.setCameraIndex(getCameraIndexCount());
                mOpenCvCameraView.enableView();
                break;
        }
    }
}