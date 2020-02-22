package com.example.indoorapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.annotation.Nullable;
@SuppressLint({"NewApi", "AppCompatCustomView"})
public class MyImageView extends ImageView {
    Paint paint;
    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(110,110,210,paint);
    }
}
