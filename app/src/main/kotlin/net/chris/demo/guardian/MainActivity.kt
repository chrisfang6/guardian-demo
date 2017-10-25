package net.chris.demo.guardian

import android.content.DialogInterface
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import kotterknife.bindView
import net.chris.demo.guardian.model.inject.MainComponent
import net.chris.demo.guardian.ui.FilterFragment
import net.chris.demo.guardian.ui.model.ConcreteCommand
import net.chris.demo.guardian.ui.model.ListViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var listViewModel: ListViewModel

    val fab: FloatingActionButton by bindView(R.id.fab)
    val toolbar: Toolbar by bindView(R.id.toolbar)
    val drawer: DrawerLayout by bindView(R.id.drawer_layout)
    val navigationView: NavigationView by bindView(R.id.nav_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainComponent.inject(this)
        listViewModel.init(this)

        setSupportActionBar(toolbar)

        fab.setOnClickListener({ FilterFragment.create(listViewModel.getFilterViewModel()).show(supportFragmentManager, "filter") })

        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.activity_main_container, MainFragment.create(listViewModel.portrait), "main fragment")
                .commit()
    }

    override fun onResume() {
        super.onResume()
        swiftScreen()
    }

    override fun onDestroy() {
        listViewModel.uninit(this)
        MainComponent.unInject()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> showSettingDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        swiftScreen()
    }

    private fun showSettingDialog() {
        val dialog = AlertDialog.Builder(this)
                .setView(R.layout.layout_setting)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel) { dialog1, _ -> dialog1.dismiss() }
                .create()
        listViewModel.pageSize
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pageSize -> (dialog.findViewById<EditText>(R.id.setting_page_size_ed))?.setText(pageSize) }
                ) { throwable -> Log.e(TAG, "get page size error", throwable) }
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener { _ ->
            try {
                val editText = dialog.findViewById<EditText>(R.id.setting_page_size_ed)
                val size = Integer.parseInt(editText?.text.toString())
                if (size < 1 || size > 50) {
                    editText?.error = "Page size should be from 1 to 50."
                } else {
                    ConcreteCommand.create(listViewModel.getReceiverUpdatePageSize(size)).execute()
                    dialog.dismiss()
                }
            } catch (e: Exception) {
                Log.e(TAG, "set page size error", e)
            }
        }
    }

    private fun swiftScreen() {
        when (resources.configuration.orientation) {
            ORIENTATION_PORTRAIT -> listViewModel.portrait.set(true)
            ORIENTATION_LANDSCAPE -> listViewModel.portrait.set(false)
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
