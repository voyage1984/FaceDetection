package com.example.facedetection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

public class Insert extends Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    EditText mName,mAge;
    RadioGroup mSexGroup;
    RadioButton man;
    String mSex = "男";
    Button saveBtn;

    public native boolean checkInput(String input);

    static{
        System.loadLibrary("rotate");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Init();
    }


    public void Init(){
        mName = findViewById(R.id.insertName);
        mAge = findViewById(R.id.insertAge);
        man = findViewById(R.id.sex_man);

        saveBtn = findViewById(R.id.insertSave);
        saveBtn.setOnClickListener(this);
        mSexGroup = findViewById(R.id.sexgroup);
        mSexGroup.setOnCheckedChangeListener(this);
    }

    public boolean checkSave(String name){
        if(!checkInput(name)){
            Toast.makeText(this,"文件名不合法",Toast.LENGTH_SHORT).show();
            return false;
        }
        String PATH = Environment.getExternalStorageDirectory() + "/FaceDetect/" + name + ".bmp";
        File src = new File(PATH);
        if(src.exists()){
            Toast.makeText(this,"文件已存在",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void doSave(){
        String name = mName.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this,"请检查姓名输入",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkSave(name)){
            return;
        }
        String strAge = mAge.getText().toString();
        if(strAge.isEmpty()){
            Toast.makeText(this,"请输入年龄",Toast.LENGTH_SHORT).show();
            return;
        }
        int age = Integer.parseInt(strAge);
        Intent shotactivity = new Intent(this,Record.class);
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        bundle.putInt("age",age);
        bundle.putString("sex",mSex);

        shotactivity.putExtras(bundle);
        startActivity(shotactivity);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.insertSave:
                doSave();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int i) {
        mSex = i == R.id.sex_man?"男":"女";
    }
}
