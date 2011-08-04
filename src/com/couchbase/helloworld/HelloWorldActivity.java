package com.couchbase.helloworld;

import org.json.JSONException;

import com.couchbase.libcouch.AndCouch;
import com.couchbase.libcouch.CouchDB;
import com.couchbase.libcouch.ICouchClient;

import android.app.Activity;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.webkit.WebView;

public class HelloWorldActivity extends Activity {
	
	private ServiceConnection couchServiceConnection;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        couchServiceConnection = CouchDB.getService(getBaseContext(), null, "release-0.1", mCallback);
    }
    private final ICouchClient mCallback = new ICouchClient.Stub() {
		
		@Override
		public void installing(int completed, int total) throws RemoteException {
			// todo nothing
		}
		
		@Override
		public void exit(String error) throws RemoteException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void couchStarted(String host, int port) throws RemoteException {
			// TODO Auto-generated method stub
			WebView webView = new WebView(HelloWorldActivity.this);
			String startUrl = "http://" + host + ":" + Integer.toString(port) + "/grocery-sync/_design/grocery/index.html";
			String sessionUrl = "http://" + host + ":" + Integer.toString(port) + "/_session";
			String syncpoint = "http://couchbase.ic.ht/grocery-sync";
			String localdb = "grocery-sync";
			// trigger replication with this json
			String startRep = "{\"source\":\""+ syncpoint +"\", \"target\":\""+ localdb +"\", \"create_target\":true}";
			String replicateUrl = "http://" + host + ":" + Integer.toString(port) + "/_replicate";
			try {
				AndCouch req = AndCouch.post(replicateUrl, startRep);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			setContentView(webView);
			webView.loadUrl(startUrl);
		}
	};
}