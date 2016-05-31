package citrusbits.com.customcamera;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import citrusbits.com.customcamera.Service.DB.CommentsDataSource;
import citrusbits.com.customcamera.Service.DB.ModelClass;


/**
 * Created by Touseef on 2/24/2016.
 */
public class PictureViewActivity extends Activity implements View.OnClickListener {

    ImageView ivPic;

    ImageButton ibSave;
    ImageButton ibShare;
    ImageButton ibDelete;
    Button BackBtn;
    TextView tvComment;
    Bitmap bitmap;
    Uri uri;
    LinearLayout bottomLayout;
    boolean save;
    public static String imageFilePath;
    boolean funny;
    RelativeLayout topPanel1;
    File pictureFileDir;
    List<String> normalComment;
    List<String> funnyComment;
    int randNo;
    String Comment;
    CommentsDataSource commentsDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_view_layout);
        ivPic = (ImageView) findViewById(R.id.ivPic);
        bitmap = MainActivity.picCapturednRotated;
        ivPic.setImageBitmap(bitmap);
        ibShare = (ImageButton) findViewById(R.id.ib_share);
        ibShare.setOnClickListener(this);
        ibSave=(ImageButton)findViewById(R.id.ib_save);
        ibSave.setOnClickListener(this);
        ibDelete=(ImageButton)findViewById(R.id.ib_delete);
        ibDelete.setOnClickListener(this);
        BackBtn=(Button)findViewById(R.id.BackBtn);
        BackBtn.setOnClickListener(this);
        tvComment=(TextView)findViewById(R.id.tvComment);
        bottomLayout=(LinearLayout)findViewById(R.id.bottomLayout);
        topPanel1=(RelativeLayout)findViewById(R.id.topPanel1);
        SharedPreferences mPrefs = getSharedPreferences(takeAPictureActivity.MyPREFERENCES,0);
        String CODE = mPrefs.getString("CODE", "");

        commentsDataSource=new CommentsDataSource(getApplicationContext());
        commentsDataSource.open();
        normalComment=new ArrayList<String>();
        funnyComment=new ArrayList<String>();
        List<ModelClass> commentsList=commentsDataSource.findAll();
        if(commentsList!=null){
            if(commentsList.size()>0){
                for(int i=0;i<commentsList.size();i++) {
                    normalComment.add(commentsList.get(i).getTag());
                }
                for(int i=0;i<commentsList.size();i++) {
                    funnyComment.add(commentsList.get(i).getComment());
                }
            }
        }else {


            normalComment.add("Hey gorgeous ");
            normalComment.add(" Looking Awesome");
            normalComment.add("Cool dude");
            normalComment.add("Looking Handsome");
            normalComment.add("Whose That sexy beast?");
            normalComment.add("WaaaoooW");
            normalComment.add("B E A Utiful");


            funnyComment.add("Creepy Face");
            funnyComment.add("Your Face gives me cancer.");
            funnyComment.add("In the morning I will be sober but you will still be ugly.");
            funnyComment.add("Looking at this photo makes me wonder, why didn’t your parents slept early THAT night.");
            funnyComment.add("You look like Justin Bieber.");
            funnyComment.add("Please do me a favour and kill yourself.");
            funnyComment.add("Take sometime out of your daily routine and die!");
            funnyComment.add("You look like you are stoned.");
        }








        if(CODE.compareTo("YES")==0){
            funny=true;
            generateRand(funnyComment.size()-1);
            Comment=funnyComment.get(randNo);
            tvComment.setText(Comment);
        }else{
            funny=false;
            generateRand(normalComment.size()-1);
            Comment=normalComment.get(randNo);
            tvComment.setText(Comment);
        }
        save=false;
        screenShot();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ib_share) {
            openShareIntent();
           // deleteImageFile();
           // shareImage();
           // shareImage1();
        }
        if(v.getId()==R.id.ib_save){
            save=true;
           // screenShot();
            Intent intent=new Intent(this,takeAPictureActivity.class);
            startActivity(intent);
            this.finish();
        }
        if(v.getId()==R.id.ib_delete){
            deleteImageFile();
            Intent intent=new Intent(this,takeAPictureActivity.class);
            startActivity(intent);
            this.finish();
        }
        if(v.getId()==R.id.BackBtn){
            if(!save){
                deleteImageFile();
            }
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }

    }

    public void openShareIntent() {

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

//        Uri uri = Uri.parse(imageFilePath);
        // shareIntent.putExtra(Intent.EXTRA_STREAM, imageFilePath);

        shareIntent.putExtra(Intent.EXTRA_STREAM,uri );
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
        startActivity(Intent.createChooser(shareIntent, "Share Image"));

       // deleteImageFile();

    }

    public void screenShot() {
        bottomLayout.setVisibility(View.INVISIBLE);
        topPanel1.setVisibility(View.INVISIBLE);
        View view = getWindow().getDecorView().getRootView();
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
//                view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//       // return bitmap;
//        SavePictureToDirectory(bitmap);


        view.setDrawingCacheEnabled(true);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false); // clear drawing cache
        bottomLayout.setVisibility(View.VISIBLE);
        topPanel1.setVisibility(View.VISIBLE);
        SavePictureToDirectory(b);
        //return b;


    }

    public void SavePictureToDirectory(Bitmap Image) {
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        pictureFileDir = new File(sdDir, "FunkyCam");

        if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

            Log.e("Picture save", "Can't create directory to save image.");
            //return;

        }

        //Step 2. write image byte array to file
        String photoFile = "Picture_" + getDatenTime() + ".jpg";
        imageFilePath = pictureFileDir.getPath() + File.separator + photoFile;
        File pictureFile = new File(imageFilePath);
         uri=Uri.fromFile(pictureFile);

        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            Image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            // fos.write(data);
            fos.close();
