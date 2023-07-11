package tm.wearable.wearabletfm.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import org.json.JSONObject
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.adapter.DeviceAdapter
import tm.wearable.wearabletfm.data.model.Device
import tm.wearable.wearabletfm.data.model.FitbitOauth
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.viewmodel.DeviceViewModel
import tm.wearable.wearabletfm.data.viewmodel.FriendViewModel
import tm.wearable.wearabletfm.data.viewmodel.UserViewModel
import tm.wearable.wearabletfm.databinding.WearableFragmentBinding
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.WearableDialogs

@AndroidEntryPoint
class WearableFragment : Fragment(R.layout.wearable_fragment) {
    private var binding: WearableFragmentBinding? = null
    private lateinit var toast: Toast
    private val userViewModel: UserViewModel by viewModels()
    private val deviceViewModel: DeviceViewModel by viewModels()
    private lateinit var user: User
    private lateinit var deviceAdapter: DeviceAdapter
    companion object{
        fun newInstance(): WearableFragment {
            return WearableFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding = WearableFragmentBinding.bind(view)
        binding = fragmentBinding!!
        toast = Toast(this@WearableFragment.requireContext())
        val prefsUser = this@WearableFragment.requireContext()?.getSharedPreferences(
            resources.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        user = User(JSONObject(prefsUser!!.getString("user","")))
        deviceAdapter = DeviceAdapter(context = this@WearableFragment.requireContext(), list =  arrayListOf())
        binding?.rvWearables?.layoutManager = LinearLayoutManager(this@WearableFragment.requireContext())
        binding?.rvWearables?.adapter = deviceAdapter

        events()
        coroutines()
        callApi()
    }

    fun events() {
        binding?.addWearable?.setOnClickListener {
            userViewModel.fitbit_oauth(user_id = user.id.toString())
        }
    }

    fun coroutines() {
        lifecycleScope.launchWhenCreated {
            userViewModel.compositionFitbitOauth.collect { result->
                when(result){
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<FitbitOauth, String>> ->
                        {
                            Log.e("", "coroutines: "+result.data.data.url )
                            Log.e("", "coroutines: "+result.data.data.codeChallenge )
                            Log.e("", "coroutines: "+result.data.data.codeVerifier )
                            openView(url = result.data.data.url)
                        }
                    else ->Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            deviceViewModel.compositionDevices.collect { result->
                when(result){
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<ArrayList<Device>, String>> ->{
                        deviceAdapter.setNewData(arrayList = result.data.data)

                        binding?.rvWearables?.isVisible = true
                        binding?.placeholder?.isVisible = false
                    }
                    is tm.wearable.wearabletfm.utils.Result.Error -> {
                        binding?.rvWearables?.isVisible = false
                        binding?.placeholder?.isVisible = true
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            deviceViewModel.loadingProgress.collect {
                binding?.linear?.isVisible = !it
                binding?.progressBar?.isVisible = it
            }
        }
    }

    fun showToast(message: String){
        toast?.cancel()
        toast = Toast.makeText(this@WearableFragment.context,message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun openView(url: String){
        WearableDialogs.openURLEnWebView2(context = this@WearableFragment.requireContext(), url = url)
    }

    fun callApi(){
        Log.e("", "callApi: "+user.id.toString() )
        deviceViewModel.fetch_devices(user_id = user.id.toString())
    }
}