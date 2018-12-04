package nenad2379.diplomskirad;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Korsn on 7.11.2018..
 */


public class ProizvodiHome extends AppCompatActivity {

    Button btn;
    ListView lv;
    String url = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.proizvod";
    ProgressDialog prgDialog;
    AsyncHttpClient client;
    int idProizvod, idProdavnica;
    LinkedHashMap<String, Integer> hashProizvod;
    ArrayList<String> listaImenaProizvoda;
    TextView tv;
    String nazivProdavnice;
    SessionHandler session;
    boolean provera;
    LinkedList<String> rokUpotrebe;
    LinkedList<Integer> stanje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proizvodihome);


        stanje = new LinkedList<>();
        rokUpotrebe = new LinkedList<>();
        session = new SessionHandler(this);
        hashProizvod = new LinkedHashMap<>();
        listaImenaProizvoda  = new ArrayList<>();
        idProdavnica = getIntent().getIntExtra("idProdavnica", 0);
        nazivProdavnice = getIntent().getStringExtra("nazivProdavnice");
        btn = (Button) findViewById(R.id.addProizvod);
        lv = (ListView) findViewById(R.id.proizvodi_list);
        tv = (TextView) findViewById(R.id.imeProd);
        tv.setText("Prodavnica: " + nazivProdavnice);
        lv.setChoiceMode(ListView.CHOICE_MODE_NONE);
        lv.setTextFilterEnabled(true);
        registerForContextMenu(lv);

        client = new AsyncHttpClient();
       /* session = new SessionHandler(this);
        final HashMap<String, String> hash = session.getUserDetail();
        final String idUser = hash.get(session.ID);*/

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Sacekajte...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        provera = getIntent().getBooleanExtra("provera", true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        if (!provera) {
            btn.setVisibility(View.GONE);
        }

        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);



                try {

                    JSONArray array = response.getJSONArray("proizvod");

                    for (int i = 0; i < array.length(); i++) {

                         JSONObject obj = array.getJSONObject(i);
                        // tv2.setText(obj.get("nazivProdavnica").toString());

                        if (idProdavnica == obj.getInt("idProdavnica")) {

                            hashProizvod.put(obj.getString("nazivProizvod"), obj.getInt("idProizvod"));
                            // imenaProdavnica.add(obj.get("nazivProdavnica").toString());
                            rokUpotrebe.add(obj.getString("rokupotrebe"));
                            stanje.add(obj.getInt("stanje"));

                        }
                    }


                    for (String key : hashProizvod.keySet()) {
                        listaImenaProizvoda.add(key);
                    }

                    CustomAdapter ca = new CustomAdapter(ProizvodiHome.this, listaImenaProizvoda, rokUpotrebe, stanje);
                   // lv.setAdapter(new ArrayAdapter<String>(ProizvodiHome.this, android.R.layout.simple_list_item_1, listaImenaProizvoda));
                    lv.setAdapter(ca);




                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);


                try {


                    for (int i = 0; i < response.length(); i++) {

                        if (idProdavnica == response.getJSONObject(i).getInt("idProdavnica")) {

                            hashProizvod.put(response.getJSONObject(i).getString("nazivProizvod"), response.getJSONObject(i).getInt("idProizvod"));
                            rokUpotrebe.add(response.getJSONObject(i).getString("rokupotrebe"));
                            stanje.add(response.getJSONObject(i).getInt("stanje"));



                        }
                    }

                    for (String key : hashProizvod.keySet()) {
                        listaImenaProizvoda.add(key);
                    }

                    CustomAdapter ca = new CustomAdapter(ProizvodiHome.this, listaImenaProizvoda, rokUpotrebe,stanje);

                   // lv.setAdapter(new ArrayAdapter<String>(ProizvodiHome.this, android.R.layout.simple_list_item_1, listaImenaProizvoda));
                    lv.setAdapter(ca);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(ProizvodiHome.this, "Greska prilikom ucitavanja" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ProizvodiHome.this, "Greska prilikom ucitavanja" , Toast.LENGTH_SHORT).show();
            }
        });




        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               /*   List<Integer> list = new ArrayList<Integer>(hashProd.values());
        idProdavnica = list.get(position);
*/
               // parent.setOnCreateContextMenuListener(ProizvodiHome.this);


                Toast.makeText(ProizvodiHome.this, "Kliknite i zadrzite", Toast.LENGTH_SHORT).show();
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProizvodiHome.this, ProizvodAdd.class);
                i.putExtra("idProdavnice" , idProdavnica);
                i.putExtra("nazivProdavnice", nazivProdavnice);
                finish();
                startActivity(i);
            }
        });

    }



    //kontekstni meni
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);
        CreateMenuContext(menu);

        List<Integer> listaIDProizvoda = new ArrayList<Integer>(hashProizvod.values());

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        idProizvod = listaIDProizvoda.get(info.position);
        menu.setHeaderTitle(listaImenaProizvoda.get(info.position));
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        return MenuChoiceContext(item);
    }
    //***********




    //opcioni meni
    /*public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        CreateMenuOption(menu);
        return true;
    }*/
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return MenuChoiceOption(item);
    }*/
    //***************

    private void CreateMenuContext(Menu menu){
        menu.setQwertyMode(true);
        MenuItem mnu1 = menu.add(0, 0, 0, "Izmeni");{

        }
        MenuItem mnu2 = menu.add(0, 1, 1, "Obrisi");{

        }

    }

/*
    private void CreateMenuOption(Menu menu){
        menu.setQwertyMode(true);
        MenuItem mnu1 = menu.add(0, 0, 0, "aa");{

        }


    }*/
/*
    private boolean MenuChoiceOption(MenuItem item) {
        switch (item.getItemId()) {
            case 0:

                return  true;


        }
        return false;
    }*/

    private boolean MenuChoiceContext(MenuItem item){
        switch (item.getItemId()) {
            case 0:

               client.get(url + "/" + idProizvod, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);




                        String nazivProizvoda = "";
                        int idProd = 0;
                        int idProiz = 0;
                        int idUser = 0;
                        int stanjeRobe = 0;
                        int minimum = 0;
                        String rok = "";

                        try {
                            nazivProizvoda = response.getString("nazivProizvod");
                            idProd = response.getInt("idProdavnica");
                            idUser = response.getInt("idUser");
                            idProiz = response.getInt("idProizvod");
                            stanjeRobe = response.getInt("stanje");
                            minimum = response.getInt("minimum");
                            rok = response.getString("rokupotrebe");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(ProizvodiHome.this, ProizvodEdit.class);
                        intent.putExtra("nazivProizvoda", nazivProizvoda);
                        intent.putExtra("idProd", idProd);
                        intent.putExtra("idUser", idUser);
                        intent.putExtra("idProizvoda", idProiz);
                        intent.putExtra("stanje", stanjeRobe);
                        intent.putExtra("minimum", minimum);
                        intent.putExtra("rok", rok);
                        intent.putExtra("nazivProdavnice", nazivProdavnice);
                        intent.putExtra("provera", provera);
                        finish();
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

                return true;
            case 1:

                client.delete(url + "/" + idProizvod, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        finish();
                        startActivity(getIntent());

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                Toast.makeText(this, "Brisanje",
                        Toast.LENGTH_LONG).show();
                return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent i = new Intent(ProizvodiHome.this, Home.class);
        i.putExtra("admin", provera);
        finish();
        startActivity(i);

    }



}
