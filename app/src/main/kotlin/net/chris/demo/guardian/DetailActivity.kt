package net.chris.demo.guardian

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import net.chris.demo.guardian.database.Columns

class DetailActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_detail)
        if (fragment.arguments == null) {
            fragment.arguments = Bundle()
        }
        fragment.arguments.putString(Columns.WEB_URL, intent.getStringExtra(Columns.WEB_URL))
    }

}
