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
public class MusicVisualizer extends View {

   // Random random = new Random();
    Paint paint = new Paint();
    private Runnable animateView = new Runnable() {
        @Override
        public void run() {

            //run every 150 ms
            postDelayed(this, 120);
            invalidate();
        }
    };


    public MusicVisualizer(Context context) {
        super(context);
        new MusicVisualizer(context, null);
    }

    public MusicVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);

        //start runnable
        removeCallbacks(animateView);
        post(animateView);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

       paint.setStyle(Paint.Style.FILL);

        byte[] mBytes;
       mBytes=MusicPlayer.getmBytes();
        if (mBytes == null) {
            return;
        }
         Rect mRect = new Rect();
        float[] mPoints=new float[4];

        if (mPoints == null || mPoints.length < mBytes.length * 4) {
            mPoints = new float[mBytes.length * 4];
        }

        mRect.set(0, 0, getWidth(), getHeight());
        for (int i = 0; i < mBytes.length - 1; i=i+(mBytes.length/4)) {
            float left      = mRect.width() * i / (mBytes.length - 1);
            float top       = mRect.height() / 2 + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            float right     = (mRect.width() * (i + (mBytes.length/4))/ (mBytes.length - 1))-3;
            float bottom    = mRect.height() ;
            canvas.drawRect(left,top,right,bottom,paint);
        }



    }



    /**
     * Links the visualizer to a player
    // * @param AudioSessionId - MediaPlayer ID instance to link to
     *
    public void link(int AudioSessionId)
    {

        if(AudioSessionId == 0)
        {
            throw new NullPointerException("Cannot link to null MediaPlayer");
        }

        if(mVisualizer==null)
        {
            Log.e("aa4","new binder for visulizer inslizing");
            // Create the Visualizer object and attach it to our media player.
            mVisualizer = new Visualizer(AudioSessionId);
            mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);

            // Pass through Visualizer data to VisualizerView
            Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
            {
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                                  int samplingRate)
                {
                    updateVisualizer(bytes);
                }

                @Override
                public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                             int samplingRate)
                {
                    //  updateVisualizerFFT(bytes);
                }
            };

            mVisualizer.setDataCaptureListener(captureListener,
                    Visualizer.getMaxCaptureRate()/2 , true, true);


            linked=true;
            mVisualizer.setEnabled(true);
        }else
            {
                throw new NullPointerException("error some ware link to null MediaPlayer");
            }


    }

     /*  mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener()
     {
     @Override
     public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
     int samplingRate)
     {

     }
     @Override
     public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
     int samplingRate)
     {
     //  updateVisualizerFFT(bytes);
     }
     };);*/



    public void setColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    //get all dimensions in dp so that views behaves properly on different screen resolutions
    private int getDimensionInPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
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