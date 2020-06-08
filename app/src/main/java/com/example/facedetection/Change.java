package com.example.facedetection;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
public class Change extends Activity implements View.OnClickListener{

    private Button insert,cancel;
    private EditText n_name,n_sex,n_age;
    private ImageView img;

    private MySQLite user;
    private SQLiteDatabase mydb;
    private String name = "";
    int id = 0;

    static {
        System.loadLibrary("rotate");
    }

    public native boolean CheckInput(String input);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        name =bundle.getString("str");
        id = bundle.getInt("id");
        Init();
    }

    private void Init() {
        user = new MySQLite(this);

        insert = findViewById(R.id.change_save);
        cancel = findViewById(R.id.change_cancel);
        insert.setOnClickListener(this);
        cancel.setOnClickListener(this);
        n_name = findViewById(R.id.change_name);
        n_age = findViewById(R.id.change_age);
        n_sex = findViewById(R.id.change_sex);

        img = findViewById(R.id.change_img);

        MySQLite user_database = new MySQLite(Change.this);
        mydb = user_database.getWritableDatabase();

        n_name.setText(name);
        n_sex.setText(user.getUserById(mydb,Integer.toString(id))[1]);
        n_age.setText(user.getUserById(mydb,Integer.toString(id))[2]);

        String path = Environment.getExternalStorageDirectory() + "/FaceDetect/Grey/"+name+".bmp";
        img.setImageURI(Uri.fromFile(new File(path)));
    }

    public boolean renameFile(String newname) {
        String path2 = Environment.getExternalStorageDirectory() + "/FaceDetect/Grey/";
        File oldfile = new File(path2 + "/" + name+".bmp");
        File newfile = new File(path2 + "/" + newname+".bmp");
        if(!newname.equals(name)) {
            if (!oldfile.exists()) {
                Toast.makeText(this, "读取源文件失败！", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (newfile.exists()) {
                Toast.makeText(this, "用户：" + newname + " 已存在!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                oldfile.renameTo(newfile);
                return true;
            }
        }
        return true;
    }


    @Override
    public void onClick(View view){
        if (view.getId() == R.id.change_save) {
            String str_name = n_name.getText().toString();
            String str_sex = n_sex.getText().toString();
            int num_age = Integer.parseInt(n_age.getText().toString());

            if(!CheckInput(str_name)){
                Toast.makeText(this, "名字含有非法字符！", Toast.LENGTH_SHORT).show();
                return;
            }

            if(renameFile(str_name)) {
                user.update(mydb, id, str_name, str_sex, num_age);
                Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else if(view.getId() == R.id.change_cancel){
            finish();
        }
    }
}
