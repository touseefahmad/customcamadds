package citrusbits.com.customcamera;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import Utilitypackage.ConnectionDetector;
import citrusbits.com.customcamera.Service.DB.CommentsDataSource;
import citrusbits.com.customcamera.Service.DB.ModelClass;
import citrusbits.com.customcamera.Service.Datum;
import citrusbits.com.customcamera.Service.Service;

/**
 * Created by Touseef on 2/22/2016.
 */
public class splashClass extends Activity implements Observer{
     boolean THREAD=true;
    Service service;
    ConnectionDetector cd;
    ProgressDialog pd;
    CommentsDataSource commentsDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
       /* final Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    while(THREAD) {
                        sleep(2000);
                        THREAD =false;
                        //handler.post(runner);



                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();*/
        service =new Service();
        service.addObserver(this);
        cd=new ConnectionDetector(getApplicationContext());
        commentsDataSource=new CommentsDataSource(getApplicationContext());
        commentsDataSource.open();
        List<ModelClass> commentsList=commentsDataSource.findAll();
        if(commentsList!=null) {
            if (commentsList.size() > 0) {
                Intent intent = new Intent(splashClass.this, takeAPictureActivity.class);
                startActivity(intent);
                splashClass.this.finish();
            } else {
                if (cd.isConnected()) {
                    service.call(getApplicationContext());
                }
            }
        }else {
            if(cd.isConnected()) {
                service.call(getApplicationContext());
            }
        }




    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable==service){
            if(service.getResponse().getStatus().compareTo(200)==0){
                List<Datum> comments=service.getResponse().getData();
                for(int i=0;i<comments.size();i++) {
                    //funny will contain funny comment and tag will contain normal comment
                    //DUMB base data base error
                    String funny = comments.get(i).getFunny();
                    String Tag = comments.get(i).getNormal();
                    ModelClass modelClass=new ModelClass();
                    modelClass.setComment(funny);
                    modelClass.setTag(Tag);
                    commentsDataSource.add(modelClass);
                }



            }
            Intent intent=new Intent(splashClass.this,takeAPictureActivity.class);
            startActivity(intent);
            splashClass.this.finish();
        }

    }
}
