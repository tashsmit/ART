package rayacevedo45.c4q.nyc.art;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


public class Cards extends ActionBarActivity {

    TextView date, time, location, temp, amPm, day1, day2, day3, day4, day5, day6, date1, date2, date3, date4, date5, date6, newsTV, newsTV2, temp_1, temp_2, temp_3, temp_4, temp_5, temp_6;
    private String weatherAPI, sevenDayForecast, dateStr, city, tempStr, amPmStr, temp1, temp2, temp3, temp4, temp5, temp6, descrip1, descrip2, descrip3, descrip4, descrip5, descrip6;
    TextView welcome, horoscopeTV;
    private String name, birthdayS, zipcodeS, userSign, timeFormatS, degreeS, weatherDescription;
    private JSONParser parser;
    private JSONObject dailyHoroscopeObject, weatherAPIObject, sevenDayForecastObject;
    private String dailyHoroscopeString, linkS, abstractS;
    private double currentTemp;
    boolean night;
    CardView horoscopeCV, weatherCard, cardView4, stocksCV, topCV;
    LinearLayout top;
    CalendarView cv;
    Calendar rightNow;
    View weather_layout, sevenDayView;
    ArrayList<String> daysofWeek;
    ArrayList<TextView> daysofWeekTextViews, datesofWeekTextViews;
    ArrayList<Integer> shortImageList, fullImageList;
    ListView todoList;
    ImageView todayWeather, forecast1, forecast2, forecast3, forecast4, forecast5, forecast6;
    ;
    private ArrayList mNotes;
    private ArrayAdapter basicAdapter;
    private ListView stockLV;
    private TextView stockInfoTV;
    private ArrayList mStocks;
    private StockAdapter stockAdapter;
    private String stockParams;


//    public static final String MyPREFERENCES = "MyPrefs" ;
//    SharedPreferences sharedpreferences;


