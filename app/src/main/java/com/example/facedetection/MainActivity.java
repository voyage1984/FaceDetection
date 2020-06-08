package com.example.facedetection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    Button main_record,main_shotcompare,main_recordcompare,main_showdata,main_readme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init();
    }

    public void Init(){
        main_record         = findViewById(R.id.main_record);
        main_shotcompare    = findViewById(R.id.main_shotcompare);
        main_recordcompare  = findViewById(R.id.main_recordcompare);
        main_showdata       = findViewById(R.id.main_showdata);
        main_readme         = findViewById(R.id.main_readme);

        main_record.setOnClickListener(this);
        main_shotcompare.setOnClickListener(this);
        main_recordcompare.setOnClickListener(this);
        main_showdata.setOnClickListener(this);
        main_readme.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.main_record:
                Intent insert = new Intent(this,Insert.class);
                startActivity(insert);
                break;
            case R.id.main_shotcompare:
                Intent shotcompare = new Intent(this,ShotCompare.class);
                startActivity(shotcompare);
                break;
            case R.id.main_recordcompare:
                Intent recordcompare = new Intent(this,RecordCompare.class);
                startActivity(recordcompare);
                break;
            case R.id.main_showdata:
                Intent showdata = new Intent(this,ShowData.class);
                startActivity(showdata);
                break;
            case R.id.main_readme:
                Intent readme = new Intent(this,Readme.class);
                startActivity(readme);
                break;
        }
    }
}
