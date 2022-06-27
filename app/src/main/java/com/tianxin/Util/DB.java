package com.tianxin.Util;

import android.content.Context;
import android.os.Environment;
import android.widget.EditText;

import java.io.File;

public class DB {

    public static void c(Context paramContext)
    {
        a(f(paramContext));
        a(d(paramContext));
        a(e(paramContext));
    }

    public static String d(Context paramContext)
    {
        return f(paramContext) + "/" + "MMP_PIC";
    }

    public static String e(Context paramContext)
    {
        return f(paramContext) + "/" + "MMP_BOOK";
    }

    private static String f(Context paramContext)
    {
        return a() + "/MMP_LOVE";
    }


    public static File a(String paramString)
    {
        File localFile = new File(paramString);
        if (!localFile.exists())
            localFile.mkdirs();
        return localFile;
    }

    public static String a()
    {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    //保存登录帐号密码
    public static void saveputString(Context context, EditText username, EditText password, int i) {
        if (Config.sussess == i) {
            String user = username.getText().toString().trim();
            String pwd = password.getText().toString().trim();
            Config.putString(context, "username", user);
            Config.putString(context, "password", pwd);
        } else {
            try {
                //读取登录的数据
                String user = Config.getString(context, "username");
                String pwd = Config.getString(context, "password");
                username.setText(user);
                password.setText(pwd);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

}
