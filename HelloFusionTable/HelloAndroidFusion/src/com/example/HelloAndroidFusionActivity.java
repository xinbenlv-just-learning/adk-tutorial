package com.example;

import java.io.IOException;

import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import android.app.Activity;
import android.os.Bundle;

public class HelloAndroidFusionActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
        	
			FusionHandler fh = new FusionHandler(email,password);//TODO put your account name and password here, in string
			fh.runSelect("show tables");
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

}