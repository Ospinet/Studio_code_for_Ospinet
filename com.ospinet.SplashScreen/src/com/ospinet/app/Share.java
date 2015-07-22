package com.ospinet.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class Share extends Activity {
	String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname,toemail,friends_ids;
    EditText YourEmail,ToEmail;
    Button btnCancel, btnShare;
    public static String user_id;
    ProgressDialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.share);

        toemail = getIntent().getStringExtra("friends_email_ids");
        friends_ids = getIntent().getStringExtra("friends_ids");
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

        ArrayList<NameValuePair> record_details = new ArrayList<NameValuePair>();
        SharedPreferences myrec = Share.this
                .getSharedPreferences("remember", Context.MODE_PRIVATE);
        String userId = myrec.getString("userid", null);
        record_details.add(new BasicNameValuePair("SrecId",recId));
        record_details.add(new BasicNameValuePair("SmemFname",memfname));
        record_details.add(new BasicNameValuePair("SmemLname",memlname));
        record_details.add(new BasicNameValuePair("SmemId",memid));
        record_details.add(new BasicNameValuePair("SrecDesce",recDesc));
        record_details.add(new BasicNameValuePair("SrecDate",recDate));
        record_details.add(new BasicNameValuePair("SrecTitle",recTitle));
        record_details.add(new BasicNameValuePair("SrecTag",recTag));

        if(toemail != null){
           // String[] splits = toemail.split(";");
            final EditText ToEmail = (EditText) findViewById(R.id.ToEmail);
            ToEmail.setText(toemail);

            System.out.println(record_details);
        }
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

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnShare = (Button) findViewById(R.id.btnShare);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent records = new Intent(Share.this, ShareRecordsFragment.class);
                Share.this.startActivity(records);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toemail != null)
                {
                   // new Sharerecord().execute();
                    Toast.makeText(Share.this, "Record share successfully ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Share.this, "Select at lest one contact", Toast.LENGTH_LONG).show();
                }
            }
        });

	}
    public void onBackPressed() {
        Intent i = new Intent(Share.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }

    public class Sharerecord extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String retstring = "";
            try {
                ArrayList<NameValuePair> share_record = new ArrayList<NameValuePair>();
                SharedPreferences myPrefs = Share.this
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
                String userId = myPrefs.getString("userid", null);
                share_record.add(new BasicNameValuePair("from_id",userId));
                share_record.add(new BasicNameValuePair("to_id",friends_ids));
                share_record.add(new BasicNameValuePair("record_id",recId));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/share_records",
                                share_record);
                retstring = response.toString();
            } catch (Exception io) {

            }
            onBackPressed();
            Toast.makeText(Share.this, "Record share successfully ", Toast.LENGTH_LONG).show();
            return retstring;

        }
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
