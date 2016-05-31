package citrusbits.com.customcamera;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.system.Os;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends Activity implements Camera.PictureCallback, SurfaceHolder.Callback, View.OnClickListener {
    public static final String EXTRA_CAMERA_DATA = "camera_data";
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    private static final String KEY_IS_CAPTURING = "is_capturing";
    private static final int SELECT_PHOTO = 100;
    private String file_dj_path;
    String imageFilePath;
    BroadcastReceiver reciever;
    IntentFilter filter;


    private List<Camera.Size> mSupportedPreviewSizes;
    private Camera.Size mPreviewSize;


    //private ImageView mCameraImage;
    private ImageView pic;
    private SurfaceView mCameraPreview;
    private Button mCaptureImageButton;
    private ImageButton doneButton;
    private ImageButton btnFlash;
    private Button crossCamera;

//    private ImageButton cameraLayoutButton;
//    private ImageButton galleryLayoutButton;


    private Camera mCamera;
    private byte[] mCameraData;
    private boolean mIsCapturing;
    private boolean isCamera = true;


    public static Bitmap picCapturednRotated;


    /**********************
     * for gallery
     **********************/
//    int column_index;
//    Intent intent=null;
//    // Declare our Views, so we can access them later
//    String logo,imagePath,Logo;
//    Cursor cursor;
//    //YOU CAN EDIT THIS TO WHATEVER YOU WANT
//    private static final int SELECT_PICTURE = 1;
    File file;

    String selectedImagePath;
    //ADDED
    String filemanagerstring;
    /********************************************/
///////////////////////////////////////////////////////

    int camId;
    int camId2;
    //    int which = 0;
    private boolean flash = false;
    //private boolean front = true;
    SurfaceHolder surfaceHolder;
    private boolean front = false;
    int which = 1;


    private View.OnClickListener mCaptureImageButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            captureImage();
        }
    };

    private View.OnClickListener mRecaptureImageButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setupImageCapture();
        }
    };

    private View.OnClickListener mDoneButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCameraData != null) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_CAMERA_DATA, mCameraData);
                setResult(RESULT_OK, intent);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(reciever);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //  private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            CammeraPermissions();
        } else {

        }

        filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);


        reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                if ((intent.getAction().equals(Intent.ACTION_SCREEN_OFF))) {
                    // Toast.makeText(MainActivity.this,"Screen Off",Toast.LENGTH_SHORT).show();
//                        if (mCamera != null) {
//                            mCamera.setPreviewCallback(null);
//                            mCamera.release();
//                            mCamera = null;
//                            mCameraPreview.getHolder().getSurface().release();
//                            mCameraPreview = null;
//                            //mCameraPreview.destroyDrawingCache();)
//                        }
                }
                if ((intent.getAction().equals(Intent.ACTION_SCREEN_ON))) {
                    //Toast.makeText(MainActivity.this,"Screen ON",Toast.LENGTH_SHORT).show();
                    showFront();
//                            if(mCamera!=null) {
//
//                                mCamera.stopPreview();
//                                mCamera.release();
//                                mCamera = null;
//                            }
//                            else {
//                                // mCamera = null;
//
//                            }
//                            surfaceHolder.removeCallback(MainActivity.this);
//                            surfaceHolder=null;
//                            beforeInitializecam();
//                            initializeCamera();

                }


            }
        };
        registerReceiver(reciever, filter);

        // mCameraImage = (ImageView) findViewById(R.id.camera_image_view);
        // mCameraImage.setVisibility(View.VISIBLE);
        //  mCameraImage.setImageResource(R.mipmap.crop_circle);
        pic = (ImageView) findViewById(R.id.pic);
        crossCamera = (Button) findViewById(R.id.crossCamera);
        crossCamera.setOnClickListener(this);


        beforeInitializecam();
        //front cam=0; backcam=1;
        //  Toast.makeText(getApplicationContext(), "Front CamID=" + camId + " backCamId=" + camId2, Toast.LENGTH_LONG).show();

        mCaptureImageButton = (Button) findViewById(R.id.capture_image_button);
        mCaptureImageButton.setOnClickListener(this);
        btnFlash = (ImageButton) findViewById(R.id.btnFlash);
        btnFlash.setBackgroundResource(R.drawable.flash_icon);

        btnFlash.setOnClickListener(this);


        initializeCamera();
        //showBack();
        doneButton = (ImageButton) findViewById(R.id.done_button);
        doneButton.setOnClickListener(mCaptureImageButtonClickListener);

        getDatenTime();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        mCameraData = data;


        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File pictureFileDir = new File(sdDir, "FunkyCam");

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Log.e("Picture save", "Can't create directory to save image.");
            return;

        }

        //Step 2. write image byte array to file
        String photoFile = "Picture_" + getDatenTime() + ".jpg";
        imageFilePath = pictureFileDir.getPath() + File.separator + photoFile;
        File pictureFile = new File(imageFilePath);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);

            fos.write(data);
            fos.close();
