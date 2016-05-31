package citrusbits.com.customcamera;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Touseef on 2/22/2016.
 */
public class takeAPictureActivity extends Activity implements View.OnClickListener{

    Button btnTakePic;
    Button btnTransp;
    Context context;
    public static final String MyPREFERENCES = "prefs" ;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    public static boolean CODE=false;
    int counter=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.take_a_picture_activity);
        context=this;

        btnTakePic=(Button)findViewById(R.id.btnTakePic);
        btnTransp=(Button)findViewById(R.id.btnTransp);

        btnTakePic.setOnClickListener(this);
        btnTransp.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        editor.putString("CODE", "NO");

        editor.commit();

        sharedpreferences = getSharedPreferences(takeAPictureActivity.MyPREFERENCES,0);
        String coachMark = sharedpreferences.getString("CoachMark", "");
        if(coachMark.compareTo("YES")==0){

        }else{
            showOverLay();
        }

      //  Toast.makeText(this, "NO", Toast.LENGTH_LONG).show();


        //MainActivity
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnTakePic){
            StartActivityFun();
        }
        if(v.getId()==R.id.btnTransp){

                counter++;
                //Toast.makeText(this,"clicked"+counter,Toast.LENGTH_SHORT).show();
             if( counter>3){
                    counter=0;
            }else if(counter==3){
                btnTransp.setText("Code Accepted");
                 CODE=true;
                editor.putString("CODE","YES");
                editor.commit();
                //Toast.makeText(this,"YES",Toast.LENGTH_SHORT).show();

            }

        }
    }
    public void StartActivityFun(){
        Intent intent=new Intent(takeAPictureActivity.this,MainActivity.class);
        startActivity(intent);
        takeAPictureActivity.this.finish();
    }


    private void showOverLay(){

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        dialog.setContentView(R.layout.overlay_view);

        LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.mockLayout);

        layout.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View arg0) {

                dialog.dismiss();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                editor = sharedpreferences.edit();

                editor.putString("CoachMark", "YES");

                editor.commit();

            }

        });

        dialog.show();

    }

}
