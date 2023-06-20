package tm.wearable.wearabletfm.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tm.wearable.wearabletfm.data.model.*
import tm.wearable.wearabletfm.data.repository.repo.DeviceRepository
import tm.wearable.wearabletfm.data.repository.repo.UserRepository
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.Result
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
)  : ViewModel() {
    private val _compositionDevices = MutableStateFlow<Result<CompositionObj<ArrayList<Device>, String>>>(Result.Empty)
    val compositionDevices : StateFlow<Result<CompositionObj<ArrayList<Device>, String>>> = _compositionDevices

    private val _compositionMetrics = MutableStateFlow<Result<CompositionObj<ArrayList<Metrics>, String>>>(Result.Empty)
    val compositionMetrics :  StateFlow<Result<CompositionObj<ArrayList<Metrics>, String>>> = _compositionMetrics


    private val _compositionUserFriendsMetrics = MutableStateFlow<Result<ArrayList<CompositionObj<User, ArrayList<Metrics>>>>>(Result.Empty)
    val compositionUserFriendsMetrics :  StateFlow<Result<ArrayList<CompositionObj<User, ArrayList<Metrics>>>>> = _compositionUserFriendsMetrics


    private val _loadingProgress = MutableStateFlow(false)
    val loadingProgress: StateFlow<Boolean> = _loadingProgress

    private val _loadingProgressSpe = MutableStateFlow(false)
    val loadingProgressSpe: StateFlow<Boolean> = _loadingProgressSpe

    fun fetch_devices(user_id: String) = viewModelScope.launch {
        _compositionDevices.value = Result.Empty
        _loadingProgress.value = true
        _compositionDevices.value = deviceRepository.fetch_devices(user_id = user_id)
        _loadingProgress.value = false
    }

    fun fetch_metrics_by_user_date(user_id: String, date: String) = viewModelScope.launch {
        _compositionMetrics.value = Result.Empty
        _loadingProgress.value = true
        _compositionMetrics.value = deviceRepository.fetch_metrics_by_user_date(user_id = user_id, date = date)
        _loadingProgress.value = false
    }

    fun fetch_last_metrics_by_user(user_id: String) = viewModelScope.launch {
        _compositionMetrics.value = Result.Empty
        _loadingProgress.value = true
        _compositionMetrics.value = deviceRepository.fetch_last_metrics_by_user(user_id = user_id)
        _loadingProgress.value = false
    }

    fun fetch_last_metrics_by_user_type_date(user_id: String, date: String, type: String, limit: String, offset: String, loadProgress: Boolean = true) = viewModelScope.launch {
        _compositionMetrics.value = Result.Empty
        if (loadProgress)
            _loadingProgress.value = true
        _compositionMetrics.value = deviceRepository.fetch_last_metrics_by_user_type_date(user_id = user_id, date = date, type = type, limit = limit, offset = offset)
        _loadingProgress.value = false
    }

    fun fetch_last_metrics_by_user_type_date(user_id: String) = viewModelScope.launch {
        _compositionUserFriendsMetrics.value = Result.Empty
        _loadingProgressSpe.value = true
        _compositionUserFriendsMetrics.value = deviceRepository.fetch_metrics_friends_by_user(user_id = user_id)
        _loadingProgressSpe.value = false
    }
}