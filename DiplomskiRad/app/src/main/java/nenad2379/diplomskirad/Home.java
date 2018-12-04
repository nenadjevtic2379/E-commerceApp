package nenad2379.diplomskirad;


import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.preference.PreferenceActivity;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.support.v7.app.ActionBar;
import org.json.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class Home extends AppCompatActivity {

    TextView tv, tv2;
    SessionHandler session;
    Button button, button2;
    String url = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.prodavnica";
    String url2 = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.proizvod";
    int idProdavnica;
    LinkedHashMap<String, Integer> hashProd;
    ArrayList<String> listaImenaProdavnica;
    AsyncHttpClient client;
    String nazivProdavniceZaSlanje, idUser = "1", username, ime;
    String CHANNEL_ID = "123";
    ArrayList<String> imenaProizvodaNot;
    ArrayList<String> datumiProizvodaNot;
    ArrayList<Integer> stanjeNot;
    ArrayList<Integer> minimumNot;

    //true ako je komercijalista , false ako je radnik
    boolean provera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        imenaProizvodaNot = new ArrayList<>();
        datumiProizvodaNot = new ArrayList<>();
        stanjeNot = new ArrayList<>();
        minimumNot = new ArrayList<>();
        hashProd = new LinkedHashMap<>();
        listaImenaProdavnica  = new ArrayList<>();
        client = new AsyncHttpClient();
        tv = (TextView) findViewById(R.id.logUser);
       // button = (Button) findViewById(R.id.btnLogout);
        tv2 = (TextView) findViewById(R.id.naslov1);
        button2 = (Button) findViewById(R.id.addProdavnica);
        session = new SessionHandler(this);
        session.checkLogin();
       // final ListView lv = getListView();
        final ListView lstView = (ListView) findViewById(R.id.android_list);
        lstView.setChoiceMode(ListView.CHOICE_MODE_NONE);
        lstView.setTextFilterEnabled(true);
        registerForContextMenu(lstView);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        if (session.isLoggin()) {

            final HashMap<String, String> hash = session.getUserDetail();
            username = hash.get(session.USERNAME);
            idUser = hash.get(session.ID);
            ime = hash.get(session.NAME);

            provera = getIntent().getBooleanExtra("admin", true);



        }


        if (!provera) {
            button2.setVisibility(View.GONE);
            tv2.setText("Prodavnice za koje ste zaduženi");
        }

        //tv.setText("Welcome " + ime + "! Vas username je: " + username + " a id: " + idUser);
        tv.setText("Dobrodošli, " + username);






        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

                try {


                    for (int i = 0; i < response.length(); i++) {

                        //ukoliko je rola komercijalista
                        if (provera) {

                            //provera da li se ulogovani korisnik poklapa sa korisnikom koji je dodao prodavnicu
                            if (idUser.equals(String.valueOf(response.getJSONObject(i).getInt("idUser")))) {

                                hashProd.put(response.getJSONObject(i).getString("nazivProdavnica"), response.getJSONObject(i).getInt("idProdavnica"));

                            }
                        }

                        else {
                            //ako je rola radnik
                            if (idUser.equals(String.valueOf(response.getJSONObject(i).getInt("idZaduzenog")))) {

                                hashProd.put(response.getJSONObject(i).getString("nazivProdavnica"), response.getJSONObject(i).getInt("idProdavnica"));

                            }
                        }

                    }

                    //izdvajanje imena prodavnica u posebnu listu
                    for (String key : hashProd.keySet()) {
                        listaImenaProdavnica.add(key);
                    }

                    lstView.setAdapter(new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, listaImenaProdavnica));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {


                      JSONArray array = response.getJSONArray("prodavnica");

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject obj = array.getJSONObject(i);


                        if (provera) {
                            //ispis prodavnica samo za one usere koji su je i dodali
                            if (idUser.equals(String.valueOf(obj.getInt("idUser")))) {

                                hashProd.put(obj.getString("nazivProdavnica"), obj.getInt("idProdavnica"));

                            }

                        }
                        else {
                            //ispis prodavnica za koje je ulogovani radnik zaduzen
                            if (idUser.equals(String.valueOf(obj.getInt("idZaduzenog")))) {

                                hashProd.put(obj.getString("nazivProdavnica"), obj.getInt("idProdavnica"));

                            }
                        }
                    }


                    for (String key : hashProd.keySet()) {
                        listaImenaProdavnica.add(key);
                    }

                    lstView.setAdapter(new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1, listaImenaProdavnica));


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers,String responseString,Throwable throwable){
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(Home.this, "Greska prilikom ucitavanja prodavnica" , Toast.LENGTH_SHORT).show();
            }

        });


      /*  button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
            }
        });*/

       button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Home.this, Prodavnica.class);
                finish();
                startActivity(intent);

            }
        });

        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               List<Integer> list = new ArrayList<Integer>(hashProd.values());
                idProdavnica = list.get(position);
                final String nazivProdavnice = listaImenaProdavnica.get(position);

                client.get(url2, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);



                        try {

                            JSONArray array = response.getJSONArray("proizvod");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject obj = array.getJSONObject(i);
                                // tv2.setText(obj.get("nazivProdavnica").toString());

                                if (idProdavnica == obj.getInt("idProdavnica")) {

                                   // hashProizvod.put(obj.getString("nazivProizvod"), obj.getInt("idProizvod"));
                                    // imenaProdavnica.add(obj.get("nazivProdavnica").toString());
                                    datumiProizvodaNot.add(obj.getString("rokupotrebe"));
                                    stanjeNot.add(obj.getInt("stanje"));
                                    minimumNot.add(obj.getInt("minimum"));
                                    imenaProizvodaNot.add(obj.getString("nazivProizvod"));

                                }
                            }

                            

                            Date today = Calendar.getInstance().getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            ArrayList<String> lista = new ArrayList<String>();
                            ArrayList<String> lista2 = new ArrayList<String>();
                            Date d = null;
                            for (int i=0; i < imenaProizvodaNot.size(); i++) {

                                if (stanjeNot.get(i) < minimumNot.get(i)) {
                                    lista.add(imenaProizvodaNot.get(i));

                                }

                                try {
                                    d = sdf.parse(datumiProizvodaNot.get(i));
                                    if(today.after(d)) {
                                        lista2.add(imenaProizvodaNot.get(i));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }



                            }
                            addNotification(nazivProdavnice,"Stanje ispod minimuma:", lista, 0);
                            addNotification(nazivProdavnice, "Istekao rok:", lista2, 1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        datumiProizvodaNot.clear();
                        stanjeNot.clear();
                        minimumNot.clear();
                        imenaProizvodaNot.clear();


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        super.onSuccess(statusCode, headers, response);


                        try {


                            for (int i = 0; i < response.length(); i++) {

                                if (idProdavnica == response.getJSONObject(i).getInt("idProdavnica")) {

                                    datumiProizvodaNot.add(response.getJSONObject(i).getString("rokupotrebe"));
                                    stanjeNot.add(response.getJSONObject(i).getInt("stanje"));
                                    minimumNot.add(response.getJSONObject(i).getInt("minimum"));
                                    imenaProizvodaNot.add(response.getJSONObject(i).getString("nazivProizvod"));



                                }
                            }


                            Date today = Calendar.getInstance().getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            ArrayList<String> lista = new ArrayList<String>();
                            ArrayList<String> lista2 = new ArrayList<String>();
                            Date d = null;

                            for (int i=0; i < imenaProizvodaNot.size(); i++) {

                                if (stanjeNot.get(i) < minimumNot.get(i)) {
                                    lista.add(imenaProizvodaNot.get(i));

                                }

                                try {
                                    d = sdf.parse(datumiProizvodaNot.get(i));
                                    if(today.after(d)) {
                                        lista2.add(imenaProizvodaNot.get(i));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                            addNotification(nazivProdavnice, "Stanje ispod minimuma:" ,lista, 0);
                            addNotification(nazivProdavnice, "Istekao rok:" ,lista2, 1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        datumiProizvodaNot.clear();
                        stanjeNot.clear();
                        minimumNot.clear();
                        imenaProizvodaNot.clear();

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                        Toast.makeText(Home.this, "Greska prilikom ucitavanja" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(Home.this, "Greska prilikom ucitavanja" , Toast.LENGTH_SHORT).show();
                    }
                });


               // Toast.makeText(Home.this, "Kliknite i zadrzite", Toast.LENGTH_SHORT).show();
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

        List<Integer> listaIDProdavnica = new ArrayList<Integer>(hashProd.values());

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        idProdavnica = listaIDProdavnica.get(info.position);
        nazivProdavniceZaSlanje = listaImenaProdavnica.get(info.position);
        menu.setHeaderTitle(nazivProdavniceZaSlanje);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        return MenuChoiceContext(item);
    }
    //***********

    //opcioni meni
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        CreateMenuOption(menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        return MenuChoiceOption(item);
    }
    //***************

    private void CreateMenuContext(Menu menu){
        menu.setQwertyMode(true);


        if (provera) {

            MenuItem mnu1 = menu.add(0, 0, 0, "Izmeni");
            {

            }


            MenuItem mnu2 = menu.add(0, 1, 1, "Obrisi");
            {

            }

        }
    MenuItem mnu3 = menu.add(0, 2, 2, "Proizvodi");
    {

    }


    }


    private void CreateMenuOption(Menu menu){
        menu.setQwertyMode(true);

if (provera) {

    MenuItem mnu1 = menu.add(0, 0, 0, "Dodaj radnika");
    {


    }

}
        MenuItem mnu2 = menu.add(0, 1, 1, "Odjavi se");{

        }
      /*  MenuItem mnu3 = menu.add(0, 2, 2, "Logout");{

        }*/

    }

    private boolean MenuChoiceOption(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                Intent i = new Intent(Home.this, RadnikAdd.class);
                i.putExtra("idUsera" , idUser);
                finish();
                startActivity(i);
                return  true;
            case 1:
                session.logout();
                return true;
         /*   case 2:


                return true; */

        }
        return false;
    }

    private boolean MenuChoiceContext(MenuItem item){
        switch (item.getItemId()) {
            case 0:

                client.get(url + "/" + idProdavnica, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        String nazivProdavnice = "";
                        String idZaduzenog = "";
                        int idProd = 0;
                        int idUser = 0;

                        try {
                            nazivProdavnice = response.getString("nazivProdavnica");
                            idProd = response.getInt("idProdavnica");
                            idUser = response.optInt("idUser");
                            idZaduzenog = response.getString("idZaduzenog");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(getBaseContext(), ProdavnicaEdit.class);
                        intent.putExtra("nazivProd", nazivProdavnice);
                        intent.putExtra("idProd", idProd);
                        intent.putExtra("idUser", idUser);
                        intent.putExtra("idZaduzenog", idZaduzenog);
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

                client.delete(url + "/" + idProdavnica, new AsyncHttpResponseHandler() {
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

            case 2:

                Intent intent = new Intent(getBaseContext(), ProizvodiHome.class);
                intent.putExtra("idProdavnica" , idProdavnica);
                intent.putExtra("nazivProdavnice", nazivProdavniceZaSlanje);
                intent.putExtra("provera" , provera);
                finish();
                startActivity(intent);

                return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        session.logout();
    }

    public void addNotification(String s1, String opis, ArrayList<String> s2, int i){

    createNotificationChannel();

    // Create an explicit intent for an Activity in your app
    Intent intent = new Intent(this, Home.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String proizvodi = "";

        for (String s : s2) {
            proizvodi += s + " ";
        }

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Naziv prodavnice: " + s1)
            .setContentText(opis + " " + proizvodi )
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(opis + " " + proizvodi ))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            ;

    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
    notificationManager.notify(i , builder.build());


}
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "chanelName";
            String description = "chanelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public String checkDigit (int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

}
