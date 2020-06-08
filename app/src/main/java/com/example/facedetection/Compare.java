package com.example.facedetection;

import android.os.Bundle;
import android.os.Environment;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_UNCHANGED;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.CV_COMP_CORREL;

public class Compare{

    private static final Scalar    FACE_RECT_COLOR     = new Scalar(0, 255, 0, 255);

//    public static String getPercentFormat(double d,int IntegerDigits,int FractionDigits){
//        NumberFormat nf = java.text.NumberFormat.getPercentInstance();
//        nf.setMaximumIntegerDigits(IntegerDigits);//小数点前保留几位
//        nf.setMinimumFractionDigits(FractionDigits);// 小数点后保留几位
//        String str = nf.format(d);
//        return str;
//    }

    public double ComparePic(Mat pic,String src) {
        String path = Environment.getExternalStorageDirectory() + "/FaceDetect/Grey/";
        Mat srcMat1 = pic;
        Mat desMat1 = imread(path+src, CV_LOAD_IMAGE_UNCHANGED);
        srcMat1.convertTo(srcMat1, CvType.CV_32F);
        desMat1.convertTo(desMat1, CvType.CV_32F);
        double t = Imgproc.compareHist(srcMat1, desMat1, CV_COMP_CORREL);
        return t;
    }

    public static double calcArea(Rect rect)
    {
        return rect.width*rect.height;
    }

    public String doCompare(Mat mat){
        Mat mat2 = new Mat();
        Size size = new Size(100, 100);

        Imgproc.resize(mat, mat2, size);
        File dir = new File(Environment.getExternalStorageDirectory() + "/FaceDetect/Grey/");
        File[] files = dir.listFiles();
        String str = "";
        double max = 0;
        double thiz;
        if(files!=null){
            for(int i=0;i<files.length;i++){
                String fileName = files[i].getName();
                thiz = ComparePic(mat2,fileName);
                if(thiz>max){
                    max = thiz;
                    str = fileName;
                }
            }
            if(max>0.85){
                System.out.println("输出："+str.substring(0,str.length()-4));
                return str.substring(0,str.length()-4);
            }
            else {
                return null;
            }
        }
        else{
            return null;
        }
        //return getPercentFormat(max,3,2)+str;
    }

    public Mat detectFace(Mat img,MatOfRect mrect){
        Rect[] rects = mrect.toArray();
        Rect maxRect;
        maxRect=new Rect(0,0,0,0);
        Mat roi_img = img;
        if(rects != null && rects.length >= 1){
            for (Rect rect : rects) {
                if(calcArea(maxRect)<calcArea(rect)) {
                maxRect=rect;
            }
            }
            if(calcArea(maxRect)>0){
                roi_img = new Mat(img,maxRect);
            }
        }
        else{
            return null;
        }
        return roi_img;
    }

}


