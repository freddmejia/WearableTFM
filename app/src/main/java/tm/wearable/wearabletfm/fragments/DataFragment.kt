package tm.wearable.wearabletfm.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import tm.wearable.wearabletfm.DetailDataActivity
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.adapter.DeviceAdapter
import tm.wearable.wearabletfm.data.adapter.MetricsGeneralAdapter
import tm.wearable.wearabletfm.data.adapter.UsersMetricsGenAdapter
import tm.wearable.wearabletfm.data.interfaces.UIMetric
import tm.wearable.wearabletfm.data.interfaces.UIMetricFriend
import tm.wearable.wearabletfm.data.model.Device
import tm.wearable.wearabletfm.data.model.FitbitOauth
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.viewmodel.DeviceViewModel
import tm.wearable.wearabletfm.data.viewmodel.UserViewModel
import tm.wearable.wearabletfm.databinding.DataFragmentBinding
import tm.wearable.wearabletfm.databinding.WearableFragmentBinding
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.WearableDialogs
import java.text.SimpleDateFormat
import java.util.GregorianCalendar

@AndroidEntryPoint
class DataFragment : Fragment(R.layout.data_fragment), UIMetric, UIMetricFriend {
    private var binding: DataFragmentBinding? = null
    private lateinit var toast: Toast
    private val deviceViewModel: DeviceViewModel by viewModels()
    private lateinit var user: User
    private lateinit var metricsGeneralAdapter: MetricsGeneralAdapter
    private lateinit var usersMetricsGenAdapter: UsersMetricsGenAdapter
    private lateinit var prefsUser: SharedPreferences
    companion object{
        fun newInstance(): DataFragment {
            return DataFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentBinding = DataFragmentBinding.bind(view)
        binding = fragmentBinding!!
        toast = Toast(this@DataFragment.requireContext())
        prefsUser = this@DataFragment.requireContext()?.getSharedPreferences(
            resources.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )!!
        user = User(JSONObject(prefsUser!!.getString("user","")))

        metricsGeneralAdapter = MetricsGeneralAdapter(context = this@DataFragment.requireContext(), list =  arrayListOf(), observer = this)
        binding?.rvMetrics?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvMetrics?.adapter = metricsGeneralAdapter
        binding?.rvMetrics?.setHasFixedSize(true)

        usersMetricsGenAdapter = UsersMetricsGenAdapter(context = this@DataFragment.requireContext(), list = arrayListOf(), observer = this)
        binding?.rvUsersMetrics?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding?.rvUsersMetrics?.adapter = usersMetricsGenAdapter

        events()
        coroutines()
        callApi()
    }

    fun goToDetailMetrics(){
        val actualDate = GregorianCalendar()
        val intent =
            Intent(this@DataFragment.requireContext(), DetailDataActivity::class.java)
        intent.putExtra("user", Gson().toJson(user))
        intent.putExtra("date_start", SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(actualDate.time))
        intent.putExtra(
            "date_selected",
            SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(actualDate.time)
        )
        startActivity(intent)
    }
    fun events() {
        binding?.cvAccessData?.setOnClickListener {
            user = User(JSONObject(prefsUser!!.getString("user","")))
            goToDetailMetrics()
        }
    }

    fun coroutines() {
        lifecycleScope.launchWhenCreated {
            deviceViewModel.compositionMetrics.collect { result->
                when(result){
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<ArrayList<Metrics>, String>> ->{
                        metricsGeneralAdapter.setNewData(arrayList = result.data.data)

                        binding?.rvMetrics?.isVisible = true
                        binding?.vytalSignal?.isVisible = true
                        binding?.cvAccessData?.isVisible = true
                        binding?.cvAccessData?.isVisible = true
                        binding?.noData?.isVisible = false

                    }
                    is tm.wearable.wearabletfm.utils.Result.Error -> {
                        showToast(message = result.error)
                        binding?.cvAccessData?.isVisible = false
                        binding?.vytalSignal?.isVisible = false
                        binding?.rvMetrics?.isVisible = false
                        binding?.noData?.isVisible = true
                        binding?.cvAccessData?.isVisible = true
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

        lifecycleScope.launchWhenCreated {
            deviceViewModel.compositionUserFriendsMetrics.collect { result->
                when(result){
                    is tm.wearable.wearabletfm.utils.Result.Success<ArrayList<CompositionObj<User,ArrayList<Metrics>>>> ->{
                        usersMetricsGenAdapter.setNewData(arrayList = result.data)

                        binding?.rvUsersMetrics?.isVisible = true
                        binding?.tvNoDataUsersMetrics?.isVisible = false

                    }
                    is tm.wearable.wearabletfm.utils.Result.Error -> {
                        binding?.rvUsersMetrics?.isVisible = false
                        binding?.tvNoDataUsersMetrics?.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    fun showToast(message: String){
        toast?.cancel()
        toast = Toast.makeText(this@DataFragment.context,message, Toast.LENGTH_LONG)
        toast.show()
    }


    fun callApi(){
        deviceViewModel.fetch_last_metrics_by_user(user_id = user.id.toString())
        deviceViewModel.fetch_last_metrics_by_user_type_date(user_id = user.id.toString())
    }

    override fun onClick(metrics: Metrics) {

    }

    override fun onClick(friendMetric: CompositionObj<User, ArrayList<Metrics>>) {
        user = friendMetric.data
        goToDetailMetrics()
    }

    override fun onResume() {
        super.onResume()
        user = User(JSONObject(prefsUser!!.getString("user","")))
    }
}