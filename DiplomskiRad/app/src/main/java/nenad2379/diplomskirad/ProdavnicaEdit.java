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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Korsn on 6.11.2018..
 */

public class ProdavnicaEdit extends AppCompatActivity {

    EditText et;
    Button btn;
    AsyncHttpClient client;
    String url = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.prodavnica";
    String url2 = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.user";
    ProgressDialog prgDialog;
    JSONObject obj = new JSONObject();
    StringEntity entity = null;

    //par username-iduser koji se vuce iz baze a koji je dodao ulogovani user
    HashMap<String, Integer> hashUser;
    Spinner s1;

    //lista username koje je komercijalista dodao, uzima se iz mape
    ArrayList<String> listaUsername;

    //id trenutno zaduzenog usera koji se nalazi u bazi
    int idZaduzeniUser;

    //lista id koja odgovara username iz gore navedene liste, vrednosti se takodje uzimaju iz mape
    List<Integer> listaIDZaduzenihUsera2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodavnicaedit);



        hashUser = new HashMap<>();
        listaUsername = new ArrayList<>();
        s1 = (Spinner) findViewById(R.id.spinnerEdit);
        client = new AsyncHttpClient();
        et = (EditText) findViewById(R.id.prodavnicaNameEdit);
        btn = (Button) findViewById(R.id.addProdavnicaEdit);
        String nazivProdavnice = getIntent().getStringExtra("nazivProd");
        final String idZaduzenog = getIntent().getStringExtra("idZaduzenog");
        final int idProdavnice = getIntent().getIntExtra("idProd", 0);
        final int idUser = getIntent().getIntExtra("idUser", 0);
        et.setText(nazivProdavnice);




        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Sacekajte...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);




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

                        if (String.valueOf(idUser).equals(response.getJSONObject(i).getString("addedBy"))) {

                            hashUser.put(response.getJSONObject(i).getString("username"), response.getJSONObject(i).getInt("idUser"));

                        }

                    }

                    listaIDZaduzenihUsera2 = new ArrayList<Integer>(hashUser.values());




                    for (String key : hashUser.keySet()) {
                        listaUsername.add(key);
                    }


                    ArrayAdapter<String> aa = new ArrayAdapter<String>(ProdavnicaEdit.this, android.R.layout.simple_spinner_item, listaUsername);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s1.setAdapter(aa);

                    //postavljanje spinera na vrednost koja se vuce iz baze
                    //iz liste sa username se postavlja onaj username koji odgovara id trenutno zaduzenog usera koji odgovara listi sa ID
                    s1.setSelection(aa.getPosition(listaUsername.get(listaIDZaduzenihUsera2.indexOf(Integer.parseInt(idZaduzenog)))));

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
                        if (String.valueOf(idUser).equals(obj.getString("addedBy"))) {

                            hashUser.put(obj.getString("username"), obj.getInt("idUser"));
                            // imenaProdavnica.add(obj.get("nazivProdavnica").toString());
                        }

                    }

                    listaIDZaduzenihUsera2 = new ArrayList<Integer>(hashUser.values());



                    for (String key : hashUser.keySet()) {
                        listaUsername.add(key);
                    }
                  //  System.out.println("BB" + listaIDZaduzenihUsera2.indexOf(idZaduzenog));

                    ArrayAdapter<String> aa = new ArrayAdapter<String>(ProdavnicaEdit.this, android.R.layout.simple_spinner_item, listaUsername);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s1.setAdapter(aa);

                    //postavljanje spinera na vrednost koja se vuce iz baze

                    s1.setSelection(aa.getPosition(listaUsername.get(listaIDZaduzenihUsera2.indexOf(Integer.parseInt(idZaduzenog)))));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers,String responseString,Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ProdavnicaEdit.this, "Greska prilikom ucitavanja korisnika" , Toast.LENGTH_SHORT).show();

            }

        });




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et.getText().toString().matches("")) {

                    try {
                        obj.put("nazivProdavnica", et.getText().toString());
                        obj.put("idUser", idUser);
                        obj.put("idProdavnica", idProdavnice);
                        obj.put("idZaduzenog", idZaduzeniUser);
                        entity = new StringEntity(obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    prgDialog.show();
                    client.put(ProdavnicaEdit.this, url + "/" + idProdavnice, entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            prgDialog.hide();

                            Intent intent = new Intent(ProdavnicaEdit.this, Home.class);
                            Toast.makeText(ProdavnicaEdit.this, "Prodavnica uspesno izmenjena", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(intent);

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            prgDialog.hide();

                            Toast.makeText(ProdavnicaEdit.this, "Neuspesna operacija", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                else {
                    Toast.makeText(ProdavnicaEdit.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent i = new Intent(ProdavnicaEdit.this, Home.class);
        finish();
        startActivity(i);

    }


}
