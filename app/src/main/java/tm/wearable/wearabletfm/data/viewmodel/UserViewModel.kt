package tm.wearable.wearabletfm.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tm.wearable.wearabletfm.data.model.*
import tm.wearable.wearabletfm.data.repository.api.forgotPass
import tm.wearable.wearabletfm.data.repository.repo.UserRepository
import tm.wearable.wearabletfm.utils.CompositionObj
import javax.inject.Inject
import tm.wearable.wearabletfm.utils.*

@HiltViewModel
class UserViewModel  @Inject constructor(
    private val userRepository: UserRepository
)  : ViewModel() {
    private val _compositionLogin = MutableStateFlow<Result<CompositionObj<User, String>>>(Result.Empty)
    val compositionLogin : StateFlow<Result<CompositionObj<User, String>>> = _compositionLogin

    private val _compositionLogout = MutableStateFlow<Result<CompositionObj<String, String>>>(Result.Empty)
    val compositionLogout : StateFlow<Result<CompositionObj<String, String>>> = _compositionLogout

    private val _compositionUpdateUser = MutableStateFlow<Result<CompositionObj<shortUser, String>>>(Result.Empty)
    val compositionUpdateUser :  StateFlow<Result<CompositionObj<shortUser, String>>> = _compositionUpdateUser

    private val _compositionUpdateHealthUser = MutableStateFlow<Result<CompositionObj<Health, String>>>(Result.Empty)
    val compositionUpdateHealthUser :  StateFlow<Result<CompositionObj<Health, String>>> = _compositionUpdateHealthUser

    private val _compositionFitbitOauth = MutableStateFlow<Result<CompositionObj<FitbitOauth, String>>>(Result.Empty)
    val compositionFitbitOauth :  StateFlow<Result<CompositionObj<FitbitOauth, String>>> = _compositionFitbitOauth

    private val _compositionNotifications = MutableStateFlow<Result<CompositionObj<ArrayList<Notification>,String>>>(Result.Empty)
    val compositionNotifications :  StateFlow<Result<CompositionObj<ArrayList<Notification>,String>>> = _compositionNotifications

    private val _compositionNotification = MutableStateFlow<Result<CompositionObj<Notification,String>>>(Result.Empty)
    val compositionNotification :  StateFlow<Result<CompositionObj<Notification,String>>> = _compositionNotification

    private val _compositionRecoverAccount = MutableStateFlow<Result<CompositionObj<forgotPass, String>>>(Result.Empty)
    val compositionRecoverAccount : StateFlow<Result<CompositionObj<forgotPass, String>>> = _compositionRecoverAccount


    private val _loadingProgress = MutableStateFlow(false)
    val loadingProgress: StateFlow<Boolean> = _loadingProgress

    fun login(email: String, password: String) = viewModelScope.launch {
        _compositionLogin.value = Result.Empty
        _loadingProgress.value = true
        _compositionLogin.value = userRepository.login(email = email, password = password)
        _loadingProgress.value = false
    }

    fun register(username: String,email: String,password: String,password2: String) = viewModelScope.launch {
        _compositionLogin.value = Result.Empty
        _loadingProgress.value = true
        _compositionLogin.value = userRepository.register(username = username, email = email,password = password, password2 = password2)
        _loadingProgress.value = false
    }

    fun update_user(name: String,email: String, password: String) = viewModelScope.launch {
        _compositionUpdateUser.value = Result.Empty
        _loadingProgress.value = true
        _compositionUpdateUser.value = userRepository.update_user(email = email, password = password, name = name)
        _loadingProgress.value = false
    }

    fun update_health_user(height: String, weight: String, birthay: String) = viewModelScope.launch {
        _compositionUpdateHealthUser.value = Result.Empty
        _loadingProgress.value = true
        _compositionUpdateHealthUser.value = userRepository.update_health_user(height = height, weight = weight, birthay = birthay)
        _loadingProgress.value = false
    }

    fun fitbit_oauth(user_id: String) = viewModelScope.launch {
        _compositionFitbitOauth.value = Result.Empty
        _loadingProgress.value = true
        _compositionFitbitOauth.value = userRepository.fitbit_oauth(user_id = user_id)
        _loadingProgress.value = false
    }

    fun fetch_notification_by_user(user_id: String) = viewModelScope.launch (Dispatchers.IO) {
        _compositionNotifications.value = Result.Empty
        _loadingProgress.value = true
        _compositionNotifications.value = userRepository.fetc_notificatio_by_user(user_id = user_id)
        _loadingProgress.value = false
    }

    fun delete_notification(notification_id: String) = viewModelScope.launch (Dispatchers.IO) {
        _compositionNotification.value = Result.Empty
        _loadingProgress.value = true
        _compositionNotification.value = userRepository.delete_notification(notification_id = notification_id)
        _loadingProgress.value = false
    }

    fun forgot_password_step_one(email: String) = viewModelScope.launch (Dispatchers.IO) {
        _compositionRecoverAccount.value = Result.Empty
        _loadingProgress.value = true
        _compositionRecoverAccount.value = userRepository.forgot_password_step_one(email = email)
        _loadingProgress.value = false
    }

    fun update_password_step_last(user_id: String, name: String, email: String, password: String, c_password: String) = viewModelScope.launch (Dispatchers.IO) {
        _compositionLogin.value = Result.Empty
        _loadingProgress.value = true
        _compositionLogin.value = userRepository.update_password_step_last(user_id = user_id, name = name, email = email, password = password, c_password = c_password)
        _loadingProgress.value = false
    }

    fun logout(user_id: String) = viewModelScope.launch (Dispatchers.IO) {
        _compositionLogout.value = Result.Empty
        _loadingProgress.value = true
        _compositionLogout.value = userRepository.logout(user_id = user_id)
        _loadingProgress.value = false
    }

    fun update_token(user_id: String, token: String) = viewModelScope.launch (Dispatchers.IO) {
        userRepository.update_token(user_id = user_id, token = token)
    }

}