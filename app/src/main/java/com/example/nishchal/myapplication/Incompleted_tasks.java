package com.example.nishchal.myapplication;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.ArrayList;

/**
 * Created by Nishchal on 16-02-2017.
 */
public class Incompleted_tasks extends AppCompatActivity {
    RecyclerView mRecyclerView1;
    RecyclerView.LayoutManager mLayoutManager1;
    private RecyclerView.Adapter mAdapter1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerviewlayout);
        DatabaseOperations mydb1 = new DatabaseOperations(this);
        Cursor res = mydb1.getAlldata2();
        final DatabaseOperations db = new DatabaseOperations(this);
        final ArrayList<Dataretrieve> arrayListToDo1 = new ArrayList<Dataretrieve>();

        while (res.moveToNext()) {
            Dataretrieve obj = new Dataretrieve();
            obj.setId(res.getInt(0));
            obj.setName(res.getString(1));
            obj.setDate(res.getString(2));
            obj.setTime(res.getString(3));
            obj.setNot(res.getString(4));
            arrayListToDo1.add(obj);

        }

        mRecyclerView1 = (RecyclerView) findViewById(R.id.my_recycler_view1);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView1.setHasFixedSize(true);
        mLayoutManager1 = new LinearLayoutManager(this);
        mRecyclerView1.setLayoutManager(mLayoutManager1);
        mRecyclerView1.setItemAnimator(new DefaultItemAnimator());
        mAdapter1 = new CustomAdapter(arrayListToDo1,"n");

        mRecyclerView1.setAdapter(mAdapter1);
        final CustomAdapter cv2 = new CustomAdapter(arrayListToDo1,"n");
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.RIGHT) {    //if swipe right

                    AlertDialog.Builder builder = new AlertDialog.Builder(Incompleted_tasks.this); //alert for confirm to delete
                    builder.setMessage("Are you sure to delete?");
                    builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter1.notifyItemRemoved(position);    //item removed from recylcerview
                            db.del2(arrayListToDo1.get(position).getId()); //query for delete
                            cv2.rem(position);  //then remove item

                            return;
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter1.notifyItemRemoved(position);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                            mAdapter1.notifyItemRangeChanged(position, mAdapter1.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            return;
                        }
                    }).show();  //show alert dialog
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView1);




    }
}
