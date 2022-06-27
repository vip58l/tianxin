package com.tianxin.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.tianxin.R;

public class CustomIndView extends View {

    private int a;
    private int b;
    private Paint c;

    public CustomIndView(Context context) {
        super(context);
    }

    public CustomIndView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomIndView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void a() {
        this.c = new Paint();
        this.c.setAntiAlias(true);
    }

    public void a(int paramInt) {
        this.b = paramInt;
        invalidate();
    }

    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        int i = density(4.0F);
        int j = density(4.0F);
        int k = density(5.0F);
        int i2;
        int i3;
        int i4;
        int i5;
        if (this.a > 1) {
            int m = getWidth();
            int n = getHeight();
            int i1 = (m - i * this.a - k * (-1 + this.a)) / 2;
            i2 = (n - j) / 2;
            i3 = j + (n - j) / 2;
            i4 = 1;
            i5 = i1;
            if (i4 > this.a) {
                LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams((i + k) * this.a, -1);
                localLayoutParams.rightMargin = density(5.0F);
                setLayoutParams(localLayoutParams);
            }
        } else {
            return;
        }


        while (true) {
            if (this.b == i4 - 1) {
                paramCanvas.drawRect(i5, i2, i5 + i, i3, this.c);
                int i6 = k + (i5 + i);
                i4++;
                i5 = i6;
                c.setColor(getResources().getColor(R.color.c_main));
                break;
            }
            c.setColor(Color.parseColor("#cdcdcd"));
        }

    }

    public void setMaxPageCount(int paramInt) {
        this.a = paramInt;
    }

    public void setNowPage(int paramInt) {
        this.b = paramInt;
    }

    public int density(float paramFloat) {
        return (int) (0.5F + paramFloat * getContext().getResources().getDisplayMetrics().density);
    }
}
