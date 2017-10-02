package com.searchAgain.oramusic.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.searchAgain.oramusic.MusicPlayer;

import java.util.Random;

/**
 * a music visualizer sort of animation (with random data)
 */
public class MusicVisualizer_gradient extends View {

   // Random random = new Random();
    Paint paint = new Paint();
    private Runnable animateView = new Runnable() {
        @Override
        public void run() {

            //run every 150 ms
            postDelayed(this, 100);
            invalidate();
        }
    };



    public MusicVisualizer_gradient(Context context) {
        super(context);
        new MusicVisualizer(context, null);
    }

    public MusicVisualizer_gradient(Context context, AttributeSet attrs) {
        super(context, attrs);

        //start runnable
        removeCallbacks(animateView);
        post(animateView);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        byte[] mBytes;
        mBytes=MusicPlayer.getmBytes();
        if (mBytes == null) {
            return;
        }
      /*
       paint.setStyle(Paint.Style.FILL);
                Rect mRect = new Rect();
            float[] mPoints=new float[4];

        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }

        mRect.set(0, 0, getWidth(), getHeight());
        for (int i = 0; i < mBytes.length - 1; i++) {
            float left      = mRect.width() * i / (mBytes.length - 1);
            float top       = mRect.height() / 2 + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            float right     = (mRect.width() * (i + (mBytes.length))/ (mBytes.length - 1));
            float bottom    = mRect.height() / 2;
            canvas.drawRect(left,top,right,bottom,paint);
        }

*/
/*

        Rect mRect = new Rect();

         mRect.set(0, 0, getWidth(), getHeight());
        for (int i = 0; i < mBytes.length - 1; i++) {
            float left      = mRect.width() * i / (mBytes.length - 1);
            float top       = mRect.height() / 2 + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            float right     = (mRect.width() * (i + (mBytes.length))/ (mBytes.length - 1));
            float bottom    = mRect.height() / 2;
            canvas.drawRect(left,top,right,bottom,paint);
        }


       Rect mRect = new Rect();
        mRect.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < mBytes.length - 1; i++) {
            float left      =   mRect.width() * i / (mBytes.length - 1);
            float right     = mRect.width() * (i + (mBytes.length))/ (mBytes.length - 1);

            float top       =  mRect.height()-((byte) (mBytes[i] + 128)) * (mRect.height() ) / 128;

            float bottom    =  mRect.height();
            canvas.drawRect(left,top,right,bottom,paint);
        }

*/
        paint.setStyle(Paint.Style.FILL);
        Rect mRect = new Rect();
        float[] mPoints=null;
        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }
        mRect.set(0, 0, (getWidth()*1), (getHeight()*1));
        for (int i = 0; i < mBytes.length - 1; i++) {
            mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);

            if( mBytes[i]<128)
            //mPoints[i * 4 + 1]=mRect.height() / 2 + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            mPoints[i * 4 + 1]= mRect.height()-(float)Math.sqrt(mBytes[i]*mBytes[i]);

            mPoints[i * 4 + 1]= mRect.height()-((float)Math.pow(Math.sqrt(mBytes[i]*mBytes[i]),3)*3*3*3/mRect.height());

            //half filter
           // if( mPoints[i * 4 + 1]>(mRect.height() / 2)) mPoints[i * 4 + 1]= mRect.height() / 2;
            mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
            mPoints[i * 4 + 3] = mRect.height();

            canvas.drawRect(mPoints[i * 4],mPoints[i * 4+1],mPoints[i * 4+2],mPoints[i * 4+3], paint);
        }
        //canvas.drawRect(0,getHeight()-2, getWidth(), getHeight(), paint);
        //canvas.drawLines(mPoints, paint);









    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == VISIBLE) {
            removeCallbacks(animateView);
            post(animateView);


        } else if (visibility == GONE) {
            removeCallbacks(animateView);

        }
    }



}