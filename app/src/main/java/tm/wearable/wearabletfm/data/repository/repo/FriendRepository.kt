package tm.wearable.wearabletfm.data.repository.repo

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tm.wearable.wearabletfm.data.model.Friend
import tm.wearable.wearabletfm.data.model.FriendRequest
import tm.wearable.wearabletfm.data.model.GeofenceFriend
import tm.wearable.wearabletfm.data.repository.datasource.remote.FriendRemoteDataSource
import tm.wearable.wearabletfm.utils.CompositionObj
import java.lang.Exception
import javax.inject.Inject
import tm.wearable.wearabletfm.utils.*

class FriendRepository @Inject constructor(
    private val friendRemoteDataSource: FriendRemoteDataSource
) {
    suspend fun fetch_possible_friends(email_name: String): Result<CompositionObj<ArrayList<Friend>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["email_name"] = email_name
                val response = friendRemoteDataSource.fetchPossibleFriends(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.poss_friends, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    //val errorBody = (response.errorBody() as HttpException).response()?.errorBody()?.string()
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun fetch_friends_request(): Result<CompositionObj<ArrayList<Friend>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val response = friendRemoteDataSource.fetchFriendsRequest()
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.friends_requests, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    //val errorBody = (response.errorBody() as HttpException).response()?.errorBody()?.string()
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun fetch_friends(user_id: String): Result<CompositionObj<ArrayList<Friend>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id"] = user_id
                val response = friendRemoteDataSource.fetchFriends(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.friends, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun friend_request(user_id1: String, user_id2: String): Result<CompositionObj<FriendRequest, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id1"] = user_id1
                requestBody["user_id2"] = user_id2
                val response = friendRemoteDataSource.friendRequest(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.friend_request, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun accept_friend_request(user_id1: String, user_id2: String): Result<CompositionObj<FriendRequest, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id1"] = user_id1
                requestBody["user_id2"] = user_id2
                val response = friendRemoteDataSource.acceptFriendRequest(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.friend_request, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    //val errorBody = (response.errorBody() as HttpException).response()?.errorBody()?.string()
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun delete_friend_request(user_id1: String, user_id2: String): Result<CompositionObj<FriendRequest, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id1"] = user_id1
                requestBody["user_id2"] = user_id2
                val response = friendRemoteDataSource.deleteFriendRequest(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.friend_request, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    //val errorBody = (response.errorBody() as HttpException).response()?.errorBody()?.string()
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun saveFriendGeofence(user_id1: String, user_id2: String,
                                   latitude: String, longitude: String,
                                   ratio: String, zone: String): Result<CompositionObj<GeofenceFriend, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id1"] = user_id1
                requestBody["user_id2"] = user_id2

                requestBody["latitude"] = latitude
                requestBody["longitude"] = longitude

                requestBody["ratio"] = ratio
                requestBody["zone"] = zone

                val response = friendRemoteDataSource.saveFriendGeofence(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.geofence_friend, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    //val errorBody = (response.errorBody() as HttpException).response()?.errorBody()?.string()
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun fetch_geofence_byfriend(user_id1: String, user_id2: String): Result<CompositionObj<ArrayList<GeofenceFriend>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id1"] = user_id1
                requestBody["user_id2"] = user_id2
                val response = friendRemoteDataSource.fetchFriendGeofence(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.geofences_friend, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun delete_geofence_byfriend(geofenceId: String): Result<CompositionObj<GeofenceFriend, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["geofence_id"] = geofenceId
                val response = friendRemoteDataSource.deleteFriendGeofence(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.geofence_friend, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    Utils.errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                Utils.errorResult(message = e.message ?: e.toString())
            }
        }
    }
}