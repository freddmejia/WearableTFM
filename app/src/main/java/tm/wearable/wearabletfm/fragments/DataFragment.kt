package tm.wearable.wearabletfm.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.databinding.DataFragmentBinding

@AndroidEntryPoint
class DataFragment : Fragment(R.layout.data_fragment) {
    private var binding: DataFragmentBinding? = null
    companion object{
        fun newInstance(): DataFragment {
            return DataFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding = DataFragmentBinding.bind(view)
        binding = fragmentBinding!!
    }
}