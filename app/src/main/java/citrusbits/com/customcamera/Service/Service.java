package citrusbits.com.customcamera.Service;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import Utilitypackage.GsonRequest;
import citrusbits.com.customcamera.DataObservor;
import citrusbits.com.customcamera.R;
import citrusbits.com.customcamera.SingletonClass;

/**
 * Created by Touseef on 5/24/16.
 */
public class Service extends DataObservor {
    ServiceResponse response;

    public ServiceResponse getResponse() {
        return response;
    }

    public void call(Context context){

        String url=context.getResources().getString(R.string.url);

        Log.d("URL String:", url);

        RequestQueue requestQueue = SingletonClass.getInstance().getRequestQueue();
        GsonRequest<ServiceResponse> request = new GsonRequest<ServiceResponse>(
                url, ServiceResponse.class, null,
                successListener(), errorListener());
        requestQueue.add(request);

    }



    private Response.Listener<ServiceResponse> successListener() {
        // TODO Auto-generated method stub
        return new Response.Listener<ServiceResponse>() {

            @Override
            public void onResponse(ServiceResponse response) {
                // TODO Auto-generated method stub
                try{
                    Service.this.response=response;
                }catch(Exception e)
                {
                    Log.e("Service Exception",e.toString());
                    Service.this.response=new ServiceResponse();
                    Service.this.response.setStatus(0);

                }
                triggerObservers();
            }
        };
    }

    private Response.ErrorListener errorListener() {
        // TODO Auto-generated method stub
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("Service  Exception", error.toString());
                Service.this.response= new ServiceResponse();
                Service.this.response.setStatus(0);
                Service.this.response.setStatus(0);
                triggerObservers();

            }
        };
    }

}
