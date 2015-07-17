package com.ospinet.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Notifications_Details extends Activity {
	String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.notifications);
	}

    public void onBackPressed() {
        Intent i = new Intent(Notifications_Details.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }
}
