package nenad2379.diplomskirad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Korsn on 9.11.2018..
 */

public class RadnikAdd extends AppCompatActivity {

    EditText emailBox, passwordBox, usernameBox, nameBox, lastNameBox;
    Button registerButton;
    String URL = "http://192.168.0.12:8080/RestFul/webresources/auth/registerRadnik";
    ProgressDialog prgDialog;
    String addedById;
    SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radnikadd);

        session = new SessionHandler(this);
        usernameBox = (EditText) findViewById(R.id.usernameBoxRadnik);
        nameBox = (EditText) findViewById(R.id.nameBoxRadnik);
        lastNameBox = (EditText) findViewById(R.id.lastNameBoxRadnik);
        emailBox = (EditText)findViewById(R.id.emailBoxRadnik);
        passwordBox = (EditText)findViewById(R.id.passwordBoxRadnik);
        registerButton = (Button)findViewById(R.id.registerButtonRadnik);

        final HashMap<String, String> hash = session.getUserDetail();
        addedById = hash.get(session.ID);




        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!usernameBox.getText().toString().matches("") && !nameBox.getText().toString().matches("") && !lastNameBox.getText().toString().matches("") &&
                        !emailBox.getText().toString().matches("") && !passwordBox.getText().toString().matches("")) {

                    prgDialog.show();
                    final StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            prgDialog.hide();
                            if (s.equals("true")) {

                                String[] to = {emailBox.getText().toString()};
                                String subject = "parametri za prijavljivanje";
                                String tekst = "Vasi parametri za prijavljivanje su: " +
                                        "Username: " + usernameBox.getText().toString() + "\n" +
                                        "Password: " + passwordBox.getText().toString();

                                sendEmail(to, null, subject, tekst);

                                Toast.makeText(RadnikAdd.this, "Radnik uspesno dodat", Toast.LENGTH_LONG).show();

                        /*    Intent i = new Intent(RadnikAdd.this , Home.class);
                            finish();
                            startActivity(i);*/

                                usernameBox.setText("");
                                nameBox.setText("");
                                lastNameBox.setText("");
                                emailBox.setText("");
                                passwordBox.setText("");


                            } else {
                                Toast.makeText(RadnikAdd.this, "Username vec postoji", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(RadnikAdd.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("username", usernameBox.getText().toString());
                            parameters.put("password", passwordBox.getText().toString());
                            parameters.put("email", emailBox.getText().toString());
                            parameters.put("ime", nameBox.getText().toString());
                            parameters.put("prezime", lastNameBox.getText().toString());
                            parameters.put("added_by", addedById);
                            return parameters;
                        }
                    };

                    RequestQueue rQueue = Volley.newRequestQueue(RadnikAdd.this);
                    rQueue.add(request);


                }

                else {
                    Toast.makeText(RadnikAdd.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void sendEmail(String[] emailAddresses, String[] carbonCopies,
                           String subject, String message)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));

        String[] to = emailAddresses;
        String[] cc = carbonCopies;
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(RadnikAdd.this, Home.class);
        finish();
        startActivity(i);
    }
}
