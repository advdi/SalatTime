package iak.advdi.salattime;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DetailFragment extends Fragment {


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        Intent intent = getActivity().getIntent();
        if (bundle != null) {

            int id = bundle.getInt("id");
            String date_for = bundle.getString("date_for");
            String fajr = bundle.getString("fajr");
            String dhuhr = bundle.getString("dhuhr");
            String asr = bundle.getString("asr");
            String maghrib = bundle.getString("maghrib");
            String isha = bundle.getString("isha");

            TextView textview_date_for = (TextView) view.findViewById(R.id.textview_date_for);
            TextView textview_fajr = (TextView) view.findViewById(R.id.textview_fajr);
            TextView textview_dhuhr = (TextView) view.findViewById(R.id.textview_dhuhr);
            TextView textview_asr = (TextView) view.findViewById(R.id.textview_asr);
            TextView textview_maghrib = (TextView) view.findViewById(R.id.textview_maghrib);
            TextView textview_isha = (TextView) view.findViewById(R.id.textview_isha);

            textview_date_for.setText(date_for);
            textview_fajr.setText(fajr);
            textview_dhuhr.setText(dhuhr);
            textview_asr.setText(asr);
            textview_maghrib.setText(maghrib);
            textview_isha.setText(isha);
        }

        return view;
    }

}
