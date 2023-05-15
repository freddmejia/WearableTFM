package tm.wearable.wearabletfm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.adapter.AddMedicineAdapter
import tm.wearable.wearabletfm.data.adapter.MedicinesAdapter
import tm.wearable.wearabletfm.data.interfaces.UIMed
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.data.model.MedicineDetail
import tm.wearable.wearabletfm.data.viewmodel.MedicineViewModel
import tm.wearable.wearabletfm.databinding.ActivityAddMedicineBinding
import tm.wearable.wearabletfm.databinding.ActivityMedicineBinding
import tm.wearable.wearabletfm.databinding.MainToolbarBinding
import tm.wearable.wearabletfm.utils.CompositionObj
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
@AndroidEntryPoint
class MedicineActivity : AppCompatActivity(), UIMed {
    private lateinit var binding: ActivityMedicineBinding
    private lateinit var toast: Toast
    private val medicineViewModel: MedicineViewModel by viewModels()
    private lateinit var toolbarAppBinding: MainToolbarBinding
    private lateinit var medicinesAdapter: MedicinesAdapter
    private lateinit var medicines: ArrayList<Medicine>
    private lateinit var actualDate: GregorianCalendar
    private val pattern = "yyyy-MM-dd"
    private lateinit var  formatter: SimpleDateFormat
    private lateinit var startDate: Calendar
    private lateinit var endDate: Calendar
    private lateinit var calendar: Calendar

    private var year: Int = 0
    private var month: Int = 0
    private var dayOfMonth: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolBar()
        toast = Toast(this)
        medicines = arrayListOf()
        medicinesAdapter = MedicinesAdapter(this, medicines, this)
        binding.rvMedicineDetails.layoutManager = LinearLayoutManager(this)
        binding.rvMedicineDetails.adapter = medicinesAdapter

        api()
        events()
        coroutines()

    }

    fun setUpToolBar() {
        toolbarAppBinding = MainToolbarBinding.bind(binding.root)
        setSupportActionBar(toolbarAppBinding.toolbar)
        toolbarAppBinding.titleBar.text = resources.getString(R.string.medicines)
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

    fun api() {
        medicineViewModel.fetch_medicines_by_user()
    }

    fun events() {
        binding.addMedicine.setOnClickListener {
            startActivity(
                Intent(this@MedicineActivity, AddMedicineActivity::class.java)
            )
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
            medicineViewModel.compositionMedicines.collect { result->
                when(result){
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<ArrayList<Medicine>, String>> ->{
                        medicines.clear()
                        medicines.addAll(result.data.data)
                        medicinesAdapter.setNewData(medicines)
                    }
                    is tm.wearable.wearabletfm.utils.Result.Error ->
                        showToast(message = result.error)
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            medicineViewModel.compositionMedicine.collect { result->
                when(result){
                    is tm.wearable.wearabletfm.utils.Result.Success<CompositionObj<Medicine, String>> ->{
                        deleteMedicine(medicineId = result.data.data.id)
                        medicinesAdapter.setNewData(medicines)
                    }
                    is tm.wearable.wearabletfm.utils.Result.Error ->
                        showToast(message = result.error)
                    else -> Unit
                }
            }
        }

    }

    fun showToast(message: String){
        toast?.cancel()
        toast = Toast.makeText(this,message, Toast.LENGTH_LONG)
        toast.show()
    }

    override fun onResume() {
        super.onResume()
        api()
    }

    override fun edit(medicine: Medicine) {
        val intent = Intent(this, AddMedicineActivity::class.java)
        intent.putExtra("medicine", Gson().toJson(medicine))
        startActivity(
            intent
        )
    }

    override fun delete(medicine: Medicine) {
        medicineViewModel.delete_medicine_by_user(medicine_id = medicine.id.toString())
    }

    fun deleteMedicine(medicineId: Int){
        val med = medicines.filter { it.id == medicineId }.singleOrNull()
        if (med != null){
            medicines.remove(med)
        }
    }

}