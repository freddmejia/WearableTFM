package tm.wearable.wearabletfm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import org.json.JSONObject
import tm.wearable.wearabletfm.data.adapter.CalendarAdapter
import tm.wearable.wearabletfm.data.model.CDay
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.viewmodel.CalendarViewModel
import tm.wearable.wearabletfm.data.viewmodel.DeviceViewModel
import tm.wearable.wearabletfm.databinding.ActivityDetailDataBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.utils.CompositionObj
import java.text.SimpleDateFormat
import java.util.*

class DetailDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailDataBinding
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private val deviceViewModel: DeviceViewModel by viewModels()
    private lateinit var adapterCalendar: CalendarAdapter
    private var daysCalendar : ArrayList<CDay> = arrayListOf()

    private var firstDate = GregorianCalendar()
    private var lastDate = GregorianCalendar()
    private var actualDate = GregorianCalendar()
    private lateinit var toast: Toast
    private var idDevice = 0
    private val calendarViewModel: CalendarViewModel by viewModels()
    private var dateSelected = GregorianCalendar()
    private lateinit var user : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolBar()

        adapterCalendar = CalendarAdapter(context = this, listDays = daysCalendar, observer = this)
        binding.rvCalendar.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding.rvCalendar.adapter = adapterCalendar

        val prefsUser = .getSharedPreferences(
            resources.getString(R.string.title_preference_file),
            Context.MODE_PRIVATE
        )
        user = User(JSONObject(prefsUser!!.getString("user","")))

        events()
        coroutines()
        callApi()
    }

    fun events() {

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
        toast = Toast.makeText(this,message, Toast.LENGTH_LONG)
        toast.show()
    }


    fun callApi(){
        Log.e("", "callApi: "+user.id.toString() )
        deviceViewModel.fetch_last_metrics_by_user(user_id = user.id.toString())
    }


    fun setUpToolBar(){
        toolbarAppBinding = MainToolbarBinding.bind(binding.root)
        setSupportActionBar(toolbarAppBinding.toolbar)
        toolbarAppBinding.titleBar.text = resources.getString(R.string.datos_detail)
        toolbarAppBinding.profile.isVisible = false
        toolbarAppBinding.calendar.isVisible = false
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->
            {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }



}