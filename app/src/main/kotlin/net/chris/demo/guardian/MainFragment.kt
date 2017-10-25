package net.chris.demo.guardian

import android.databinding.DataBindingUtil
import android.databinding.ObservableBoolean
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import net.chris.demo.guardian.databinding.FragmentMainBinding

class MainFragment : RxFragment() {

    private var portrait: ObservableBoolean? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.portrait = portrait
        return binding.root
    }

    companion object {

        fun create(portrait: ObservableBoolean): MainFragment {
            val fragment = MainFragment()
            fragment.portrait = portrait
            return fragment
        }
    }

}
