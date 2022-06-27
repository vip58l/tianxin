package com.tianxin.activity.edit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tianxin.activity.register.activity_phone_bind;
import com.tianxin.adapter.Tiktokholder.mysetAdapter;
import com.tianxin.BasActivity.BasActivity2;
import com.tianxin.Util.StatusBarUtil;
import com.tianxin.dialog.Dialog_fenxing;
import com.tianxin.getHandler.JsonUitl;
import com.tencent.opensource.model.City;
import com.tencent.opensource.model.address;
import com.tencent.opensource.model.personal;
import com.tianxin.widget.itembackTopbr;
import com.tianxin.R;
import com.tianxin.Util.ActivityLocation;
import com.tianxin.Util.Constants;
import com.tianxin.Util.Toashow;
import com.tianxin.Util.Config;
import com.tianxin.listener.Paymnets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tianxin.Util.ActivityLocation.OPEN_SET_REQUEST_CODE;

/**
 * 更新个人资料
 */
public class activity_updateedit extends BasActivity2 implements Paymnets {

    String TAG = activity_updateedit.class.getSimpleName();
    @BindView(R.id.itembackTopbr)
    itembackTopbr itembackTopbr;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.age2)
    TextView age2;
    @BindView(R.id.height2)
    TextView height2;
    @BindView(R.id.pesigntext2)
    TextView pesigntext2;
    private mysetAdapter mysetAdapter;
    private personal personal;
    private List<String> mylist = new ArrayList<>();

    public static void starsetAction(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, activity_updateedit.class);
        context.startActivity(intent);
    }

    @Override
    protected int getview() {
        StatusBarUtil.setStatusBar(activity, Color.TRANSPARENT);
        return R.layout.activity_editingmaterials;
    }

    @Override
    public void iniview() {
        itembackTopbr.settitle(getString(R.string.edittitle));
        itembackTopbr.righttext.setText(R.string.tvmore);
        itembackTopbr.setHaidtopBackgroundColor(Color.TRANSPARENT);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));
        recyclerview.setAdapter(mysetAdapter = new mysetAdapter(context));
        mysetAdapter.setPayment(this);
        boolean checkpermissions = ActivityLocation.checkpermissions(activity);//申请定位权限
        if (mapLocation == null && checkpermissions) {
            lbsamap.getmyLocation(callback);
        }

    }

    @Override
    public void initData() {
        datamodule.personal(paymnets);
    }

    @OnClick({R.id.tv3title, R.id.l1, R.id.l2, R.id.l3})
    public void OnClick(View v) {
        List<String> mOptionsItems1 = new ArrayList<>();
        switch (v.getId()) {
            case R.id.tv3title:
                startActivityForResult(new Intent(context, activity_edits.class), Config.sussess);
                break;
            case R.id.l1:
                for (int i = 18; i <= 60; i++) {
                    mOptionsItems1.add(String.valueOf(i));
                }
                initTimePicker4(mOptionsItems1, 19, age2);
                break;
            case R.id.l2:
                for (int i = 155; i <= 220; i++) {
                    mOptionsItems1.add(String.valueOf(i));
                }
                initTimePicker4(mOptionsItems1, 20, height2);
                break;
            case R.id.l3: {
                //征友条件
                activity_nickname2.starsetAction(activity, 21);
            }
            break;
        }
    }

    @Override
    public void OnEorr() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            initData();
            //回调给上个一页ACTIVITY
            setResult(resultCode, data);
        }
    }

    /**
     * 时间选择器
     */
    private void initTimePicker() {
        //Dialog 模式下，在底部弹出
        TimePickerView pvTime = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                int gettime = Integer.valueOf(gettime());
                int time = Integer.parseInt(getTime(date));
                int age = gettime - time;
                if (age < 18) {
                    Toashow.show(getString(R.string.tm134));
                    return;
                }
                if (age > 70) {
                    Toashow.show(getString(R.string.tm136));
                    return;
                }

                initTimePicker2(Arrays.asList(String.valueOf(age)), 1000);

            }
        }).setSubmitColor(getResources().getColor(R.color.c_fu))
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(24)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setTitleText(getString(R.string.tm137))
                .build();
        pvTime.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    /**
     * 当前时间
     */
    private String gettime() {
        long s = System.currentTimeMillis();
        Date date = new Date(s);
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String format1 = format.format(date);
        return format1;
    }

    /**
     * 条件选择器
     */
    private void initTimePicker2(List<String> mOptionsItems1, int result) {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

                //选择日期
                if (result == 1000) {
                    int age = Integer.parseInt(mOptionsItems1.get(0));
                    if (age < 18 && age > 70) {
                        return;
                    }
                    datamodule.myupdatepost(mapLocation, mOptionsItems1.get(options1), 1, activity_updateedit.this);
                } else {
                    datamodule.myupdatepost(mapLocation, mOptionsItems1.get(options1), result, activity_updateedit.this);
                }


            }
        })
                .setSubmitColor(getResources().getColor(R.color.c_fu))
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setTitleText(getString(R.string.tm138))
                .build();
        pvOptions.setPicker(mOptionsItems1);
        pvOptions.show();
    }

    /**
     * 条件选择器
     */
    private void initTimePicker4(List<String> mOptionsItems1, int result, TextView t1) {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String name = mOptionsItems1.get(options1);
                t1.setText(name);
                datamodule.myupdatepost(mapLocation, name, result, activity_updateedit.this);
            }
        })
                .setSubmitColor(getResources().getColor(R.color.c_fu))
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setTitleText(getString(R.string.tm138))
                .build();
        pvOptions.setPicker(mOptionsItems1);
        pvOptions.show();
    }

    /**
     * 选择地址
     */
    private void initTimePicker3() {
        String json = Config.getJson("address.json", context);
        List<address> addresses = JsonUitl.stringToList(json, address.class);
        List<String> options1Items = new ArrayList<>();
        List<List<String>> options2Items = new ArrayList<>();
        List<List<List<String>>> options3Items = new ArrayList<>();
        for (address address : addresses) {
            List<String> options2Items_01 = new ArrayList<>();
            List<List<String>> options3Items_01 = new ArrayList<>();
            for (City city : address.getCity()) {
                //北京市
                options2Items_01.add(city.getName());
                //县区
                List<String> options3Items_01_01 = new ArrayList<>();
                for (String s : city.getArea()) {
                    options3Items_01_01.add(s);
                }
                options3Items_01.add(options3Items_01_01);
            }

            options1Items.add(address.getName());
            options2Items.add(options2Items_01);
            options3Items.add(options3Items_01);
        }


        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                // Toashow.show(options1Items.get(options1) + " ---" + options2Items.get(options1).get(options2) + " ---" + options3Items.get(options1).get(options2).get(options3));
                datamodule.myupdatepost(mapLocation, options1Items.get(options1) + "." + options2Items.get(options1).get(options2) + "." + options3Items.get(options1).get(options2).get(options3), 12, activity_updateedit.this);
            }
        })
                .setTitleText("选择地址")
                .setContentTextSize(16)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                //.setSelectOptions(0, 0, 0)//默认选中项
                //.setBgColor(Color.WHITE)
                //.setTitleBgColor(Color.WHITE)
                //.setTitleColor(Color.LTGRAY)
                .setCancelColor(getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(getResources().getColor(R.color.c_fu))
                .setSubmitColor(getResources().getColor(R.color.c_fu))
                .setTextColorCenter(Color.BLACK)
                //.isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();

        //pvOptions.setPicker(options1Items);
        //pvOptions.setPicker(options1Items, options2Items);
        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();


    }

    /**
     * 选择地址
     */
    public static void initTimePicker4(Context context, String title, int type, Paymnets paymnets) {
        List<String> options1Items = new ArrayList<>();
        List<List<String>> options2Items = new ArrayList<>();
        List<List<List<String>>> options3Items = new ArrayList<>();
        dialogoptions(options1Items, options2Items, options3Items, type);

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String s1 = options1Items.get(options1);
                String s2 = options2Items.get(options1).get(options2);
                String s3 = options3Items.get(options1).get(options2).get(options3);
                if (paymnets != null) {
                    String format = String.format("%s%s%s", s1, s2, s3);
                    paymnets.payonItemClick(format, type);
                }

            }
        })
                .setTitleText(title)
                .setContentTextSize(16)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                //.setSelectOptions(0, 0, 0)//默认选中项
                //.setBgColor(Color.WHITE)
                //.setTitleBgColor(Color.WHITE)
                //.setTitleColor(Color.LTGRAY)
                .setCancelColor(context.getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(context.getResources().getColor(R.color.c_fu))
                .setSubmitColor(context.getResources().getColor(R.color.c_fu))
                .setTextColorCenter(Color.BLACK)
                //.isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();

        //pvOptions.setPicker(options1Items);
        //pvOptions.setPicker(options1Items, options2Items);

        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }

    /**
     * 选择地址
     */
    public static void initTimePicker5(Context context, String title, Paymnets paymnets) {
        List<String> options1Items = Arrays.asList(context.getResources().getStringArray(R.array.tabs10));

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String s1 = options1Items.get(options1);
                if (paymnets != null) {
                    paymnets.onSuccess(s1);
                }

            }
        })
                .setTitleText(title)
                .setContentTextSize(16)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                //.setSelectOptions(0, 0, 0)//默认选中项
                //.setBgColor(Color.WHITE)
                //.setTitleBgColor(Color.WHITE)
                //.setTitleColor(Color.LTGRAY)
                .setCancelColor(context.getResources().getColor(R.color.home))
                .setContentTextSize(22)
                .setTextColorCenter(context.getResources().getColor(R.color.c_fu))
                .setSubmitColor(context.getResources().getColor(R.color.c_fu))
                .setTextColorCenter(Color.BLACK)
                //.isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOutSideColor(0x00000000) //设置外部遮罩颜色
                .build();
        pvOptions.setPicker(options1Items);
        pvOptions.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == OPEN_SET_REQUEST_CODE) {
            for (String permission : permissions) {
                int STATE = ContextCompat.checkSelfPermission(this, permission);
                if (STATE != PackageManager.PERMISSION_GRANTED) {
                    //未获取授权转入申请授权
                    Toashow.show(getString(R.string.ts_gps));
                    return;
                }
            }
            lbsamap.getmyLocation(callback);
        }

    }

    @Override
    public void status(int position) {
        //回调操作
        List<String> mOptionsItems1 = new ArrayList<>();
        switch (position) {
            case 0: {//编辑昵称
                Intent intent1 = new Intent(activity, activity_nickname1.class);
                intent1.putExtra(Constants.POSITION, position);
                startActivityForResult(intent1, Config.sussess);
                break;
            }
            case 1: {
                //复制登录帐号
                Dialog_fenxing.paramcopy(context, mylist.get(position));
                Toashow.toastMessage(getString(R.string.toast01));
                break;
            }
            case 2: {
                //复制登录密码
                String pwd = mylist.get(position);
                if (TextUtils.isEmpty(pwd)) {
                    return;
                }
                Dialog_fenxing.paramcopy(context, pwd);
                Toashow.toastMessage(getString(R.string.toast02));
                break;
            }
            case 3: {
                //绑定手机号
                String phone = mylist.get(position);
                if (TextUtils.isEmpty(phone) || phone.equals(getString(R.string.phone_msg))) {
                    activity_phone_bind.starAction(activity);
                }
                break;
            }
            case 4: {
                //年龄
                //initTimePicker();
                for (int i = 18; i <= 60; i++) {
                    mOptionsItems1.add(String.valueOf(i));
                }
                initTimePicker2(mOptionsItems1, 1000);
                break;
            }
            case 5: {
                //身高
                //mOptionsItems1 = Arrays.asList(getResources().getStringArray(R.array.arrayitem5));
                for (int i = 155; i <= 220; i++) {
                    mOptionsItems1.add(String.valueOf(i));
                }
                initTimePicker2(mOptionsItems1, 2);
                break;
            }
            case 6: {
                //体重
                //mOptionsItems1 = Arrays.asList(getResources().getStringArray(R.array.arrayitem4));
                for (int i = 90; i <= 220; i++) {
                    mOptionsItems1.add(String.valueOf(i));
                }
                initTimePicker2(mOptionsItems1, 3);
                break;
            }
            case 7: {
                //学历
                mOptionsItems1 = Arrays.asList(getResources().getStringArray(R.array.arrayitem3));
                initTimePicker2(mOptionsItems1, 4);
                break;
            }
            case 8: {
                //职业
                mOptionsItems1 = Arrays.asList(getResources().getStringArray(R.array.arrayitem2));
                initTimePicker2(mOptionsItems1, 5);
                break;
            }
            case 9: {
                //感情
                mOptionsItems1 = Arrays.asList(getResources().getStringArray(R.array.arrayitem1));
                initTimePicker2(mOptionsItems1, 9);
                break;
            }
            case 10: {
                //地址
                      /*  String json = config.getJson("address.json", activity_editingmaterials.this);
                        List<address> addresses = JsonUitl.stringToList(json, address.class);
                        for (address address : addresses) {
                            mOptionsItems1.add(address.getName());
                        }
                        initTimePicker2(mOptionsItems1, 12);*/

                initTimePicker3();
                break;
            }
            case 11: {
                //个人介绍
                startActivityForResult(new Intent(activity, activity_nickname2.class).putExtra(Constants.POSITION, 13), Config.sussess);
                break;
            }
            case 12: {
                //征友条件
                if (personal != null && !TextUtils.isEmpty(personal.getCforsds())) {
                    Intent intent = new Intent(activity, activity_nickname4.class).putExtra(Constants.POSITION, 14);
                    intent.putExtra(Constants.TITLE, personal.getCforsds());
                    startActivityForResult(intent, Config.sussess);
                }
                break;
            }
            default: {
                break;
            }
             /*

                    case 7:
                        mOptionsItems1.add("苗条");
                        mOptionsItems1.add("偏瘦");
                        mOptionsItems1.add("偏胖");
                        mOptionsItems1.add("微胖");

                        initTimePicker2(mOptionsItems1, 7);
                        break;
                    case 8:
                        mOptionsItems1.add("唱歌");
                        mOptionsItems1.add("交友");
                        mOptionsItems1.add("聊天");
                        mOptionsItems1.add("谈感情");
                        initTimePicker2(mOptionsItems1, 8);
                        break;
                         case 10:
                        mOptionsItems1.add("白羊座");
                        mOptionsItems1.add("金牛座");
                        mOptionsItems1.add("双子座");
                        mOptionsItems1.add("巨蟹座");
                        mOptionsItems1.add("狮子座");
                        mOptionsItems1.add("处女座");
                        mOptionsItems1.add("天秤座");
                        mOptionsItems1.add("天蝎座");
                        mOptionsItems1.add("射手座");
                        mOptionsItems1.add("魔羯座");
                        mOptionsItems1.add("水瓶座");
                        mOptionsItems1.add("双鱼座");
                        initTimePicker2(mOptionsItems1, 10);
                        */
        }
    }

    @Override
    public void onSuccess() {
        initData();
    }

    /**
     * 设置条目内容
     * 重新填充对像属性内容
     *
     * @param personal
     */
    private void setItem(personal personal) {
        this.personal = personal;
        mylist.clear();
        mylist.add(userInfo.getName());     //名称
        mylist.add(userInfo.getUsername()); //帐号
        mylist.add(userInfo.getPwd());      //密码
        mylist.add(TextUtils.isEmpty(userInfo.getMobile()) ? getString(R.string.setphone) + "" : userInfo.getMobile().trim());   //手机号

        mylist.add(String.valueOf(personal.getAge()));//年龄
        mylist.add(String.valueOf(personal.getHeight()));//身高
        mylist.add(String.valueOf(personal.getWeight()));//体重
        mylist.add(personal.getEducation());  //学历
        mylist.add(personal.getOccupation()); //职业
        mylist.add(personal.getFeeling());    //感情
        mylist.add(personal.getPree());       //居住地
        mylist.add(personal.getPesigntext()); //个性签名
        mylist.add(personal.getCforsds());    //征友条件

        //寻找真爱
        age2.setText("" + personal.getAge2());      //年龄
        height2.setText("" + personal.getHeight2());//身高
        pesigntext2.setText(TextUtils.isEmpty(personal.getPesigntext2()) ? "" : personal.getPesigntext2());//其他条件
        List<com.tianxin.adapter.Tiktokholder.mysetAdapter.editinfo> list = mysetAdapter.getList();
        for (int i = 0; i < list.size(); i++) {
            com.tianxin.adapter.Tiktokholder.mysetAdapter.editinfo editinfo = list.get(i);
            editinfo.setTitle(mylist.get(i));
        }
        mysetAdapter.notifyDataSetChanged();
    }

    private Paymnets paymnets = new Paymnets() {
        @Override
        public void isNetworkAvailable() {
            Toashow.show(getString(R.string.eorrfali2));
        }

        @Override
        public void onFail() {
            Toashow.show(getString(R.string.eorrfali3));
        }

        @Override
        public void onSuccess(String msg) {
            Toashow.show(msg);
        }

        @Override
        public void onSuccess(Object object) {
            setItem((com.tencent.opensource.model.personal) object);//刷新UI数据
        }

        @Override
        public void ToKen(String msg) {
            activity_updateedit.super.paymnets.ToKen(msg);
        }
    };


    /**
     * 初始化 年 月 日 时
     *
     * @param options1Items
     * @param options2Items
     * @param options3Items
     */
    private static void dialogoptions
    (List<String> options1Items, List<List<String>> options2Items, List<List<List<String>>> options3Items,
     int type) {
        Calendar cal = Calendar.getInstance();
        //阳历
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        //农历
        int dow = cal.get(Calendar.DAY_OF_WEEK);
        int dom = cal.get(Calendar.DAY_OF_MONTH);
        int doy = cal.get(Calendar.DAY_OF_YEAR);

        //时
        int hour1 = cal.get(Calendar.HOUR_OF_DAY);// 24小时制
        int hour2 = cal.get(Calendar.HOUR); // 12小时制

//        System.out.println(cal.get(cal.HOUR));   //时
//        System.out.println(cal.get(cal.MINUTE)); //分
//        System.out.println(cal.get(cal.SECOND)); //秒


//        System.out.println("Current Date: " + cal.getTime());
//        System.out.println("Day: " + day);
//        System.out.println("Month: " + month);
//        System.out.println("Year: " + year);
//        System.out.println("Day of Week: " + dow);
//        System.out.println("Day of Month: " + dom);
//        System.out.println("Day of Year: " + doy);
//        System.out.println("hour: " + hour1);


        for (int i = 0; i < (12 - month); i++) {
            options1Items.add(String.format("%s月", (month + i)));
            options2Items.add(daylist(day, type));
            options3Items.add(hour1list(day, hour1, type));
        }
    }

    /**
     * 日
     */
    private static List<String> daylist(int day, int type) {
        List<String> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            list.add(String.format("%s日", i));
        }
        return list;
    }

    /**
     * 时
     *
     * @param hour1
     * @return
     */
    private static List<List<String>> hour1list(int day, int hour1, int type) {
        List<List<String>> s = new ArrayList<>();
        for (int i = 0; i < daylist(day, type).size(); i++) {
            s.add(hos(hour1, type));
        }
        return s;
    }

    private static List<String> hos(int hour1, int type) {
        List<String> list = new ArrayList<>();
        for (int i1 = 0; i1 < 24; i1++) {
            list.add(String.format("%s:00", i1));
        }
        return list;
    }

}