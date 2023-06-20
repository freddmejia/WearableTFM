package tm.wearable.wearabletfm.data.repository.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import tm.wearable.wearabletfm.data.model.Device
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.Result
import tm.wearable.wearabletfm.utils.Utils

interface DeviceServiceRemote{
    @POST(Utils.fetch_devices)
    suspend fun fetch_devices(@Body requestBody: Map<String,String>): retrofit2.Response<ListDevicesResponseApi>

    @POST(Utils.fetch_metrics_by_user_date)
    suspend fun fetch_metrics_by_user_date(@Body requestBody: Map<String,String>): retrofit2.Response<ListMetricsResponseApi>

    @POST(Utils.fetch_last_metrics_by_user)
    suspend fun fetch_last_metrics_by_user(@Body requestBody: Map<String,String>): retrofit2.Response<ListMetricsResponseApi>

    @POST(Utils.fetch_metrics_by_user_type_date)
    suspend fun fetch_metrics_by_user_type_date(@Body requestBody: Map<String,String>): retrofit2.Response<ListMetricsResponseApi>

    @POST(Utils.fetch_metrics_friends_by_user)
    suspend fun fetch_metrics_friends_by_user(@Body requestBody: Map<String,String>): retrofit2.Response<ListMetricsFriendsResponseApi>
}

class ListDevicesResponseApi {
    @SerializedName("devices") var devices: ArrayList<Device> = arrayListOf()
    @SerializedName("message") var message: String = ""
}

class ListMetricsResponseApi {
    @SerializedName("metrics") var metrics: ArrayList<Metrics> = arrayListOf()
    @SerializedName("message") var message: String = ""
}

class ListMetricsFriendsResponseApi {
    @SerializedName("metrics_friends") var metrics_friends: ArrayList<UserFriendsResponseApi> = arrayListOf()
    @SerializedName("message") var message: String = ""
}

class UserFriendsResponseApi {
    @SerializedName("user") var user: User = User()
    @SerializedName("metrics") var metrics: ArrayList<Metrics> = arrayListOf()
}

class MetricsFriendsResponseApi {
    @SerializedName("metrics") var metrics: ArrayList<Metrics> = arrayListOf()
}