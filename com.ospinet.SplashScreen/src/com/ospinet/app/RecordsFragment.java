package com.ospinet.app;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;
import com.ospinet.app.R;
import com.ospinet.app.Records_Home.DeleteRecord;
import com.ospinet.app.Records_Home.Loadrecord;

public class RecordsFragment extends SherlockFragment implements ISideNavigationCallback {
    private SearchView search;
    private SideNavigationView sideNavigationView;
    String memid;
    String user_id;
    ArrayList<record> arrrecords;
    public static RecordAdapter rad;
    ProgressDialog dialog;
    ListView recordList;
    TextView txtNoRec;
    Button btnNew;

    public static String strQuery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.records, container, false);
        showActionBar();
        sideNavigationView = (SideNavigationView) rootView.findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);
        dialog = new ProgressDialog(getActivity());
        //memid = getActivity().getIntent().getStringExtra("member_id");
        user_id = getActivity().getIntent().getStringExtra("userid");
        txtNoRec = (TextView) rootView.findViewById(R.id.txt_home_norec);
        btnNew= (Button) rootView.findViewById(R.id.btnNewAdd);
        txtNoRec.setVisibility(View.GONE);
        btnNew.setVisibility(View.GONE);
        recordList = (ListView)rootView.findViewById(R.id.recordList);
        arrrecords = new ArrayList<record>();
        new Loadrecord().execute();
        return rootView;
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

                SharedPreferences myPrefs = getActivity()
                        .getSharedPreferences("remember", Context.MODE_PRIVATE);
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
                Toast.makeText(getActivity(), "Record deleted successfully",
                        Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(), Records_Home.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);
                i.putExtra("member_id",memid);
                getActivity().startActivity(i);

            }

        }
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

                Toast.makeText(getActivity(), "Sorry no record found",
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
                /*txtNoRec.setVisibility(View.GONE);
                btnNew.setVisibility(View.GONE);
                recordList.setVisibility(View.VISIBLE);
            */
                rad = new RecordAdapter(getActivity(),
                        arrrecords);

            /*    recordList.setAdapter(rad);*/
                rad.notifyDataSetChanged();
             /*   recordList.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        TextView txtId = (TextView) arg1
                                .findViewById(R.id.txtId);
                        final String recId = txtId.getText().toString();
                        final Dialog builder = new Dialog(getActivity());

                        final View view = getActivity().getLayoutInflater().inflate(
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
                                        getActivity());
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
                                Intent i = new Intent(getActivity(), Record_Edit.class);
                                i.putExtra("record_id", recId);
                                i.putExtra("member_id", memid);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("EXIT", true);

                                getActivity().startActivity(i);
                                //finish();
                            }
                        });
                        btnView.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), Record_View.class);
                                i.putExtra("record_id", recId);
                                i.putExtra("member_id", memid);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("EXIT", true);

                                getActivity().startActivity(i);
                                finish();
                            }
                        });

                    }

                });
*/
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public class Loadrecord extends AsyncTask<String, String, String> {

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
                ArrayList<NameValuePair> allrecords = new ArrayList<NameValuePair>();
                allrecords.add(new BasicNameValuePair("userid",user_id));
                String response = CustomHttpClient
                        .executeHttpPost("http://ospinet.com/app_ws/android_app_fun/get_all_records",
                                allrecords);
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
                        .optJSONArray("member_records");
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
                rad = new RecordAdapter(getActivity(),
                        arrrecords);

                recordList.setAdapter(rad);
                recordList.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        TextView txtId = (TextView) arg1
                                .findViewById(R.id.txtId);
                        final String recId = txtId.getText().toString();
                        final Dialog builder = new Dialog(getActivity());

                        final View view = getActivity().getLayoutInflater().inflate(
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
                                        getActivity());
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
                                Intent i = new Intent(getActivity(), Record_Edit.class);
                                i.putExtra("record_id", recId);
                                i.putExtra("member_id", memid);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("EXIT", true);

                                getActivity().startActivity(i);
                                //finish();
                            }
                        });
                        btnView.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), Record_View.class);
                                i.putExtra("record_id", recId);
                                i.putExtra("member_id", memid);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("EXIT", true);

                                getActivity().startActivity(i);

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

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.menu2, null);
        com.actionbarsherlock.app.ActionBar actionBar = getSherlockActivity().getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);
        search=(SearchView) v.findViewById(R.id.search);
        search.setQueryHint("Search");

        //*** setOnQueryTextFocusChangeListener ***
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                Toast.makeText(getActivity(), String.valueOf(hasFocus),
                        Toast.LENGTH_SHORT).show();
            }
        });

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
                Intent intent = new Intent(getActivity(), RecordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                intent.putExtra("member_id", memid);
                getActivity().startActivity(intent);

            }
        });
        imgMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                sideNavigationView.toggleMenu();
	                /*RelativeLayout rel = (RelativeLayout) findViewById(R.id.rel);
	                rel.bringChildToFront(sideNavigationView);*/

            }
        });
        ImageButton imgLogo = (ImageButton) v.findViewById(R.id.logo);
        TextView txtLogoName = (TextView) v.findViewById(R.id.logoName);

        imgLogo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getActivity(),PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        txtLogoName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getActivity(),PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });

    }

    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(getActivity(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                getActivity().startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(getActivity(), Member_Home.class);
                getActivity().startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(getActivity(), com.ospinet.app.help.class);
                getActivity().startActivity(help);

                break;

            default:
                return;
        }
        // finish();
    }

}