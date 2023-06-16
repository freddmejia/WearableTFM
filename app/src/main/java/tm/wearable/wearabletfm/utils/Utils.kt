package tm.wearable.wearabletfm.utils

import android.util.Log
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.HashMap

class Utils {
    companion object {
        const val api_version = "/api"
        const val domainApi = "http://192.168.1.16:8000/"
        //const val domainApi = "http://52.54.33.62/"
        const val domainApiFitbit = "https://www.fitbit.com/"
        const val oauthFitbit = "/oauth2"

        //user
        const val login = "$api_version/login"
        const val register = "$api_version/register"
        const val fetch_user = "$api_version/fetch_user"
        const val fetch_health_data = "$api_version/fetch_health_data"
        const val update_user = "$api_version/update_user"
        const val update_user_image = "$api_version/update_user_image"
        const val update_health_data_user = "$api_version/update_health_data_user"

        //medicine
        const val store_medicine = "$api_version/store_medicine"
        const val updateMedicine = "$api_version/updateMedicine"
        const val fetch_three_last_medicines_by_user = "$api_version/fetch_three_last_medicines_by_user"
        const val delete_medicine_by_user = "$api_version/delete_medicine_by_user"
        const val fetch_medicines_by_user = "$api_version/fetch_medicines_by_user"

        //friends
        const val fetch_possible_friends = "$api_version/fetch_possible_friends"
        const val fetch_friends = "$api_version/fetch_friends"
        const val fetch_friends_request = "$api_version/fetch_friends_request"

        const val friend_request = "$api_version/friend_request"
        const val accept_friend_request = "$api_version/accept_friend_request"
        const val delete_friend_request = "$api_version/delete_friend_request"
        const val save_geofence_friend = "$api_version/save_geofence_friend"
        const val update_geofence_friend = "$api_version/update_geofence_friend"
        const val fetch_geofence_byfriend = "$api_version/fetch_geofence_byfriend"
        const val delete_geofence_byfriend = "$api_version/delete_geofence_byfriend"

        //fitbit
        const val fitbit_oauth = "$api_version/fitbit_oauth"

        //wearables
        const val fetch_devices = "$api_version/fetch_devices"
        const val fetch_metrics_by_user_date = "$api_version/fetch_metrics_by_user_date"
        const val fetch_last_metrics_by_user = "$api_version/fetch_last_metrics_by_user"
        const val fetch_metrics_by_user_type_date = "$api_version/fetch_metrics_by_user_type_date"


        const val batteryLevelMin = 30
        const val friend_added = 1
        const val are_friend = 2
        const val friend_cancel = 3

        const val noId = 0
        const val negativeId = -1
        const val monday = 0
        const val tuesday = 1
        const val wednesday = 2
        const val thursday = 3
        const val friday = 4
        const val saturday = 5
        const val sunday = 6

        var medicinesDays: HashMap<String, Int> = HashMap()

        fun <T> errorResult(message: String,errorBody: ResponseBody? = null): Result<T> {
            //Timber.d(message)
            var mess_d = message
            if (errorBody != null) {
                val json = JSONObject(errorBody?.string())
                mess_d = json.getString("message")
            }
            Log.e("", "coroutines errorResult: "+mess_d.toString() )
            return Result.Error(mess_d)
        }

        fun medicineDetailJson(hour: String, day: Int): JSONObject {
            var jsonArray = JSONArray()
            var jsonObject = JSONObject()
            jsonObject.put("hour",hour)
            jsonObject.put("day",day.toString())
            jsonArray.put(jsonObject)
            return jsonObject
        }

        fun convertDateFormat(dateTime: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

            val date = inputFormat.parse(dateTime)
            return outputFormat.format(date)
        }
    }
}