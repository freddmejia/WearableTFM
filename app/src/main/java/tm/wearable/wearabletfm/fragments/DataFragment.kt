package tm.wearable.wearabletfm.fragments

import android.content.Context
import android.content.Intent
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
import tm.wearable.wearabletfm.data.interfaces.UIMetric
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
class DataFragment : Fragment(R.layout.data_fragment), UIMetric {
    private var binding: DataFragmentBinding? = null
    private lateinit var toast: Toast
    private val deviceViewModel: DeviceViewModel by viewModels()
    private lateinit var user: User
    private lateinit var metricsGeneralAdapter: MetricsGeneralAdapter

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
        val prefsUser = this@DataFragment.requireContext()?.getSharedPreferences(
            resources.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        user = User(JSONObject(prefsUser!!.getString("user","")))

        metricsGeneralAdapter = MetricsGeneralAdapter(context = this@DataFragment.requireContext(), list =  arrayListOf(), observer = this)
        binding?.rvMetrics?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding?.rvMetrics?.adapter = metricsGeneralAdapter
        binding?.rvMetrics?.setHasFixedSize(true)

        events()
        coroutines()
        callApi()
    }

    fun events() {
        binding?.cvAccessData?.setOnClickListener {
            val actualDate = GregorianCalendar()
            val intent =
                Intent(this@DataFragment.requireContext(), DetailDataActivity::class.java)
            intent.putExtra("date_start", SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(actualDate.time))
            intent.putExtra(
                "date_selected",
                SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(actualDate.time)
            )
            startActivity(intent)
        }
    }

    fun coroutines() {

        lifecycleScope.launchWhenCreated {
            deviceViewModel.compositionMetrics.collect { result->
                when(result){
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<ArrayList<Metrics>, String>> ->{
                        metricsGeneralAdapter.setNewData(arrayList = result.data.data)

                        binding?.rvMetrics?.isVisible = true
                        binding?.cvAccessData?.isVisible = true
                        binding?.noData?.isVisible = false

                    }
                    is tm.wearable.wearabletfm.utils.Result.Error -> {
                        showToast(message = result.error)

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
    }

    fun showToast(message: String){
        toast?.cancel()
        toast = Toast.makeText(this@DataFragment.context,message, Toast.LENGTH_LONG)
        toast.show()
    }


    fun callApi(){
        Log.e("", "callApi: "+user.id.toString() )
        deviceViewModel.fetch_last_metrics_by_user(user_id = user.id.toString())
    }

    override fun onClick(metrics: Metrics) {

    }
}