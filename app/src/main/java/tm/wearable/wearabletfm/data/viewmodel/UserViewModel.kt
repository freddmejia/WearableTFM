package tm.wearable.wearabletfm.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import tm.wearable.wearabletfm.data.model.FitbitOauth
import tm.wearable.wearabletfm.data.model.Health
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.model.shortUser
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

    private val _compositionUpdateUser = MutableStateFlow<Result<CompositionObj<shortUser, String>>>(Result.Empty)
    val compositionUpdateUser :  StateFlow<Result<CompositionObj<shortUser, String>>> = _compositionUpdateUser

    private val _compositionUpdateHealthUser = MutableStateFlow<Result<CompositionObj<Health, String>>>(Result.Empty)
    val compositionUpdateHealthUser :  StateFlow<Result<CompositionObj<Health, String>>> = _compositionUpdateHealthUser

    private val _compositionFitbitOauth = MutableStateFlow<Result<CompositionObj<FitbitOauth, String>>>(Result.Empty)
    val compositionFitbitOauth :  StateFlow<Result<CompositionObj<FitbitOauth, String>>> = _compositionFitbitOauth


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


}