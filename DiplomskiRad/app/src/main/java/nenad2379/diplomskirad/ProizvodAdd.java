package nenad2379.diplomskirad;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;


import android.text.format.DateFormat;
import android.text.format.Time;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import android.view.View;

import net.danlew.android.joda.JodaTimeAndroid;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class ProizvodAdd extends AppCompatActivity {

    static EditText et1,et2,et3,et4;
    Button b1, b2;
    String url = "http://192.168.0.12:8080/RestFul/webresources/ecommerce.proizvod";
    AsyncHttpClient client;
    SessionHandler session;
    JSONObject obj = new JSONObject();
    StringEntity entity = null;
    int idProdavnica;
    ProgressDialog prgDialog;
    String nazivProdavnice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proizvodiadd);



        et1 = (EditText) findViewById(R.id.nazivProizvoda);
        et2 = (EditText) findViewById(R.id.stanje);
        et3 = (EditText) findViewById(R.id.minimum);
        et4 = (EditText) findViewById(R.id.rok);
        b2 = (Button) findViewById(R.id.dodajProizvod);
        b1 = (Button) findViewById(R.id.datum);
        client = new AsyncHttpClient();
        session = new SessionHandler(this);

        nazivProdavnice = getIntent().getStringExtra("nazivProdavnice");

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Sacekajte...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        final HashMap<String, String> hash = session.getUserDetail();
        final String idUser = hash.get(session.ID);
        idProdavnica = getIntent().getIntExtra("idProdavnice", 0);

       // System.out.println("AAAAAAA" + idUser + idProdavnica);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!et1.getText().toString().matches("") && !et2.getText().toString().matches("") && !et3.getText().toString().matches("") &&
                        !et4.getText().toString().matches("")) {

                    try {


                        obj.put("idProdavnica", idProdavnica);
                        obj.put("idUser", Integer.parseInt(idUser));
                        obj.put("nazivProizvod", et1.getText().toString());
                        obj.put("stanje", Integer.parseInt(et2.getText().toString()));
                        obj.put("minimum", Integer.parseInt(et3.getText().toString()));
                        obj.put("rokupotrebe", et4.getText().toString());


                        entity = new StringEntity(obj.toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    prgDialog.show();
                    client.post(ProizvodAdd.this, url, entity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            prgDialog.hide();

                            Toast.makeText(ProizvodAdd.this, "Proizvod uspesno dodat", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent i = new Intent(ProizvodAdd.this, ProizvodiHome.class);
                            i.putExtra("nazivProdavnice", nazivProdavnice);
                            i.putExtra("idProdavnica", idProdavnica);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            prgDialog.hide();
                            Toast.makeText(ProizvodAdd.this, "Neuspesna operacija", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                else {
                    Toast.makeText(ProizvodAdd.this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
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
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        Intent i = new Intent(ProizvodAdd.this, ProizvodiHome.class);
        i.putExtra("idProdavnica", idProdavnica);
        i.putExtra("nazivProdavnice", nazivProdavnice);
        finish();
        startActivity(i);

    }


}
