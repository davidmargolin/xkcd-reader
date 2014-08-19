package com.tod.android.xkcdreader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Margolin on 8/19/2014.
 */
public class ShowGifView  extends View {

    // Set true to use decodeStream
    // Set false to use decodeByteArray
    private static final boolean DECODE_STREAM = false;
    Loder task;
    InputStream gifInputStream;
    private Movie gifMovie;
    private int movieWidth, movieHeight;
    private long movieDuration;
    private long mMovieStart;

    static String gifURL;

    public ShowGifView(Context context, String a) {
        super(context);
        gifURL = a;
        init(context);
    }

    public ShowGifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShowGifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void stopthread(){
        task.cancel(true);
    }

    private void init(final Context context) {
        setFocusable(true);

        gifMovie = null;
        movieWidth = 0;
        movieHeight = 0;
        movieDuration = 0;
        task = new Loder();
        task.execute(new String[] { gifURL });

    }

    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(parentWidth, parentWidth+1/movieWidth+1/movieHeight+1);
        setMeasuredDimension(parentWidth, parentHeight);

    }

    public int getMovieWidth() {
        return movieWidth;
    }

    public int getMovieHeight() {
        return movieHeight;
    }

    public long getMovieDuration() {
        return movieDuration;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        long now = android.os.SystemClock.uptimeMillis();
        if (mMovieStart == 0) { // first time
            mMovieStart = now;
        }

        if (gifMovie != null) {

            int dur = gifMovie.duration();
            if (dur == 0) {
                dur = 1000;
            }

            int relTime = (int) ((now - mMovieStart) % dur);

            gifMovie.setTime(relTime);
            if (movieWidth>movieHeight) {
                canvas.scale((float) this.getWidth() / (float) movieWidth, (float) this.getWidth() / (float) movieWidth);
            }else{
                canvas.scale((float) this.getHeight() / (float) movieHeight, (float) this.getHeight() / (float) movieHeight);
            }

            gifMovie.draw(canvas, 0, 0);

            invalidate();

        }

    }

    private class Loder extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            URL gifURL;
            try {
                gifURL = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) gifURL
                        .openConnection();

                gifInputStream = connection.getInputStream();

                if (DECODE_STREAM) {
                    gifMovie = Movie.decodeStream(gifInputStream);
                } else {
                    byte[] array = streamToBytes(gifInputStream);
                    gifMovie = Movie.decodeByteArray(array, 0, array.length);
                }
                movieWidth = gifMovie.width();
                movieHeight = gifMovie.height();
                movieDuration = gifMovie.duration();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            invalidate();
            requestLayout();

        }
    }

}
