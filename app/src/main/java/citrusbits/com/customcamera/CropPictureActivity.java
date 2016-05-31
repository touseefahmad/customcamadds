package citrusbits.com.customcamera;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by citrusbits on 1/1/2016.
 */
public class CropPictureActivity extends Activity implements View.OnClickListener {

    ImageButton cropAcceptButton;
    ImageButton crossCropPicture;
   Button crossCameraCrop;

    ImageView pictureView;
    ImageView ivCropView;

    CanvasView canvasView;



    public static Bitmap bitmap;
    public static  Bitmap bottomImage;
    public static Bitmap cropedBitmap;

    public static int bottomWidth;
    public static int bottomHeight;
    public static int windowWidth;
    public static int windowHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.crop_activity);
        cropAcceptButton=(ImageButton)findViewById(R.id.cropAcceptButton);
        crossCropPicture=(ImageButton)findViewById(R.id.CrossCropImage);
        crossCameraCrop=(Button)findViewById(R.id.crossCameraCrop);
        crossCameraCrop.setOnClickListener(this);

        cropAcceptButton.setOnClickListener(this);
        crossCropPicture.setOnClickListener(this);
        pictureView=(ImageView)findViewById(R.id.pictureView);
        bitmap=MainActivity.picCapturednRotated;
        windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        windowHeight=getWindowManager().getDefaultDisplay().getHeight();
        canvasView = (CanvasView) findViewById(R.id.SignatureCanvasView);
        Log.e("Windows Width",windowWidth+"");
        Log.e("Windows Height", windowHeight + "");
        pictureView.setImageBitmap(bitmap);
        bottomImage= BitmapFactory.decodeResource(getResources(),R.mipmap.camera_bottom);
        bottomWidth=bottomImage.getWidth();
        bottomHeight=bottomImage.getHeight();



        //  MainActivity.picCapturednRotated.recycle();

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.cropAcceptButton){
            Toast.makeText(getApplicationContext(), "mx=" + (int) canvasView.mX + " my=" +
                    (int) canvasView.mY, Toast.LENGTH_SHORT).show();
            cropedBitmap=canvasView.cropped();
            // pictureView.setImageBitmap(cropedBitmap);
            Intent intent =new Intent(CropPictureActivity.this,CropPicActivity.class);
            startActivity(intent);

        }
        if(v.getId()==R.id.crossCameraCrop){
            Intent intent=new Intent(CropPictureActivity.this, MainActivity.class);
            startActivity(intent);
            CropPictureActivity.this.finish();
        }

    }







//    public void drawbitmap(){
//        Bitmap circularBitmap= BitmapFactory.decodeResource(getResources(),
//                R.mipmap.crop_circle);
//        bitmap= Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),
//                Bitmap.Config.ARGB_8888);
//        int bitmapHeight=bitmap.getHeight();
//        int bitmapWidth=bitmap.getWidth();
////        ivCropView.requestLayout();
////        image_view.getLayoutParams().height = 20;
//
//        int circularHeight=circularBitmap.getHeight();
//        int circularWidth=circularBitmap.getWidth();
//        Log.e("CircularHeight=",circularHeight+"");
//        Log.e("CircularWidth=",circularWidth+"");
//        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas=new Canvas(mutableBitmap);
//
//        Paint maskPaint=new Paint();
//        maskPaint.setAntiAlias(true);
//
//        maskPaint.setColor(Color.BLUE);
//        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        Bitmap mutableCircle=circularBitmap.copy(Bitmap.Config.ARGB_8888,true);
//if(NXcenterOfCircle>0 && NYcenterOfCircle>0){
//    canvas.drawBitmap(mutableCircle, NXcenterOfCircle / 2, NYcenterOfCircle / 2, maskPaint);
//   // ivCropView.setImageBitmap(null);
//    ivCropView.setImageBitmap(mutableCircle);
//}else {
//    canvas.drawBitmap(mutableCircle, circularWidth / 2, circularWidth / 2, maskPaint);
//}
//        ivCropView.setImageBitmap(mutableCircle);
//        final int afterHeight= ivCropView.getMeasuredHeight();
//        int AfterWidth= ivCropView.getMeasuredWidth();
//
//
//        vto= ivCropView.getViewTreeObserver();
//        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            public boolean onPreDraw() {
//                ivCropView.getViewTreeObserver().removeOnPreDrawListener(this);
//
//                int AfterHeight = ivCropView.getMeasuredHeight();
//                int AfterWidth = ivCropView.getMeasuredWidth();
//                XcenterOfCircle=AfterWidth/2;
//                YcenterOfCircle=AfterHeight/2;
//                Log.e("ImageViewAfterHeigt", AfterHeight + "");
//                Log.e("ImageViewAfterWidth", AfterWidth + "");
//
//                return true;
//            }
//        });
//
//
//
//// DST_IN = Whatever was there, keep the part that overlaps
//// with what I'm drawing now
////        Paint maskPaint = new Paint();
////        maskPaint.setXfermode(
////                new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
////        canvas.drawBitmap(mask, 0, 0, maskPaint);
//
//
////        Bitmap bitmap = Bitmap.createBitmap(
////                original.getWidth(), original.getHeight(),
////                Bitmap.Config.ARGB_8888);
////        Bitmap mutableBitmap = original.copy(Bitmap.Config.ARGB_8888, true);
////     Canvas canvas = new Canvas(original);
////
////// Draw the original bitmap (DST during Porter-Duff transfer)
////        canvas.drawBitmap(original, 0, 0, null);
//
//// DST_IN = Whatever was there, keep the part that overlaps
//// with what I'm drawing now
////        Paint maskPaint = new Paint();
////        maskPaint.setXfermode(
////                new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
////        canvas.drawBitmap(original, 0, 0, maskPaint);
//        //canvas.drawBitmap();
//    }


}
