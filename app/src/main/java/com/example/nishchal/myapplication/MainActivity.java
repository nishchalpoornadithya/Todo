package com.example.nishchal.myapplication;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
ImageView img; StringBuilder total;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    Context ctx;String user,email;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
     TextView tv1;
    TextView tv2;

    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = getApplicationContext();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseOperations mydb1 = new DatabaseOperations(this);
        Cursor res = mydb1.getAlldata();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = mNavigationView.getHeaderView(0);
        final  TextView tv1=(TextView)header.findViewById(R.id.tv1);
        final TextView tv2=(TextView)header.findViewById(R.id.tv2);
        final DatabaseOperations db = new DatabaseOperations(this);

       final ArrayList<dataretrieve> arrayListToDo = new ArrayList<dataretrieve>();

        StringBuffer sb = new StringBuffer();

        while (res.moveToNext()) {
            dataretrieve obj = new dataretrieve();
            obj.setId(res.getInt(0));
            obj.setName(res.getString(1));
            obj.setDate(res.getString(2));
            obj.setTime(res.getString(3));
            obj.setNot(res.getString(4));
            arrayListToDo.add(obj);
         /*  sb.append("NAME :"+arrayListToDo.get(0).getId()+"\n");
                sb.append("date :"+arrayListToDo.get(1).getDate()+"\n");
                sb.append("time :"+arrayListToDo.get(2).getTime()+"\n");
                sb.append("not :"+arrayListToDo.get(3).getNot()+"\n\n");*/

        }
        try {
            FileInputStream inputStream = openFileInput("userI");
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            r.close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        int i=0;String s1="",s2="";
        while(i<=total.length()){
            if(total.charAt(i)=='0'){
                s1=total.substring(0,i);
                s2=total.substring(i+1,total.length());
                break;
            }
            i++;
        }
        tv1.setText(s1);
        tv2.setText(s2);




        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        // specify an adapter (see also next example)
        mAdapter = new CustomAdapter(arrayListToDo);
        mRecyclerView.setAdapter(mAdapter);

        final CustomAdapter cv1 = new CustomAdapter(arrayListToDo);
        //  showmsg("data",sb.toString());
        // beautiful piece of code
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.RIGHT) {    //if swipe right

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //alert for confirm to delete
                    builder.setMessage("Are you sure to delete?");    //set message

                    builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.notifyItemRemoved(position);    //item removed from recylcerview
                            db.del(arrayListToDo.get(position).getId()); //query for delete
                            cv1.rem(position);  //then remove item

                            return;
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                            mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            return;
                        }
                    }).show();  //show alert dialog
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView); //set swipe to recylcerview


         img=(ImageView)header.findViewById(R.id.imageView);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                final LinearLayout layout = new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final TextView tx1=new TextView(MainActivity.this);
                TextView tx2=new TextView(MainActivity.this);
                tx1.setText("User name:");
                tx2.setText("email:");

                tx1.setTextColor(Color.BLACK);
                tx2.setTextColor(Color.BLACK);
                final EditText i1 = new EditText(MainActivity.this);
                final EditText i2 = new EditText(MainActivity.this);
                layout.addView(tx1);
                layout.addView(i1);
                layout.addView(tx2);
                layout.addView(i2);

                alert.setView(layout);
               alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String u1,u2;
                        u1=i1.getText().toString();
                        u2=i2.getText().toString();


                        user = u1+"0"+u2;

                        try {
                            FileOutputStream outputStream = openFileOutput("userI", Context.MODE_PRIVATE);
                            outputStream.write(user.getBytes());

                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tv1.setText(u1);
                        tv2.setText(u2);



                    }
                });

                alert.show();
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in1 = new Intent(MainActivity.this, dialog1cl.class);
                MainActivity.this.startActivity(in1);


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void showmsg(String t, String m) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(t);
        builder.setMessage(m);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        int id = item.getItemId();

        if (id == R.id.comp_task) {
            // Handle the camera action
        } else if (id == R.id.incomp_task) {

        }

        else if (id == R.id.abt_dev) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
}
