package net.chris.demo.guardian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.chris.demo.guardian.database.Columns.WEB_URL;
import static net.chris.demo.guardian.system.Constant.ACTION_SHOW_WEB;

public class DetailFragment extends RxFragment {

    private static final String TAG = DetailFragment.class.getSimpleName();

    @BindView(R.id.detail_web)
    WebView webView;

    private Handler handler;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String url = intent.getStringExtra(WEB_URL);
                handler.post(() -> loadUrl(url));
            }
        };
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(ACTION_SHOW_WEB));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            loadUrl(getArguments().getString(WEB_URL));
        } catch (Exception e) {
            Log.e(TAG, "get url failed", e);
        }
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private void loadUrl(String url) {
        webView.stopLoading();
        webView.loadUrl(url);
    }
}
