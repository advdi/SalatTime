package iak.advdi.salattime;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;


public class SalatFragment extends Fragment {

    private ArrayAdapter<String> mSalatAdapter;
    DBHelper dbh;

    public SalatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dbh = new DBHelper(getActivity());
        List<String> weekSalat = dbh.getAllData();
        mSalatAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_salat,
                R.id.list_item_salat_textview,
                weekSalat
        );

        View view = inflater.inflate(R.layout.fragment_salat, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listview_salat);
        listView.setAdapter(mSalatAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String salat = mSalatAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, salat);
                startActivity(intent);
            }
        });

        return view;
    }

    public class FetchSalatTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchSalatTask.class.getSimpleName();

        private String[] getSalatDataFromJson(String salatJsonStr) throws JSONException {

            final String TAG_ITEMS = "items";
            final String TAG_DATE_FOR = "date_for";
            final String TAG_FAJR = "fajr";
            final String TAG_DHUHR = "dhuhr";
            final String TAG_ASR = "asr";
            final String TAG_MAGHRIB = "maghrib";
            final String TAG_ISHA = "isha";

            JSONObject salatJson = new JSONObject(salatJsonStr);
            JSONArray salatArray = salatJson.getJSONArray(TAG_ITEMS);

            String resultStr[] = new String[salatArray.length()];
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
                resultStr[i] = date_for;
            }

            return resultStr;
        }

        @Override
        protected String[] doInBackground(String... params) {

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
                return getSalatDataFromJson(salatJsonStr);
            }
            catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                mSalatAdapter.clear();
                for(String daySalatStr : result) {
                    mSalatAdapter.add(daySalatStr);
                }
            }
        }
    }
}