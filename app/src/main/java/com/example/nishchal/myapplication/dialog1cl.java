package com.example.nishchal.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class dialog1cl extends AppCompatActivity {
 public EditText ed;
    private static final int av= 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog1);
        ed=(EditText)findViewById(R.id.editText);
        ImageButton im=(ImageButton)findViewById(R.id.imageButton);
        im.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                promptspeech();
            }
        });

    }


    public void promptspeech()
    {
        Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something!");

      //  try {
            startActivityForResult(i,av );
       // }
       /* catch(ActivityNotFoundException a)
        {
            Toast.makeText(dialog1cl.this,"Sorry! Your device doesnt support speech Language",Toast.LENGTH_LONG).show();
        }*/
    }

    public void onActivityResult(int request_code , int result_code, Intent i){



           //  if(request_code ==av && result_code==RESULT_OK)
           //  {
                List<String> result=i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                ed.setText(result.get(0));
          //  }
        super.onActivityResult(request_code, result_code, i);

    }
}
