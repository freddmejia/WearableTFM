package tm.wearable.wearabletfm

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import tm.wearable.wearabletfm.data.adapter.CalendarAdapter
import tm.wearable.wearabletfm.data.adapter.MetricsGeneralAdapter
import tm.wearable.wearabletfm.data.adapter.MetricsGeneralDetailAdapter
import tm.wearable.wearabletfm.data.interfaces.UICDay
import tm.wearable.wearabletfm.data.interfaces.UIMetric
import tm.wearable.wearabletfm.data.model.CDay
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.viewmodel.CalendarViewModel
import tm.wearable.wearabletfm.data.viewmodel.DeviceViewModel
import tm.wearable.wearabletfm.databinding.ActivityDetailDataBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.utils.CDayUtils
import tm.wearable.wearabletfm.utils.CompositionObj
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

import tm.wearable.wearabletfm.utils.*
@AndroidEntryPoint
class DetailDataActivity : AppCompatActivity(), UICDay, UIMetric {
    private lateinit var binding: ActivityDetailDataBinding
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private val deviceViewModel: DeviceViewModel by viewModels()
    private lateinit var adapterCalendar: CalendarAdapter
    private var daysCalendar : ArrayList<CDay> = arrayListOf()
    private lateinit var metricsGeneralDetailAdapter: MetricsGeneralDetailAdapter
    private var firstDate = GregorianCalendar()
    private var lastDate = GregorianCalendar()
    private var actualDate = GregorianCalendar()
    private lateinit var toast: Toast
    private var idDevice = 0
    private val calendarViewModel: CalendarViewModel by viewModels()
    private var dateSelected = GregorianCalendar()
    private lateinit var user : User
    private lateinit var userLogged : User
    private lateinit var prefsUser: SharedPreferences
    //private  var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolBar()

        prefsUser = getSharedPreferences(
            resources.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )!!
        userLogged = User(JSONObject(prefsUser!!.getString("user","")))

        adapterCalendar = CalendarAdapter(context = this, listDays = daysCalendar, observer = this)
        binding.rvCalendar.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        binding.rvCalendar.adapter = adapterCalendar



        metricsGeneralDetailAdapter = MetricsGeneralDetailAdapter(context = this, list =  arrayListOf(), observer = this)
        binding.rvData.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        binding.rvData.adapter = metricsGeneralDetailAdapter



        intent?.extras?.let { getExtraDeviceData(it) }

