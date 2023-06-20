package tm.wearable.wearabletfm.data.repository.repo

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tm.wearable.wearabletfm.data.model.Device
import tm.wearable.wearabletfm.data.model.Friend
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.repository.datasource.remote.DeviceRemoteDatasource
import tm.wearable.wearabletfm.data.repository.datasource.remote.FriendRemoteDataSource
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.Result
import tm.wearable.wearabletfm.utils.Utils
import java.lang.Exception
import javax.inject.Inject

class DeviceRepository@Inject constructor(
    private val deviceRemoteDatasource: DeviceRemoteDatasource
) {
    suspend fun fetch_devices(user_id: String): Result<CompositionObj<ArrayList<Device>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id"] = user_id
                val response = deviceRemoteDatasource.fetchDevices(requestBody = requestBody)
                val body = response.body()
                Log.e("", "fetch_devices: "+body.toString() )
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.devices, response.body()!!.message)
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

    suspend fun fetch_metrics_by_user_date(user_id: String,date: String): Result<CompositionObj<ArrayList<Metrics>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id"] = user_id
                requestBody["date"] = date
                val response = deviceRemoteDatasource.fetchMetricsByUserDate(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.metrics, response.body()!!.message)
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

    suspend fun fetch_last_metrics_by_user(user_id: String): Result<CompositionObj<ArrayList<Metrics>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id"] = user_id
                val response = deviceRemoteDatasource.fetchLastMetricsByUser(requestBody = requestBody)
                val body = response.body()
                Log.e("", "fetch_last_metrics_by_user: "+body.toString() )
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.metrics, response.body()!!.message)
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

    suspend fun fetch_last_metrics_by_user_type_date(user_id: String,date: String, type: String, limit: String, offset: String): Result<CompositionObj<ArrayList<Metrics>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id"] = user_id
                requestBody["type"] = type
                requestBody["date"] = date
                requestBody["limit"] = limit
                requestBody["offset"] = offset
                val response = deviceRemoteDatasource.fetchLastMetricsByUserTypeDate(requestBody = requestBody)
                val body = response.body()

                if (body != null) {
                    val me = response.body()!!.metrics
                    me.forEach {
                        it.details.forEach {
                            Log.e("", "fetch_last_metrics_by_user_type_date: "+it.toString() )
                        }
                    }
                    val ab = CompositionObj(me, response.body()!!.message)
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

    suspend fun fetch_metrics_friends_by_user(user_id: String): Result<ArrayList<CompositionObj<User, ArrayList<Metrics>>>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id"] = user_id
                val response = deviceRemoteDatasource.fetchMetricsFriendsByUser(requestBody = requestBody)
                val body = response.body()

                if (body != null) {
                    var dataSuccess = ArrayList<CompositionObj<User, ArrayList<Metrics>>>()
                    response.body()!!.metrics_friends.forEach {
                        Log.e("", "fetch_metrics_friends_by_user: "+it.toString() )
                        dataSuccess.add(
                            CompositionObj(
                                it.user,
                                it.metrics
                            )
                        )
                    }
                    Result.Success(dataSuccess)
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