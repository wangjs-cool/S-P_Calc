package com.wjs.calc;



import androidx.constraintlayout.widget.ConstraintLayout;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import static java.lang.Math.abs;
import static java.lang.Math.max;

public class SciCalc extends Activity implements View.OnTouchListener {

    protected ConstraintLayout entireScreen, display_C;
    protected Button btn_sidebar;
    protected LinedEditText display;
    Integer[] ids = {R.id.btn_list, R.id.btn_up, R.id.btn_left, R.id.btn_right, R.id.btn_down, R.id.btn_clr,
            R.id.btn_fun1, R.id.btn_fun2, R.id.btn_fun3, R.id.btn_fun4, R.id.btn_fun5, R.id.btn_fun6, R.id.btn_fun7, R.id.btn_fun8, R.id.btn_fun9, R.id.btn_fun10, R.id.btn_fun11, R.id.btn_fun12,
            R.id.btn_num7, R.id.btn_num8, R.id.btn_num9, R.id.btn_del, R.id.btn_ac, R.id.btn_num4, R.id.btn_num5, R.id.btn_num6, R.id.btn_mul, R.id.btn_div,
            R.id.btn_num1, R.id.btn_num2, R.id.btn_num3, R.id.btn_add, R.id.btn_sub, R.id.btn_pi, R.id.btn_num0, R.id.btn_point, R.id.btn_ans, R.id.btn_equal};
    protected Button btn_LIST, btn_UP, btn_LEFT, btn_RIGHT, btn_DOWN, btn_CLR,
            btn_SQRT, btn_SIN, btn_COS, btn_TAN, btn_LG, btn_LN, btn_NESG, btn_POWER, btn_SQUARE, btn_LEFTPARE, btn_RIGHTPARE, btn_ANGLE,
            btn_NUM7, btn_NUM8, btn_NUM9, btn_DEL, btn_AC, btn_NUM4, btn_NUM5, btn_NUM6, btn_MUL, btn_DIV, btn_NUM1, btn_NUM2, btn_NUM3, btn_ADD, btn_SUB, btn_PI, btn_NUM0, btn_POINT, btn_ANS, btn_EQUAL;
    protected Button[] btns = {btn_LIST, btn_UP, btn_LEFT, btn_RIGHT, btn_DOWN, btn_CLR,
            btn_SQRT, btn_SIN, btn_COS, btn_TAN, btn_LG, btn_LN, btn_NESG, btn_POWER, btn_SQUARE, btn_LEFTPARE, btn_RIGHTPARE, btn_ANGLE,
            btn_NUM7, btn_NUM8, btn_NUM9, btn_DEL, btn_AC, btn_NUM4, btn_NUM5, btn_NUM6, btn_MUL, btn_DIV, btn_NUM1, btn_NUM2, btn_NUM3, btn_ADD, btn_SUB, btn_PI, btn_NUM0, btn_POINT, btn_ANS, btn_EQUAL};

    protected float x1=0,y1=0,x2=0,y2=0;
    protected Boolean move=false;
    protected Handler handlerLong=new Handler();
    protected View longClickView;

    protected DecimalFormat decimalFormat = new DecimalFormat();
    protected DecimalFormat decimalFormatE = new DecimalFormat();
    
    protected Editable display_edit=null;
    protected String data="";
    protected String ANS="0";
    protected String[] btn_shift=new String[]{"","","","","","","3√","arcsin()","arccos()","arctan()","","","","","^2","","","","","","","","","","","","","","","","","","","","","e","%",""};
    protected String[] btn_input=new String[]{"","","","","","","√","sin()","cos()","tan()","lg()","ln()","","-","^","(",")","","7","8","9","","","4","5","6","×","÷","1","2","3","+","-","0",".","π","",""};
    protected int textLength;                       //文本长度
    protected int cursorLocation;                   //光标位置
    protected int enterBeforeN;                     //光标前“\n”位置（不含当前位置）
    protected int enterBehindN;                     //光标后“\n”位置（不含当前位置）
    protected int enterBeforeY;                     //光标前“\n”位置（含当前位置）
    protected int enterBehindY;                     //光标后“\n”位置（含当前位置）
    protected int equalBeforeN;                     //光标前“=”位置（不含当前位置）
    protected int equalBehindN;                     //光标后“=”位置（不含当前位置）
    protected int equalBeforeY;                     //光标前“=”位置（含当前位置）
    protected int equalBehindY;                     //光标后“=”位置（含当前位置）