        events()
        coroutines()
        callApi()
    }

    fun events() {
        binding?.ivArrowLeft?.setOnClickListener {
            if (daysCalendar.isNotEmpty()) {
                firstDate = GregorianCalendar()
                firstDate = CDayUtils.getDateByNumber(dateSelected = daysCalendar.get(0).date, number = -1)
                calendarViewModel.getActualWeek(dateSelected = firstDate, isPrevious = true)
            }
        }

        binding?.ivArrowRight?.setOnClickListener {
            if (daysCalendar.isNotEmpty()){
                lastDate = GregorianCalendar()
                lastDate = CDayUtils.getDateByNumber(daysCalendar.get(daysCalendar.size -1).date, number = +1)
                val temp = GregorianCalendar()
                temp.time = lastDate.time
                temp.add(Calendar.DATE, +6)
                actualDate = GregorianCalendar()
                if (temp.time < actualDate.time) {
                    calendarViewModel.getActualWeek(dateSelected = lastDate, isPrevious = false, dateToSelect = actualDate)
                }
                else{
                    calendarViewModel.getActualWeek(dateSelected = actualDate, isPrevious = true, dateToSelect = actualDate)
                }
            }
        }

        toolbarAppBinding.calendar.setOnClickListener {
            if (dateSelected == null)
                dateSelected = GregorianCalendar()
            val year = dateSelected.get(Calendar.YEAR)
            val month = dateSelected.get(Calendar.MONTH)
            val day = dateSelected.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(this@DetailDataActivity, DatePickerDialog.OnDateSetListener{
                    view, year, monthOfYear, dayOfMonth ->
                val newC = GregorianCalendar()
                newC.set(GregorianCalendar.YEAR, year)
                newC.set(GregorianCalendar.MONTH, monthOfYear)
                newC.set(GregorianCalendar.DAY_OF_MONTH, dayOfMonth)
                calendarViewModel.dateSelected(dateSelected = newC, true)
            }, year, month, day)
            dpd.datePicker.maxDate = GregorianCalendar().time.time
            dpd.show()
        }
    }

    fun coroutines() {

        lifecycleScope.launchWhenCreated {
            deviceViewModel.compositionMetrics.collect { result->
                when(result){
                    is Result.Success<CompositionObj<ArrayList<Metrics>, String>> ->{

                        metricsGeneralDetailAdapter.setNewData(result.data.data)
                        binding?.rvData?.isVisible = true
                        binding?.noData?.isVisible = false

                    }
                    is Result.Error -> {

                        binding?.rvData?.isVisible = false
                        binding?.noData?.isVisible = true
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.cDays.collect{
                    if (it.size > 0){
                        daysCalendar.clear()
                        daysCalendar.addAll(it)
                        adapterCalendar.setNewData(daysCalendar)
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                calendarViewModel.SelectedDate.observe(this@DetailDataActivity, androidx.lifecycle.Observer {  dateS ->
                    when (dateS) {
                        is Result.Success<*> -> {
                            if (dateS != null) {
                                val result = dateS.data as CalendarViewModel.selectedCalendarByView
                                actualDate = result.date
                                if (result.isSelected) //is date selected by activity
                                {
                                    calendarViewModel.getActualWeek(
                                        dateSelected = actualDate,
                                        isPrevious = false,
                                        dateToSelect = actualDate
                                    )
                                }
                            }
                        }
                        else -> Unit
                    }
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            deviceViewModel.loadingProgress.collect {
                binding?.linear?.isVisible = !it
                binding?.progressBar?.isVisible = it
            }
        }
    }

    fun callApi(){
        deviceViewModel.fetch_last_metrics_by_user(user_id = user.id.toString())
    }


    fun setUpToolBar(){
        toolbarAppBinding = MainToolbarBinding.bind(binding.root)
        setSupportActionBar(toolbarAppBinding.toolbar)
        toolbarAppBinding.titleBar.text = resources.getString(R.string.datos_detail)
        toolbarAppBinding.calendar.isVisible = true
        toolbarAppBinding.profile.isVisible = false
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

    override fun onClick(cDay: CDay) {
        daysCalendar.forEach {
            it.isSelected = false
            if (it.date == cDay.date){
                it.isSelected = true
            }
        }
        adapterCalendar.setNewData(daysCalendar)
    }

    override fun dateChange(cDay: CDay) {
        calendarViewModel.dateSelected(dateSelected = cDay.date)
        deviceViewModel.fetch_metrics_by_user_date(
            user_id = user.id.toString(),
            date = SimpleDateFormat("yyyy-MM-dd").format(cDay.date.time)
        )
    }

    override fun onClick(metrics: Metrics) {
        try {

            val dateSelected = daysCalendar.filter { it.isSelected }.single()
            val intent = Intent(this, SubDetailDataActivity::class.java)
            intent.putExtra("user", Gson().toJson(user))
            intent.putExtra("metrics", Gson().toJson(metrics))
            intent.putExtra("date_start", SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(daysCalendar.get(0).date.time))
            intent.putExtra(
                "date_selected",
                SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(dateSelected.date.time)
            )
            startActivity(intent)
        }catch (e: java.lang.Exception){

        }
    }

    fun getExtraDeviceData(bundle: Bundle){
        if (bundle != null){
            try {
                 val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale("es","ES"))
                dateSelected.time = formatter.parse(bundle.getString("date_selected").toString())

                var date_start = GregorianCalendar()
                date_start.time = formatter.parse(bundle.getString("date_start").toString())
                val userType: Type = object : TypeToken<User>() {}.type
                user = Gson().fromJson(bundle.getString("user"),userType)
                toolbarAppBinding.titleBar.text = if (user.id != userLogged.id) user.name else toolbarAppBinding.titleBar.text
                actualDate = dateSelected
                calendarViewModel.getActualWeek(dateSelected = date_start, isPrevious = false, dateToSelect = actualDate)
            }catch (e: java.lang.Exception){
                Log.e("", "getExtraDeviceData: "+e.message )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            intent?.extras?.let { getExtraDeviceData(it) }
        }catch (e: java.lang.Exception){

        }
    }

}