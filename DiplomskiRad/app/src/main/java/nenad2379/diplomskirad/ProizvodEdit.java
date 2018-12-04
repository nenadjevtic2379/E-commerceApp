package nenad2379.diplomskirad;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Korsn on 8.11.2018..
 */

public class ProizvodEdit extends AppCompatActivity {

    EditText et1,et2,et3;
    static EditText et4;
    Button b1, b2;
    AsyncHttpClient client;
    SessionHandler session;
    String url = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.proizvod";
    ProgressDialog prgDialog;
    JSONObject obj = new JSONObject();
    StringEntity entity = null;
    String imeProdavnice;
    int idProdavnica;
    boolean provera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proizvodiedit);

        et1 = (EditText) findViewById(R.id.nazivProizvodaEdit);
        et2 = (EditText) findViewById(R.id.stanjeEdit);
        et3 = (EditText) findViewById(R.id.minimumEdit);
        et4 = (EditText) findViewById(R.id.rokEdit);
        b2 = (Button) findViewById(R.id.dodajProizvodEdit);
        b1 = (Button) findViewById(R.id.datumEdit);
        client = new AsyncHttpClient();
        session = new SessionHandler(this);

        final int idProizvod = getIntent().getIntExtra("idProizvoda", 0);
        final int idUser = getIntent().getIntExtra("idUser", 0);
        idProdavnica = getIntent().getIntExtra("idProd", 0);
        final int minimum = getIntent().getIntExtra("minimum", 0);
        final int stanje = getIntent().getIntExtra("stanje", 0);
        final String nazivProizvoda = getIntent().getStringExtra("nazivProizvoda");
        final String rok = getIntent().getStringExtra("rok");
        imeProdavnice = getIntent().getStringExtra("nazivProdavnice");
        provera = getIntent().getBooleanExtra("provera", true);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Sacekajte...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        et1.setText(nazivProizvoda);
        et2.setText(String.valueOf(stanje));
        et3.setText(String.valueOf(minimum));
        et4.setText(rok);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et1.getText().toString().matches("") && !et2.getText().toString().matches("") && !et3.getText().toString().matches("") &&
                        !et4.getText().toString().matches("")) {

                    try {
                        obj.put("idProizvod", idProizvod);
                        obj.put("idUser", idUser);
                        obj.put("idProdavnica", idProdavnica);
                        obj.put("nazivProizvod", et1.getText().toString());
                        obj.put("rokupotrebe", et4.getText().toString());
                        obj.put("stanje", Integer.parseInt(et2.getText().toString()));
                        obj.put("minimum", Integer.parseInt(et3.getText().toString()));

                        entity = new StringEntity(obj.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    prgDialog.show();
                    client.put(ProizvodEdit.this, url + "/" + idProizvod, entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            prgDialog.hide();

                            Intent intent = new Intent(ProizvodEdit.this, ProizvodiHome.class);
                            intent.putExtra("idProdavnica", idProdavnica);
                            intent.putExtra("nazivProdavnice", imeProdavnice);
                            intent.putExtra("provera", provera);
                            Toast.makeText(ProizvodEdit.this, "Proizvod uspesno izmenjen", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            prgDialog.hide();
                            Toast.makeText(ProizvodEdit.this, "Neuspesna operacija", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else {
                    Toast.makeText(ProizvodEdit.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public String checkDigit (int number) {
            return number <= 9 ? "0" + number : String.valueOf(number);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            Calendar cal = Calendar.getInstance();


            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DATE);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month,day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            et4.setText(String.valueOf(checkDigit(year)) + "-" + String.valueOf(checkDigit(month+1)) + "-" + String.valueOf(checkDigit(day)));
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new ProizvodEdit.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent i = new Intent(ProizvodEdit.this, ProizvodiHome.class);
        i.putExtra("idProdavnica", idProdavnica);
        i.putExtra("nazivProdavnice", imeProdavnice);
        i.putExtra("provera", provera);
        finish();
        startActivity(i);

    }


}