    public static final String[] CARDS = {"To-Do List", "Horoscope", "Weather", "Stocks"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        initializeViewsAndValues();
        parser = new JSONParser();
        AsyncTime getDailyHoroscope = new AsyncTime();
        getDailyHoroscope.execute();

        AsyncCurrentWeather getTodayWeather = new AsyncCurrentWeather();
        getTodayWeather.execute();
        AsyncSevenDayWeather getSevenDayWeather = new AsyncSevenDayWeather();
        getSevenDayWeather.execute();


        AsyncStocks getStocks = new AsyncStocks();
        getStocks.execute();

        //when weather card is click, show seven day view with elongated background
        weatherCard = (CardView) findViewById(R.id.weather_card);
        weather_layout = findViewById(R.id.main_view);
        sevenDayView = findViewById(R.id.sevenday_view);
        weatherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sevenDayView.getVisibility() == View.VISIBLE) {
                    sevenDayView.setVisibility(View.GONE);
                    weather_layout.setBackgroundResource(R.drawable.aurora_short);
                } else {
                    sevenDayView.setVisibility(View.VISIBLE);
                    weather_layout.setBackgroundResource(R.drawable.aurora_full);
                }
            }
        });


        parser = new JSONParser();


        final CheckBox fbCheck = (CheckBox) findViewById(R.id.fbStockButton_id);
        CheckBox twitcheck = (CheckBox) findViewById(R.id.twitstockbutton_id);
        CheckBox corningcheck = (CheckBox) findViewById(R.id.corningStockButton_id);
        CheckBox teslacheck = (CheckBox) findViewById(R.id.teslaStockButton_id);
        fbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(fbCheck.isChecked()){
                 //
                }
            }
        });

        Handler handler = new Handler();
        //handler.postDelayed()
        Runnable runnable = new Runnable() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(Cards.this);


                builder.setContentTitle("ART");
                builder.setContentText("Start wrapping up your awesome demo.");
                builder.setSmallIcon(R.drawable.monarealframe);

                //Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.monarealframe);
                //builder.setLargeIcon(largeIcon);
                builder.setPriority(2);
                builder.setLights(Color.BLUE, 500, 500);


                Notification notification = builder.build();
                nm.notify(1, notification);
            }
        };
        handler.postDelayed(runnable, 60000 * 4);


        //can set conditions that this loads the screen for
        // creating a new note or it shows the list depending on current content.
        ImageButton NoteTest = (ImageButton) findViewById(R.id.openListButton);
        NoteTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cards.this, NoteListActivity.class);
                startActivity(intent);
            }
        });

        ImageButton addNewNoteButton = (ImageButton) findViewById(R.id.openNoteButton);
        addNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note();
                note.setTitle("");
                NotePad.get(getApplicationContext()).addNote(note);
                Intent i = new Intent(getApplicationContext(), NotePagerActivity.class);
                i.putExtra(NoteFragment.EXTRA_NOTE_ID, note.getId());
                startActivityForResult(i, 0);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        basicAdapter.notifyDataSetChanged();
    }


    public void initializeViewsAndValues() {
        stockInfoTV = (TextView) findViewById(R.id.stockInfo_id);
        mNotes = NotePad.get(getApplicationContext()).getNotes();
        mStocks = new ArrayList<Stock>();
        welcome = (TextView) findViewById(R.id.welcomeTV);
        horoscopeCV = (CardView) findViewById(R.id.card_view2);
        horoscopeTV = (TextView) findViewById(R.id.horoscopeTVID);
        top = (LinearLayout) findViewById(R.id.calendarLL);
        //cv = (CalendarView) findViewById(R.id.cv);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        location = (TextView) findViewById(R.id.location);
        temp = (TextView) findViewById(R.id.temperature);
        amPm = (TextView) findViewById(R.id.am_pm);
        weatherCard = (CardView) findViewById(R.id.weather_card);
        day1 = (TextView) findViewById(R.id.dayOne);
        day2 = (TextView) findViewById(R.id.dayTwo);
        day3 = (TextView) findViewById(R.id.dayThree);
        day4 = (TextView) findViewById(R.id.dayFour);
        day5 = (TextView) findViewById(R.id.dayFive);
        day6 = (TextView) findViewById(R.id.daySix);
        date1 = (TextView) findViewById(R.id.dateOne);
        date2 = (TextView) findViewById(R.id.dateTwo);
        date3 = (TextView) findViewById(R.id.dateThree);
        date4 = (TextView) findViewById(R.id.dateFour);
        date5 = (TextView) findViewById(R.id.dateFive);
        date6 = (TextView) findViewById(R.id.dateSix);
        temp_1 = (TextView) findViewById(R.id.dayoneTemp);
        temp_2 = (TextView) findViewById(R.id.daytwoTemp);
        temp_3 = (TextView) findViewById(R.id.daythreeTemp);
        temp_4 = (TextView) findViewById(R.id.dayfourTemp);
        temp_5 = (TextView) findViewById(R.id.dayfiveTemp);
        temp_6 = (TextView) findViewById(R.id.daysixTemp);
        forecast1 = (ImageView) findViewById(R.id.dayone_icon);
        forecast2 = (ImageView) findViewById(R.id.daytwo_icon);
        forecast3 = (ImageView) findViewById(R.id.daythree_icon);
        forecast4 = (ImageView) findViewById(R.id.dayfour_icon);
        forecast5 = (ImageView) findViewById(R.id.dayfive_icon);
        forecast6 = (ImageView) findViewById(R.id.daysix_icon);
        todoList = (ListView) findViewById(R.id.todoListView);
        newsTV = (TextView) findViewById(R.id.newTVID);
        cardView4 = (CardView) findViewById(R.id.card_view4);
        stocksCV = (CardView) findViewById(R.id.card_viewStocks);
        topCV = (CardView) findViewById(R.id.topID);
        todayWeather = (ImageView) findViewById(R.id.main_forecast);

        stockLV = (ListView) findViewById(R.id.stockLV_id);

        basicAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mNotes);
        todoList.setAdapter(basicAdapter);
        todoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Cards.this, NotePagerActivity.class);
                Note c = (Note) todoList.getItemAtPosition(position);
                intent.putExtra(NoteFragment.EXTRA_NOTE_ID, c.getId());
                startActivity(intent);

            }
        });

        SharedPreferences settings;
        settings = Cards.this.getSharedPreferences("PREFS_NAME", 0);
        name = settings.getString("name", "");
        birthdayS = settings.getString("bday", "");
        zipcodeS = settings.getString("zipcode", "");
        Log.d("!!!", zipcodeS);
        timeFormatS = settings.getString("timeformat", "");
        Log.d("@@@", timeFormatS);
        degreeS = settings.getString("degree", "");
        Log.d("###", degreeS);


        Bundle extras = getIntent().getExtras();

        welcome.setText("Hello, " + name);

        findUserSign();
        intializeDateTime();


        topCV.setOnTouchListener(new OnSwipeTouchListener(Cards.this) {
            @Override
            public void onSwipeLeft() {
                topCV.setVisibility(View.GONE);
            }
        });
