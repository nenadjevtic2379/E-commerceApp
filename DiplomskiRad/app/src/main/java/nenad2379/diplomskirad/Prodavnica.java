package nenad2379.diplomskirad;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Prodavnica extends AppCompatActivity{

    EditText et;
    Button btn;
    AsyncHttpClient client;
    String url = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.prodavnica";
    String url2 = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.user";
    StringEntity entity = null;
    JSONObject obj = new JSONObject();
    SessionHandler session;
    ProgressDialog prgDialog;
    String idUser;
    HashMap<String, Integer> hashUser;
    Spinner s1;
    ArrayList<String> listaUsername;
    int idZaduzeniUser;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodavnica);


        //tv = (TextView) findViewById(R.id.idZaduzenogUsera);
        listaUsername = new ArrayList<>();
        s1 = (Spinner) findViewById(R.id.spinner);
        hashUser = new HashMap<>();
        session = new SessionHandler(this);
        et = (EditText) findViewById(R.id.prodavnicaName);
        btn = (Button) findViewById(R.id.addProdavnica);
        client = new AsyncHttpClient();
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Sacekajte...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        //s1.setSelection(0);


        final HashMap<String, String> hash = session.getUserDetail();
        idUser = hash.get(session.ID);





        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                List<Integer> listaIDZaduzenihUsera = new ArrayList<Integer>(hashUser.values());

                idZaduzeniUser = listaIDZaduzenihUsera.get(position);
                //tv.setText(String.valueOf(idZaduzeniUser));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        client.get(url2, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);



                try {


                    for (int i = 0; i < response.length(); i++) {

                        if (idUser.equals(response.getJSONObject(i).getString("addedBy"))) {

                            hashUser.put(response.getJSONObject(i).getString("username"), response.getJSONObject(i).getInt("idUser"));

                        }

                    }

                    for (String key : hashUser.keySet()) {
                        listaUsername.add(key);
                    }

                    ArrayAdapter<String> aa = new ArrayAdapter<String>(Prodavnica.this, android.R.layout.simple_spinner_item, listaUsername);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s1.setAdapter(aa);



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);



                try {

                    JSONArray array = response.getJSONArray("user");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = array.getJSONObject(i);
                        // tv2.setText(obj.get("nazivProdavnica").toString());

                        //ispis prodavnica samo za one usere koji su je i dodali
                       if (idUser.equals(obj.getString("addedBy"))) {

                            hashUser.put(obj.getString("username"), obj.getInt("idUser"));
                            // imenaProdavnica.add(obj.get("nazivProdavnica").toString());
                       }

                    }


                    for (String key : hashUser.keySet()) {
                        listaUsername.add(key);
                    }

                    ArrayAdapter<String> aa = new ArrayAdapter<String>(Prodavnica.this, android.R.layout.simple_spinner_item, listaUsername);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s1.setAdapter(aa);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers,String responseString,Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(Prodavnica.this, "Greska prilikom ucitavanja korisnika" , Toast.LENGTH_SHORT).show();

            }

        });





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (!et.getText().toString().matches("") && s1.getSelectedItem() != null) {

                try {
                    obj.put("nazivProdavnica", et.getText().toString());
                    obj.put("idUser", Integer.parseInt(idUser));
                    obj.put("idZaduzenog", idZaduzeniUser);
                    entity = new StringEntity(obj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                prgDialog.show();
                client.post(Prodavnica.this, url, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.hide();
                        Toast.makeText(Prodavnica.this, "Prodavnica dodata", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(Prodavnica.this, Home.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        prgDialog.hide();
                        Toast.makeText(Prodavnica.this, "Neuspesna operacija", Toast.LENGTH_SHORT).show();
                    }


                });
            }
            else {
                Toast.makeText(Prodavnica.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent i = new Intent(Prodavnica.this, Home.class);
        finish();
        startActivity(i);

    }
}
