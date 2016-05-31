package Utilitypackage;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	private Context _context;
	public ConnectionDetector(Context context)
	{
		this._context=context;
	}
	public boolean isConnectionToInternet()
	{
		ConnectivityManager connectivity=(ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity!=null)
		{
			NetworkInfo[] info=connectivity.getAllNetworkInfo();
			if(info!=null)
			{
				for(int i=0;i<info.length;i++)
				{
					if(info[i].getState()==NetworkInfo.State.CONNECTED)
						return true;
				}
			}
		}return false;
	}
	public boolean isConnected(){
		boolean networkStatus=false;
		ConnectivityManager connMgr=(ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

		final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // check for mobile data
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
if(wifi ==null){
	networkStatus= false;
}else if( wifi.isConnected()  ) {
        	networkStatus= true;
        } else if( mobile!=null) {
        	if(mobile.isConnected()){
        	networkStatus= true;
        	}else{
        		networkStatus= false;
        	}
        } else {
        	networkStatus= false;
        }

        return networkStatus;
	}
	

}