//            Toast.makeText(MainActivity.this, "New Image saved:" + photoFile,
//                    Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Log.e("FileName", "File" + photoFile + "not saved: "
                    + error.getMessage());
//            Toast.makeText(MainActivity.this, "Image could not be saved.",
//                    Toast.LENGTH_LONG).show();
        }


        //Step 3. Get Exif Info from File path
        ExifInterface exif;
        try {
            exif = new ExifInterface(imageFilePath);
            String make = exif.getAttribute(ExifInterface.TAG_MAKE);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (front && (rotation == 6 || rotation == 1)) {
                // rotationInDegrees=-90;

                float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
                matrix = new Matrix();
                Matrix matrixMirrorY = new Matrix();
                matrixMirrorY.setValues(mirrorY);
                matrix.postConcat(matrixMirrorY);
                matrix.preRotate(270);
                matrix.preRotate(rotationInDegrees);
            } else {

                if (rotation != 0f) {
//                    float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
//                    matrix = new Matrix();
//                    Matrix matrixMirrorY = new Matrix();
//                    matrixMirrorY.setValues(mirrorY);
//                    matrix.postConcat(matrixMirrorY);
//                    matrix.preRotate(90);
                    matrix.preRotate(rotationInDegrees);
                    // matrix.preRotate(rotationInDegrees);
                } else if (rotation == 0f) {
                    if (front) {
                        float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
                        matrix = new Matrix();
                        Matrix matrixMirrorY = new Matrix();
                        matrixMirrorY.setValues(mirrorY);
                        matrix.postConcat(matrixMirrorY);
                        matrix.preRotate(270);
                        matrix.preRotate(rotationInDegrees);
                    }


                }
            }
            Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
            Bitmap adjustedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            picCapturednRotated = adjustedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupImageDisplay();


        // startNewActivity();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, takeAPictureActivity.class);
        startActivity(intent);
        this.finish();

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnFlash) {
            if (!front) {
                if (flash) {
                    flash = false;
                    setupImageCapture();
                    btnFlash.setBackgroundResource(R.drawable.flash_icon);

                } else {

                    flash = true;
                    setupImageCapture();
                    btnFlash.setBackgroundResource(R.drawable.flash_icon_auto);
                }
            }
        }


        if (v.getId() == R.id.capture_image_button) {
            Thread t = new Thread() {

                public void run() {
                    //myViewer is the SurfaceView object which uses
                    //the camera
                    if (front == true) {
                        front = false;
                        which = 1;

                    } else {
                        front = true;
                        which = 0;
                    }

                    flipit();
                }
            };
            t.start();
        }

        if (v.getId() == R.id.done_button) {
            // captures picture;
        }

//        if(v.getId()==R.id.cameraLayoutButton){
//            // TODO
//            isCamera=true;
//            initializeCamera();
//        }

