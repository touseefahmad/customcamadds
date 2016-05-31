package citrusbits.com.customcamera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by citrusbits on 1/4/2016.
 */
public class CanvasView extends View {

    Context c;
    Path mPath;
    Bitmap mBitmap;
    Canvas mCanvas;
    Paint mPaint;

    int width;
    int height;

    int bitMapWidth;
    int bitMapHeight;

    int topBitMapHeight;

    public static float mX, mY;
    private static final float TOLERANCE = 5;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;


        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);

    }


    // override onSizeChanged

    @Override

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);


        // your Canvas will draw onto the defined Bitmap

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);
    }
    // override onDraw

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // draw the mPath with the mPaint on the canvas when onDraw

        //canvas.drawPath(mPath, mPaint);


        float w, h, cx, cy;
        w = getWidth();
        h = getHeight();
        cx = w / 2;
        cy = h / 2;

        //  canvas.drawRect(0, 0, w, h, mPaint);

        Bitmap myBitmap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.crop_circle);

        int widthOfDrawable = myBitmap.getWidth();
        int heightOfDrawable = myBitmap.getHeight();

        bitMapWidth = widthOfDrawable;
        bitMapHeight = heightOfDrawable;


        Bitmap topBitmap = BitmapFactory.decodeResource(
                getResources(),
                R.mipmap.crop_circle);

        int topBitMapHeight = topBitmap.getHeight();




        Log.e("Width of Drawable", widthOfDrawable + "");
        Log.e("value of y", mY + "");

        if (mX / 2 < CropPictureActivity.windowWidth && mX > 0) {
            Log.d("Sizes", "mx" + mX + "window=" + CropPictureActivity.windowWidth);
            canvas.drawBitmap(myBitmap, mX / 2, mY / 2, null);
            Toast.makeText(c, mX + " and " + mY, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(c, mX + " is greater than " + CropPictureActivity.windowWidth, Toast.LENGTH_SHORT).show();
        }


    }

    // when ACTION_DOWN start touch according to the x,y values

    private void startTouch(float x, float y) {

        mPath.moveTo(x, y);

        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values

    private void moveTouch(float x, float y) {

        float dx = Math.abs(x - mX);

        float dy = Math.abs(y - mY);

        if (dx >= TOLERANCE || dy >= TOLERANCE) {

            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);

            mX = x;

            mY = y;

        }

    }

    public void clearCanvas() {

        mPath.reset();
        invalidate();


    }
// when ACTION_UP stop touch

    private void upTouch() {

        mPath.lineTo(mX, mY);

    }

//override the onTouchEvent

    @Override

    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();

        float y = event.getY();
        // Log.e("value of y",y);

        // x is touch point
        //MainActivity.windowWidth is screen width
        //mBitmap.getWidth is width of bitmap
        // in last && conditon i check that width half wich is radius +touch of x-axis does not
        // exceed screen max width
        // first condition check that if the touch is not less than 0
        // first con is lower limit and last checks the upper limit
        if (x == 0 && y == 0) {
            x = CropPictureActivity.windowWidth / 2;
            y = CropPictureActivity.windowHeight / 2;
        }

        Log.e("value of x=", x + "");
        Log.e("value of x/2", (x / 2) + "");
        Log.e("value of y=", y + "");
        Log.e("value of y/2", (y / 2) + "");
        Log.e("value (touch)+radius", "" + "bitmap width/2=" + mBitmap.getWidth() / 2 + "value" + (mBitmap.getWidth() / 2 + (x / 2)));
        Log.e("bitMapWidth", bitMapWidth + "");
        Log.e("bitMapHeight", bitMapHeight + "");
        Log.e("croppic window width", "" + CropPictureActivity.windowWidth);
        Log.e("croppic window Height", "" + CropPictureActivity.windowHeight);


        if ((x / 2) < CropPictureActivity.windowWidth && x > 0 && (CropPictureActivity.windowWidth - x) >= (bitMapWidth / 2)

                && y / 2 < CropPictureActivity.windowHeight && y >= 0
                && (CropPictureActivity.windowHeight - CropPictureActivity.bottomHeight - y) >= (bitMapHeight / 2)
                && y>(topBitMapHeight+CropPictureActivity.bottomHeight/2)

                ) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    startTouch(x, y);

                    invalidate();

                    break;

                case MotionEvent.ACTION_MOVE:

                    moveTouch(x, y);

                    invalidate();

                    break;

                case MotionEvent.ACTION_UP:

                    upTouch();

                    invalidate();

                    break;

            }
        }

        return true;

    }

    public Bitmap cropped() {

        // left top width height;
      //  Bitmap croppedBitmap = Bitmap.createBitmap(CropPictureActivity.bitmap, (int)mX/2, (int)mY/2,bitMapWidth, bitMapHeight);

        Bitmap croppedBitmap = Bitmap.createBitmap(CropPictureActivity.bitmap, (int)mX/2, (int)mY/2,bitMapWidth, bitMapHeight);
        Log.e("Cropped left",mX/2+"");
        Log.e("Cropped height",mY/2+"");
        Log.e("bitMapWidth",bitMapWidth+"");
        Log.e("bitMapHeight",bitMapHeight+"");
        return  croppedBitmap;

    }


}


