package com.ospinet.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;


public class Profile_view extends  SherlockActivity implements ISideNavigationCallback {
	private SideNavigationView sideNavigationView;
    TextView TextNameLetters, txtName, txtEmail, txtGender, txtDOB;
    Button Change_password;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view);
        showActionBar();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

        TextNameLetters = (TextView) findViewById(R.id.TextNameLetters);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtGender = (TextView) findViewById(R.id.txtGender);
        txtDOB = (TextView) findViewById(R.id.txtDOB);

        Change_password = (Button) findViewById(R.id.btnpwd);

        SharedPreferences myPrefs = Profile_view.this.getSharedPreferences("remember", MODE_PRIVATE);
        String email = myPrefs.getString("email", null);
        String fname = myPrefs.getString("fname", null);
        String lname = myPrefs.getString("lname", null);
        String age = myPrefs.getString("age", null);
        String gender = myPrefs.getString("gender", null);
        Character First = fname.charAt(0);
        Character Last = lname.charAt(0);
        TextNameLetters.setText(""+ First + Last);
        txtName.setText(fname +" " + lname);
        txtDOB.setText(age);
        txtEmail.setText(email);
        txtGender.setText(gender);

  /*      Change_password.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent records = new Intent(Profile_view.this, Member_Home.class);
                Profile_view.this.startActivity(records);
            }
        });

        Send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation val = new Validation();
                if(val.Is_Valid_Email(edtEmail) && !Description.getText().toString().trim().equals(""))
                {
                    Toast.makeText(Profile_view.this, "Problem in network.. Try after some time", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(Profile_view.this, "Please enter your email address and Problem", Toast.LENGTH_LONG).show();
                }
            }
        });*/
    }
    public void onBackPressed() {
        Intent i = new Intent(Profile_view.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }

    @Override
    public void onSideNavigationItemClick(int itemId) {
    	switch(itemId)
    	{
    case R.id.side_navigation_menu_item1:
        Intent i = new Intent(Profile_view.this, LoginActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	i.putExtra("EXIT", true);

    	Profile_view.this.startActivity(i);

    	break;

    case R.id.side_navigation_menu_item2:
         Intent records = new Intent(Profile_view.this, Member_Home.class);
         Profile_view.this.startActivity(records);

         break;

    case R.id.side_navigation_menu_item3:
         Intent help = new Intent(Profile_view.this, Profile_view.class);
         Profile_view.this.startActivity(help);

         break;

            default:
        return;
        }
       // finish();
    }

    private void showActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View v = inflator.inflate(R.layout.menu1, null);
    com.actionbarsherlock.app.ActionBar actionBar = getSupportActionBar();
    actionBar.setDisplayHomeAsUpEnabled(false);
    actionBar.setDisplayShowHomeEnabled (false);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setCustomView(v);
    ImageButton imgAdd = (ImageButton) v.findViewById(R.id.add); //it's important to use your actionbar view that you inflated before
    ImageButton imgMenu = (ImageButton) v.findViewById(R.id.options);
        imgAdd.setVisibility(View.INVISIBLE);
    imgAdd.setOnClickListener(new OnClickListener() {

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent intent = new Intent(Profile_view.this, MemberActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.putExtra("EXIT", true);
    		Profile_view.this.startActivity(intent);
            
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
    		Intent i = new Intent(Profile_view.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });
    txtLogoName.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(Profile_view.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });



    }

}


