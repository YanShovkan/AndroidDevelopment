package com.example.shovkanlab1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

public class SmileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Smile(this));
    }



}
class Smile extends View {
    Paint paint;

    public Smile(Context context){
        super(context);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawRGB(0,0,0);

        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(50);
        canvas.drawCircle(360, 560, 300, paint);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(20);
        canvas.drawCircle(260, 460, 60, paint);
        canvas.drawCircle(460, 460, 60, paint);

        paint.setColor(Color.BLACK);

        canvas.drawCircle(260, 460, 20, paint);
        canvas.drawCircle(460, 460, 20, paint);
        canvas.drawLine(110, 600, 610, 600,  paint);

    }
}