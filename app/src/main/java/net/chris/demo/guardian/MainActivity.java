package net.chris.demo.guardian;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import net.chris.demo.guardian.model.inject.MainComponent;
import net.chris.demo.guardian.ui.FilterFragment;
import net.chris.demo.guardian.ui.model.ConcreteCommand;
import net.chris.demo.guardian.ui.model.ListViewModel;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Inject
    ListViewModel listViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        MainComponent.getInstance().inject(this);
        listViewModel.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_main_container, MainFragment.create(listViewModel.portrait), "main fragment")
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        swiftScreen();
    }

    @Override
    protected void onDestroy() {
        listViewModel.uninit(this);
        MainComponent.uninject();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showSettingDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        swiftScreen();
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        FilterFragment.create(listViewModel.getFilterViewModel()).show(getSupportFragmentManager(), "filter");
    }

    private void showSettingDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(R.layout.layout_setting)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, (dialog1, which) -> dialog1.dismiss())
                .create();
        listViewModel.getPageSize()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pageSize -> ((EditText) dialog.findViewById(R.id.setting_page_size_ed)).setText(pageSize),
                        throwable -> Log.e(TAG, "get page size error", throwable));
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            try {
                EditText editText = (EditText) dialog.findViewById(R.id.setting_page_size_ed);
                int size = Integer.parseInt(editText.getText().toString());
                if (size < 1 || size > 50) {
                    editText.setError("Page size should be from 1 to 50.");
                } else {
                    ConcreteCommand.create(listViewModel.getReceiverUpdatePageSize(size)).execute();
                    dialog.dismiss();
                }
            } catch (Exception e) {
                Log.e(TAG, "set page size error", e);
            }
        });
    }

    private void swiftScreen() {
        switch (getResources().getConfiguration().orientation) {
            case ORIENTATION_PORTRAIT:
                listViewModel.portrait.set(true);
                break;
            case ORIENTATION_LANDSCAPE:
                listViewModel.portrait.set(false);
                break;
        }
    }
}
