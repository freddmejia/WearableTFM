package tm.wearable.wearabletfm.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.data.model.MedicineDetail
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.repository.repo.MedicineRepository
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.Result
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository
) : ViewModel() {

    private val _compositionMedicine = MutableStateFlow<Result<CompositionObj<Medicine, String>>>(Result.Empty)
    val compositionMedicine : StateFlow<Result<CompositionObj<Medicine, String>>> = _compositionMedicine

    private val _loadingProgress = MutableStateFlow(false)
    val loadingProgress: StateFlow<Boolean> = _loadingProgress

    private val _compositionMedicines = MutableStateFlow<Result<CompositionObj<ArrayList<Medicine>, String>>>(Result.Empty)
    val compositionMedicines : StateFlow<Result<CompositionObj<ArrayList<Medicine>, String>>> = _compositionMedicines

    fun store_medicine(name: String, date_from: String, date_until: String, details: ArrayList<MedicineDetail>) = viewModelScope.launch {
        _compositionMedicine.value = Result.Empty
        _loadingProgress.value = true
        _compositionMedicine.value = medicineRepository.store_medicine(name = name, date_from = date_from, date_until = date_until, details = details)
        _loadingProgress.value = false
    }

    fun update_medicine(medicine_id: String,name: String, date_from: String, date_until: String, details: ArrayList<MedicineDetail>) = viewModelScope.launch {
        _compositionMedicine.value = Result.Empty
        _loadingProgress.value = true
        _compositionMedicine.value = medicineRepository.update_medicine( medicine_id = medicine_id,name = name, date_from = date_from, date_until = date_until, details = details)
        _loadingProgress.value = false
    }

    fun fetch_three_last_medicines_by_user() = viewModelScope.launch {
        _compositionMedicines.value = Result.Empty
        _loadingProgress.value = true
        _compositionMedicines.value = medicineRepository.fetch_three_last_medicines_by_user()
        _loadingProgress.value = false
    }

    fun delete_medicine_by_user(medicine_id: String) = viewModelScope.launch {
        _compositionMedicine.value = Result.Empty
        _loadingProgress.value = true
        _compositionMedicine.value = medicineRepository.delete_medicine_by_user(medicine_id = medicine_id)
        _loadingProgress.value = false
    }

    fun fetch_medicines_by_user() = viewModelScope.launch {
        _compositionMedicines.value = Result.Empty
        _loadingProgress.value = true
        _compositionMedicines.value = medicineRepository.fetch_medicines_by_user()
        _loadingProgress.value = false
    }
}