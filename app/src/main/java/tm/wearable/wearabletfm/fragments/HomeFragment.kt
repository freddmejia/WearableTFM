package tm.wearable.wearabletfm.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.databinding.HomeFragmentBinding

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment) {
    private var binding: HomeFragmentBinding ? = null
    companion object{
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding = HomeFragmentBinding.bind(view)
        binding = fragmentBinding!!
    }
}