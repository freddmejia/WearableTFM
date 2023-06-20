package tm.wearable.wearabletfm.data.repository.datasource.remote

import tm.wearable.wearabletfm.data.repository.api.DeviceServiceRemote
import javax.inject.Inject

class DeviceRemoteDatasource  @Inject constructor(
    private val wearableServiceRemote: DeviceServiceRemote
) {
    suspend fun fetchDevices(requestBody: MutableMap<String, String>) = wearableServiceRemote.fetch_devices(requestBody)
    suspend fun fetchMetricsByUserDate(requestBody: MutableMap<String, String>) = wearableServiceRemote.fetch_metrics_by_user_date(requestBody)
    suspend fun fetchLastMetricsByUser(requestBody: MutableMap<String, String>) = wearableServiceRemote.fetch_last_metrics_by_user(requestBody)
    suspend fun fetchLastMetricsByUserTypeDate(requestBody: MutableMap<String, String>) = wearableServiceRemote.fetch_metrics_by_user_type_date(requestBody)
    suspend fun fetchMetricsFriendsByUser(requestBody: MutableMap<String, String>) = wearableServiceRemote.fetch_metrics_friends_by_user(requestBody)

}