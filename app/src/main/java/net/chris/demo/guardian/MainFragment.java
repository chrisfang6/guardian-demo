package net.chris.demo.guardian;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import net.chris.demo.guardian.databinding.FragmentMainBinding;

import butterknife.ButterKnife;

public class MainFragment extends RxFragment {

    private ObservableBoolean portrait;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        ButterKnife.bind(this, binding.getRoot());
        binding.setPortrait(portrait);
        return binding.getRoot();
    }

    public static MainFragment create(@NonNull ObservableBoolean portrait) {
        MainFragment fragment = new MainFragment();
        fragment.portrait = portrait;
        return fragment;
    }

}
