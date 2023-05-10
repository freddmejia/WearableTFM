package tm.wearable.wearabletfm.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.databinding.WearableFragmentBinding

@AndroidEntryPoint
class WearableFragment : Fragment(R.layout.wearable_fragment) {
    private var binding: WearableFragmentBinding? = null
    companion object{
        fun newInstance(): WearableFragment {
            return WearableFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding = WearableFragmentBinding.bind(view)
        binding = fragmentBinding!!
    }
}