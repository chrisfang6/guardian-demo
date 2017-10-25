package net.chris.demo.guardian

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.trello.rxlifecycle2.components.support.RxFragment
import kotterknife.bindView
import net.chris.demo.guardian.database.Columns.Companion.WEB_URL
import net.chris.demo.guardian.system.Constant.Companion.ACTION_SHOW_WEB

class DetailFragment : RxFragment() {

    val webView: WebView by bindView(R.id.detail_web)

    private lateinit var handler: Handler
    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler()
        localBroadcastManager = LocalBroadcastManager.getInstance(activity)
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val url = intent.getStringExtra(WEB_URL)
                handler.post { loadUrl(url) }
            }
        }
        localBroadcastManager.registerReceiver(broadcastReceiver, IntentFilter(ACTION_SHOW_WEB))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onResume() {
        super.onResume()
        try {
            loadUrl(arguments?.getString(WEB_URL))
        } catch (e: Exception) {
            Log.e(TAG, "get url failed", e)
        }

    }

    override fun onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    private fun loadUrl(url: String?) {
        webView.stopLoading()
        if (url != null) {
            webView.loadUrl(url)
        }
    }

    companion object {
        private val TAG = DetailFragment::class.java.simpleName
    }
}
