package com.wjs.calc;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Document extends Activity implements View.OnClickListener {

    protected Button back;
    protected TextView title, body;
    protected String[] title_txt=new String[]{"软件功能","操作手势","科学计算器说明","程序员计算器说明","关于 S&P Calc"};
    protected String[] datas=new String[5];
    protected Integer name=0;

    protected SQLiteDatabase db;
    protected MyDBOpenHelper myDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        // 获取当前软件版本
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

        // 初始化所有菜单项内容
        initdatas();


        name=getIntent().getIntExtra("name",0);
        if (name!=0) {
            showData();
        }
    }

    /*
    获取 xml 中控件的 id
     */
    public void getViewId(){
        back=findViewById(R.id.back);
        back.setOnClickListener(this);
        title=findViewById(R.id.title);
        body=findViewById(R.id.body);
    }

    /*
    显示菜单项内容
     */
    public void showData(){
        title.setText(title_txt[name-1]);
        body.setText(Html.fromHtml(datas[name-1]));
    }

    /*
    点击事件
     */
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.back: onBackPressed();break;
        }
    }

    /*
    初始化所有菜单项内容
     */
    public void initdatas(){
        datas[0]="<div><big>欢迎使用 <b>S&P Calc</b></big>（科学&程序员计算器）。</div>" +
                 "<div>本软件<b>无广告，不联网，不申请任何权限，绝对纯净</b>。</div>" +
                 "<div><strike>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                 "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strike></div>" +
                 "<div>本计算器分为两个模块：<b>科学计算器</b>、<b>程序员计算器</b>。</div>" +
                 "<div><b>科学计算器</b>可进行常规的四则运算、幂运算、开方运算、对数运算、三角函数运算及角度运算。</div>" +
                 "<div><b>程序员计算器</b>可进行16进制、10进制、8进制和2进制之间的转换，每个进制都可进行简单的逻辑运算与四则运算。</div>" +
                 "<div>本计算器可对运算进行记录，可在历史记录(LIST)中进行查询与清空。</div>" +
                 "<div><strike>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                 "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strike></div>" +
                 "<div>操作方法可见 “<b>操作手势</b>” 。</div>" +
                 "<div>详细说明可见 “<b>科学计算器说明</b>” 与 “<b>程序员计算器说明</b>” 。</div>" +
                 "<div>更多关于本软件信息可见 “<b>关于 S&P Calc</b>” 。</div>"+
                 "<div></div>" +
                 "<div></div>";
        datas[1]="<div>本软件主要有三个界面：科学计算器界面，程序员计算器界面，菜单栏界面。</div>" +
                 "<div>科学计算器：在<b>键盘区域</b>左滑进入程序员计算器，右滑打开菜单栏。</div>" +
                 "<div>程序员计算器：在<b>键盘区域</b>右滑返回科学计算器。</div>" +
                 "<div>菜单栏：左滑或点击空白区域返回。</div>" +
                 "<div><strike>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                 "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strike></div>" +
                 "<div>科学计算器有些按键有<b>第二功能</b>，如 “sin( )” 的第二功能为 “arcsin( )” ，按下按键后<b>上滑</b>即可触发第二功能。</div>"+
                 "<div></div>" +
                 "<div></div>";
        datas[2]="<div>科学计算器具有以下功能：<br/>四则运算、幂运算、开方运算、三角与反三角函数运算、对数运算、角度换算、阶乘。</div>" +
                "<div><strike>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strike></div>" +
                 "<div>下面对个别功能进行详细说明：</div>" +
                 "<div><b>[ ° ' &quot; ]</b>：此按键可依次输入度、分、秒，进行角度运算。若当前无数值输入，此按键可将前一次结果转化为度分秒格式，若前一次结果已为度分秒格式，可转化为度，如将9°30'转化为9.5°，反之亦可。" +
                 "在反三角函数计算中，此按键可将弧度（rad）转化为角度。</div>" +
                 "<div><b>三角函数</b>：三角函数中的数值默认为弧度，若要输入角度，请加 “°”，如sin(π/2)=sin(90°)。<br/>" +
                "<small>注：由于精度问题，三角函数计算可能不准确，如sin(π)=1.2246……×10<sup><small>-16</small></sup>，此时可忽略极小的数。</small></div>" +
                 "<div><b>反三角函数</b>：反三角函数计算结果为弧度（rad），若要转化为角度，可按 [ ° ' &quot; ] 键。</div>" +
                 "<div><b>对数运算</b>：目前仅支持以10为底的对数 “lg( )”，和以e为底的对数 “ln( )”，对于其他底数，请转化为这两种进行计算。</div>" +
                 "<div><b>[ENG]</b>：将结果在科学计数法与一般计数之间切换，如将 “9600” 转化为 “9.6E3”（即9.6×10<sup><small>3</small></sup>），反之亦可。</div>" +
                 "<div><b>[n!]</b>：计算阶乘。</div>" +
                 "<div><b>[ANS]</b>：输入前一次计算结果。</div>" +
                 "<div><b>[LIST]</b>：显示历史记录<small>（打开历史记录后，点击空白处可返回）</small>。若点击某条记录，可将此次运算重新显示在输入框。<br/>" +
                 "<small>注：单条记录长度为128个字符，若此次运算长度超出，将不予记录，最多记录1024条，超出后依次删除最早一条记录。若有增大长度的需求，可联系开发者。</small></div>" +
                 "<div></div>" +
                 "<div></div>";
        datas[3]="<div>程序员计算器具有以下功能：<br/>16进制、10进制、8进制和2进制之间进行转换，四则运算，逻辑运算（与、或、非、异或）。</div>" +
                 "<div><strike>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                 "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strike></div>" +
                 "<div>下面对个别功能进行详细说明：</div>" +
                 "<div>界面有 “HEX” “DEC” “OCT” “BIN” 四个显示框，点击显示框便可选择当前输入的进制类型。<br/>" +
                 "<small>注：暂不支持输入和计算负数。</small></div>" +
                 "<div><b>[DWORD/WORD/BYTE]</b>：此按键可设置输入和计算的位数，DWORD（双字）为32位，WORD（字）为16位，BYTE（字节）为8位。如进行取非运算时，DWORD下 ~1=fffffffe，BYTE下 ~1=fe。</div>" +
                 "<div><b>[LIST]</b>：显示历史记录<small>（打开历史记录后，点击空白处可返回）</small>。若点击某条记录，可将此次运算重新显示在输入框。<br/>" +
                 "<small>注：单条记录长度为128个字符，若此次运算长度超出，将不予记录，最多记录1024条，超出后依次删除最早一条记录。若有增大长度的需求，可联系开发者。</small></div>" +
                 "<div></div>" +
                 "<div></div>";
        datas[4]="<div><big>欢迎使用 <b>S&P Calc</b></big>。</div>" +
                 "<div><strike>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                 "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</strike></div>" +
                 "<div>本软件为开发者正式开发的第一款APP，利用空闲时间断断续续开发了一两年，虽然大致成品一年前就做出来了，但是一直到现在才完善。</div>" +
                 "<div>本开发者并非专业开发者，仅作为爱好，通过自学开发了本软件。所以Bug肯定是有的，欢迎各位使用者进行反馈。</div>" +
                 "<div>由于在学习过程中受到各开源大佬的帮助，觉得要回报社会，以表感激。遂将此软件完善并进行免费公开，源代码可在GitHub中查看，地址见底部。</div>" +
                 "<div>再次声明：本软件<b>无广告，不联网，不申请任何权限，绝对纯净</b>。若发现异常，即为盗版。</div>" +
                 "<div><b>本软件仅供个人使用，严禁用作商业用途！</b></div>" +
                 "<div><b>本软件非专业人士开发，且未经严格测试，若使用过程中因Bug对你造成损失，概不负责。<br/>使用此软件便视为接受上述条款。</b></div>" +
                 "<div>GitHub: </div>" +
                 "<div>如需进行交流和反馈，可加入qq群：476570264</div>" +
                 "<div>也可发送邮件至 wang_js@foxmail.com</div>" +
                 "<div></div>" +
                 "<div></div>";
    }

    /*
    重写返回键
     */
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
