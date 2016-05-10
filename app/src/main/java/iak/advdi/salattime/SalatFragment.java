package iak.advdi.salattime;

import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class SalatFragment extends Fragment {

    public ListAdapter listAdapter;
    private ListView listView;
    private ArrayList<SalatTime> salatTimes;
    DBHelper dbh;

    public SalatFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*FetchSalatTask fetchSalatTask = new FetchSalatTask();
        fetchSalatTask.execute();*/

        dbh = new DBHelper(getActivity());
        try {
            dbh.createDatabase();;
        }
        catch (IOException e) {
            throw new Error("Unable to create database");
        }
        try {
            dbh.openDatabase();
        }
        catch(SQLException e) {
            throw e;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*salatTimes = dbh.getAllData();*/
        salatTimes = new ArrayList<>();

        FetchSalatTask fetchSalatTask = new FetchSalatTask();
        fetchSalatTask.execute();

        listAdapter = new ListAdapter(this.getActivity(), salatTimes);

        View view = inflater.inflate(R.layout.fragment_salat, container, false);

        listView = (ListView) view.findViewById(R.id.listview_salat);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putInt("id", salatTimes.get(i).getId());
                bundle.putString("date_for", salatTimes.get(i).getDate_for());
                bundle.putString("fajr", salatTimes.get(i).getFajr());
                bundle.putString("dhuhr", salatTimes.get(i).getDhuhr());
                bundle.putString("asr", salatTimes.get(i).getAsr());
                bundle.putString("maghrib", salatTimes.get(i).getMaghrib());
                bundle.putString("isha", salatTimes.get(i).getIsha());

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;
    }

    public class FetchSalatTask extends AsyncTask<String, Void, ArrayList<SalatTime>> {

        private final String LOG_TAG = FetchSalatTask.class.getSimpleName();

        private ArrayList<SalatTime> getSalatTimeDataFromJson(String salatJsonStr) throws JSONException {

            final String TAG_ITEMS = "items";
            final String TAG_DATE_FOR = "date_for";
            final String TAG_FAJR = "fajr";
            final String TAG_DHUHR = "dhuhr";
            final String TAG_ASR = "asr";
            final String TAG_MAGHRIB = "maghrib";
            final String TAG_ISHA = "isha";

            JSONObject salatJson = new JSONObject(salatJsonStr);
            JSONArray salatArray = salatJson.getJSONArray(TAG_ITEMS);

            ArrayList<SalatTime> salatTimesList = new ArrayList<>();
            for (int i = 0; i < salatArray.length(); i++) {

                String date_for;
                String fajr, dhuhr, asr, maghrib, isha;

                JSONObject daySalat = salatArray.getJSONObject(i);
                date_for = daySalat.getString(TAG_DATE_FOR);
                fajr = daySalat.getString(TAG_FAJR);
                dhuhr = daySalat.getString(TAG_DHUHR);
                asr = daySalat.getString(TAG_ASR);
                maghrib = daySalat.getString(TAG_MAGHRIB);
                isha = daySalat.getString(TAG_ISHA);

                salatTimesList.add(
                        new SalatTime(i, date_for, fajr, dhuhr, asr, maghrib, isha)
                );
            }

            return  salatTimesList;
        }

        @Override
        protected ArrayList<SalatTime> doInBackground(String... params) {

            if(params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String salatJsonStr = null;

            try {

                final String SALAT_BASE_URL = "http://muslimsalat.com/surabaya/monthly.json?key=aa35192db0a6a4561c8ea1470b5369b9";
                URL url = new URL(SALAT_BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                salatJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getSalatTimeDataFromJson(salatJsonStr);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<SalatTime> result) {
            if (result != null) {
                salatTimes.clear();
                for(SalatTime salatTime : result) {
                    salatTimes.add(salatTime);
                    dbh.insert(
                            salatTime.getDate_for(),
                            salatTime.getFajr(),
                            salatTime.getDhuhr(),
                            salatTime.getAsr(),
                            salatTime.getMaghrib(),
                            salatTime.getIsha()
                    );
                }
                listAdapter.notifyDataSetChanged();
                listView.invalidate();
            }
        }
    }
}