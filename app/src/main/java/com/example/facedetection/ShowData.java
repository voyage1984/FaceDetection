package com.example.facedetection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowData extends Activity {

    private ListView userlist;
    private List<UserInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        Init();
        DoWork();
    }

    private void Init(){
        userlist = findViewById(R.id.userlist);
        MySQLite user = new MySQLite(this);
        SQLiteDatabase mydb = user.getReadableDatabase();
        list = user.querydata(mydb);
    }

    private void DoWork() {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> listitem = new HashMap<String, Object>();
            listitem.put("ids", list.get(i).getId());
            listitem.put("names", list.get(i).getUsername());
            listitem.put("ages", list.get(i).getAge());
            listitem.put("sexs", list.get(i).getSex());
            mapList.add(listitem);
        }

        SimpleAdapter simpleAdapter;
        simpleAdapter = new SimpleAdapter(this, mapList, R.layout.simple_adapter, new String[]{"ids", "names", "ages", "sexs"}, new int[]{R.id.tv_id, R.id.tv_name,  R.id.tv_age, R.id.tv_sex});
        userlist.setAdapter(simpleAdapter);

        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long l) {
                Intent intent2 = new Intent(ShowData.this, ShowData.class);
                startActivity(intent2);

                final String name = list.get(position).getUsername();
                Intent intent = new Intent(ShowData.this,Change.class);
                intent.putExtra("str",name);
                intent.putExtra("id",list.get(position).getId());
                startActivity(intent);
                finish();

            }
        });
        userlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long l) {
                final int id = list.get(position).getId();
                new AlertDialog.Builder(ShowData.this).setTitle("删除数据").setMessage("确定删除?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MySQLite user_database = new MySQLite(ShowData.this);
                        SQLiteDatabase db = user_database.getWritableDatabase();
                        user_database.delete(db, id);
                        File file = new File(Environment.getExternalStorageDirectory() + "/FaceDetect/Grey/"+list.get(position).getUsername()+".bmp");
                        file.delete();
                        finish();
                        Intent intent = new Intent(ShowData.this, ShowData.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                return false;
            }
        });

    }
}
