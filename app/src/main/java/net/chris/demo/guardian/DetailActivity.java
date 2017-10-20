package net.chris.demo.guardian;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import static net.chris.demo.guardian.database.Columns.WEB_URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
        if (fragment.getArguments() == null) {
            fragment.setArguments(new Bundle());
        }
        fragment.getArguments().putString(WEB_URL, getIntent().getStringExtra(WEB_URL));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
