package tm.wearable.wearabletfm.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import tm.wearable.wearabletfm.data.model.Friend
import tm.wearable.wearabletfm.data.model.FriendRequest
import tm.wearable.wearabletfm.data.model.GeofenceFriend
import tm.wearable.wearabletfm.data.model.shortUser
import tm.wearable.wearabletfm.data.repository.repo.FriendRepository
import javax.inject.Inject
import tm.wearable.wearabletfm.utils.*
@HiltViewModel
class FriendViewModel @Inject constructor(
    private val friendRepository: FriendRepository
)  : ViewModel() {
    private var apiCallJob: Job? = null
    private val inputTextFlow = MutableStateFlow("")

    private val _compositionFetchPossibleFriends = MutableStateFlow<Result<CompositionObj<ArrayList<Friend>, String>>>(Result.Empty)
    val compositionFetchPossibleFriends : StateFlow<Result<CompositionObj<ArrayList<Friend>, String>>> = _compositionFetchPossibleFriends


    private val _compositionFetchFriends = MutableStateFlow<Result<CompositionObj<ArrayList<shortUser>, String>>>(Result.Empty)
    val compositionFetchFriends : StateFlow<Result<CompositionObj<ArrayList<shortUser>, String>>> = _compositionFetchFriends

    private val _compositionFetchFriendsRequest = MutableStateFlow<Result<CompositionObj<ArrayList<Friend>, String>>>(Result.Empty)
    val compositionFetchFriendsRequest : StateFlow<Result<CompositionObj<ArrayList<Friend>, String>>> = _compositionFetchFriendsRequest

    private val _compositionGeofencesFriends = MutableStateFlow<Result<CompositionObj<ArrayList<GeofenceFriend>, String>>>(Result.Empty)
    val compositionGeofencesFriends : StateFlow<Result<CompositionObj<ArrayList<GeofenceFriend>, String>>> = _compositionGeofencesFriends

    private val _compositionGeofenceFriend = MutableStateFlow<Result<CompositionObj<GeofenceFriend, String>>>(Result.Empty)
    val compositionGeofenceFriend : StateFlow<Result<CompositionObj<GeofenceFriend, String>>> = _compositionGeofenceFriend


    private val _loadingProgress = MutableStateFlow(false)
    val loadingProgress: StateFlow<Boolean> = _loadingProgress

    private val _compositionFriendRequest = MutableStateFlow<Result<CompositionObj<FriendRequest, String>>>(Result.Empty)
    val compositionFriendRequest : StateFlow<Result<CompositionObj<FriendRequest, String>>> = _compositionFriendRequest


    fun setInputText(inputText: String) {
        inputTextFlow.value = inputText
    }

    fun fetch_possible_friends() = viewModelScope.launch {
        inputTextFlow
            .debounce(500)
            .collect { inputText ->
                apiCallJob?.cancel()
                apiCallJob = launch {
                    delay(500)
                    if (inputText.length > 0) {
                        _compositionFetchPossibleFriends.value = Result.Empty
                        _loadingProgress.value = true
                        _compositionFetchPossibleFriends.value =
                            friendRepository.fetch_possible_friends(email_name = inputTextFlow.value)
                        _loadingProgress.value = false
                    }
                }
            }
    }

    fun fetch_friends(user_id: String) = viewModelScope.launch {
        _compositionFetchPossibleFriends.value = Result.Empty
        _loadingProgress.value = true
        _compositionFetchPossibleFriends.value = friendRepository.fetch_friends(user_id = user_id)
        _loadingProgress.value = false
    }

    fun friend_request(user_id1: String, user_id2: String) = viewModelScope.launch {
        _compositionFriendRequest.value = Result.Empty
        _loadingProgress.value = true
        _compositionFriendRequest.value = friendRepository.friend_request(user_id1 = user_id1, user_id2 = user_id2)
        _loadingProgress.value = false
    }

    fun fetch_friends_request() = viewModelScope.launch {
        _compositionFetchFriendsRequest.value = Result.Empty
        _loadingProgress.value = true
        _compositionFetchFriendsRequest.value = friendRepository.fetch_friends_request()
        _loadingProgress.value = false
    }

    fun accept_friend_request(user_id1: String, user_id2: String) = viewModelScope.launch {
        _compositionFriendRequest.value = Result.Empty
        _loadingProgress.value = true
        _compositionFriendRequest.value = friendRepository.accept_friend_request(user_id1 = user_id1, user_id2 = user_id2)
        _loadingProgress.value = false
    }

    fun delete_friend_request(user_id1: String, user_id2: String) = viewModelScope.launch {
        _compositionFriendRequest.value = Result.Empty
        _loadingProgress.value = true
        _compositionFriendRequest.value = friendRepository.delete_friend_request(user_id1 = user_id1, user_id2 = user_id2)
        _loadingProgress.value = false
    }

    fun set_compositionFetchPossibleFriends(state: Result<CompositionObj<ArrayList<Friend>, String>>)  = viewModelScope.launch{
        _compositionFetchPossibleFriends.value = Result.Empty
        delay(200)
        _compositionFetchPossibleFriends.value = state

    }

    fun saveFriendGeofence(user_id1: String, user_id2: String,
                           latitude: String, longitude: String,
                           ratio: String, zone: String) = viewModelScope.launch {
        _compositionGeofenceFriend.value = Result.Empty
        _loadingProgress.value = true
        _compositionGeofenceFriend.value = friendRepository.saveFriendGeofence(
            user_id1 = user_id1, user_id2 = user_id2,
            latitude = latitude, longitude = longitude,
            ratio = ratio, zone = zone
        )
        _loadingProgress.value = false
    }

    fun fetch_geofence_byfriend(user_id1: String, user_id2: String) = viewModelScope.launch {
        _compositionGeofencesFriends.value = Result.Empty
        _loadingProgress.value = true
        _compositionGeofencesFriends.value = friendRepository.fetch_geofence_byfriend(user_id1 = user_id1, user_id2 = user_id2)
        _loadingProgress.value = false
    }

    fun delete_geofence_byfriend(geofenceId: String) = viewModelScope.launch {
        _compositionGeofenceFriend.value = Result.Empty
        _loadingProgress.value = true
        _compositionGeofenceFriend.value = friendRepository.delete_geofence_byfriend(geofenceId = geofenceId)
        _loadingProgress.value = false
    }




}