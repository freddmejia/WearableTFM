package tm.wearable.wearabletfm.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import tm.wearable.wearabletfm.data.model.Health
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.databinding.ActivityAddMedicineBinding

import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.adapter.AddMedicineAdapter
import tm.wearable.wearabletfm.data.interfaces.UIMedicine
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.data.model.MedicineDetail
import tm.wearable.wearabletfm.data.viewmodel.MedicineViewModel
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.Utils
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddMedicineActivity : AppCompatActivity() , UIMedicine{
    private lateinit var binding: ActivityAddMedicineBinding
    private lateinit var toast: Toast
    private val medicineViewModel: MedicineViewModel by viewModels()
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private lateinit var addMedicineAdapter: AddMedicineAdapter
    private lateinit var medicinesDetails: ArrayList<MedicineDetail>
    private lateinit var actualDate: GregorianCalendar
    private val pattern = "yyyy-MM-dd"
    private lateinit var  formatter: SimpleDateFormat
    private lateinit var startDate: Calendar
    private lateinit var endDate: Calendar
    private lateinit var calendar: Calendar
    private lateinit var medicine: Medicine

    private var year: Int = 0
    private var month: Int = 0
    private var dayOfMonth: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolBar()
        medicinesDetails = arrayListOf()
        addMedicineAdapter = AddMedicineAdapter(this@AddMedicineActivity, medicinesDetails, this)
        binding.rvMedicineDetails.layoutManager = LinearLayoutManager(this@AddMedicineActivity)
        binding.rvMedicineDetails.adapter = addMedicineAdapter
        actualDate = GregorianCalendar()
        toast = Toast(this)
        formatter = SimpleDateFormat(pattern, Locale.getDefault())
        startDate = Calendar.getInstance()
        endDate = Calendar.getInstance()
        calendar = Calendar.getInstance()

        binding.dateStartMedicine.setText(formatter.format(startDate.time))
        binding.dateFinishMedicine.setText(formatter.format(endDate.time))

        medicine = Medicine()
        intent?.extras?.let { setEditMedicine(it) }
        events()
        coroutines()
    }


    fun setUpToolBar() {
        toolbarAppBinding = MainToolbarBinding.bind(binding.root)
        setSupportActionBar(toolbarAppBinding.toolbar)
        toolbarAppBinding.titleBar.text = resources.getString(R.string.medicine)
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
    fun events() {
        binding.cvSave.setOnClickListener {
            val nameMedicine = binding.nameMedicine.text.toString()
            val dateS = binding.dateStartMedicine.text.toString()
            val dateF = binding.dateFinishMedicine.text.toString()
            val existDayMedSelected = medicinesDetails.filter { it.day >= Utils.monday }
            if (existDayMedSelected.isEmpty()){
                showToast(message = resources.getString(R.string.error_not_day_medicine))
                return@setOnClickListener
            }
            if (nameMedicine.trim().isNotEmpty() && dateS.trim().isNotEmpty()
                && dateF.trim().isNotEmpty()
            ){
                if (medicine.id > 0){
                    medicineViewModel.update_medicine(
                        medicine_id = medicine.id.toString(),
                        name = nameMedicine,
                        date_from = dateS,
                        date_until = dateF,
                        details = medicinesDetails
                    )
                    return@setOnClickListener
                }
                medicineViewModel.store_medicine(
                    name = nameMedicine,
                    date_from = dateS,
                    date_until = dateF,
                    details = medicinesDetails
                )
                return@setOnClickListener
            }
            showToast(message = resources.getString(R.string.error_fill))

        }
        binding.cvDateStartMedicine.setOnClickListener {
            calendar = Calendar.getInstance()
            year = startDate.get(Calendar.YEAR)
            month = startDate.get(Calendar.MONTH)
            dayOfMonth = startDate.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val newDate = formatter.format(calendar.time)
                if (!validateDates(startDate = calendar, endDate = endDate)){
                    showToast(message = resources.getString(R.string.error_dates_same))
                    return@DatePickerDialog
                }
                startDate.time = calendar.time
                Log.e("", "events:formatter startDate "+formatter.format(startDate.time) )
                binding.dateStartMedicine.text = newDate
            }, year, month, dayOfMonth)
            datePicker.datePicker.minDate = actualDate.timeInMillis
            datePicker.show()
        }

        binding.cvDateFinishMedicine.setOnClickListener {
            calendar = Calendar.getInstance()
            year = endDate.get(Calendar.YEAR)
            month = endDate.get(Calendar.MONTH)
            dayOfMonth = endDate.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val newDate = formatter.format(calendar.time)
                if (!validateDates(startDate = startDate, endDate = calendar)){
                    showToast(message = resources.getString(R.string.error_dates_same))
                    return@DatePickerDialog
                }
                endDate.time = calendar.time
                Log.e("", "events:formatter endDate "+formatter.format(endDate.time) )
                binding.dateFinishMedicine.text = newDate
            }, year, month, dayOfMonth)
            datePicker.datePicker.minDate = actualDate.timeInMillis
            datePicker.show()
        }
        binding.addDetail.setOnClickListener {
            if (medicinesDetails.size <= Utils.sunday){
                medicinesDetails.add(
                    MedicineDetail(
                        id = generateId(),
                        medicineId = Utils.noId,
                        hour = SimpleDateFormat("HH:mm").format(actualDate.time),
                        day = Utils.negativeId,
                        isSelected = false
                    )
                )

                addMedicineAdapter.setNewData(medicinesDetails)
                return@setOnClickListener
            }

        }
    }
    fun coroutines() {
        lifecycleScope.launchWhenCreated {
            medicineViewModel.loadingProgress.collect {
                binding.linear.isVisible = !it
                binding.progressBar.isVisible = it
            }
        }

        lifecycleScope.launchWhenCreated {
            medicineViewModel.compositionMedicine.collect { result ->
                when(result) {
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<Medicine, String>> -> {
                        showToast(message = result.data.message)
                        finish()
                    }

                    is tm.wearable.wearabletfm.utils.Result.Error ->{
                        showToast(message = result.error)
                    }
                    else -> Unit
                }
            }
        }


    }

    fun choosDatePicker (medicineDetail: MedicineDetail) {
        val calendar = Calendar.getInstance()

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                // Se actualiza el Calendar con la nueva hora seleccionada
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                // Se actualiza el TextView con la nueva hora
                val hour = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                if (validateDaysAndHour(day = medicineDetail.day, hour = hour)){
                    updateMedicineDetail(
                        id =  medicineDetail.id,
                        hour =  hour,
                        day = medicineDetail.day,
                        isSelected = medicineDetail.isSelected
                    )
                    return@TimePickerDialog
                }
                showToast(message = resources.getString(R.string.error_medicine_detail))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

// Se muestra el diálogo
        timePickerDialog.show()
    }
    override fun chooseDate(medicineDetail: MedicineDetail) {
        choosDatePicker(medicineDetail = medicineDetail)
    }

    override fun chooseDay(medicineDetail: MedicineDetail, day: Int) {
        cancelToast()
        if (validateDaysAndHour(day = day, hour = medicineDetail.hour)) {
            updateMedicineDetail(
                id =  medicineDetail.id,
                hour =  medicineDetail.hour,
                day = day,
                isSelected = !medicineDetail.isSelected
            )
            return
        }
        showToast(message = resources.getString(R.string.error_medicine_detail))

    }

    override fun editDate(medicineDetail: MedicineDetail) {

    }

    override fun deleteDay(medicineDetail: MedicineDetail) {
        medicinesDetails.remove(medicineDetail)
        addMedicineAdapter.setNewData(medicinesDetails)
    }

    fun generateId() : Int {
        var id : Int = UUID.randomUUID().hashCode()
        var findObj = medicinesDetails.filter { it.id == id }.singleOrNull()
        while (findObj != null ) {
            id = UUID.randomUUID().hashCode()
            findObj = medicinesDetails.filter { it.id == id }.singleOrNull()
        }
        return id
    }

    fun validateDaysAndHour(day: Int, hour: String) : Boolean {
        var isPss = medicinesDetails.filter {
            it.hour == hour &&
                    it.day == day &&
                    ( it.day != Utils.negativeId || it.day == Utils.negativeId  )
        }.singleOrNull() == null
        return isPss
    }

    fun showToast(message: String){
        cancelToast()
        toast = Toast.makeText(this@AddMedicineActivity,message, Toast.LENGTH_LONG)
        toast.show()
    }

    fun cancelToast() = toast?.cancel()

    fun updateMedicineDetail(id: Int, hour: String, day: Int, isSelected: Boolean) {
        medicinesDetails.forEach {
            if (it.id == id) {
                it.hour = hour
                it.day = day
                it.isSelected = isSelected
            }
        }
        Log.e("", "chooseDay: #############################################", )
        medicinesDetails.forEach {
            Log.e("", "chooseDay: "+it.day.toString() + " "+it.hour )
        }
        addMedicineAdapter.setNewData(medicinesDetails)
    }

    fun validateDates(startDate: Calendar, endDate: Calendar): Boolean {
        var dateString = formatter.format(startDate.time)
        var date = formatter.parse(dateString)
        val secondsStartDate = date.time / 1000

        dateString = formatter.format(endDate.time)
        date = formatter.parse(dateString)
        val secondsEndDate = date.time / 1000

        if (secondsStartDate > secondsEndDate)
            return false
        return true
    }

    fun setEditMedicine(bundle: Bundle) {
        if (bundle != null){
            val brandType: Type = object : TypeToken<Medicine>() {}.type
            medicine = Gson().fromJson(bundle.getString("medicine"),brandType)
            binding.nameMedicine.setText(medicine.name)
            medicine.details.forEach {
                medicinesDetails.add(
                    MedicineDetail(
                        id = it.id,
                        medicineId = medicine.id,
                        hour = it.hour,
                        day = it.day,
                        isSelected = true
                    )
                )
            }
            addMedicineAdapter.setNewData(medicinesDetails)

            Log.e("", "setEditMedicine: "+medicine.details.joinToString { it.hour + " "+it.day } )

        }
    }

}