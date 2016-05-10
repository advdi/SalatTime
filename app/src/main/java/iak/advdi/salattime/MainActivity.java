package iak.advdi.salattime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            SalatFragment salatFragment = new SalatFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, salatFragment)
                    .commit();
        }
    }
}
