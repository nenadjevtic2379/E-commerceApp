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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText usernameBox, passwordBox;
    Button loginButton;
    TextView registerLink;
    ProgressDialog prgDialog;
    String URL = "http://192.168.0.12:8080/RestFul/webresources/auth/login";
    String ime, username, idUser;
    SessionHandler session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameBox = (EditText)findViewById(R.id.usernameBox);
        passwordBox = (EditText)findViewById(R.id.passwordBox);
        loginButton = (Button)findViewById(R.id.loginButton1);
        registerLink = (TextView)findViewById(R.id.registerLink1);
        session = new SessionHandler(this);
       // session.checkLogin();


        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                prgDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){

                    @Override
                    public void onResponse(String s) {
                        prgDialog.hide();
                        try {
                            JSONObject json = new JSONObject(s);
                            ime = (String) json.get("ime");
                            username = (String) json.get("username");
                            idUser = (String) json.get("idUser");

                            if(json.get("status").equals("true") && json.get("idRole").equals(1)){

                                session.createSession(username, ime, idUser);

                                Toast.makeText(Login.this, "Login Successful Admin", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Login.this,Home.class);
                                i.putExtra("ulogovaniUser" , ime);
                                i.putExtra("admin", true);
                                startActivity(i);
                            }

                            else if(json.get("status").equals("true") && json.get("idRole").equals(2)){

                                session.createSession(username, ime, idUser);
                                Toast.makeText(Login.this, "Login Successful User", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Login.this,Home.class);
                                i.putExtra("admin", false);
                                startActivity(i);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    /*    if(s.equals("true")){
                            Toast.makeText(Login.this, "Login Successful Admin", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this,Home.class));
                        }
                        else if (s.equals("user")) {
                            Toast.makeText(Login.this, "Login Successful User", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(Login.this,Home.class));
                        }
                        else{
                            Toast.makeText(Login.this, "Incorrect Details", Toast.LENGTH_LONG).show();
                        }*/
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(Login.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("username", usernameBox.getText().toString());
                        parameters.put("password", passwordBox.getText().toString());
                        return parameters;
                    }
                };

                RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                rQueue.add(request);
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
}
