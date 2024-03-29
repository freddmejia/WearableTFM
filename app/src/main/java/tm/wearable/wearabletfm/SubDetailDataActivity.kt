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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.datatransport.cct.internal.LogEvent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import tm.wearable.wearabletfm.data.adapter.CalendarAdapter
import tm.wearable.wearabletfm.data.adapter.MetricsDetailAdapter
import tm.wearable.wearabletfm.data.adapter.MetricsGeneralDetailAdapter
import tm.wearable.wearabletfm.data.interfaces.UICDay
import tm.wearable.wearabletfm.data.interfaces.UIMetric
import tm.wearable.wearabletfm.data.model.CDay
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.viewmodel.CalendarViewModel
import tm.wearable.wearabletfm.data.viewmodel.DeviceViewModel
import tm.wearable.wearabletfm.databinding.ActivityDetailDataBinding
import tm.wearable.wearabletfm.databinding.ActivitySubDetailDataBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.utils.*
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
@AndroidEntryPoint
class SubDetailDataActivity : AppCompatActivity(), UICDay {

    private lateinit var binding: ActivitySubDetailDataBinding
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private val deviceViewModel: DeviceViewModel by viewModels()
    private lateinit var adapterCalendar: CalendarAdapter
    private var daysCalendar : ArrayList<CDay> = arrayListOf()
    private lateinit var metricsDetailAdapter: MetricsDetailAdapter
    private lateinit var listMetrics : ArrayList<Metrics?>
    private var firstDate = GregorianCalendar()
    private var lastDate = GregorianCalendar()
    private var actualDate = GregorianCalendar()
    private val calendarViewModel: CalendarViewModel by viewModels()
    private var dateSelected = GregorianCalendar()
    private lateinit var metrics: Metrics


    private var offset = 0
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var scrollListener: RecyclerViewLoadMoreScroll
    private var currentPage = 0
    private var hasData = false
    private lateinit var user : User
    private lateinit var userLogged : User
    private lateinit var prefsUser: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubDetailDataBinding.inflate(layoutInflater)
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

        dateSelected = GregorianCalendar()
        actualDate = GregorianCalendar()
        offset = 0
        currentPage = 0
        listMetrics = arrayListOf()
        metrics = Metrics()
        metricsDetailAdapter = MetricsDetailAdapter(context = this, list = listMetrics)
        metricsDetailAdapter.setHasStableIds(true)
        binding.rvData.adapter = metricsDetailAdapter
        setRVLayoutManager()
        setRVScrollListener()

        /*val prefsUser = getSharedPreferences(
            resources.getString(R.string.shared_preferences),
            Context.MODE_PRIVATE
        )
        user = User(JSONObject(prefsUser!!.getString("user","")))*/

        intent?.extras?.let { getExtraDeviceData(it) }

