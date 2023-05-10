package tm.wearable.wearabletfm.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.databinding.FriendFragmentBinding

@AndroidEntryPoint
class FriendFragment: Fragment(R.layout.friend_fragment) {
    private var binding: FriendFragmentBinding? = null
    companion object{
        fun newInstance(): FriendFragment {
            return FriendFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding = FriendFragmentBinding.bind(view)
        binding = fragmentBinding!!
    }
}