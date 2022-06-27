package com.tianxin.activity.edit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * 日期控件
 */
public class getDaysDate {
    private static final String TAG = getDaysDate.class.getSimpleName();
    private Context context;
    private String onDateSet;

    public getDaysDate(Context context) {
        this.context = context;
    }

    public String getOnDateSet() {
        return this.onDateSet;
    }

    public void show() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //日历控件日期控件 DatePicker
        DatePickerDialog dp = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int monthOfYear, int dayOfMonth) {
                String year, month, day;
                year = String.valueOf(iyear);
                if (monthOfYear < 10) {
                    month = "0" + monthOfYear;
                } else {
                    month = String.valueOf(monthOfYear);
                }
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = String.valueOf(dayOfMonth);
                }
                onDateSet = year + "-" + month + "-" + day;
            }
        }, year, month, day);
        dp.show();
    }

    /**
     * 分割字符串 2020-07-18
     * @param birthday
     */
    public void setbirthday(String birthday) {
        if (TextUtils.isEmpty(birthday) || birthday.length() < 8) {
            birthday = "19700101";
        }
        StringBuilder sb = new StringBuilder(birthday);
        sb.insert(4, "-");
        sb.insert(7, "-");
        //onDateSet = sb.toString();
        Log.d(TAG, "GameActivityfinish: "+sb.toString());

    }

    /**
     *  生日
     * @param mBirthday
     */
    public void getDays(String mBirthday){
        String birthday = TextUtils.isEmpty(mBirthday) ? "" : mBirthday;
        String birthdayValue = birthday.replace("-","");
    }
}