        events()
        coroutines()
    }

    private fun setRVLayoutManager() {
        mLayoutManager = LinearLayoutManager(this)
        binding.rvData.layoutManager = mLayoutManager
        binding.rvData.setHasFixedSize(true)
    }

    private  fun setRVScrollListener() {
        mLayoutManager = LinearLayoutManager(this)
        scrollListener = RecyclerViewLoadMoreScroll(mLayoutManager as LinearLayoutManager)
        scrollListener.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                loadMoreData()
            }
        })
        binding.rvData.addOnScrollListener(scrollListener)
    }

    private fun loadMoreData() {
        if (hasData) {
            //adapter.addLoadingView()
            currentPage = currentPage + 1
            offset = currentPage * Utils.paginationLimit
            deviceViewModel.fetch_last_metrics_by_user_type_date(
                user_id = user.id.toString(),
                date = Utils.parseShortDate(date = actualDate.time),
                limit = Utils.paginationLimit.toString(),
                offset = offset.toString(),
                type = metrics.type,
                loadProgress = false
            )
        }
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
            val dpd = DatePickerDialog(this@SubDetailDataActivity, DatePickerDialog.OnDateSetListener{
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
            repeatOnLifecycle(Lifecycle.State.STARTED){
                deviceViewModel.compositionMetrics.collect{ result->
                    when(result){
                        is Result.Success<CompositionObj<ArrayList<Metrics>, String>> ->{
                            showData()
                            metricsDetailAdapter.addData(newList = result.data.data as ArrayList<Metrics?>)
                            hasData = true
                            scrollListener.setLoaded()
                            binding.rvData.post {
                                metricsDetailAdapter.notifyDataSetChanged()
                            }

                        }
                        is Result.Error ->{
                            if (metricsDetailAdapter.isData() ){
                                showData()
                            }else {
                                metricsDetailAdapter.removeNull()
                                scrollListener.setLoaded()
                                binding?.rvData?.isVisible = false
                                binding?.noData?.isVisible = true
                                hasData = false
                            }
                        }
                        else-> Unit
                    }
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
                calendarViewModel.SelectedDate.observe(this@SubDetailDataActivity, androidx.lifecycle.Observer {  dateS ->
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

        metricsDetailAdapter.setNewD()
        offset = 0
        currentPage = 0
        binding.rvData.recycledViewPool.clear()
        binding.rvData.scrollToPosition(0)
        binding.rvData.clearOnScrollListeners()
        setRVScrollListener()


        deviceViewModel.fetch_last_metrics_by_user_type_date(
            user_id = user.id.toString(),
            limit = Utils.paginationLimit.toString(),
            offset = offset.toString(),
            type = metrics.type,
            date = SimpleDateFormat("yyyy-MM-dd").format(cDay.date.time)
        )
    }

    fun getExtraDeviceData(bundle: Bundle){
        if (bundle != null){
            try {

                val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale("es","ES"))
                dateSelected.time = formatter.parse(bundle.getString("date_selected").toString())

                var date_start = GregorianCalendar()
                date_start.time = formatter.parse(bundle.getString("date_start").toString())

                val metricsType: Type = object : TypeToken<Metrics>() {}.type
                metrics = Gson().fromJson(bundle.getString("metrics"),metricsType)
                val userType: Type = object : TypeToken<User>() {}.type
                user = Gson().fromJson(bundle.getString("user"),userType)
                toolbarAppBinding.titleBar.text = if (user.id != userLogged.id) user.name else toolbarAppBinding.titleBar.text

                binding.tvMeasure.text = resources.getString(R.string.title_from) + " "+ resources.getString(R.string.title_to)+ " "+TypeMetrics.getNameSensor(metrics.type)
                actualDate = dateSelected
                calendarViewModel.getActualWeek(dateSelected = date_start, isPrevious = false, dateToSelect = actualDate)
            }catch (e: java.lang.Exception){
                Log.e("", "getExtraDeviceData: "+e.message )
            }
        }
    }

    fun showData() {
        binding?.rvData?.isVisible = true
        binding?.noData?.isVisible = false

    }

}


interface OnLoadMoreListener {
    fun onLoadMore()
}

class RecyclerViewLoadMoreScroll : RecyclerView.OnScrollListener {

    private var visibleThreshold = 5
    private lateinit var mOnLoadMoreListener: OnLoadMoreListener
    private var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var mLayoutManager: RecyclerView.LayoutManager

    fun setLoaded() {
        isLoading = false
    }

    fun getLoaded(): Boolean {
        return isLoading
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    constructor(layoutManager: LinearLayoutManager) {
        this.mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        totalItemCount = mLayoutManager.itemCount

        if (mLayoutManager is StaggeredGridLayoutManager) {
            val lastVisibleItemPositions =
                (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
            // get maximum element within the list
            lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
        } else if (mLayoutManager is GridLayoutManager) {
            lastVisibleItem = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
        } else if (mLayoutManager is LinearLayoutManager) {
            lastVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            mOnLoadMoreListener.onLoadMore()
            isLoading = true
        }

    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}