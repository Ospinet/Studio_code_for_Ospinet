package com.ospinet.app;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.androidquery.AQuery;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

public class Record_Details extends SherlockActivity {
	String recId, memid,recDesc,recDate,recTitle,recTag,memfname,memlname;
	ProgressDialog dialog;


	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.record_details);

		recId = getIntent().getStringExtra("record_id");
        memfname = getIntent().getStringExtra("member_fname");
        memlname = getIntent().getStringExtra("member_lname");
		memid = getIntent().getStringExtra("member_id");
        recDesc = getIntent().getStringExtra("record_desc");
        recDate = getIntent().getStringExtra("record_date");
        recTitle = getIntent().getStringExtra("record_title");
        recTag = getIntent().getStringExtra("record_tag");
		dialog = new ProgressDialog(Record_Details.this);

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
			// TODO Auto-generated method stub
			Intent i = new Intent(Record_Details.this, RecordsFragment.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("member_id", memid);
			i.putExtra("EXIT", true);
			Record_Details.this.startActivity(i);

		}
}
