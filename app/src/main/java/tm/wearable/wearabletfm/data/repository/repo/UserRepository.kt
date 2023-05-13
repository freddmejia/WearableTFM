package tm.wearable.wearabletfm.data.repository.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.model.shortUser
import tm.wearable.wearabletfm.data.repository.datasource.remote.UserRemoteDataSource
import tm.wearable.wearabletfm.utils.CompositionObj
import java.lang.Exception
import javax.inject.Inject
import tm.wearable.wearabletfm.utils.Result
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
}