//            Toast.makeText(this, "New Image saved:" + photoFile,
//                    Toast.LENGTH_LONG).show();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (Exception error) {
            Log.e("FileName", "File" + photoFile + "not saved: "
                    + error.getMessage());
//            Toast.makeText(this, "Image could not be saved.",
//                    Toast.LENGTH_LONG).show();
        }
    }

    public String getDatenTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strDate = sdf.format(c.getTime());
        Log.e("date=", strDate);
        return strDate;
    }

public void shareImage() {
    try

    {
       // File myFile = new File(Environment.getExternalStorageDirectory() + "/100MEDIA/aa.jpg");
        Intent sharingIntent = new Intent("android.intent.action.SEND");
//        Toast.makeText(this,imageFilePath,Toast.LENGTH_SHORT).show();
        sharingIntent.setType(getMimeType(Uri.parse(imageFilePath).getPath()));
        sharingIntent.putExtra("android.intent.extra.STREAM", Uri.parse(imageFilePath));
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    } catch (
            Exception e
            )

    {
        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }
}

    public String getMimeType(String filePath) {
        String type = null;
        String extension = getFileExtensionFromUrl(filePath);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public String getFileExtensionFromUrl(String url) {
        int dotPos = url.lastIndexOf('.');
        if (0 <= dotPos) {
            return (url.substring(dotPos + 1)).toLowerCase();
        }
        return "";
    }

public void shareImage1(){

    Uri uri = Uri.parse(imageFilePath);
//    Intent share = new Intent(Intent.ACTION_SEND);
//    share.putExtra(Intent.EXTRA_STREAM, uri);
//    share.setType("image/*");
//    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//    startActivity(Intent.createChooser(share, "Share image File"));




    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
    shareIntent.setType("image/*");
//    List<ResolveInfo> resInfos =
//            getPackageManager().queryIntentActivities(shareIntent,
//                    PackageManager.MATCH_DEFAULT_ONLY);
//    for (ResolveInfo info : resInfos) {
//        getApplicationContext().grantUriPermission(info.activityInfo.packageName,
//                uri,
//                Intent.FLAG_GRANT_READ_URI_PERMISSION);
//    }
    startActivity(shareIntent);
}

    public void deleteImageFile()
    {
        if(imageFilePath==null){
          //  Toast.makeText(this,"Path Null",Toast.LENGTH_SHORT).show();
            return;
        }

        File fdelete = new File( imageFilePath);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                System.out.println("file Deleted :" + imageFilePath);
               // Toast.makeText(this, "imageFilePath Deleted", Toast.LENGTH_SHORT).show();
//                imageFilePath = pictureFileDir.getPath();// + File.separator + photoFile;
//                File pictureFile = new File(imageFilePath);
               // uri=Uri.fromFile(pictureFile);

                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(imageFilePath))));


            } else {
                System.out.println("file not Deleted :" +  imageFilePath);
              //  Toast.makeText(this, "imageFilePath Not Deleted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(!save){
            deleteImageFile();
        }
        Intent intent =new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    public void generateRand(int maxLim){
        int NO=maxLim;
        Random random = new Random();
        randNo = random.nextInt(maxLim - 0) + 0;
        if(randNo<=maxLim){
            return;
        }else{
            generateRand(maxLim);
        }

    }
}