    protected long exitTime = 0;

    protected SQLiteDatabase db;
    protected MyDBOpenHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scicalc);

        // 管理activity，方便统一杀死
        ActivityCollector.addActivity(this);

        // 获取当前软件版本
        int version=1;
        try {
            version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
        }catch (Exception e){
        }

        // 创建数据库实例
        myDBHelper = new MyDBOpenHelper(this, "calc.db", null, version);
        db = myDBHelper.getWritableDatabase();

        // 格式化数值
        decimalFormat.applyPattern("0.################################");
        decimalFormatE.applyPattern("0.################################E0");

        // 获取 xml 中控件的 id
        getViewId();

        // 创建显示框
        createDisplayView();
    }

    /*
     获取 xml 中控件的 id
     */
    public void getViewId(){
        entireScreen=findViewById(R.id.entireScreen);
        entireScreen.setOnTouchListener(this);
        btn_sidebar=findViewById(R.id.sidebar);
        btn_sidebar.setOnTouchListener(this);
        display_C=findViewById(R.id.display_C);
        for(int i=0;i<38;i++){
            btns[i] = findViewById(ids[i]);
            btns[i].setOnTouchListener(this);
        }
    }

    /*
    创建显示框
     */
    public void createDisplayView(){

        // 创建带行线的显示框
        display=new LinedEditText(this,null);
        display.setTextSize(18);
        display.setGravity(Gravity.TOP);
        display.setBackground(null);
        display.setLineSpacing(RadixUtils.dp2px(6,this),1);
        ConstraintLayout.LayoutParams displayParams=new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
        displayParams.leftToLeft = R.id.display_C;
        displayParams.topToTop = R.id.display_C;
        displayParams.rightToRight = R.id.display_C;
        displayParams.bottomToBottom = R.id.display_C;
        displayParams.setMargins(80,40,80,40);
        display.setLayoutParams(displayParams);
        display_C.addView(display);

        // 修改光标样式
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(display, R.drawable.cursor_style);
        }catch (Exception e){
        }

        // 获取焦点并隐藏系统自带键盘
        display.requestFocus();
        display.setFocusable(true);
        display.setShowSoftInputOnFocus(false);

        // 创建显示框的 Editable ，可对显示框内容进行编辑
        display_edit=display.getText();

        // 监听显示框内容改变
        display.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                updateLocation();
            }
        });
    }

    /*
    点击事件
     */
    public void onClick(View view) {

        // 更新显示框各变量
        updateLocation();

        // 无数值时如果直接点击加减乘除，则将前一次运算结果当作第一个数值
        if((view.getId()==R.id.btn_mul|| view.getId()==R.id.btn_div|| view.getId()==R.id.btn_add|| view.getId()==R.id.btn_sub)
                &&(display.getText().toString().length()==0||display.getText().toString().substring(display.getSelectionStart()-1).equals("\n"))){
            inputANS();
        }

        // 设置按键点击事件
        switch (view.getId()){
            case R.id.sidebar: gotoSidebar(); break;
            case R.id.btn_list: gotoHistRecSci(); break;
            case R.id.btn_up: up(); break;
            case R.id.btn_left: left(); break;
            case R.id.btn_right: right(); break;
            case R.id.btn_down: down(); break;
            case R.id.btn_clr: display.getText().clear();ANS="0"; break;
            case R.id.btn_fun1: display_edit.insert(cursorLocation,btn_input[6]); break;
            case R.id.btn_fun2: display_edit.insert(cursorLocation,btn_input[7]); left(); break;
            case R.id.btn_fun3: display_edit.insert(cursorLocation,btn_input[8]); left(); break;
            case R.id.btn_fun4: display_edit.insert(cursorLocation,btn_input[9]); left(); break;
            case R.id.btn_fun5: display_edit.insert(cursorLocation,btn_input[10]); left(); break;
            case R.id.btn_fun6: display_edit.insert(cursorLocation,btn_input[11]); left(); break;
            case R.id.btn_fun7: formatConversion(); break;
            case R.id.btn_fun8: display_edit.insert(cursorLocation,btn_input[13]); break;
            case R.id.btn_fun9: display_edit.insert(cursorLocation,btn_input[14]); break;
            case R.id.btn_fun10: display_edit.insert(cursorLocation,btn_input[15]); break;
            case R.id.btn_fun11: display_edit.insert(cursorLocation,btn_input[16]); break;
            case R.id.btn_fun12: angle(); break;
            case R.id.btn_num7: display_edit.insert(cursorLocation,btn_input[18]); break;
            case R.id.btn_num8: display_edit.insert(cursorLocation,btn_input[19]); break;
            case R.id.btn_num9: display_edit.insert(cursorLocation,btn_input[20]); break;
            case R.id.btn_del:
                // 删除本次运算的字符，但不删除之前运算的内容
                if(cursorLocation>0&&(!data.substring(cursorLocation-1,cursorLocation).equals("\n"))) {
                    display_edit.delete(cursorLocation-1,cursorLocation);
                }; break;
            case R.id.btn_ac:
                if(textLength!=0) {
                    // 清除本次运算所有字符，但不删除之前运算的内容
                    if (!data.substring(textLength - 1, textLength).equals("\n")) {
                        display_edit.delete(data.lastIndexOf("\n", textLength) + 1, textLength);
                    } else {
                        display.setSelection(textLength);       //光标移动到文末
                    }
                };break;
            case R.id.btn_num4: display_edit.insert(cursorLocation,btn_input[23]); break;
            case R.id.btn_num5: display_edit.insert(cursorLocation,btn_input[24]); break;
            case R.id.btn_num6: display_edit.insert(cursorLocation,btn_input[25]); break;
            case R.id.btn_mul: display_edit.insert(cursorLocation,btn_input[26]); break;
            case R.id.btn_div: display_edit.insert(cursorLocation,btn_input[27]); break;
            case R.id.btn_num1: display_edit.insert(cursorLocation,btn_input[28]); break;
            case R.id.btn_num2: display_edit.insert(cursorLocation,btn_input[29]); break;
            case R.id.btn_num3: display_edit.insert(cursorLocation,btn_input[30]); break;
            case R.id.btn_add: display_edit.insert(cursorLocation,btn_input[31]); break;
            case R.id.btn_sub: display_edit.insert(cursorLocation,btn_input[32]); break;
            case R.id.btn_num0: display_edit.insert(cursorLocation,btn_input[33]); break;
            case R.id.btn_point: display_edit.insert(cursorLocation,btn_input[34]); break;
            case R.id.btn_pi: display_edit.insert(cursorLocation,btn_input[35]); break;
            case R.id.btn_ans: inputANS(); break;
            case R.id.btn_equal: equal(); break;
            default: Toast.makeText(this, "此按键未设置", Toast.LENGTH_SHORT).show();
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
                // 判断是否为长按，目前只允许长按删除键进行连续删除，由于多点触控时易触发bug，不允许连续输入
                if(view.getId()==R.id.btn_del) {
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
                        go_up(view);
                    } else if (((x2 - x1) > abs(y2 - y1)) && ((x2 - x1) > MoveLengthH)) {
                        //向右滑动
                        gotoSidebar();
                    } else if (((x1 - x2) > abs(y2 - y1)) && ((x1 - x2) > MoveLengthH)) {
                        //向左滑动
                        gotoPgCalc();
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
    上滑事件，触发按键第二功能
     */
    public void go_up(View view){
        switch (view.getId()){
            case R.id.btn_fun1: display_edit.insert(display.getSelectionStart(),btn_shift[6]);break;
            case R.id.btn_fun2: display_edit.insert(display.getSelectionStart(),btn_shift[7]);left();break;
            case R.id.btn_fun3: display_edit.insert(display.getSelectionStart(),btn_shift[8]);left();break;
            case R.id.btn_fun4: display_edit.insert(display.getSelectionStart(),btn_shift[9]);left();break;
            case R.id.btn_fun9: display_edit.insert(display.getSelectionStart(),btn_shift[14]);break;
            case R.id.btn_fun12: display_edit.insert(display.getSelectionStart(),"!");break;
            case R.id.btn_pi: display_edit.insert(display.getSelectionStart(),btn_shift[35]);break;
            case R.id.btn_ans: display_edit.insert(display.getSelectionStart(),btn_shift[36]);break;
            default: break;
        }
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
    更新显示框变量
     */
    public void updateLocation(){
        data=display.getText().toString();
        textLength=data.length();
        cursorLocation=display.getSelectionStart();
        enterBeforeN=data.lastIndexOf("\n",cursorLocation-1);
        enterBehindN=data.indexOf("\n",cursorLocation+1);
        enterBeforeY=data.lastIndexOf("\n",cursorLocation);
        enterBehindY=data.indexOf("\n",cursorLocation);
        equalBeforeN=data.lastIndexOf("=",cursorLocation-1);
        equalBehindN=data.indexOf("=",cursorLocation+1);
        equalBeforeY=data.lastIndexOf("=",cursorLocation);
        equalBehindY=data.indexOf("=",cursorLocation);
        // 删除多余空行
        if (textLength>2&&data.substring(textLength - 1, textLength).equals("\n") && data.substring(textLength - 2, textLength - 1).equals("\n")) {
            display_edit.delete(textLength - 1, textLength);
        }
    }

    /*
    进行运算
     */
    public void equal(){

        int location_save_start;
        double result;
        String data_f,data_r="",data_s="";
        boolean angle=false,arc=false;

        if(enterBehindY>0) {
            display.setSelection(enterBehindY);       //光标移动到该行末尾
        }else {
            display.setSelection(textLength);       //光标移动到文末
        }
        updateLocation();

        //查找光标前是否有换行
        if(data.lastIndexOf("\n",display.getSelectionStart()-1)>0) {
            //选取光标前最后一个换行到光标之间的字符串
            data_f = data.substring(enterBeforeN+1, cursorLocation);
            location_save_start=enterBeforeN+1;
        }else{
            //选取文首到光标之间的字符串
            data_f = data.substring(0, cursorLocation);
            location_save_start=0;
        }

        //判断选定字符串中是否含有“=”和字符串是否为空
        if(!((data_f.contains("=")||data_f.length()==0))) {

            data_r=data_f;
            //处理三角函数
            if(data_r.contains("sin")||data_r.contains("cos")||data_r.contains("tan")){
                String data_temp;
                int location=max(max(data_r.lastIndexOf("sin",data_r.length()),data_r.lastIndexOf("cos",data_r.length())),data_r.lastIndexOf("tan",data_r.length()));
                for(;location>=0;){
                    String data_t;
                    int count=1,start=location+4;
                    location+=4;
                    for(;count!=0;location++){
                        try {
                            if (data_r.substring(location, location + 1).equals("(")) {
                                count++;
                            } else if (data_r.substring(location, location + 1).equals(")")) {
                                count--;
                            }
                        }catch (Exception e){
                            data_f="0.0/0.0";
                            data_r="0.0/0.0";
                            break;
                        }
                    }
                    try {
                        data_t = data_r.substring(start - 4, location);
                        data_temp = data_r.substring(start, location - 1);
                    }catch (Exception e){
                        data_f="0.0/0.0";
                        data_r="0.0/0.0";
                        break;
                    }
                    if(data_temp.contains("°")){
                        data_temp= data_t.replace(data_temp, RadixUtils.angle2red(data_temp));
                        data_r=data_r.replace(data_t,data_temp);
                    }
                    if(start!=4) {
                        location = max(max(data_r.lastIndexOf("sin", start - 5), data_r.lastIndexOf("cos", start - 5)), data_r.lastIndexOf("tan", start - 5));
                    }else {
                        location=-1;
                    }
                }
            }

            if(data_f.contains("arc")){
                if(data_f.contains("°")){
                    data_f="0.0/0.0";
                    data_r="0.0/0.0";
                }else {
                    arc = true;
                }
            }

            //处理度分秒
            if(data_r.contains("°")) {
                data_r = deal_angle(data_r);
                angle=true;
            }

            if(data_f.contains("°")){
                result = Calculator.conversion(data_r);
            }else {
                result = Calculator.conversion(data_f);       //计算结果
            }
//            result=Double.valueOf(String.format("%.9f",result));
            //判断结果是否为整数
            if (result - Math.round(result) == 0) {
                //将结果转化为整型输出
                if (!String.valueOf((long) result).equals("NaN")) {
                    ANS = String.valueOf((long) result);
                    if(arc){
                        ANS = String.valueOf((long) result)+"rad";
                    }
                    if(angle){
                        ANS= RadixUtils.double2angle((long)result+"");
                    }
                }
                if (enterBehindN > 0) {
                    display_edit.delete(display.getSelectionStart(), data.indexOf("\n", display.getSelectionStart() + 1));
                    if(arc){
                        display_edit.insert(cursorLocation, "\n=" + (long)result+"rad");
                    }else if (angle) {
                        display_edit.insert(cursorLocation, "\n=" + RadixUtils.double2angle((long)result+""));
                    } else {
                        display_edit.insert(cursorLocation, "\n=" + (long)result);
                    }
                } else {
                    if(arc){
                        display_edit.insert(cursorLocation, "\n=" + (long)result+"rad\n");
                    }else if (angle) {
                        display_edit.insert(cursorLocation, "\n=" + RadixUtils.double2angle((long)result+"")+ "\n");
                    } else {
                        display_edit.insert(display.getSelectionStart(), "\n=" + (long) result + "\n");
                    }
                }
            }else {
                if(!String.valueOf(result).equals("NaN")) {
                    ANS = decimalFormat.format(result);
                    if(arc){
                        ANS = result+"rad";
                    }
                    if(angle){
                        ANS= RadixUtils.double2angle(result+"");
                    }
                }
                if(enterBehindN > 0){
                    display_edit.delete(display.getSelectionStart(), data.indexOf("\n", display.getSelectionStart() + 1));
                    if(arc){
                        display_edit.insert(cursorLocation, "\n=" + result+"rad");
                    }else if(angle){
                        display_edit.insert(cursorLocation, "\n=" + RadixUtils.double2angle(result+""));
                    }else {
                        display_edit.insert(cursorLocation, "\n=" + decimalFormat.format(result));
                    }
                }else {
                    if(arc){
                        display_edit.insert(cursorLocation, "\n=" + result+"rad\n");
                    }else if (angle) {
                        display_edit.insert(cursorLocation, "\n=" + RadixUtils.double2angle(result+"") +"\n");
                    } else {
                        display_edit.insert(display.getSelectionStart(), "\n=" + decimalFormat.format(result) + "\n");
                    }
                }
            }
            if(data.substring(cursorLocation-1, cursorLocation).equals("\n")) {
                 data_s = data.substring(location_save_start, cursorLocation-1);
            }else {
                data_s = data.substring(location_save_start, cursorLocation);
            }
            SQLiteUtils.saveData(db, "listSci", data_s,this);
        }else{
            //将光标移到文末
            display.setSelection(textLength);
            updateLocation();
        }
    }

    /*
    计算角度
     */
    public void angle(){
        if(cursorLocation!=0) {
            if (data.substring(cursorLocation - 1, cursorLocation).equals("\n")) {
                String data_s=ANS;
                if(ANS.contains("rad")){
                    display_edit.insert(cursorLocation, ANS  + "\n=" + RadixUtils.rad2angle(ANS) + "\n");
                    ANS = RadixUtils.rad2angle(ANS);
                }else if(!ANS.contains("°")) {
                    display_edit.insert(cursorLocation, ANS + "°" + "\n=" + RadixUtils.double2angle(ANS) + "\n");
                    ANS = RadixUtils.double2angle(ANS);
                }else {
                    display_edit.insert(cursorLocation, ANS + "\n=" + RadixUtils.angle2double(ANS) + "°\n");
                    ANS = RadixUtils.angle2double(ANS);
                }
                data_s += "\n="+ANS;
                SQLiteUtils.saveData(db, "listSci",data_s,this);
            }else if(!(data.substring(cursorLocation-1,cursorLocation).equals("°")||data.substring(cursorLocation-1,cursorLocation).equals("′")||data.substring(cursorLocation-1,cursorLocation).equals("″"))&& RadixUtils.isNum(data.substring(cursorLocation-1,cursorLocation).toCharArray())){
                String data_t;
                int start=cursorLocation;
                for(int i = 0; RadixUtils.isNum(data.substring(cursorLocation-1-i,cursorLocation-i).toCharArray())||data.substring(cursorLocation-1-i,cursorLocation-i).equals("°")||data.substring(cursorLocation-1-i,cursorLocation-i).equals("′")||data.substring(cursorLocation-1-i,cursorLocation-i).equals("″"); i++){
                    start--;
                    if(cursorLocation-1-i==0){
                        break;
                    }
                }
                data_t=data.substring(start,cursorLocation);
                if(!data_t.contains("″")) {
                    int Deg = data_t.lastIndexOf("°", cursorLocation);
                    int Min = data_t.lastIndexOf("′", cursorLocation);
                    if (Min > 0 ) {
                        display_edit.insert(cursorLocation, "″");
                    } else if (Deg > 0 ) {
                        display_edit.insert(cursorLocation, "′");
                    }else {
                        display_edit.insert(cursorLocation, "°");
                    }
                }
            }
        }
    }

    /*
    科学计数法转换
     */
    public void formatConversion(){
        if(!ANS.equals("0")) {
            display.setSelection(textLength);
            String data_s = ANS;
            if (ANS.contains("E")) {
                display_edit.insert(cursorLocation, ANS + "\n=" + decimalFormat.format(Double.valueOf(ANS)) + "\n");
                ANS = decimalFormat.format(Double.valueOf(ANS));
            } else {
                display_edit.insert(cursorLocation, ANS + "\n=" + decimalFormatE.format(Double.valueOf(ANS)) + "\n");
                ANS = decimalFormatE.format(Double.valueOf(ANS));
            }
            data_s += "\n=" + ANS;
            SQLiteUtils.saveData(db, "listSci", data_s, this);
        }
    }

    /*
     输入前一次计算结果
     */
    public void inputANS() {
        // 如果前一次结果是科学计数法，则转化为一般计数法
        if (ANS.contains("E")) {
            ANS = decimalFormat.format(Double.valueOf(ANS));
        }
        display_edit.insert(cursorLocation, ANS);
    }

    /*
    光标上移
     */
    public void up(){
        data=display.getText().toString();
        if(data.lastIndexOf("\n",display.getSelectionStart()-1)>0) {
            int length = display.getSelectionStart() - data.lastIndexOf("\n", display.getSelectionStart() - 1);
            display.setSelection(display.getSelectionStart() - length);
            int length_last = display.getSelectionStart() - data.lastIndexOf("\n", display.getSelectionStart() - 1);
            if(length_last>length) {
                display.setSelection(display.getSelectionStart() - length_last + length);
            }
        }
    }
    /*
    光标下移
     */
    public void down(){
        data=display.getText().toString();
        if(data.indexOf("\n",display.getSelectionStart())>0) {
            int length = display.getSelectionStart() - data.lastIndexOf("\n", display.getSelectionStart() - 1);
            int add = data.indexOf("\n", display.getSelectionStart()) - display.getSelectionStart();
            display.setSelection(display.getSelectionStart() + add);
            if(data.indexOf("\n", display.getSelectionStart()+1)>0) {
                int length_next = data.indexOf("\n", display.getSelectionStart() + 1) - display.getSelectionStart();
                if (length_next > length) {
                    display.setSelection(display.getSelectionStart() + length);
                } else {
                    display.setSelection(display.getSelectionStart() + length_next);
                }
            }else {
                display.setSelection(display.getSelectionStart() + 1);
            }
        }
    }
    /*
    光标左移
     */
    public void left(){
        if(display.getSelectionStart()!=0) {
            display.setSelection(display.getSelectionStart() - 1);
        }
    }
    /*
    光标右移
     */
    public void right(){
        data=display.getText().toString();
        if(display.getSelectionStart()!=data.length()) {
            display.setSelection(display.getSelectionStart() + 1);
        }
    }

    /*
    处理度分秒
     */
    public String deal_angle(String data_r){
        for(;data_r.contains("°");){
            int start,end;
            String data_temp;
            start=data_r.indexOf("°",0);
            for(int i = 0; RadixUtils.isNum(data_r.substring(data_r.indexOf("°",0)-1-i,data_r.indexOf("°",0)-i).toCharArray()); i++){
                start--;
                if(data_r.indexOf("°",0)-1-i==0){
                    break;
                }
            }
            if(data_r.contains("″")){
                end=data_r.indexOf("″",0)+1;
            }else if(data_r.contains("′")){
                end=data_r.indexOf("′",0)+1;
            }else {
                end=data_r.indexOf("°",0)+1;
            }
            data_temp=data_r.substring(start,end);
            data_r = data_r.replace(data_temp, RadixUtils.angle2double(data_temp));
        }
        return data_r;
    }

    /*
     启动侧边栏
     */
    public void gotoSidebar(){
        Intent intent=new Intent();
        intent.setClass(this, Sidebar.class);
        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    /*
    启动历史记录
     */
    public void gotoHistRecSci(){
        Intent intent=new Intent();
        intent.setClass(this, HisRec.class);
        intent.putExtra("table","listSci");
        startActivityForResult(intent,0);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /*
    启动程序员计算器
     */
    public void gotoPgCalc(){
        Intent intent=new Intent();
        intent.setClass(this, PgCalc.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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
                    if (textLength != 0) {
                        display.setSelection(textLength);       //光标移动到文末
                        if (!data.substring(textLength - 1, textLength).equals("\n")) {
                            display_edit.insert(cursorLocation, "\n");
                        }
                    }
                    display_edit.insert(cursorLocation, tempdata + "\n");
                }
            }
            // 侧边栏界面返回的数据
            if(requestCode==1) {
                int exit = intent.getIntExtra("exit",0);
                if (exit==1) {
                    ActivityCollector.finishAll();
                }
            }
        }
    }

    /*
    重写返回键：双击退出
     */
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - exitTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = mNowTime;
        } else{
            ActivityCollector.finishAll();
        }
    }
}
