package tm.wearable.wearabletfm.data.repository.datasource.remote

import tm.wearable.wearabletfm.data.repository.api.FriendServiceRemote
import javax.inject.Inject

class FriendRemoteDataSource @Inject constructor(
    private val friendService: FriendServiceRemote
) {
    suspend fun fetchPossibleFriends(requestBody: MutableMap<String, String>) = friendService.fetchPossibleFriends(requestBody)
    suspend fun fetchFriendsRequest() = friendService.fetchFriendsRequest()

    suspend fun fetchFriends(requestBody: MutableMap<String, String>) = friendService.fetchFriends(requestBody)
    suspend fun friendRequest(requestBody: MutableMap<String, String>) = friendService.friendRequest(requestBody)
    suspend fun acceptFriendRequest(requestBody: MutableMap<String, String>) = friendService.acceptFriendRequest(requestBody)
    suspend fun deleteFriendRequest(requestBody: MutableMap<String, String>) = friendService.deleteFriendRequest(requestBody)
    suspend fun saveFriendGeofence(requestBody: MutableMap<String, String>) = friendService.saveFriendGeofence(requestBody)
    suspend fun updateFriendGeofence(requestBody: MutableMap<String, String>) = friendService.updateFriendGeofence(requestBody)
    suspend fun fetchFriendGeofence(requestBody: MutableMap<String, String>) = friendService.fetchFriendGeofence(requestBody)
    suspend fun deleteFriendGeofence(requestBody: MutableMap<String, String>) = friendService.deleteFriendGeofence(requestBody)
}