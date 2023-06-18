package tm.wearable.wearabletfm.data.repository.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tm.wearable.wearabletfm.data.model.*
import tm.wearable.wearabletfm.data.repository.api.FitbitResponseApi
import tm.wearable.wearabletfm.data.repository.datasource.remote.UserRemoteDataSource
import tm.wearable.wearabletfm.utils.CompositionObj
import java.lang.Exception
import javax.inject.Inject
import tm.wearable.wearabletfm.utils.Result
import tm.wearable.wearabletfm.utils.Utils
import tm.wearable.wearabletfm.utils.Utils.Companion.errorResult

class UserRepository  @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) {
    suspend fun login(email: String,password: String): Result<CompositionObj<User, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["email"] = email
                requestBody["password"] = password
                val response = userRemoteDataSource.login(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    var user = response.body()!!.user
                    user.health = response.body()!!.health_data
                    val ab = CompositionObj<User,String>(user, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun register(username: String,email: String,password: String,password2: String): Result<CompositionObj<User, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["name"] = username
                requestBody["email"] = email
                requestBody["password"] = password
                requestBody["c_password"] = password2
                val response = userRemoteDataSource.registerUser(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj<User,String>(response.body()!!.user, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun update_user(name: String,email: String, password: String): Result<CompositionObj<shortUser, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["name"] = name
                requestBody["email"] = email
                if (password.isNotEmpty())
                    requestBody["password"] = password
                val response = userRemoteDataSource.updateUser(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj<shortUser,String>(response.body()!!.user, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun update_health_user(height: String, weight: String, birthay: String): Result<CompositionObj<Health, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["height"] = height
                requestBody["weight"] = weight
                requestBody["birthay"] = birthay
                val response = userRemoteDataSource.updateHealthDataUser(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.health_data, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun fitbit_oauth(user_id: String): Result<CompositionObj<FitbitOauth, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id"] = user_id
                val response = userRemoteDataSource.fitbitOAuth(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val fitbitOauth = FitbitOauth(codeVerifier = response.body()!!.codeVerifier,
                        codeChallenge = response.body()!!.codeChallenge,
                        url = response.body()!!.url
                    )
                    val ab = CompositionObj(fitbitOauth, "")
                    Result.Success(ab)
                }
                else{
                    errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun fetc_notificatio_by_user(user_id: String): Result<CompositionObj<ArrayList<Notification>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["user_id"] = user_id
                val response = userRemoteDataSource.fetchNotificationByUser(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    if (response.body()!!.notifications.isEmpty()){
                        Result.Error(Utils.no_data)
                    }else {
                        val ab = CompositionObj(
                            response.body()!!.notifications,
                            response.body()!!.message
                        )
                        Result.Success(ab)
                    }
                }
                else{
                    errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                errorResult(message = e.message ?: e.toString())
            }
        }
    }

    suspend fun delete_notification(notification_id: String): Result<CompositionObj<Notification, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["notification_id"] = notification_id
                val response = userRemoteDataSource.deleteNotification(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.notification, response.body()!!.message)
                    Result.Success(ab)
                }
                else{
                    errorResult( message = "",errorBody = response.errorBody()!!)
                }
            }catch (e: Exception){
                errorResult(message = e.message ?: e.toString())
            }
        }
    }


}