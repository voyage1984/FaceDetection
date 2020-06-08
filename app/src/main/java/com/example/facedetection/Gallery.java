package com.example.facedetection;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;


//import static org.opencv.imgcodecs.Imgcodecs.imread;

public class Gallery extends Activity{
    TextView info_name,info_sex,info_age;
    ImageView info_img;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String msg=bundle.getString("str");
        info_name = findViewById(R.id.info_name);
        info_age = findViewById(R.id.info_age);
        info_sex = findViewById(R.id.info_sex);
        info_img = findViewById(R.id.info_img);
        readData(msg);
    }

    public void readData(String name){
        MySQLite user_database = new MySQLite(this);
        SQLiteDatabase db = user_database.getReadableDatabase();
        String mName = user_database.getUserByName(db,name)[0];
        info_name.setText(mName);
        info_sex.setText(user_database.getUserByName(db,name)[1]);
        info_age.setText(user_database.getUserByName(db,name)[2]);
        String path = Environment.getExternalStorageDirectory() + "/FaceDetect/Grey/"+mName+".bmp";
        info_img.setImageURI(Uri.fromFile(new File(path)));
    }
}
