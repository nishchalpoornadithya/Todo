package com.example.nishchal.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class dialog1cl extends AppCompatActivity {
 private EditText ed;
     private EditText ed1;
    private static final int av= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog1);
        ed=(EditText)findViewById(R.id.editText);
        String st=ed.getText().toString();
      ed1=(EditText)findViewById(R.id.editText2);
        ImageButton im2=(ImageButton)findViewById(R.id.imageButton2);
        im2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                new DatePickerDialog(dialog1cl.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


                               }});

        ImageButton im=(ImageButton)findViewById(R.id.imageButton);
        im.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                promptspeech();
            }
        });

    }

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    public void promptspeech()
    {
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Ssup!");
        startActivityForResult(i,av );
       // try {

       // }
        //catch(ActivityNotFoundException a)
        //{
           // Toast.makeText(dialog1cl.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        //}
    }

    public void onActivityResult(int request_code , int result_code, Intent i)
    {
             if(request_code ==av && result_code==RESULT_OK)
             {
                ArrayList<String> result=i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                ed.setText(result.get(0));
             }
        super.onActivityResult(request_code, result_code, i);

    }


    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        ed1.setText(sdf.format(myCalendar.getTime()));
    }
}