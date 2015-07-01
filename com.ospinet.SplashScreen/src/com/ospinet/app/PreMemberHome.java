package com.ospinet.app;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.androidquery.AQuery;
import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

public class PreMemberHome extends SherlockActivity implements ISideNavigationCallback {
    private SideNavigationView sideNavigationView;
    ProgressDialog dialog;

    // rahul
    TextView txtname;
    ImageView imageView_round;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_new);
// rahul
        dialog = new ProgressDialog(PreMemberHome.this);
        txtname = (TextView) findViewById(R.id.txtName);
        imageView_round = (ImageView) findViewById(R.id.imageView_round);
        SharedPreferences myPrefs = PreMemberHome.this
                .getSharedPreferences("remember", MODE_PRIVATE);
        String fname = myPrefs.getString("fname", null);
        String lname = myPrefs.getString("lname", null);
        String profile_pic = myPrefs.getString("profile_pic", null);
        String type = myPrefs.getString("type", null);
        txtname.setText(fname +" " + lname);
        String profile_image = (profile_pic +"_250." + type);
        AQuery androidAQuery = new AQuery(
                PreMemberHome.this);


        if(profile_pic == null){
            androidAQuery.id(imageView_round).image(
                    "http://ospinet.com/assets/images/people/250/default_avatar_250x250.png", false, false,0, 0);   //"http://ospinet.com/assets/images/people/250/default_avatar_250x250.png"
        }else{
            androidAQuery.id(imageView_round).image(
                    "http://ospinet.com/profile_pic/member_pic_250/" + profile_image, false, false,0, 0);   //"http://ospinet.com/profile_pic/member_pic_250/" + profile_image;
        }
        //new GetLoginUser().execute();
        showActionBar();
        sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
        sideNavigationView.setMenuItems(R.menu.side_navigation_menu);
        sideNavigationView.setMenuClickCallback(this);
        sideNavigationView.setMode(Mode.LEFT);

    }

    @Override
    public void onSideNavigationItemClick(int itemId) {
        switch(itemId)
        {
            case R.id.side_navigation_menu_item1:
                Intent i = new Intent(PreMemberHome.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("EXIT", true);

                PreMemberHome.this.startActivity(i);

                break;

            case R.id.side_navigation_menu_item2:
                Intent records = new Intent(PreMemberHome.this, Member_Home.class);
                PreMemberHome.this.startActivity(records);

                break;

            case R.id.side_navigation_menu_item3:
                Intent help = new Intent(PreMemberHome.this, com.ospinet.app.help.class);
                PreMemberHome.this.startActivity(help);

                break;

            default:
                return;
        }
        // finish();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent i = new Intent(PreMemberHome.this,LoginActivity.class);
        this.startActivity(i);
        finish();

    }
    public void showMembers(View v)
    {
        try
        {
            Intent intent = new Intent(PreMemberHome.this, Member_Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);//finish();

        }
        catch(Exception ex)
        {

        }
    }
    public void logout(View v)
    {
        try
        {
            Intent i = new Intent(PreMemberHome.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("EXIT", true);

            PreMemberHome.this.startActivity(i);

        }
        catch(Exception ex)
        {

        }
    }
    public void help(View v)
    {
        try
        {
            Intent i = new Intent(PreMemberHome.this, help.class);

            PreMemberHome.this.startActivity(i);

        }
        catch(Exception ex)
        {

        }
    }
    public void Search(View v)
    {
        try
        {
            Intent i = new Intent(PreMemberHome.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PreMemberHome.this.startActivity(i);

        }
        catch(Exception ex)
        {

        }
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
                Intent intent = new Intent(PreMemberHome.this, MemberActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                PreMemberHome.this.startActivity(intent);

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
                Intent i = new Intent(PreMemberHome.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });
        txtLogoName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(PreMemberHome.this,PreMemberHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(i);
            }
        });



    }


}