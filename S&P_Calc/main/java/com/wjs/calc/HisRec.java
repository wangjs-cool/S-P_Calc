package com.wjs.calc;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;

public class HisRec extends Activity implements View.OnClickListener{

    protected ConstraintLayout HistRec;
    protected View BlankLeft, BlankTop, BlankRight, BlankBottom;
    protected TextView title;
    protected ListView listView;
    protected TextView ClearAll;
    protected String data;

    protected String table="";
    protected SQLiteDatabase db;
    protected MyDBOpenHelper myDBHelper;

    protected long DCkickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hisrec);

        // 获取软件版本
        int version=1;
        try {
            version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
        }catch (Exception e){
        }

        // 创建数据库实例
        myDBHelper = new MyDBOpenHelper(this, "calc.db", null, version);
        db = myDBHelper.getWritableDatabase();

        // 获取 xml 中控件的 id
        BlankLeft=findViewById(R.id.blank_left);
        BlankLeft.setOnClickListener(this);
        BlankTop=findViewById(R.id.blank_top);
        BlankTop.setOnClickListener(this);
        BlankRight=findViewById(R.id.blank_right);
        BlankRight.setOnClickListener(this);
        BlankBottom=findViewById(R.id.blank_bottom);
        BlankBottom.setOnClickListener(this);
        HistRec=findViewById(R.id.histrec);
        HistRec.setOnClickListener(this);
        title=findViewById(R.id.title);
        listView = findViewById(R.id.listview);
        ClearAll=findViewById(R.id.clearall);
        ClearAll.setOnClickListener(this);

        // 获取主程序传来的数据
        table=getIntent().getStringExtra("table");
        if(table!=null && (table.equals("listSci") || table.equals("listPg"))) {
            // 创建历史记录框
            createList();
        }

    }

    /*
   创建历史记录框
    */
    void createList(){
        Integer[] ids;
        // 获取数据表中数据的数量与id号
        ids= SQLiteUtils.getids(db, table);

        //判断数据库是否为空
        if(ids[0]!=0) {
            final String[] datas=new String[ids[2]];
            String[] datas_show=new String[ids[2]];

            // 获取数据表中所有数据
            for (int i = 0; i < ids[2]; i++) {
                datas[i]=SQLiteUtils.queryData(db, table,String.valueOf(ids[1]-i));
                int len=datas[i].length();
                // 判断是科学计算器发起的历史记录还是程序员计算器发起的历史记录
                if(table.equals("listSci")) {
                    //如果是科学计算器的历史记录，不进行截取
                    datas_show[i] = datas[i];
                }else{
                    //如果是程序员计算器的历史记录，进行截取
                    datas_show[i] = datas[i].substring(0, len - 4);
                }
            }
            // 创建记录显示框
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datas_show);
            listView.setAdapter(adapter);
            //监控点击事件
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // 记录当前点击的数据
                    data=datas[position];
                    onBackPressed();
                }
            });
        }else{
            // 如果数据库为空，则显示 “No Record”
            TextView txt=new TextView(this);
            txt.setText("No Record");
            txt.setTextSize(20);
            txt.setTextColor(Color.rgb(160, 160, 160));
            ConstraintLayout.LayoutParams txtParams=new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
            txtParams.leftToLeft = R.id.histrec;
            txtParams.topToBottom = R.id.title;
            txtParams.rightToRight = R.id.histrec;
            txtParams.bottomToBottom = R.id.histrec;
            txtParams.verticalBias=0.3f;
            txt.setLayoutParams(txtParams);
            HistRec.addView(txt);
        }

    }

    /*
    清除所有历史纪律
     */
    void clearall(){
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - DCkickTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次清除所有历史记录", Toast.LENGTH_SHORT).show();
            DCkickTime = mNowTime;
        } else{
            SQLiteUtils.clearData(db, table);
            onBackPressed();
        }
    }

    /*
   点击事件
    */
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.blank_left: onBackPressed(); break;
            case R.id.blank_top: onBackPressed(); break;
            case R.id.blank_right: onBackPressed(); break;
            case R.id.blank_bottom: onBackPressed(); break;
            case R.id.clearall: clearall(); break;
        }
    }

    /*
    重写返回键：向主程序发送当前点击的数据
     */
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("data",data);
        setResult(RESULT_OK,intent);
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
