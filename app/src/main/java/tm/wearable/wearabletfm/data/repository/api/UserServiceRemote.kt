package tm.wearable.wearabletfm.data.repository.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import tm.wearable.wearabletfm.data.model.Health
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.model.shortUser
import tm.wearable.wearabletfm.utils.Utils

interface UserServiceRemote {
    @POST(Utils.login)
    suspend fun loginUser(@Body requestBody: Map<String,String>): retrofit2.Response<UserResponseApi>

    @POST(Utils.register)
    suspend fun registerUser(@Body requestBody: Map<String,String>): retrofit2.Response<UserResponseApi>

    @POST(Utils.fetch_user)
    suspend fun fetchUser(@Body requestBody: Map<String,String>): retrofit2.Response<UserShortResponseApi>

    @POST(Utils.fetch_health_data)
    suspend fun fetchHealthData(@Body requestBody: Map<String,String>): retrofit2.Response<HealthResponseApi>

    @POST(Utils.update_user)
    suspend fun updateUser(@Body requestBody: Map<String,String>): retrofit2.Response<UserShortResponseApi>

    @POST(Utils.update_user_image)
    suspend fun updateUserImage (@Body requestBody: Map<String,String>): retrofit2.Response<UserShortResponseApi>

    @POST(Utils.update_health_data_user)
    suspend fun updateHealthDataUser (@Body requestBody: Map<String,String>): retrofit2.Response<HealthResponseApi>

    @POST(Utils.fitbit_oauth)
    suspend fun fitbitOAuth (@Body requestBody: Map<String,String>): retrofit2.Response<FitbitResponseApi>
}

class UserResponseApi {
    @SerializedName("user") var user: User = User()
    @SerializedName("message") var message: String = ""
    @SerializedName("health_data") var health_data: Health = Health()
}
class UserShortResponseApi {
    @SerializedName("user") var user: shortUser = shortUser()
    @SerializedName("message") var message: String = ""
}
class HealthResponseApi {
    @SerializedName("health_data") var health_data: Health = Health()
    @SerializedName("message") var message: String = ""
}
class FitbitResponseApi {
    @SerializedName("codeVerifier") var codeVerifier: String = ""
    @SerializedName("codeChallenge") var codeChallenge: String = ""
    @SerializedName("url") var url: String = ""
}