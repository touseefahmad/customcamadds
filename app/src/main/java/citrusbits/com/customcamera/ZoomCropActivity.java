package citrusbits.com.customcamera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by citrusbits on 1/6/2016.
 */
public class ZoomCropActivity extends Activity implements  View.OnClickListener{



   SubsamplingScaleImageView myImageView;
    Button crossZoomCrop;
    ImageButton cropAcceptButton;
    ImageView cropCircleZoom;


    Bitmap bitmap;
    public static Bitmap cropedBitmap;
    int bmpWidth, bmpHeight;

    //Touch event related variables
    int touchState;
    final int IDLE = 0;
    final int TOUCH = 1;
    final int PINCH = 2;
    float dist0, distCurrent;
    String FilePath;
    int xCircle,yCircle;
    ImageView cropCircleZoom1;
    int heightOfCropCircle;
    int widthOfCropCircle;

    public int windowWidth;
    public int windowHeight;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.zoom_crop_activity);
        myImageView=(SubsamplingScaleImageView)findViewById(R.id.ZoomImage);
        cropAcceptButton=(ImageButton)findViewById(R.id.cropAcceptButton);
        cropAcceptButton.setOnClickListener(this);
        crossZoomCrop=(Button)findViewById(R.id.crossZoomCrop);
        crossZoomCrop.setOnClickListener(this);
        cropCircleZoom=(ImageView)findViewById(R.id.cropCircleZoom);
        myImageView.setDrawingCacheEnabled(true);



        windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        windowHeight=getWindowManager().getDefaultDisplay().getHeight();




        bitmap=MainActivity.picCapturednRotated;

       BitmapDrawable drawable=new BitmapDrawable(bitmap);





        cropCircleZoom.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver. OnGlobalLayoutListener() {


            public void onGlobalLayout() {
//                cropCircleZoom.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//                int[] locations = new int[2];
//                cropCircleZoom.getLocationOnScreen(locations);
                 heightOfCropCircle = cropCircleZoom.getHeight();
                 widthOfCropCircle=cropCircleZoom.getWidth();
                xCircle = cropCircleZoom.getLeft();
                yCircle = cropCircleZoom.getTop();




//                xCircle = locations[0];
//                yCircle = locations[1];
//                Toast.makeText(getApplicationContext(),"x/2 ="+xCircle/2+" y/2="+yCircle/2,Toast.LENGTH_SHORT).show();
//            }


//            pubic void onGlobalLayout() {
//                int height = imageView.getHeight();
//                int width = imageView.getWidth();
//                int x = imageView.getLeft();
//                int y = imageView.getTop();
//
//                //don't forget to remove the listener to prevent being called again by future layout events:
//                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });






//        cropCircleZoom. getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            public void onGlobalLayout() {
//                cropCircleZoom.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//                int[] locations = new int[2];
//                cropCircleZoom.getLocationOnScreen(locations);
//                xCircle = locations[0];
//                yCircle = locations[1];
//                Toast.makeText(getApplicationContext(),"x/2 ="+xCircle/2+" y/2="+yCircle/2,Toast.LENGTH_SHORT).show();
//            }
//        });

      //  storeImage(bitmap,photoFile);
        //saveImage();
        if(bitmap!=null) {
            myImageView.setImage(ImageSource.bitmap(bitmap));
        }
//        myImageView.setImage();
//        bmpWidth=bitmap.getWidth();
//        bmpHeight=bitmap.getHeight();

      /*  myImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                    // TODO Auto-generated method stub

                    float distx, disty;

                    switch(event.getAction() & MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_DOWN:
                            //A pressed gesture has started, the motion contains the initial starting location.
                            //myTouchEvent.setText("ACTION_DOWN");
                            Toast.makeText(getApplicationContext(),"Action Down",Toast.LENGTH_SHORT).show();
                            touchState = TOUCH;
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            //A non-primary pointer has gone down.
                            //   myTouchEvent.setText("ACTION_POINTER_DOWN");
                            Toast.makeText(getApplicationContext(),"ACTION_POINTER_DOWN",Toast.LENGTH_SHORT).show();
                            touchState = PINCH;

                            //Get the distance when the second pointer touch
                            distx = event.getX(0) - event.getX(1);
                            disty = event.getY(0) - event.getY(1);
                            dist0 = (float)Math.sqrt(((double)(distx * distx + disty * disty)));

                            break;
                        case MotionEvent.ACTION_MOVE:
                            //A change has happened during a press gesture (between ACTION_DOWN and ACTION_UP).
                            //myTouchEvent.setText("ACTION_MOVE");
                            Toast.makeText(getApplicationContext(),"ACTION_MOVE",Toast.LENGTH_SHORT).show();

                            if(touchState == PINCH){
                                //Get the current distance
                                distx = event.getX(0) - event.getX(1);
                                disty = event.getY(0) - event.getY(1);
                                distCurrent =(float)Math.sqrt((double)distx * distx + disty * disty);

                                drawMatrix();
                            }

                            break;
                        case MotionEvent.ACTION_UP:
                            //A pressed gesture has finished.
                            // myTouchEvent.setText("ACTION_UP");
                            Toast.makeText(getApplicationContext(),"ACTION_UP",Toast.LENGTH_SHORT).show();
                            touchState = IDLE;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            //A non-primary pointer has gone up.
                            //  myTouchEvent.setText("ACTION_POINTER_UP");
                            Toast.makeText(getApplicationContext(),"ACTION_POINTER_UP",Toast.LENGTH_SHORT).show();

                            touchState = TOUCH;
                            break;
                    }

                    return true;




























            }
        });*/


    }



//    OnTouchListener MyOnTouchListener
//            = new OnTouchListener(){
//
//        @Override
//        public boolean onTouch(View view, MotionEvent event)
//
//    };




    private void drawMatrix(){
        float curScale = distCurrent/dist0;
        if (curScale < 0.1){
            curScale = 0.1f;
        }

        Bitmap resizedBitmap;
        int newHeight = (int) (bmpHeight * curScale);
        int newWidth = (int) (bmpWidth * curScale);
        resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        //myImageView.setImageBitmap(resizedBitmap);
    }


/*public void saveImage(){

    File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File pictureFileDir = new File(sdDir, "ExtraPlte");

    if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

        Log.e("Picture save", "Can't create directory to save image.");
        return;

    }

    //Step 2. write image byte array to file
    String photoFile = "Picture_" + getDatenTime() + ".jpg";
    String imageFilePath = pictureFileDir.getPath() + File.separator + photoFile;
    File pictureFile = new File(bitmap);

    try
    {
        FileOutputStream fos = new FileOutputStream(pictureFile);
        //fos.write();
        fos.close();
        Toast.makeText(ZoomCropActivity.this, "New Image saved:" + photoFile,
                Toast.LENGTH_LONG).show();
    } catch (Exception error) {
        Log.e("FileName", "File" + photoFile + "not saved: "
                + error.getMessage());
        Toast.makeText(ZoomCropActivity.this, "Image could not be saved.",
                Toast.LENGTH_LONG).show();
    }

}*/

    public String getDatenTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(c.getTime());
        Log.e("date=", strDate);
        return strDate;
    }






    private String storeImage(Bitmap imageData, String filename) {
        //get path to external storage (SD card)
        File iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File sdIconStorageDir = new File(iconsStoragePath, "ExtraPlte");



//        File sdDir =
//        File pictureFileDir = new File(sdDir, "ExtraPlte");

        if (!sdIconStorageDir.exists() && !sdIconStorageDir.mkdirs()) {

            Log.e("Picture save", "Can't create directory to save image.");
            return "";

        }

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        try {
            String filePath = sdIconStorageDir.toString() + filename;
            FilePath=filePath;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);

            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return "";
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return "";
        }

        return FilePath;
    }

    public void retriveImageFromGallery(String Path){
//        File iconsStoragePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File sdIconStorageDir = new File(iconsStoragePath, "Path");
//
//
////        File dir = Environment.getExternalStorageDirectory();
////        File yourFile = new File(dir, "path/to/the/file/inside/the/sdcard.ext");


        File f = new File(Path);
        //mImgView1 = (ImageView)findViewById(R.id.imageView);
        Uri selectedImageUri=Uri.parse(Path);
        Bitmap bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
      //  myImageView.setImage(R.mipmap.accept_btn_red);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.crossZoomCrop){
            Intent intent=new Intent(ZoomCropActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
        if(v.getId()==R.id.cropAcceptButton){
          //  TODO crop
            //cropPicture();
            crop2();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void cropPicture(){
        Bitmap bitmapCircle= BitmapFactory.decodeResource(getResources(), R.mipmap.crop_circle);
       int cropWidth =bitmapCircle.getWidth();
        int cropHeight=bitmapCircle.getHeight();
        int WidthOfPicture=MainActivity.picCapturednRotated.getWidth();
        int HeightOfPicture=MainActivity.picCapturednRotated.getHeight();
        int xPWidth=(cropWidth+(xCircle/2));
        Bitmap cac = myImageView.getDrawingCache();

//        int heightOfCropCircle;
//        int widthOfCropCircle;

        //Bitmap original = ((BitmapDrawable) yourImageView.getDrawable()).getBitmap();
        Matrix matrix = myImageView.getMatrix();
//        myImageView.getScale();
//        matrix.setRotate(degrees);
        Log.e("ScaleX=",myImageView.getScaleX()+"");
        Log.e("ScaleY",myImageView.getScaleX() + "");

        matrix.postScale(myImageView.getScaleX(),myImageView.getScaleY());
       // Bitmap result = Bitmap.createBitmap(MainActivity, 0, 0, original.getWidth(), original.getHeight(), matrix, true);
     //   Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        Bitmap scaledBitMap=Bitmap.createBitmap(MainActivity.picCapturednRotated, 0, 0,
                MainActivity.picCapturednRotated.getWidth(),
                MainActivity.picCapturednRotated.getHeight(), matrix, true);
        Bitmap mutableBitmap=scaledBitMap.copy(Bitmap.Config.ARGB_8888, true);

        Log.e("Width of original ",WidthOfPicture+"");
        Log.e("height of original",HeightOfPicture+"");
        Log.e("scaled width",scaledBitMap.getWidth()+"");
        Log.e("scaled height",scaledBitMap.getHeight()+"");

        if((xCircle + widthOfCropCircle) > scaledBitMap.getWidth()) {

            mutableBitmap.setWidth(xCircle+ cropWidth);
            cropedBitmap = Bitmap.createBitmap(mutableBitmap, (int)cropCircleZoom.getWidth()/2,
                    (int)cropCircleZoom.getHeight()/2,cropWidth, cropHeight,matrix,true);
        } else {
            cropedBitmap = Bitmap.createBitmap(cac, xCircle/2,
                    yCircle/2,widthOfCropCircle, heightOfCropCircle,matrix,true);
            Toast.makeText(this, "Hurray Cropped", Toast.LENGTH_SHORT).show();
            Log.e("value of x=", xCircle + "");
            Log.e("value of y=", yCircle + "");
            Log.e("widthOfCropCircle=", widthOfCropCircle+"");
            Log.e("heightOfCropCircle=", heightOfCropCircle+"");



        }
//        cropedBitmap = Bitmap.createBitmap(mutableBitmap, (int)cropCircleZoom.getWidth()/2,
//                (int)cropCircleZoom.getHeight()/2,cropWidth, cropHeight,matrix,true);
        startActivity();

    }

    public void crop2(){
        //Bitmap pic =myImageView.getDrawingCache(true);
        View v= getWindow().getDecorView().getRootView();
        Bitmap pic=screenShot(v);
        xCircle=(windowWidth)/2-(widthOfCropCircle)/2;
        yCircle=(windowHeight)/2-(heightOfCropCircle)/2;

       // Bitmap myLogo = ((BitmapDrawable) pic).getBitmap();
        cropedBitmap = Bitmap.createBitmap(pic, xCircle,
                yCircle,widthOfCropCircle, heightOfCropCircle);
        Toast.makeText(getApplicationContext(),"bitmap gen & croped ",Toast.LENGTH_SHORT).show();
        startActivity();

    }



    public void startActivity(){
        Intent intent =new Intent(ZoomCropActivity.this,CropPicActivity.class);
        startActivity(intent);
    }
    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}
