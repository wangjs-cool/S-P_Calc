package com.wjs.calc;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.math.BigInteger;
import static java.lang.Math.abs;

public class PgCalc extends Activity implements View.OnTouchListener{

    protected ConstraintLayout display;
    protected TextView list, input_up, hex, dec, oct, bin;
    protected EditText input;
    Integer[] ids = {R.id.btn_A, R.id.btn_B, R.id.btn_C, R.id.btn_D, R.id.btn_E, R.id.btn_F, R.id.btn_and, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_del, R.id.btn_ac, R.id.btn_or, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_mul, R.id.btn_div, R.id.btn_xor, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_add, R.id.btn_sub, R.id.btn_not, R.id.btn_bit, R.id.btn_0, R.id.btn_ans, R.id.btn_equal};
    protected Button btn_A, btn_B, btn_C, btn_D, btn_E, btn_F, btn_and, btn_7, btn_8, btn_9, btn_del, btn_ac, btn_or, btn_4,
    btn_5, btn_6, btn_mul, btn_div, btn_xor, btn_1, btn_2, btn_3, btn_add, btn_sub, btn_not, btn_bit, btn_0, btn_ans, btn_equal;
    protected Button[] btns = {btn_A, btn_B, btn_C, btn_D, btn_E, btn_F, btn_and, btn_7, btn_8, btn_9, btn_del, btn_ac, btn_or, btn_4,
            btn_5, btn_6, btn_mul, btn_div, btn_xor, btn_1, btn_2, btn_3, btn_add, btn_sub, btn_not, btn_bit, btn_0, btn_ans, btn_equal};
    protected Integer[] btns_hex=new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};
    protected Integer[] btns_dec=new Integer[]{6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};
    protected Integer[] btns_oct=new Integer[]{6, 7, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28};
    protected Integer[] btns_bin=new Integer[]{6, 10, 11, 12, 16, 17, 18, 19, 22, 23, 24, 25, 26, 27, 28};
    protected String[] btn_input=new String[]{"a", "b", "c", "d", "e", "f", "", "7", "8", "9", "", "", "", "4", "5", "6", "×", "÷", "", "1", "2", "3", "＋", "－", "", "", "0", "", "", "" };

    protected Integer[] radix={2,8,10,16};
    protected Integer[] bit={8,16,32};
    protected String[] bit_txt=new String[]{"BYTE", "WORD", "DWORD"};
    protected int Flag_radix=3, Flag_bit=2;                             // radix[Flag_radix]表示当前进制模式，bit[Flag_bit]表示当前位数

    protected Editable input_edit=null;
    protected String data_up="", data="", ANS="0";
    protected int ANS_radix=3;
    protected String Number1="", Number2="";
    protected int textLength;                       //文本长度
    protected int cursorLocation;                   //光标位置

    protected Handler handlerLong=new Handler();
    protected float x1=0,y1=0,x2=0,y2=0;
    protected Boolean move=false;
    protected View longClickView;

    protected SQLiteDatabase db;
    protected MyDBOpenHelper myDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgcalc);
        ActivityCollector.addActivity(this);

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
        getViewId();

        // 设置默认进制：16进制
        changeRadix(Flag_radix);
    }

    /*
     获取 xml 中控件的 id
     */
    public void getViewId(){

        display=findViewById(R.id.display);
        display.setOnTouchListener(this);
        list=findViewById(R.id.list);
        list.setOnTouchListener(this);
        input_up=findViewById(R.id.input_up);
        input = findViewById(R.id.input);
        input.requestFocus();
        input.setFocusable(true);
        input.setShowSoftInputOnFocus(false);
        input_edit=input.getText();

        // 监视输入框数据更改
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                updateLocation();
                updateConversion();
                if(bin.getText().length()>29){
                    bin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                }else{
                    bin.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
                }
            }
        });

        // 监视显示框数据更改
        input_up.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                data_up=input_up.getText().toString();
            }
        });

        for(int i=0;i<29;i++){
            btns[i] = findViewById(ids[i]);
            btns[i].setOnTouchListener(this);
        }
        btns[25].setText(bit_txt[Flag_bit]);

        hex = findViewById(R.id.hex);
        hex.setOnTouchListener(this);
        dec = findViewById(R.id.dec);
        dec.setOnTouchListener(this);
        oct = findViewById(R.id.oct);
        oct.setOnTouchListener(this);
        bin = findViewById(R.id.bin);
        bin.setOnTouchListener(this);
    }

    /*
    点击事件
     */
    public void onClick(View view){

        // 更新输入框各变量
        updateLocation();

        // 如果当前输入框显示的是运算结果
        if(data_up.contains("=")) {
            // 如果点击的是运算符按键，则清空显示框
            if(view.getId() == R.id.btn_and || view.getId() == R.id.btn_or || view.getId() == R.id.btn_xor || view.getId() == R.id.btn_not
                    || view.getId() == R.id.btn_add || view.getId() == R.id.btn_sub || view.getId() == R.id.btn_mul || view.getId() == R.id.btn_div) {
                input_up.setText("");
            }
            // 如果点击的是数字，则清空显示框和输入框
            if(view.getId() == R.id.btn_A || view.getId() == R.id.btn_B || view.getId() == R.id.btn_C || view.getId() == R.id.btn_D || view.getId() == R.id.btn_E || view.getId() == R.id.btn_F
                    || view.getId() == R.id.btn_7 || view.getId() == R.id.btn_8 || view.getId() == R.id.btn_9 || view.getId() == R.id.btn_del
                    || view.getId() == R.id.btn_4 || view.getId() == R.id.btn_5 || view.getId() == R.id.btn_6
                    || view.getId() == R.id.btn_1 || view.getId() == R.id.btn_2 || view.getId() == R.id.btn_3
                    || view.getId() == R.id.btn_0 || view.getId() == R.id.btn_ans){
                input_up.setText("");
                input_edit.clear();
            }
        }

        switch (view.getId()){
            case R.id.list: gotoHistRecPg(); break;
            case R.id.hex: changeRadix(3); break;
            case R.id.dec: changeRadix(2); break;
            case R.id.oct: changeRadix(1); break;
            case R.id.bin: changeRadix(0); break;
            case R.id.btn_A: input_edit.insert(cursorLocation,btn_input[0]); break;
            case R.id.btn_B: input_edit.insert(cursorLocation,btn_input[1]); break;
            case R.id.btn_C: input_edit.insert(cursorLocation,btn_input[2]); break;
            case R.id.btn_D: input_edit.insert(cursorLocation,btn_input[3]); break;
            case R.id.btn_E: input_edit.insert(cursorLocation,btn_input[4]); break;
            case R.id.btn_F: input_edit.insert(cursorLocation,btn_input[5]); break;
            case R.id.btn_and: Operation(0); break;
            case R.id.btn_7: input_edit.insert(cursorLocation,btn_input[7]); break;
            case R.id.btn_8: input_edit.insert(cursorLocation,btn_input[8]); break;
            case R.id.btn_9: input_edit.insert(cursorLocation,btn_input[9]); break;
            case R.id.btn_del: if(input.getSelectionStart()>0&&(!data.substring(input.getSelectionStart()-1,input.getSelectionStart()).equals("\n"))) { input_edit.delete(input.getSelectionStart()-1,input.getSelectionStart()); }; break;
            case R.id.btn_ac: input_edit.clear();input_up.setText(""); break;
            case R.id.btn_or: Operation(1); break;
            case R.id.btn_4: input_edit.insert(cursorLocation,btn_input[13]); break;
            case R.id.btn_5: input_edit.insert(cursorLocation,btn_input[14]); break;
            case R.id.btn_6: input_edit.insert(cursorLocation,btn_input[15]); break;
            case R.id.btn_mul: Operation(6); break;
            case R.id.btn_div: Operation(7); break;
            case R.id.btn_xor: Operation(2); break;
            case R.id.btn_1: input_edit.insert(cursorLocation,btn_input[19]); break;
            case R.id.btn_2: input_edit.insert(cursorLocation,btn_input[20]); break;
            case R.id.btn_3: input_edit.insert(cursorLocation,btn_input[21]); break;
            case R.id.btn_add: Operation(4); break;
            case R.id.btn_sub: Operation(5); break;
            case R.id.btn_not: Operation(3); break;
            case R.id.btn_bit: changeBit(); break;
            case R.id.btn_0: input_edit.insert(cursorLocation,btn_input[26]); break;
            case R.id.btn_ans: input_edit.insert(cursorLocation,new BigInteger(ANS,radix[ANS_radix]).toString(radix[Flag_radix])); break;
            case R.id.btn_equal: equal(); break;
        }
    }

    /*
    更新输入框各变量
     */
    public void updateLocation(){
        data=input.getText().toString();
        textLength=data.length();
        cursorLocation=input.getSelectionStart();
    }

    /*
    进行进制转化
     */
    public void updateConversion(){
        if(textLength!=0) {
            int binLen = new BigInteger(data, radix[Flag_radix]).toString(2).length();
            if (binLen > bit[Flag_bit]) {
                input_edit.delete(cursorLocation-1,cursorLocation);
            }
            hex.setText(RadixUtils.radixConv(data,radix[Flag_radix],16));
            dec.setText(RadixUtils.radixConv(data,radix[Flag_radix],10));
            oct.setText(RadixUtils.radixConv(data,radix[Flag_radix],8));
            bin.setText(RadixUtils.radixConv(data,radix[Flag_radix],2));
        }else{
            hex.setText("0");
            dec.setText("0");
            oct.setText("0");
            bin.setText("0");
        }
    }

    /*
    更新不同进制对应的键盘
     */
    public void updateKeyboard(){
        int Count_all=btns.length, Count_hex=btns_hex.length, Count_dec=btns_dec.length, Count_oct=btns_oct.length, Count_bin=btns_bin.length;
        if (Flag_radix==3) {
            for(int i=0;i<Count_all;i++){
                btns[i].setEnabled(false);
            }
            for(int i=0;i<Count_hex;i++){
                btns[btns_hex[i]].setEnabled(true);
            }
        }
        if (Flag_radix==2) {
            for(int i=0;i<Count_all;i++){
                btns[i].setEnabled(false);
            }
            for(int i=0;i<Count_dec;i++){
                btns[btns_dec[i]].setEnabled(true);
            }
        }
        if (Flag_radix==1) {
            for(int i=0;i<Count_all;i++){
                btns[i].setEnabled(false);
            }
            for(int i=0;i<Count_oct;i++){
                btns[btns_oct[i]].setEnabled(true);
            }
        }
        if (Flag_radix==0) {
            for(int i=0;i<Count_all;i++){
                btns[i].setEnabled(false);
            }
            for(int i=0;i<Count_bin;i++){
                btns[btns_bin[i]].setEnabled(true);
            }
        }
    }

    /*
    改变进制，并重绘输入框样式
     */
    public void changeRadix(int r){
        int old_Flag_radix=Flag_radix;
        Flag_radix = r;
        if(textLength!=0) {
            input_edit.replace(0, textLength, new BigInteger(data, radix[old_Flag_radix]).toString(radix[Flag_radix]));
        }
        if(Flag_radix==3){
            hex.setBackground(getDrawable(R.drawable.shape_gray_black));
            dec.setBackground(getDrawable(R.drawable.shape_gray));
            oct.setBackground(getDrawable(R.drawable.shape_gray));
            bin.setBackground(getDrawable(R.drawable.shape_gray));
        }
        if(Flag_radix==2){
            hex.setBackground(getDrawable(R.drawable.shape_gray));
            dec.setBackground(getDrawable(R.drawable.shape_gray_black));
            oct.setBackground(getDrawable(R.drawable.shape_gray));
            bin.setBackground(getDrawable(R.drawable.shape_gray));
        }
        if(Flag_radix==1){
            hex.setBackground(getDrawable(R.drawable.shape_gray));
            dec.setBackground(getDrawable(R.drawable.shape_gray));
            oct.setBackground(getDrawable(R.drawable.shape_gray_black));
            bin.setBackground(getDrawable(R.drawable.shape_gray));
        }
        if(Flag_radix==0){
            hex.setBackground(getDrawable(R.drawable.shape_gray));
            dec.setBackground(getDrawable(R.drawable.shape_gray));
            oct.setBackground(getDrawable(R.drawable.shape_gray));
            bin.setBackground(getDrawable(R.drawable.shape_gray_black));
        }
        updateKeyboard();
    }

    /*
    更新输入与运算位数，若输入框数值长度大于当前位数，则进行截断
     */
    public void updateBit(int b){
        btns[25].setText(bit_txt[b]);
        if(textLength!=0) {
            String tempdata = new BigInteger(data, radix[Flag_radix]).toString(2);
            int binLen = tempdata.length();
            if (binLen > bit[b]) {
                input_edit.replace(0, textLength, new BigInteger(tempdata.substring(binLen - bit[b], binLen), 2).toString(radix[Flag_radix]));
            }
        }
    }

    /*
   改变输入与运算位数
    */
    public void changeBit(){
        Flag_bit--;
        if (Flag_bit < 0) {
            Flag_bit = 2;
        }
        updateBit(Flag_bit);
    }

    /*
    点击运算符按钮后，将输入框内容显示在显示框，并清空输入框，若为取非，直接进行运算
    mode: 0-与，1-或，2-异或，3-非，4-加，5-减，6-乘，7-除
     */
    public void Operation(int mode){
        if (textLength!=0) {
            if (data_up.contains("&") || data_up.contains("|") || data_up.contains("⊕") || data_up.contains("＋") || data_up.contains("－") || data_up.contains("×") || data_up.contains("÷")) {
                Toast.makeText(this, "暂不支持多级运算", Toast.LENGTH_SHORT).show();
            } else {
                Number1=data;
                if (mode == 0) {
                    input_up.setText(data + " & ");
                    input_edit.clear();
                }
                if (mode == 1) {
                    input_up.setText(data + " | ");
                    input_edit.clear();
                }
                if (mode == 2) {
                    input_up.setText(data + " ⊕ ");
                    input_edit.clear();
                }
                if (mode == 3) {
                    input_up.setText(" ～ "+data+" = ");
                    String result=PgCalcUtils.Not(data, radix[Flag_radix], bit[Flag_bit]);
                    input_edit.replace(0, textLength, result);
                    SQLiteUtils.saveData(db, "listPg", data_up+"\n"+data+"\n"+Flag_radix+"\n"+Flag_bit,this);
                    ANS=result;
                    ANS_radix=Flag_radix;
                }
                if (mode == 4) {
                    input_up.setText(data + "＋");
                    input_edit.clear();
                }
                if (mode == 5) {
                    input_up.setText(data + "－");
                    input_edit.clear();
                }
                if (mode == 6) {
                    input_up.setText(data + "×");
                    input_edit.clear();
                }
                if (mode == 7) {
                    input_up.setText(data + "÷");
                    input_edit.clear();
                }
            }
        }
    }

    /*
    进行运算
     */
    public void equal(){
        if(textLength!=0 && !data_up.contains("=")){
            String result="";
            if (data_up.contains("&")){
                Number2=data;
                result=PgCalcUtils.And(Number1, Number2, radix[Flag_radix], bit[Flag_bit]);
                input_up.setText(Number1+" & "+Number2+" = ");
                input_edit.replace(0, textLength, result);
                SQLiteUtils.saveData(db, "listPg", data_up+"\n"+data+"\n"+Flag_radix+"\n"+Flag_bit,this);
                ANS=result;
                ANS_radix=Flag_radix;
            }
            if (data_up.contains("|")){
                Number2=data;
                result=PgCalcUtils.Or(Number1, Number2, radix[Flag_radix], bit[Flag_bit]);
                input_up.setText(Number1+" | "+Number2+" = ");
                input_edit.replace(0, textLength, result);
                SQLiteUtils.saveData(db, "listPg", data_up+"\n"+data+"\n"+Flag_radix+"\n"+Flag_bit,this);
                ANS=result;
                ANS_radix=Flag_radix;
            }
            if (data_up.contains("⊕")){
                Number2=data;
                result=PgCalcUtils.Xor(Number1, Number2, radix[Flag_radix], bit[Flag_bit]);
                input_up.setText(Number1+" ⊕ "+Number2+" = ");
                input_edit.replace(0, textLength, result);
                SQLiteUtils.saveData(db, "listPg", data_up+"\n"+data+"\n"+Flag_radix+"\n"+Flag_bit,this);
                ANS=result;
                ANS_radix=Flag_radix;
            }
            if (data_up.contains("＋")){
                Number2=data;
                result=PgCalcUtils.add(Number1, Number2, radix[Flag_radix], bit[Flag_bit]);
                if(result!=null) {
                    input_up.setText(Number1 + "＋" + Number2 + " = ");
                    input_edit.replace(0, textLength, result);
                    SQLiteUtils.saveData(db, "listPg", data_up+"\n"+data+"\n"+Flag_radix+"\n"+Flag_bit,this);
                }else{
                    Toast.makeText(this, "结果产生溢出", Toast.LENGTH_SHORT).show();
                }
                ANS=result;
                ANS_radix=Flag_radix;
            }
            if (data_up.contains("－")){
                Number2=data;
                result=PgCalcUtils.sub(Number1, Number2, radix[Flag_radix], bit[Flag_bit]);
                if(result!=null) {
                    input_up.setText(Number1 + "－" + Number2 + " = ");
                    input_edit.replace(0, textLength, result);
                    SQLiteUtils.saveData(db, "listPg", data_up+"\n"+data+"\n"+Flag_radix+"\n"+Flag_bit,this);
                }else{
                    Toast.makeText(this, "结果不能为负数", Toast.LENGTH_SHORT).show();
                }
                ANS=result;
                ANS_radix=Flag_radix;
            }
            if (data_up.contains("×")){
                Number2=data;
                result=PgCalcUtils.mul(Number1, Number2, radix[Flag_radix], bit[Flag_bit]);
                if(result!=null) {
                    input_up.setText(Number1 + "×" + Number2 + " = ");
                    input_edit.replace(0, textLength, result);
                    SQLiteUtils.saveData(db, "listPg", data_up+"\n"+data+"\n"+Flag_radix+"\n"+Flag_bit,this);
                }else{
                    Toast.makeText(this, "结果产生溢出", Toast.LENGTH_SHORT).show();
                }
                ANS=result;
                ANS_radix=Flag_radix;
            }
            if (data_up.contains("÷")){
                Number2=data;
                result=PgCalcUtils.div(Number1, Number2, radix[Flag_radix], bit[Flag_bit]);
                if(result!=null) {
                    input_up.setText(Number1 + "÷" + Number2 + " = ");
                    input_edit.replace(0, textLength, result);
                    SQLiteUtils.saveData(db, "listPg", data_up+"\n"+data+"\n"+Flag_radix+"\n"+Flag_bit,this);
                }else{
                    Toast.makeText(this, "除数不能为 0", Toast.LENGTH_SHORT).show();
                }
                ANS=result;
                ANS_radix=Flag_radix;
            }
        }

    }

    /*
    触摸事件
     */
    @Override
    public boolean onTouch(View view, MotionEvent event){

        // 定义触摸事件滑动距离
        int MoveLengthH=200,MoveLengthV=100;

        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                // 判断是否为长按，目前只允许长按删除键进行连续删除，由于多点触控时易触发bug，不允许连续输入
                if( view.getId()==ids[10]) {
                    longClickView = view;
                    handlerLong.postDelayed(LongClickThread, 500);
                }
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
                // 移除长按事件
                handlerLong.removeCallbacks(LongClickThread);

                // 判断是滑动事件还是点击事件
                if(move) {
                    if (((y2 - y1) > abs(x2 - x1)) && ((y2 - y1 > MoveLengthV))) {
                        //向下滑動
                    } else if (((y1 - y2) > abs(x2 - x1)) && ((y1 - y2 > MoveLengthV))) {
                        //向上滑动
                    } else if (((x2 - x1) > abs(y2 - y1)) && ((x2 - x1) > MoveLengthH)) {
                        //向右滑动
                        gotoSciCalc();
                    } else if (((x1 - x2) > abs(y2 - y1)) && ((x1 - x2) > MoveLengthH)) {
                        //向左滑动
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
    长按事件，连续触发
     */
    Runnable LongClickThread = new Runnable(){
        @Override
        public void run(){
            onClick(longClickView);
            handlerLong.postDelayed(LongClickThread,50);
        }
    };

    /*
    启动科学计算器
     */
    public void gotoSciCalc(){
        Intent intent=new Intent();
        intent.setClass(this, SciCalc.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    /*
    启动历史记录
     */
    public void gotoHistRecPg(){
        Intent intent=new Intent();
        intent.setClass(this, HisRec.class);
        intent.putExtra("table","listPg");
        startActivityForResult(intent,0);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /*
    处理activity返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==RESULT_OK){
            // 历史记录界面返回的数据
            if(requestCode==0) {
                String tempdata = intent.getStringExtra("data");
                if (tempdata!=null) {
                    int len=tempdata.length();
                    changeRadix(Integer.parseInt(tempdata.substring(len-3,len-2)));
                    Flag_bit=Integer.parseInt(tempdata.substring(len-1,len));
                    updateBit(Flag_bit);
                    tempdata=tempdata.substring(0,len-4).replace("\n","");
                    len=tempdata.length();
                    int indexEqual=tempdata.indexOf("=");
                    input_up.setText(tempdata.substring(0,indexEqual+2));
                    input_edit.replace(0,textLength,tempdata.substring(indexEqual+2,len));
                }
            }
        }
    }

    /*
    重写返回键：启动科学计算器
     */
    @Override
    public void onBackPressed() {
        gotoSciCalc();
    }
}