//        if(v.getId()==R.id.galleryLayoutButton){
//            // TODO
//            isCamera=false;
//            if(mCamera!=null){
//                mCamera.release();
//            }
//           // getImageFromGallery();
//            selectImage();
//        }
        if (v.getId() == R.id.crossCamera) {
            Intent intent = new Intent(this, takeAPictureActivity.class);
            startActivity(intent);
            this.finish();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_IS_CAPTURING, mIsCapturing);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mIsCapturing = savedInstanceState.getBoolean(KEY_IS_CAPTURING, mCameraData == null);
        if (mCameraData != null) {
            setupImageDisplay();
        } else {
            setupImageCapture();
        }
    }

    @Override
    protected void onResume() {
//        Intent intent=new Intent(this,MainActivity.class);
//        startActivity(intent);
//        this.finish();
        super.onResume();
        boolean flip = false;
        if (mCamera != null) {

            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            flip = true;
            Log.e("resume Flip", String.valueOf(flip));

        } else {
            mCamera = null;
            Log.e("resume Flip", String.valueOf(flip));


        }
        surfaceHolder.removeCallback(this);
        surfaceHolder = null;
        beforeInitializecam();
        initializeCamera();
        if (flip) {
            //showFront();
        }

//        if (mCamera == null) {
//            try {
//                if (front) {
//                    mCamera = Camera.open(camId);
//                    mCamera.setPreviewDisplay(mCameraPreview.getHolder());
//                } else {
//                    mCamera = Camera.open(camId2);
//                    mCamera.setPreviewDisplay(mCameraPreview.getHolder());
//                }
//
//                if (mIsCapturing) {
//                    mCamera.startPreview();
//                }
//            } catch (Exception e) {
//                Toast.makeText(MainActivity.this, "Unable to open camera.", Toast.LENGTH_LONG)
//                        .show();
//            }
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Toast.makeText(this,"on Pause called",Toast.LENGTH_SHORT).show();

        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
            mCameraPreview.getHolder().getSurface().release();
            mCameraPreview = null;
            //mCameraPreview.destroyDrawingCache();)
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            try {
                // Toast.makeText(this,"Surface change called",Toast.LENGTH_SHORT).show();
                //holder=surfaceHolder.getSurface().get
                /*************************************************************************
                 * @ orientaion issue Touseef expected solution may be
                 *
                 * if there is orientation issue may be do pause an resume work here like set
                 * camera null relase surfe stop camera previw
                 * call before initialize and initialize functions
                 * this may not be a good approach but may b work do test just in case
                 *
                 *
                 ******************************************************************************/

                mCamera.setPreviewDisplay(holder);
                if (mIsCapturing) {
                    mCamera.startPreview();
                }
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Unable to start camera preview.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void captureImage() {
        // setupImageCapture();
        // Toast.makeText(this, "Image is Captured", Toast.LENGTH_SHORT).show();
        mCamera.takePicture(null, null, this);
        // mCamera.stopPreview();
        // mCameraImage.setImageResource(R.mipmap.crop_circle);
        //setupImageDisplay();
    }

    private void setupImageCapture() {
        /********************************************
         *
         * this run on ui thread is to display a
         * test toast
         ********************************************/
        //  mCameraPreview.setVisibility(View.GONE);
        mCameraPreview.setVisibility(View.VISIBLE);
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, "setupImageCaptureCalled", Toast.LENGTH_SHORT).show();
//            }
//        });

        // mCameraImage.setVisibility(View.INVISIBLE);

        //mCameraPreview.re
        //pic.setVisibility(View.INVISIBLE);

        if (mCamera == null) {
            mCamera = Camera.open(camId);
        }


        Camera.Parameters parameters = mCamera.getParameters();
        Camera.CameraInfo camInfo =
                new Camera.CameraInfo();
//        if(front){
//            camInfo= Camera.getCameraInfo(camId, camInfo);
//        }


//        android.hardware.Camera.CameraInfo camInfo =
//                new android.hardware.Camera.CameraInfo();
        //   mCamera.


        Display display = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;

                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result = 0;
        //if (camInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
        if (front) {
            Camera.getCameraInfo(camId, camInfo);
            result = (camInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
            Camera.Parameters params = mCamera.getParameters();
            params.setRotation(result);
            // mCamera.setParameters(params);
        } else {  // back-facing
            Camera.getCameraInfo(camId2, camInfo);
            result = (camInfo.orientation - degrees + 360) % 360;
            result = result;
            Camera.Parameters params = mCamera.getParameters();

            if (!flash) {
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
              //  Toast.makeText(MainActivity.this, "Flash Mode on", Toast.LENGTH_SHORT).show();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            }
            params.setRotation(result);
            mCamera.setParameters(params);
        }
        mCamera.setDisplayOrientation(result);
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);

        parameters.setPreviewSize(optimalSize.width, optimalSize.height);
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this,"Start Preview iS called",Toast.LENGTH_SHORT).show();
//            }
//        });
        mCamera.startPreview();
        // mCaptureImageButton.setText(R.string.capture_image);
        // captureImage();
        // mCaptureImageButton.setOnClickListener(mCaptureImageButtonClickListener);
    }

    private void setupImageDisplay() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(mCameraData, 0, mCameraData.length);
        // picCapturednRotated
        pic.setImageBitmap(picCapturednRotated);
        mCamera.stopPreview();
        mCameraPreview.setVisibility(View.INVISIBLE);
        pic.setVisibility(View.VISIBLE);
        //  pic.setImageBitmap(bitmap);
        // mCaptureImageButton.setText(R.string.recapture_image);
        //setupImageCapture();
        //  doneButton.setOnClickListener(mRecaptureImageButtonClickListener);
        mCamera.release();

        startNewActivity();
        // mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();


    }


    public void flipit() {
        //myCamera is the Camera object
        if (Camera.getNumberOfCameras() >= 2) {
            mCamera.stopPreview();
            mCamera.release();
            //"which" is just an integer flag
            switch (which) {
                case 0:
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);

//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "Front Cam", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });


                    break;
                case 1:
                    mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//                    MainActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(), "back cam", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });

                    break;
            }
            try {
                mCamera.setPreviewDisplay(mCameraPreview.getHolder());
                //"this" is a SurfaceView which implements SurfaceHolder.Callback,
                //as found in the code examples
                //mCamera.setPreviewCallback(null);
                //  mCamera.setPreviewCallback(getApplicationContext());
                mCamera.startPreview();
                setupImageCapture();

            } catch (IOException exception) {
                mCamera.release();
                mCamera = null;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://citrusbits.com.customcamera/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://citrusbits.com.customcamera/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://citrusbits.com.customcamera/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://citrusbits.com.customcamera/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }


    //public Bitmap rotateBitmapOrientation(Bitmap bitmap)
    /*{

        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
     //   BitmapFactory.decodeFile(file, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
       // Bitmap bm = BitmapFactory.decodeFile(file, opts);
        Bitmap bm=bitmap;
        ByteArrayInputStream bais= new ByteArrayInputStream(data);
        ExifReader reader = new ExifReader(bais);
        header = reader.extract();
        ContactsContract.Directory dir = header.getDirectory(ExifDirectory.class);
        // Read EXIF Data
      //  ExifInterface exif = new ExifInterface(bm);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }*/
    public String getDatenTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(c.getTime());
        Log.e("date=", strDate);
        return strDate;
    }

    private int exifToDegrees(int exifOrientation) {
        int val = 0;
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {

            val = 90;


        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            val = 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            val = 270;
        }
        return val;
    }

    public void startNewActivity() {
        // Toast.makeText(MainActivity.this, "Startting new activity", Toast.LENGTH_SHORT).show();

        Log.e("before StartingActivity", "call for new Activity");
        Log.e("before StartingActivity", "call for new Activity");
        Log.e("before StartingActivity", "call for new Activity");

        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
        //CropPictureActivity
        //Intent intent = new Intent(MainActivity.this, ZoomCropActivity.class);


        deleteImageFile();
        Intent intent = new Intent(MainActivity.this, PictureViewActivity.class);

        // intent.putExtra("BitmapImage", picCapturednRotated);
        this.startActivity(intent);//startActivity(intent);
        MainActivity.this.finish();
    }

    public void initializeCamera() {
        setupImageCapture();
        mIsCapturing = true;
    }

    public void getImageFromGallery() {
        // in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), 2);
    }

    public void deleteImageFile() {
        File fdelete = new File(imageFilePath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + imageFilePath);
                // Toast.makeText(this, "imageFilePath Deleted", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("file not Deleted :" + imageFilePath);
                // Toast.makeText(this, "imageFilePath Not Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //UPDATED
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                try {

                    InputStream inputStream = this.getContentResolver()
                            .openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            file);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    picCapturednRotated = BitmapFactory.decodeFile(file.getPath());
                    startNewActivity();

//                    File cropFilePath = new File(UploadImageUtility
//                            .genarateUri().getPath());
//                    filePath = cropFilePath.getName();
//                    fullPath = cropFilePath.getAbsolutePath();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Some Thing Went Wrong", Toast.LENGTH_SHORT).show();
                }


////////////////////////////////////////////////////////////////////////////////////////////////////


            }

        }

    }


    public static Uri handleImageUri(Uri uri) {
        Pattern pattern = Pattern.compile("(content://media/.*\\d)");
        if (uri.getPath().contains("content")) {
            Matcher matcher = pattern.matcher(uri.getPath());
            if (matcher.find())
                return Uri.parse(matcher.group(1));
            else
                throw new IllegalArgumentException("Cannot handle this URI");
        } else
            return uri;
    }


    public void selectImage() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            file = new File(Environment
                    .getExternalStorageDirectory(),
                    UploadImageUtility.genarateFileName());
        } else {
            file = new File(getFilesDir(), UploadImageUtility
                    .genarateFileName());
        }

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 2);
    }

    public static void copyStream(InputStream input, OutputStream output)
            throws IOException {

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    // @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
//        final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);

//        if (mSupportedPreviewSizes != null) {
//            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
//        }

        float ratio;
        if (mPreviewSize.height >= mPreviewSize.width)
            ratio = (float) mPreviewSize.height / (float) mPreviewSize.width;
        else
            ratio = (float) mPreviewSize.width / (float) mPreviewSize.height;

        // One of these methods should be used, second method squishes preview slightly
        // setMeasuredDimension(width, (int) (width * ratio));
//        setMeasuredDimension((int) (width * ratio), height);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    public void beforeInitializecam() {
        mCameraPreview = (SurfaceView) findViewById(R.id.preview_view);
        surfaceHolder = mCameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        //surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        int totalCameras = Camera.getNumberOfCameras();
        camId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        camId2 = Camera.CameraInfo.CAMERA_FACING_BACK;
        Log.d("Front cam", camId + "");
        Log.d("Backcam", camId2 + "");
        //mCamera.release();
        mCamera = Camera.open(camId2);

        mIsCapturing = true;
    }

    public void showFront() {
        Thread t = new Thread() {

            public void run() {
                //myViewer is the SurfaceView object which uses
                //the camera
//                if(front==true){
//                    front=false;
//                    which=1;

                //}else{
                front = true;
                which = 0;
                // }

                flipit();
            }
        };
        t.start();
    }


    public void showBack() {
        Thread t = new Thread() {

            public void run() {
                //myViewer is the SurfaceView object which uses
                //the camera
//                if(front==true){
                front = false;
                which = 1;

                //}else{
//                front=true;
//                which=0;
                // }

                flipit();
            }
        };
        t.start();
    }


    private void CammeraPermissions() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Storage");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read Storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

        //    insertDummyContact();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        int res = checkCallingOrSelfPermission(permission);
        if (!(ContextCompat.checkSelfPermission(getApplicationContext(),permission)== PackageManager.PERMISSION_GRANTED)) {
            return true;
        }
        permissionsList.add(permission);
        // Check for Rationale Option
        if (!shouldShowRequestPermissionRationale(permission))
            return false;
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    //insertDummyContact();
                } else {
                    // Permission Denied
                    //TODO
                    Intent intent= new Intent(MainActivity.this,PermissionsScreenActivity.class);
                    startActivity(intent);
                    MainActivity.this.finish();

                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
