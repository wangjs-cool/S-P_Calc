package com.wjs.calc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import static java.lang.Math.abs;

public class Sidebar extends Activity implements View.OnTouchListener{

    protected ConstraintLayout sideBar;
    protected View blank, blankright;
    protected TextView function, operation, sci_instructions, pg_instructions, about, exit;
    protected int Flag_exit=0;

    protected float x1=0,y1=0,x2=0,y2=0;
    protected Boolean move=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidebar);

        // 获取 xml 中控件的 id
        sideBar=findViewById(R.id.sidebar);
        sideBar.setOnTouchListener(this);
        function=findViewById(R.id.function);
        function.setOnTouchListener(this);
        operation=findViewById(R.id.operation);
        operation.setOnTouchListener(this);
        sci_instructions=findViewById(R.id.sci_instructions);
        sci_instructions.setOnTouchListener(this);
        pg_instructions=findViewById(R.id.pg_instructions);
        pg_instructions.setOnTouchListener(this);
        about=findViewById(R.id.about);
        about.setOnTouchListener(this);
        exit=findViewById(R.id.exit);
        exit.setOnTouchListener(this);
        blank=findViewById(R.id.blank);
        blank.setOnTouchListener(this);
        blankright=findViewById(R.id.blank_right);
        blankright.setOnTouchListener(this);

    }

    /*
   点击事件
    */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.function: gotoDocument(1); break;
            case R.id.operation: gotoDocument(2); break;
            case R.id.sci_instructions: gotoDocument(3); break;
            case R.id.pg_instructions: gotoDocument(4); break;
            case R.id.about: gotoDocument(5); break;
            case R.id.exit: Flag_exit=1; onBackPressed(); break;
            case R.id.blank_right: onBackPressed(); break;
        }
    }

    /*
    触摸事件
     */
    @Override
    public boolean onTouch(View view, MotionEvent event) {

        // 定义触摸事件滑动距离
        int MoveLengthH=200,MoveLengthV=100;

        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            // 移动
            case MotionEvent.ACTION_MOVE:
                x2 = event.getX();
                y2 = event.getY();
                // 判断移动距离是否满足
                if(abs(x2-x1)>100||abs(y2-y1)>100){
                    move=true;
                }
                break;
            // 抬起
            case MotionEvent.ACTION_UP:
                // 判断是滑动事件还是点击事件
                if(move) {
                    if (((y2 - y1) > abs(x2 - x1)) && ((y2 - y1 > MoveLengthV))) {
                        //向下滑動
                    } else if (((y1 - y2) > abs(x2 - x1)) && ((y1 - y2 > MoveLengthV))) {
                        //向上滑动
                    } else if (((x2 - x1) > abs(y2 - y1)) && ((x2 - x1) > MoveLengthH)) {
                        //向右滑动
                    } else if (((x1 - x2) > abs(y2 - y1)) && ((x1 - x2) > MoveLengthH)) {
                        //向左滑动
                        onBackPressed();
                    }
                    move=false;
                }else{
                    onClick(view);
                }
                break;
            default:break;
        }
        return false;
    }

    /*
    打开对应菜单项
     */
    public void gotoDocument(int name){
        Intent intent=new Intent();
        intent.setClass(this, Document.class);
        intent.putExtra("name",name);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /*
    重写返回键：当点击退出后，向主程序发送退出信号
     */
    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("exit",Flag_exit);
        setResult(RESULT_OK,intent);
        finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
}
