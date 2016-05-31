package citrusbits.com.customcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Touseef on 3/4/2016.
 */
public class PermissionsScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permissions_screen);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent=new Intent(this,takeAPictureActivity.class);
        startActivity(intent);
        PermissionsScreenActivity.this.finish();
    }
}
