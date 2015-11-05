package com.example.tec.signup;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;

import java.net.MalformedURLException;



/// ADD AZURE MOBILE SERVICE LIBRARIES TO LIBS FOLDER FOLDER (See Signapp/app/libs)

public class signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Button sign_up;
    Button sign_cancel;
    private EditText User_name;
    private EditText User_email;

    private EditText password;

    private ProgressBar mProgressBar;



    /**
     * Mobile Service Client reference
     */
    private MobileServiceClient mClient;

    /**
     * Mobile Service Table used to access data
     */
    private MobileServiceTable<User> mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // map buttons

        sign_up = (Button) findViewById(R.id.sign_signup);
        sign_cancel = (Button) findViewById(R.id.singin_cancel);


        // A progrees bar


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar3);




        sign_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fp = new Intent(getApplicationContext(), LoginSignup.class);
                startActivity(fp);
            }
        });



        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();    // This method bind our entered data to item and sent too mobile service table User
            }
        });



        try {


            // Create the Mobile Service Client instance, using the provided
            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "MOBILE SERVICE TABLE",
                    "MOBILESERVICE KEY",
                    this);/*.withFilter(new ProgressFilter());*/

            // Get the Mobile Service Table instance to use
            mUser = mClient.getTable(User.class);




            User_name = (EditText) findViewById(R.id.Name);
            User_email = (EditText) findViewById(R.id.edit_email);
            password = (EditText) findViewById(R.id.edit_email);






        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        }



    }



    public void addItem() {



        // START PROGRESS BAR

        mProgressBar.setVisibility(ProgressBar.VISIBLE);


// if mclient is null , return

        if (mClient == null) {
            return;
        }


        // Create a new item
        final User item = new User();

        //  item.setId("15B2F0AC-6AFC-401F-B2B6-2A8FD692062A");

        // adding value to item


        item.setText(User_name.getText().toString());
        item.setemail(User_email.getText().toString());
        item.setpassword(password.getText().toString());

// asynch task is needed becase its a main thread

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mUser.insert(item).get();

                    if (!item.isComplete()) {

                        runOnUiThread(new Runnable() {
                            public void run() {

                                Intent S = new Intent(getApplicationContext(), Success.class);
                                startActivity(S);

                                Toast toast=   Toast.makeText(getApplicationContext(), "FILL OUT BASIC INFORMATIONS", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();


                            }
                        });
                    }


                } catch (Exception exception) {
                    createAndShowDialog(exception, "Error");
                }
                return null;
            }
        }.execute();


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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String s = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }




    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        //	createAndShowDialog(ex.getMessage(), title);
    }



}
