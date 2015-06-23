package rayacevedo45.c4q.nyc.art;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class Cards extends ActionBarActivity {
    TextView welcome,horoscopeTV;
    private String name,birthdayS,zipcodeS,userSign;

    public static final String[] CARDS = {"To-Do List", "Horoscope", "Weather", "Stocks"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        //ExpandableListView listView = (ExpandableListView) findViewById(R.id.listview);

        Bundle extras = getIntent().getExtras();
        name = extras.getString("name");
        birthdayS = extras.getString("birthday");
        zipcodeS = extras.getString("zipcode");

        welcome = (TextView) findViewById(R.id.welcomeTV);
        welcome.setText("Hello, " + name);

        horoscopeTV = (TextView) findViewById(R.id.horoscopeTVID);



        findUserSign();

        AsyncTime getDialyHoroscope = new AsyncTime();
        getDialyHoroscope.execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cards, menu);
        return true;
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
    public void findUserSign(){
        String month = birthdayS.substring(0, 2);
        Log.d("month", month);
        String day = birthdayS.substring(3, 5);
        Log.d("day", day);
        int monthInt = Integer.parseInt(month);
        int bdayInt = Integer.parseInt(day);

        if (monthInt == 1 && bdayInt >= 21) {
            userSign = "aquarius";
        }
        else if (monthInt == 2 && bdayInt <= 19) {
            userSign = "aquarius";
        }
        else if (monthInt == 2 && bdayInt >= 21) {
            userSign = "pisces";
        }
        else if (monthInt == 3 && bdayInt <= 19) {
            userSign = "pisces";
        }
        else if (monthInt == 3 && bdayInt >= 21) {
            userSign = "aries";
        }
        else if (monthInt == 4 && bdayInt <= 19) {
            userSign = "aries";
        }
        else if (monthInt == 4 && bdayInt >= 20) {
            userSign = "taurus";
        }
        else if (monthInt == 5 && bdayInt <= 20) {
            userSign = "taurus";
        }
        else if (monthInt == 5 && bdayInt >= 21) {
            userSign = "gemini";
        }
        else if (monthInt == 6 && bdayInt <= 21) {
            userSign = "gemini";
        }
        else if (monthInt == 6 && bdayInt >= 22) {
            userSign = "cancer";
        }
        else if (monthInt == 7 && bdayInt <= 22) {
            userSign = "cancer";
        }
        else if (monthInt == 7 && bdayInt >= 23) {
            userSign = "leo";
        }
        else if (monthInt == 8 && bdayInt <= 22) {
            userSign = "leo";
        }
        else if (monthInt == 8 && bdayInt >= 23) {
            userSign = "virgo";
        }
        else if (monthInt == 9 && bdayInt <= 22) {
            userSign = "virgo";
        }
        else if (monthInt == 9 && bdayInt >= 23) {
            userSign = "libra";
        }
        else if (monthInt == 10 && bdayInt <= 22) {
            userSign = "libra";
        }
        else if (monthInt == 10 && bdayInt >= 23) {
            userSign = "scorpio";
        }
        else if (monthInt == 11 && bdayInt <= 21) {
            userSign = "scorpio";
        }
        else if (monthInt == 11 && bdayInt >= 22){
            userSign = "sagittarius";
        }
        else if (monthInt == 12 && bdayInt <= 19){
            userSign = "sagittarius";
        }
        else {
            userSign = "capricorn";
        }
    }
    public class AsyncTime extends AsyncTask<Void, Void, String> {
        @Override
        public String doInBackground(Void... voids) {

            try {
                String webpage = "http://widgets.fabulously40.com/horoscope.json?sign=" + userSign;
                Log.d("$$$", webpage);
                URL url = new URL(webpage);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                String Json = readStream(connection.getInputStream());
                Log.d("|||", Json);
                JSONObject horoscope = new JSONObject(Json);

                JSONObject dailyHoroscopeObject = horoscope.getJSONObject("horoscope");
                String dailyHoroscope = dailyHoroscopeObject.getString("horoscope");
                Log.d("^^^",dailyHoroscope);


                return dailyHoroscope;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public void onPostExecute(String s) {
            horoscopeTV.setText(s);
            

        }

        private String readStream(InputStream in) throws IOException {
            char[] buffer = new char[1024 * 4];
            InputStreamReader reader = new InputStreamReader(in, "UTF8");
            StringWriter writer = new StringWriter();
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            return writer.toString();
        }
    }
}


