package tm.wearable.wearabletfm.utils

import okhttp3.ResponseBody
import org.json.JSONObject

class Utils {
    companion object {
        const val api_version = "/api"
        const val domainApi = "http://192.168.1.16:8000/"
        //user
        const val login = "$api_version/login"
        const val register = "$api_version/register"
        const val fetch_user = "$api_version/fetch_user"
        const val fetch_health_data = "$api_version/fetch_health_data"
        const val update_user = "$api_version/update_user"
        const val update_user_image = "$api_version/update_user_image"
        const val update_health_data_user = "$api_version/update_health_data_user"

        fun <T> errorResult(message: String,errorBody: ResponseBody? = null): Result<T> {
            //Timber.d(message)
            var mess_d = message
            if (errorBody != null) {
                val json = JSONObject(errorBody?.string())
                mess_d = json.getString("message")
            }
            return Result.Error(mess_d)
        }
    }
}