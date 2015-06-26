package com.ospinet.app;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.ospinet.app.R;


public class help extends  SherlockActivity implements ISideNavigationCallback {
	private SideNavigationView sideNavigationView;
    EditText To_Email, edtEmail, Description;
    Button Cancel, Send;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        showActionBar();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

        To_Email = (EditText) findViewById(R.id.edtTo_Email);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        Description = (EditText) findViewById(R.id.editDescription);
        Cancel = (Button) findViewById(R.id.btnCancel);
        Send = (Button) findViewById(R.id.btnSend);
        SharedPreferences myPrefs = help.this
                .getSharedPreferences("remember", MODE_PRIVATE);
        String From_email = myPrefs.getString("email", null);
        edtEmail.setText(From_email);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent records = new Intent(help.this, Member_Home.class);
                help.this.startActivity(records);
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation val = new Validation();
                if(val.Is_Valid_Email(edtEmail) && !Description.getText().toString().trim().equals(""))
                {
                    Toast.makeText(help.this, "Problem in network.. Try after some time", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(help.this, "Please enter your email address and Problem", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void onBackPressed() {
        Intent i = new Intent(help.this, PreMemberHome.class);
        this.startActivity(i);
        finish();
    }

    @Override
    public void onSideNavigationItemClick(int itemId) {
    	switch(itemId)
    	{
    case R.id.side_navigation_menu_item1:
        Intent i = new Intent(help.this, LoginActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	i.putExtra("EXIT", true);

    	help.this.startActivity(i);

    	break;

    case R.id.side_navigation_menu_item2:
         Intent records = new Intent(help.this, Member_Home.class);
         help.this.startActivity(records);

         break;

    case R.id.side_navigation_menu_item3:
         Intent help = new Intent(help.this, com.ospinet.app.help.class);
         help.this.startActivity(help);

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
    imgAdd.setOnClickListener(new OnClickListener() {

    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent intent = new Intent(help.this, MemberActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.putExtra("EXIT", true);
    		help.this.startActivity(intent);
            
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
    		Intent i = new Intent(help.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });
    txtLogoName.setOnClickListener(new OnClickListener() {
    	
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Intent i = new Intent(help.this,PreMemberHome.class);
    		i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    		startActivity(i);
    	}
    });



    }

}


