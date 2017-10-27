package com.yotravell;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yotravell.VolleyService.AppController;
import com.yotravell.fragments.FriendsFragment;
import com.yotravell.fragments.HomeFragment;
import com.yotravell.fragments.MemberFragment;
import com.yotravell.utils.CommonUtils;
import com.yotravell.utils.SharedPrefrenceManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private TextView userName,userEmail;
    private ImageView imgloggedUser;
    //private ClipData.Item memberCount,friendCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //CommonUtils.ShowToastMessages(HomeActivity.this,AppController.aSessionUserData.getId()+" User Id ,"+AppController.aSessionUserData.getEmail()+" User Enail,"+AppController.aSessionUserData.getFullName()+" User Full name,");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        userName = (TextView)header.findViewById(R.id.loggedUserName);
        userEmail = (TextView)header.findViewById(R.id.loggedUserEmail);
        imgloggedUser = (ImageView)header.findViewById(R.id.imgHeaderUserImg);
        userName.setText(AppController.aSessionUserData.getFullName().toString());
        userEmail.setText(AppController.aSessionUserData.getEmail().toString());

        Picasso.with(this.getApplicationContext())
                .load(AppController.aSessionUserData.getProfileImage().toString().trim())//"http://i.imgur.com/DvpvklR.png")
                .placeholder(R.drawable.ic_user_default)   // optional
                .error(R.drawable.ic_user_default)      // optional  ic_error
                .resize(150,150)                        // optional
                .into(imgloggedUser);

        displayFragmentByPosition(R.id.nav_feed);
    }

    private void displayFragmentByPosition(int navItemId){
        //Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flContent);
        Fragment fragment = null;

        switch (navItemId) {
            case R.id.nav_feed: // Home
                fragment = new HomeFragment();
                break;
            case R.id.nav_members_list: // Members
                fragment = new MemberFragment();
                break;
            case R.id.nav_friends_list: // Friends
                fragment = new FriendsFragment();
                break;
            default: // Logout
            {
                SharedPrefrenceManager.getInstance(this).removeSession();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            }
        }
        if(fragment != null ){
            attachFragmentOnUi(fragment, "DashBoardFragment",navItemId);
        }
        /*
        if(position >0){
            Fragment mFrgObj = null;
            mFrgObj  = new HomeFragment();

            if(mFrgObj != null ){
                attachFragmentOnUi(mFrgObj, "");
            }

        }*/
    }


    /**
     * Insert the fragment by replacing any existing fragment
     * @author kamalkant
     * @param fragment
     */

    public void attachFragmentOnUi(Fragment fragment, String tag,int navId){
        /*if(fragment !=  null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(""+tag).commit();
        }*/
        if(fragment !=  null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if(navId==0){
                fragmentTransaction.add(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(null);
            }else{
                fragmentTransaction.replace(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(""+tag);
            }

            fragmentTransaction.commit();
            navigationView.setCheckedItem(navId);
            //navigationView.getMenu().getItem(position).setChecked(true);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }
    @Override
    public void onBackPressed() {
        //Log.e("Back","Fragment");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager manager = getSupportFragmentManager();
            if(manager.getBackStackEntryCount() > 1) {
                super.onBackPressed();
                /*HomeFragment currentFragment = (HomeFragment) manager.findFragmentById(R.id.flContent);
                if(currentFragment instanceof HomeFragment){
                    //mNavigationView.getMenu().getItem(0).setChecked(true);
                }*/
            }else{
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        //memberCount = (ClipData.Item) menu.findItem(R.id.nav_members_list);
        //friendCount = (ClipData.Item) menu.findItem(R.id.nav_friends_list);
  //      menu.getItem(1).setTitle(R.string.menu_member+" ("+AppController.aSessionUserData.getTotalMember()+")");
    //    menu.getItem(2).setTitle(R.string.menu_friend+" ("+AppController.aSessionUserData.getTotalFriend()+")");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //int id = item.getItemId();
        displayFragmentByPosition(item.getItemId());
        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
