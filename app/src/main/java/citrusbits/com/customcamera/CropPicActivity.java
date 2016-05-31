package citrusbits.com.customcamera;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created by citrusbits on 1/5/2016.
 */
public class CropPicActivity extends Activity  {

    ImageView ivImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.croped_pic_activity);
        ivImageView=(ImageView)findViewById(R.id.cCircle);
        ivImageView.setImageBitmap(ZoomCropActivity.cropedBitmap);
    }
}
