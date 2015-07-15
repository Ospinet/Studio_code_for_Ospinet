package com.ospinet.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Share extends Activity {
	String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname;
    EditText YourEmail;
    Button Cancel, Send;
    public static String user_id;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.share);

		recId = getIntent().getStringExtra("record_id");
        memfname = getIntent().getStringExtra("member_fname");
        memlname = getIntent().getStringExtra("member_lname");
		memid = getIntent().getStringExtra("member_id");
        recDesc = getIntent().getStringExtra("record_desc");
        recDate = getIntent().getStringExtra("record_date");
        recTitle = getIntent().getStringExtra("record_title");
        recTag = getIntent().getStringExtra("record_tag");
        SharedPreferences myPrefs = Share.this
                .getSharedPreferences("remember", MODE_PRIVATE);
        user_id = myPrefs.getString("userid", null);
     //   String email = myPrefs.getString("email", null);
        String fname = myPrefs.getString("fname", null);
        String lname = myPrefs.getString("lname", null);
  //      YourEmail.setText(fname +" " + lname);

        final EditText YourEmail = (EditText) findViewById(R.id.YourEmail);
        YourEmail.setText(fname +" " + lname);

        final TextView titleToChange = (TextView) findViewById(R.id.RTitle);
        titleToChange.setText(recTitle);

        final TextView tagToChange = (TextView) findViewById(R.id.RTags);
        tagToChange.setText(recTag);

        final TextView descToChange = (TextView) findViewById(R.id.RDesc);
        descToChange.setText(recDesc);

        final TextView dateToChange = (TextView) findViewById(R.id.RDate);
        dateToChange.setText(recDate);

        final TextView ownerToChange = (TextView) findViewById(R.id.OwnerName);
        ownerToChange.setText(memfname +' '+ memlname);

	}

    public void onBackPressed() {
        Intent i = new Intent(Share.this, RecordsFragment.class);
        this.startActivity(i);
        finish();
    }
    public void GetContacts(View v)
    {
        try
        {
            Intent i = new Intent(Share.this, ContactsMainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Share.this.startActivity(i);

        }
        catch(Exception ex)
        {

        }
    }
}