//        weatherCard.setOnTouchListener(new OnSwipeTouchListener(Cards.this) {
//            @Override
//            public void onSwipeLeft() {
//                weatherCard.setVisibility(View.GONE);
//            }
//        });
//        stocksCV.setOnTouchListener(new OnSwipeTouchListener(Cards.this) {
//            @Override
//            public void onSwipeLeft() {
//                stocksCV.setVisibility(View.GONE);
//            }
//        });
        horoscopeCV.setOnTouchListener(new OnSwipeTouchListener(Cards.this) {
            @Override
            public void onSwipeLeft() {
                horoscopeCV.setVisibility(View.GONE);
            }
        });
        cardView4.setOnTouchListener(new OnSwipeTouchListener(Cards.this) {
            @Override
            public void onSwipeLeft() {
                cardView4.setVisibility(View.GONE);
            }
        });
    }

    public void SetSevenDayInfo() {

    }


    public void findUserSign() {
        String month = birthdayS.substring(0, 2);
        //Log.d("{{{",month);
        Log.d("month", month);
        String day = birthdayS.substring(3, 5);
        Log.d("day", day);

        //Log.d("month", month);

        //Log.d("day", day);
        int monthInt = Integer.parseInt(month);
        int bdayInt = Integer.parseInt(day);

        if (monthInt == 1 && bdayInt >= 21) {
            userSign = "aquarius";
        } else if (monthInt == 2 && bdayInt <= 19) {
            userSign = "aquarius";
        } else if (monthInt == 2 && bdayInt >= 21) {
            userSign = "pisces";
        } else if (monthInt == 3 && bdayInt <= 19) {
            userSign = "pisces";
        } else if (monthInt == 3 && bdayInt >= 21) {
            userSign = "aries";
        } else if (monthInt == 4 && bdayInt <= 19) {
            userSign = "aries";
        } else if (monthInt == 4 && bdayInt >= 20) {
            userSign = "taurus";
        } else if (monthInt == 5 && bdayInt <= 20) {
            userSign = "taurus";
        } else if (monthInt == 5 && bdayInt >= 21) {
            userSign = "gemini";
        } else if (monthInt == 6 && bdayInt <= 21) {
            userSign = "gemini";
        } else if (monthInt == 6 && bdayInt >= 22) {
            userSign = "cancer";
        } else if (monthInt == 7 && bdayInt <= 22) {
            userSign = "cancer";
        } else if (monthInt == 7 && bdayInt >= 23) {
            userSign = "leo";
        } else if (monthInt == 8 && bdayInt <= 22) {
            userSign = "leo";
        } else if (monthInt == 8 && bdayInt >= 23) {
            userSign = "virgo";
        } else if (monthInt == 9 && bdayInt <= 22) {
            userSign = "virgo";
        } else if (monthInt == 9 && bdayInt >= 23) {
            userSign = "libra";
        } else if (monthInt == 10 && bdayInt <= 22) {
            userSign = "libra";
        } else if (monthInt == 10 && bdayInt >= 23) {
            userSign = "scorpio";
        } else if (monthInt == 11 && bdayInt <= 21) {
            userSign = "scorpio";
        } else if (monthInt == 11 && bdayInt >= 22) {
            userSign = "sagittarius";
        } else if (monthInt == 12 && bdayInt <= 19) {
            userSign = "sagittarius";

        } else {
            userSign = "capricorn";
        }
    }

    public void intializeDateTime() {

        //get day of week and display in main day of week Textview
        rightNow = Calendar.getInstance();
        int dayofweek = rightNow.get(Calendar.DAY_OF_WEEK);

        //set up arraylists
        daysofWeek = new ArrayList<String>();

        daysofWeek.add("Sun");
        daysofWeek.add("Mon");
        daysofWeek.add("Tues");
        daysofWeek.add("Wed");
        daysofWeek.add("Thu");
        daysofWeek.add("Fri");
        daysofWeek.add("Sat");

        daysofWeekTextViews = new ArrayList<TextView>();
        daysofWeekTextViews.add(day1);
        daysofWeekTextViews.add(day2);
        daysofWeekTextViews.add(day3);
        daysofWeekTextViews.add(day4);
        daysofWeekTextViews.add(day5);
        daysofWeekTextViews.add(day6);

        datesofWeekTextViews = new ArrayList<TextView>();
        datesofWeekTextViews.add(date1);
        datesofWeekTextViews.add(date2);
        datesofWeekTextViews.add(date3);
        datesofWeekTextViews.add(date4);
        datesofWeekTextViews.add(date5);
        datesofWeekTextViews.add(date6);


        if (dayofweek == 1) {
            dateStr = daysofWeek.get(0) + ", ";
        } else if (dayofweek == 2) {
            dateStr = daysofWeek.get(1) + ", ";
        } else if (dayofweek == 3) {
            dateStr = daysofWeek.get(2) + ", ";
        } else if (dayofweek == 4) {
            dateStr = daysofWeek.get(3) + ", ";
        } else if (dayofweek == 5) {
            dateStr = daysofWeek.get(4) + ", ";
        } else if (dayofweek == 6) {
            dateStr = daysofWeek.get(5) + ", ";
        } else {
            dateStr = daysofWeek.get(6) + ", ";
        }

        //get date and display in main date textView
        int month = rightNow.get(Calendar.MONTH);
        if (month == 0) {
            dateStr += "January ";
        } else if (month == 1) {
            dateStr += "February ";
        } else if (month == 2) {
            dateStr += "March ";
        } else if (month == 3) {
            dateStr += "April ";
        } else if (month == 4) {
            dateStr += "May ";
        } else if (month == 5) {
            dateStr += "June ";
        } else if (month == 6) {
            dateStr += "July ";
        } else if (month == 7) {
            dateStr += "August ";
        } else if (month == 8) {
            dateStr += "September ";
        } else if (month == 9) {
            dateStr += "October ";
        } else if (month == 10) {
            dateStr += "November ";
        } else {
            dateStr += "December ";
        }

        //iterate through textviews of seven day forecast DAYS and put in correct day of week and date
        for (int i = 0; i < 6; i++) {
            if (dayofweek == 7) {
                dayofweek = 0;
            }
            daysofWeekTextViews.get(i).setText(daysofWeek.get(dayofweek));
            dayofweek++;
        }

        for (int i = 0; i < 6; i++) {
            int m = 1;
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, m);
            String formattedDate = sdf.format(cal.getTime());
            datesofWeekTextViews.get(i).setText(formattedDate);
            dayofweek++;
            m++;
        }


        dateStr += rightNow.get(Calendar.DAY_OF_MONTH);
        date.setText(dateStr);

        //format the time depending on military boolean
        if (timeFormatS.equals("12hr time format")) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            if (sdf.format(rightNow.getTime()).startsWith("0")) {
                time.setText(sdf.format(rightNow.getTime()).substring(1));
            } else {
                time.setText(sdf.format(rightNow.getTime()));
            }
            int hours = rightNow.get(Calendar.HOUR);
            int AM_orPM = rightNow.get(Calendar.AM_PM);
            if (hours < 12) {
                if (AM_orPM == 1) {
                    amPmStr = "PM";
                }
            } else {
                if (AM_orPM == 0) {
                    amPmStr = "AM";
                }

            }
            amPm.setText(amPmStr);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
            time.setText(sdf.format(rightNow.getTime()));
            amPm.setText("");
        }

        //determine if its night or day
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour < 6 || hour > 18) {
            night = true;
        } else {
            night = false;
        }
    }


    public class AsyncTime extends AsyncTask<Void, Void, HashMap> {
        String caption, link;


        //public class AsyncTime extends AsyncTask<Void, Void, String> {

        @Override
        public HashMap doInBackground(Void... voids) {

            //parse urls into json objects

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String sdfS = (sdf.format(date));
            Log.d("!!!", sdfS);
            String month2 = sdfS.substring(5, 7);
            String day2 = sdfS.substring(8, 10);


            //parse urls into json objects
            //http://widgets.fabulously40.com/horoscope.json?sign=aries&date=2008-01-01
            // http://widgets.fabulously40.com/horoscope.json?sign=aries&date=2014-12-30
            String horoscopeAPISite = "http://widgets.fabulously40.com/horoscope.json?sign=" + userSign + "&date=2010-" + month2 + "-" + day2;
            Log.d("+++", horoscopeAPISite);

            //determine which APIs to use depending on celsius boolean

            //create hashmap to hold results
            HashMap JSONresults = new HashMap();
//            dailyStockObject = parser.parse(stockAPISite);
//            dailyWeatherObject = parser.parse(weatherAPIObject);
            try {
                dailyHoroscopeObject = parser.parse(horoscopeAPISite);
                JSONObject dailyHoroscope = dailyHoroscopeObject.getJSONObject("horoscope");
                dailyHoroscopeString = dailyHoroscope.getString("horoscope");
                Log.d("test horoscope", dailyHoroscopeString);

                //Ray - parsing nY TIMES URL
                String timesUrl = "http://api.nytimes.com/svc/news/v3/content/all/all/720.json?limit=1&offset=1&api-key=6a53d7200f35783a967c577bd64357d5%3A14%3A72395287";
                JSONObject newsJsonObject = parser.parse(timesUrl);
                //new JSONObject(newsJson);
                JSONArray results = newsJsonObject.getJSONArray("results");
                JSONObject firstItem = results.getJSONObject(0);
                caption = firstItem.getString("abstract");
                link = firstItem.getString("url");


            } catch (Exception e) {

            }

            JSONresults.put("horoscopeString", dailyHoroscopeString);
//            Log.d("{}|", dailyHoroscopeString);
            JSONresults.put("caption", caption);
            JSONresults.put("link", link);

            return JSONresults;
        }

        public void appendNewStockstoParam(String param) {
            param = stockParams; //fixme
        }


        @Override
        public void onPostExecute(HashMap s) {

            try {
                String horoscopeString = "";
                String dailyHoroscopeString3 = "";
                if (s.get("horoscopeString").toString() != null) {
                    horoscopeString = s.get("horoscopeString").toString();
                    Log.d("!@!", horoscopeString);
                    dailyHoroscopeString3 = horoscopeString.replace("&apos;", "\'");


                } else {
                    dailyHoroscopeString3 = getString(R.string.horoscopeDefault);
                }
                horoscopeTV.setText(userSign + " daily horoscope \n" + dailyHoroscopeString3);
                Log.d("@#@", dailyHoroscopeString3);

                horoscopeTV.setText(userSign.toUpperCase() + " DAILY HOROSCOPE \n" + "\n" + "     " + dailyHoroscopeString3);
                abstractS = "     " + s.get("caption");
                linkS = s.get("link").toString();
                newsTV.setText("Trending on nytimes.com \n \n" + abstractS + "\n \n" + "Read Full Story:\n " + linkS);



                linkS = s.get("link").toString();
                newsTV.setText("Trending on nytimes.com \n \n" + abstractS + "\n \n" + "Read Full Story:\n " + linkS);


            } catch (Exception e) {

            }
        }


    }


    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }


    public class AsyncStocks extends AsyncTask<Void, Void, ArrayList> {
        String stockAPI_URL = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.quote%20where%20symbol%20in%20(%22";


        @Override
        protected ArrayList doInBackground(Void... params) {
            try {
                //add new stocks to beginning of listview like so: "GLW%22%2C%22"
                String stockParams = "YHOO%22%2C%22AAPL%22%2C%22GOOG%22%2C%22MSFT%22)";
                String stockFormat = "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
                JSONObject dailyStockObject = parser.parse(stockAPI_URL + stockParams + stockFormat);
                JSONObject dailyStocksQuery = dailyStockObject.getJSONObject("query");
                JSONObject resultsJSONObject = dailyStocksQuery.getJSONObject("results");
                JSONArray stocksJSONArray = resultsJSONObject.getJSONArray("quote");

                for (int i = 0; i < stocksJSONArray.length(); i++) {
                    JSONObject x = (JSONObject) stocksJSONArray.get(i);
                    String caption = x.getString("symbol");
                    String daysHigh = x.getString("DaysHigh");
                    String daysLow = x.getString("DaysLow");
                    String yearLow = x.getString("YearLow");
                    String yearHigh = x.getString("YearHigh");
                    String marketcAP = x.getString("MarketCapitalization");
                    String lastTradePrice = x.getString("LastTradePriceOnly");


                    //( String id, String lastTradePriceOnly, String yearLow, String daysHigh, String daysLow, String yearHigh, String daysRange, String change)
                    Stock y = new Stock(caption, daysHigh, daysLow, yearHigh, yearLow, marketcAP, lastTradePrice);

                    mStocks.add(y);
                }
            } catch (Exception e) {

            }

            return mStocks;
        }

        @Override
        protected void onPostExecute(ArrayList s) {
            ArrayList<Stock> stockArrayList = new ArrayList<>();
            for (int i = 0; i < s.size(); i++) {
                stockArrayList.add(new Stock(s.get(i).toString()));
            }

            try {
                if (stockArrayList != null) {
                    stockAdapter = new StockAdapter(stockArrayList);
                    stockLV.setAdapter(stockAdapter);
                    stockInfoTV.setText("Powered by Yahoo Finance" + "\nConnect to the Internet");
                    Stock x = stockAdapter.getItem(0);
                    stockInfoTV.append(x.toString()); //by default show the top of my list.
                } else {
                    ArrayList default_stockArrayList = new ArrayList();
                    stockArrayList.add(new Stock("YHOO"));
                    stockArrayList.add(new Stock("AAPL"));
                    stockArrayList.add(new Stock("GOOG"));
                    stockArrayList.add(new Stock("MSFT"));
                    StockAdapter defaultstockAdapter = new StockAdapter(default_stockArrayList);
                    stockLV.setAdapter(defaultstockAdapter);
                }


            }   catch (Exception e){

             }

            stockLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Stock x = stockAdapter.getItem(position);
                    stockInfoTV.setText("Powered by Yahoo Finance");
                    stockInfoTV.append(x.toString());   //edit so it shows full
                    Log.d("test stock id", id + " ");
                }
            });
        }

    }

    public class StockAdapter extends ArrayAdapter<Stock> {

        public int getCount() {
            return mStocks.size();
        }

        @Override
        public Stock getItem(int position) {
            return (Stock) mStocks.get(position);
        }


        public StockAdapter(ArrayList<Stock> stocks) {
            super(getApplicationContext(), 0, stocks);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_stock, parent, false); //try list_item_note nxt
            }
            Stock c = getItem(position);


            TextView stockTitle = (TextView) convertView.findViewById(R.id.stockViewText);
            stockTitle.setText(c.getId());

            return convertView;
        }
    }

    public void setForecastImage(String description, ImageView image) {
        if (description.contains("rain") || description.contains("drizzle")) {
            if (!night) {
                image.setBackgroundResource(R.drawable.night_rain);
            } else {
                image.setBackgroundResource(R.drawable.day_thunderstorm);
            }
        } else if (description.contains("thunderstorm")) {
            if (!night) {
                image.setBackgroundResource(R.drawable.night_thunder_storm);
            } else {
                image.setBackgroundResource(R.drawable.day_thunderstorm);
            }
        } else if (description.contains("snow") || description.contains("sleet")) {
            if (!night) {
                image.setBackgroundResource(R.drawable.day_snow);
            } else {
                image.setBackgroundResource(R.drawable.night_snow);
            }
        } else {
            if (!night) {
                image.setBackgroundResource(R.drawable.day_sunny);
            } else {
                //image.setBackgroundResource(R.drawable.day_sunny);
                image.setBackgroundResource(R.drawable.night_clear);
            }
        }
    }

    public void setSevenDayImage(String description, ImageView image) {
        if (description.contains("rain") || description.contains("drizzle")) {
            image.setBackgroundResource(R.drawable.day_thunderstorm);
        } else if (description.contains("thunderstorm")) {
            image.setBackgroundResource(R.drawable.day_thunderstorm);
        } else if (description.contains("snow") || description.contains("sleet")) {
            image.setBackgroundResource(R.drawable.day_snow);
        } else {
            //image.setBackgroundResource(R.drawable.day_sunny);
            image.setBackgroundResource(R.drawable.day_sunny);
        }
    }

    public class AsyncCurrentWeather extends AsyncTask<Void, Void, HashMap> {

        //public class AsyncTime extends AsyncTask<Void, Void, String> {

        @Override
        public HashMap doInBackground(Void... voids) {

            //parse urls into json objects
            //determine which APIs to use depending on celsius boolean
            if (degreeS.equals("F")) {
                weatherAPI = "http://api.openweathermap.org/data/2.5/weather?zip=" + zipcodeS + ",us&units=imperial";
            } else {
                weatherAPI = "http://api.openweathermap.org/data/2.5/weather?zip=" + zipcodeS + ",us&units=metric";
            }

            //determine which APIs to use depending on celsius boolean
            weatherAPIObject = parser.parse(weatherAPI);
            sevenDayForecastObject = parser.parse(sevenDayForecast);

            //create hashmap to hold results
            HashMap JSONresults = new HashMap();

            try {

                //For current information: get user city, current temp and current description
                city = weatherAPIObject.getString("name");
                //get description of weather from api
                JSONArray weather = weatherAPIObject.getJSONArray("weather");
                JSONObject description = weather.getJSONObject(0);
                weatherDescription = description.getString("description");
                //get current temperature from weather api adn format according to user preference
                JSONObject main = weatherAPIObject.getJSONObject("main");
                currentTemp = main.getDouble("temp");

                if (degreeS.equals("F")) {
                    tempStr = String.valueOf(currentTemp).split("\\.")[0] + "°F";
                } else {
                    tempStr = String.valueOf(currentTemp).split("\\.")[0] + "°C";
                }
                JSONresults.put("currentTemp", tempStr);
                JSONresults.put("userCity", city);
                JSONresults.put("weatherDescription", weatherDescription);
            } catch (Exception e) {

            }


            return JSONresults;
        }

        @Override
        public void onPostExecute(HashMap s) {
            try {
                location.setText(s.get("userCity").toString());
                temp.setText(s.get("currentTemp").toString());

                //display correct forecast icon for main forecast image view
                setForecastImage(s.get("weatherDescription").toString(), todayWeather);
                // setForecastImage("rain", todayWeather);

            } catch (Exception e) {

            }

        }
    }

    public class AsyncSevenDayWeather extends AsyncTask<Void, Void, HashMap> {

        //public class AsyncTime extends AsyncTask<Void, Void, String> {

        @Override
        public HashMap doInBackground(Void... voids) {

            //parse urls into json objects
            //determine which APIs to use depending on celsius boolean and user city
            try {
                city = city.replace(" ", "%20");
                if (degreeS.equals("F")) {
                    sevenDayForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + ",US&cnt=7&units=imperial";

                } else {
                    sevenDayForecast = "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + city + ",US&cnt=7&units=metric";
                }

                //determine which APIs to use depending on celsius boolean
                weatherAPIObject = parser.parse(weatherAPI);

            } catch(Exception e){

            }

            //create hashmap to hold results
            HashMap JSONresults = new HashMap();
            /***** Parse the JSON ******/
            // You can consolidate all of your parsing code into the following code.
            // Let me know if you have questions or run into any issues.

            try {
                sevenDayForecastObject = parser.parse(sevenDayForecast);
                if (sevenDayForecast != null) {
                    JSONArray days = sevenDayForecastObject.getJSONArray("list");

                    for (int i = 1; i < days.length(); i++) {
                        JSONObject day = days.getJSONObject(i);

                        // Parse temperature
                        JSONObject temperature = day.getJSONObject("temp");
                        double min = temperature.getDouble("min");
                        double max = temperature.getDouble("max");
                        Log.d("Seven Day", "Day: " + i + " min: " + min + " max " + max);
                        //convert tempertures into one string
                        String newTemp = Double.toString(min).substring(0, 2) + "/" + Double.toString(max).substring(0, 2);

                        // Parse weather
                        JSONArray weathers = day.getJSONArray("weather");
                        // Only care about the first weather description, otherwise we can loop through this array
                        JSONObject weather = weathers.getJSONObject(0);
                        String description = weather.getString("description");
                        Log.d("Seven Day", "Weather description: " + description);

                        // Store this information in your HashMap.
                        // I would make the keys something like:
                        JSONresults.put("day_" + i + "_temp", newTemp);
                        JSONresults.put("day_" + i + "_desc", description);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return JSONresults;
        }

        @Override
        public void onPostExecute(HashMap s) {
            try {
                //display correct forecast icon for main forecast image view
                //setForecastImage(weatherDescription, todayWeather);

                temp_1.setText(s.get("day_1_temp").toString());
                temp_2.setText(s.get("day_2_temp").toString());
                temp_3.setText(s.get("day_3_temp").toString());
                temp_4.setText(s.get("day_4_temp").toString());
                temp_5.setText
                        (s.get("day_5_temp").toString());
                temp_6.setText(s.get("day_6_temp").toString());
//
                setSevenDayImage((s.get("day_1_desc").toString()), forecast1);
                setSevenDayImage((s.get("day_2_desc").toString()), forecast2);
                setSevenDayImage((s.get("day_3_desc").toString()), forecast3);
                setSevenDayImage((s.get("day_4_desc").toString()), forecast4);
                setSevenDayImage((s.get("day_5_desc").toString()), forecast5);
                setSevenDayImage((s.get("day_6_desc").toString()), forecast6);
            } catch (Exception e) {

            }

        }
    }
}