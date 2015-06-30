package com.ospinet.app;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.ospinet.app.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;
import android.view.KeyEvent;
import android.app.SearchManager;
import android.widget.SearchView.OnQueryTextListener;

public class Records_Home extends SherlockActivity implements ISideNavigationCallback {
    ProgressDialog dialog;
    String memid;
    ListView recordList;
    TextView txtNoRec;
    Button btnNew;
    private SideNavigationView sideNavigationView;
    private SearchView search;
    ArrayList<record> arrrecords;
    public static RecordAdapter rad;
    public static String strQuery;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.records);
        showActionBar();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

        memid = getIntent().getStringExtra("member_id");
        dialog = new ProgressDialog(Records_Home.this);
        txtNoRec = (TextView) findViewById(R.id.txt_home_norec);
        btnNew= (Button) findViewById(R.id.btnNewAdd);
        txtNoRec.setVisibility(View.GONE);
        btnNew.setVisibility(View.GONE);

        //super.onCreate(savedInstanceState);

        recordList = (ListView) findViewById(R.id.recordList);
        arrrecords = new ArrayList<record>();
        new Loadrecord().execute();
    }

    public class Searchrecord extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Searching...");
            dialog.show();
            dialog.setCancelable(false);
        }
        @Override
        protected String doInBackground(String... params) {
            String retstring = "";
            try {
                ArrayList<NameValuePair> searchParam = new ArrayList<NameValuePair>();
                searchParam.add(new BasicNameValuePair("search", strQuery));
                searchParam.add(new BasicNameValuePair("mem_id", memid));
                searchParam.add(new BasicNameValuePair("type", "title"));

                String response = CustomHttpClient.executeHttpPost(
                        "http://ospinet.com/app_ws/android_app_fun/search_records",
                        searchParam);

                retstring = response.toString();

            }catch (Exception io) {

                Toast.makeText(getBaseContext(), "Sorry no record found",
                        Toast.LENGTH_SHORT).show();

            }
            return retstring;
        }
        @Override
        protected void onPostExecute(String retstring) {
            if (dialog.isShowing())
                dialog.dismiss();
            JSONObject jsonResponse = null;
            try {
                String id="";
                String Description = "";
                String Date ="";
                String Title ="";
                String Tag = "";
                String AttachmentPath = "";
                jsonResponse = new JSONObject(retstring);
                int flag=0;
                JSONArray jsonMainNode = jsonResponse
                        .optJSONArray("result");
                arrrecords.clear();
                for (int i = 0; i < jsonMainNode.length(); i++) {

                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    id = jsonChildNode.optString("id");
                    Description = jsonChildNode.optString("description");
                    Title =  jsonChildNode.optString("title");
                    Date =  jsonChildNode.optString("date");
                    Tag = jsonChildNode.optString("tagname");
                    AttachmentPath = jsonChildNode.optString("filename");
                    record r = new record();
                    r.setdescription(Description);
                    r.setattachment_path(AttachmentPath);
                    r.setid(id);
                    r.setrecord_date(Date);
                    r.settag(Tag);
                    r.settitle(Title);
                    arrrecords.add(r);
                    flag=1;
                }
                if(flag == 0){
                    Toast.makeText(getBaseContext(), "No result found",
                            Toast.LENGTH_SHORT).show();
                    new Loadrecord().execute();
                }
                txtNoRec.setVisibility(View.GONE);
                btnNew.setVisibility(View.GONE);
                recordList.setVisibility(View.VISIBLE);

                rad = new RecordAdapter(Records_Home.this,
                        arrrecords);

                recordList.setAdapter(rad);
                rad.notifyDataSetChanged();
                recordList.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        TextView txtId = (TextView) arg1
                                .findViewById(R.id.txtId);
                        final String recId = txtId.getText().toString();
                        final Dialog builder = new Dialog(Records_Home.this);

                        final View view = Records_Home.this.getLayoutInflater().inflate(
                                R.layout.record_options, null);
                        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        builder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        builder.setContentView(view);
                        Button btnDelete = (Button) builder.findViewById(R.id.btn_Delete);
                        Button btnEdit = (Button) builder.findViewById(R.id.btn_Edit);
                        Button btnView = (Button) builder.findViewById(R.id.btn_view);

                        builder.show();


                        btnDelete.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                AlertDialog.Builder deleteConfirmDialog = new AlertDialog.Builder(
                                        Records_Home.this);
                                deleteConfirmDialog
                                        .setTitle("Delete Record");
                                // myAlertDialog.setMessage("Please enter new password");
                                deleteConfirmDialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(
                                                    DialogInterface arg0,
                                                    int arg1) {
                                                new DeleteRecord()
                                                        .execute(recId);
                                            }
                                        });
                                deleteConfirmDialog.setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(
                                                    DialogInterface arg0,
                                                    int arg1) {

                                            }
                                        });
                                deleteConfirmDialog.show();

                            }

                        });
                        btnEdit.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Records_Home.this, Record_Edit.class);
                                i.putExtra("record_id", recId);
                                i.putExtra("member_id", memid);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("EXIT", true);

                                Records_Home.this.startActivity(i);
                                //finish();
                            }
                        });
                        btnView.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Records_Home.this, Record_View.class);
                                i.putExtra("record_id", recId);
                                i.putExtra("member_id", memid);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("EXIT", true);

                                Records_Home.this.startActivity(i);
                                finish();
                            }
                        });

                    }

                });

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public class Loadrecord extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            if(flag != 2){
            super.onPreExecute();
            dialog.setMessage("Please Wait..");
            dialog.show();
            dialog.setCancelable(false);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String retstring = "";
            try {

                String response = CustomHttpClient
                        .executeHttpGet("http://ospinet.com/app_ws/android_app_fun/get_members_details?member_id="
                                + memid);
                retstring = response.toString();

            } catch (Exception io) {

            }
            return retstring;

        }

        @Override
        protected void onPostExecute(String retstring) {
            if (dialog.isShowing())
                dialog.dismiss();
            JSONObject jsonResponse = null;
            try {
                String id="";
                String Description = "";
                String Date ="";
                String Title ="";
                String Tag = "";
                String AttachmentPath = "";
                jsonResponse = new JSONObject(retstring);
                int flag=0;
                JSONArray jsonMainNode = jsonResponse
                        .optJSONArray("member_detail");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    if(i!=0)
                    {
                        JSONArray jArray = jsonMainNode.getJSONArray(i);
                        for(int j=0;j<jArray.length();j++)
                        {
                            JSONObject jsonChildNode = jArray.getJSONObject(j);
                            id = jsonChildNode.optString("id");
                            Description = jsonChildNode.optString("description");
                            Title =  jsonChildNode.optString("title");
                            Date =  jsonChildNode.optString("date");
                            Tag = jsonChildNode.optString("tagname");
                            AttachmentPath = jsonChildNode.optString("filename");
                            record r = new record();
                            r.setdescription(Description);
                            r.setattachment_path(AttachmentPath);
                            r.setid(id);
                            r.setrecord_date(Date);
                            r.settag(Tag);
                            r.settitle(Title);
                            arrrecords.add(r);
                            flag=1;
                        }
                    }
                    else
                    {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    }
                    if(flag==0)
                    {
                        txtNoRec.setVisibility(View.VISIBLE);
                        btnNew.setVisibility(View.VISIBLE);
                        recordList.setVisibility(View.GONE);
                    }
                    else
                    {
                        txtNoRec.setVisibility(View.GONE);
                        btnNew.setVisibility(View.GONE);
                        recordList.setVisibility(View.VISIBLE);
                    }
					/*id = jsonChildNode.optString("id");
					fname = jsonChildNode.optString("fname");
					lname = jsonChildNode.optString("lname");
					gender = jsonChildNode.optString("gender");
					age = jsonChildNode.optString("age");
					email = jsonChildNode.optString("email");

					// code to set text and button
					edtFname.setText(fname);
					edtLname.setText(lname);
					edtEmail.setText(email);
					edtAge.setText(age);
					if (gender.toLowerCase().equals("male")) {
						btnMale.setBackgroundColor(Color.WHITE);
						btnFemale
								.setBackgroundResource(R.drawable.button_custom);

					} else {
						btnFemale.setBackgroundColor(Color.WHITE);
						btnMale.setBackgroundResource(R.drawable.button_custom);

					}*/
                }
                rad = new RecordAdapter(Records_Home.this,
                        arrrecords);

                recordList.setAdapter(rad);
                recordList.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        TextView txtId = (TextView) arg1
                                .findViewById(R.id.txtId);
                        final String recId = txtId.getText().toString();
                        final Dialog builder = new Dialog(Records_Home.this);

                        final View view = Records_Home.this.getLayoutInflater().inflate(
                                R.layout.record_options, null);
                        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        builder.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        builder.setContentView(view);
                        Button btnDelete = (Button) builder.findViewById(R.id.btn_Delete);
                        Button btnEdit = (Button) builder.findViewById(R.id.btn_Edit);
                        Button btnView = (Button) builder.findViewById(R.id.btn_view);

                        builder.show();


                        btnDelete.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                AlertDialog.Builder deleteConfirmDialog = new AlertDialog.Builder(
                                        Records_Home.this);
                                deleteConfirmDialog
                                        .setTitle("Delete Record");
                                // myAlertDialog.setMessage("Please enter new password");
                                deleteConfirmDialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(
                                                    DialogInterface arg0,
                                                    int arg1) {
                                                new DeleteRecord()
                                                        .execute(recId);
                                            }
                                        });
                                deleteConfirmDialog.setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {

                                            public void onClick(
                                                    DialogInterface arg0,
                                                    int arg1) {

                                            }
                                        });
                                deleteConfirmDialog.show();

                            }

                        });
                        btnEdit.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Records_Home.this, Record_Edit.class);
                                i.putExtra("record_id", recId);
                                i.putExtra("member_id", memid);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("EXIT", true);

                                Records_Home.this.startActivity(i);
                                //finish();
                            }
                        });
                        btnView.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(Records_Home.this, Record_View.class);
                                i.putExtra("record_id", recId);
                                i.putExtra("member_id", memid);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("EXIT", true);

                                Records_Home.this.startActivity(i);
                                finish();
                            }
                        });

                    }

                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void AddRecord(View v) {
        try {
            Intent i = new Intent(this, RecordActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("EXIT", true);
            i.putExtra("member_id", memid);
            this.startActivity(i);

        } catch (Exception ex) {

        }
    }

    public class DeleteRecord extends AsyncTask<String, String, String> {

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
            String record_id = params[0];
            try {

                SharedPreferences myPrefs = Records_Home.this
                        .getSharedPreferences("remember", MODE_PRIVATE);
                String user_id = myPrefs.getString("userid", null);
                ArrayList<NameValuePair> loginParam = new ArrayList<NameValuePair>();
                loginParam.add(new BasicNameValuePair("record_id", record_id));

                String response = CustomHttpClient
                        .executeHttpPost(
                                "http://ospinet.com/app_ws/android_app_fun/delete_record",
                                loginParam);
                retstring = response.toString();

            } catch (Exception io) {

            }
            return retstring;

        }

        @Override
        protected void onPostExecute(String retstring) {
            if (dialog.isShowing())
                dialog.dismiss();

            if (retstring.contains("success = 1")) {
                Toast.makeText(Records_Home.this, "Record deleted successfully",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(Records_Home.this, Records_Home.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                i.putExtra("member_id",memid);
                Records_Home.this.startActivity(i);

            }

        }
    }

    public void new_record(View v)
    {
        try {
            Intent i = new Intent(this, RecordActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("EXIT", true);
            i.putExtra("member_id", memid);
            this.startActivity(i);

        } catch (Exception ex) {

        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Records_Home.this, Member_Home.class);
        this.startActivity(i);
        finish();
    }
    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu2, null);
        com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        search=(SearchView) v.findViewById(R.id.search);
        search.setQueryHint("Search records");

        //*** setOnQueryTextListener ***
        search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                strQuery = query;
                new Searchrecord().execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                flag = 2;
                new Loadrecord().execute();
                //	Toast.makeText(getBaseContext(), newText,
                //Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        ImageButton imgAdd = (ImageButton) v.findViewById(R.id.add); //it's important to use your actionbar view that you inflated before
        ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
        imgAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Records_Home.this, RecordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                intent.putExtra("member_id", memid);
                Records_Home.this.startActivity(intent);

            }
        });
        imgMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sideNavigationView.toggleMenu();
                RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
                rel.bringChildToFront(sideNavigationView);

            }
        });
        ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
        TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);

        imgLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Records_Home.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        txtLogoName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Records_Home.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

    }
    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(Records_Home.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                Records_Home.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(Records_Home.this, Member_Home.class);
                Records_Home.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(Records_Home.this, com.ospinet.app.help.class);
                Records_Home.this.startActivity(help);

                break;

            default:
                return;
        }
        // finish();
    }

}
