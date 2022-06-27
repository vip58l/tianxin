/**
 * Description :
 * 开发者 小清新 QQ804031885
 *
 * @author WSoban
 * @date 2021/3/4 0004
 */


package com.tianxin.mytime;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.tianxin.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class userdatetime extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_pickerview);
        initTimePicker();

    }


    /**
     * 时间选择器
     */
    private void initTimePicker() {

        //Dialog 模式下，在底部弹出
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(userdatetime.this, getTime(date), Toast.LENGTH_SHORT).show();
            }
        }).build();

        pvTime.show();
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

}
