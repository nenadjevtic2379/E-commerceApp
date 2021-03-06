package nenad2379.diplomskirad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Register extends AppCompatActivity {
    EditText emailBox, passwordBox, usernameBox, nameBox, lastNameBox;
    Button registerButton;
    TextView loginLink;
    String URL = "http://192.168.0.12:8080/RestFul/webresources/auth/register";
    ProgressDialog prgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameBox = (EditText) findViewById(R.id.usernameBox);
        nameBox = (EditText) findViewById(R.id.nameBox);
        lastNameBox = (EditText) findViewById(R.id.lastNameBox);
        emailBox = (EditText)findViewById(R.id.emailBox);
        passwordBox = (EditText)findViewById(R.id.passwordBox);
        registerButton = (Button)findViewById(R.id.registerButton);
        loginLink = (TextView)findViewById(R.id.loginLink);

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
                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                usernameBox.setText("");
                                nameBox.setText("");
                                lastNameBox.setText("");
                                emailBox.setText("");
                                passwordBox.setText("");

                            } else {
                                Toast.makeText(Register.this, "Username already exist", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(Register.this, "Some error occurred -> " + volleyError, Toast.LENGTH_LONG).show();
                            ;
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
                            return parameters;
                        }
                    };

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);

                }

                else {
                    Toast.makeText(Register.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}
