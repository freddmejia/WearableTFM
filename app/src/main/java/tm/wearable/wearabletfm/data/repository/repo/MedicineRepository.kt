package tm.wearable.wearabletfm.data.repository.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.data.model.MedicineDetail
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.data.repository.api.MedicineServiceRemote
import tm.wearable.wearabletfm.data.repository.datasource.remote.MedicineRemoteDataSource
import tm.wearable.wearabletfm.utils.CompositionObj
import tm.wearable.wearabletfm.utils.Result
import tm.wearable.wearabletfm.utils.Utils
import java.lang.Exception
import javax.inject.Inject

class MedicineRepository @Inject constructor(
    private val medicineRemoteDataSource: MedicineRemoteDataSource
) {
    suspend fun store_medicine(name: String, date_from: String, date_until: String, details: ArrayList<MedicineDetail>): Result<CompositionObj<Medicine, String>> {
        return withContext(Dispatchers.Default){
            try {
                var medDetails = JSONArray()
                details.forEach {
                    medDetails.put(
                        Utils.medicineDetailJson(hour = it.hour, day = it.day)
                    )
                }
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["name"] = name
                requestBody["date_from"] = date_from
                requestBody["date_until"] = date_until
                requestBody["details"] = medDetails.toString()
                val response = medicineRemoteDataSource.store_medicine(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.medicine, response.body()!!.message)
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

    suspend fun update_medicine(medicine_id: String,name: String, date_from: String, date_until: String, details: ArrayList<MedicineDetail>): Result<CompositionObj<Medicine, String>> {
        return withContext(Dispatchers.Default){
            try {
                var medDetails = JSONArray()
                details.forEach {
                    medDetails.put(
                        Utils.medicineDetailJson(hour = it.hour, day = it.day)
                    )
                }
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["medicine_id"] = medicine_id
                requestBody["name"] = name
                requestBody["date_from"] = date_from
                requestBody["date_until"] = date_until
                requestBody["details"] = medDetails.toString()
                val response = medicineRemoteDataSource.update_medicine(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.medicine, response.body()!!.message)
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

    suspend fun fetch_three_last_medicines_by_user(): Result<CompositionObj<ArrayList<Medicine>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val response = medicineRemoteDataSource.fetch_three_last_medicines_by_user()
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.medicines, response.body()!!.message)
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

    suspend fun delete_medicine_by_user(medicine_id: String): Result<CompositionObj<Medicine, String>> {
        return withContext(Dispatchers.Default){
            try {
                val requestBody: MutableMap<String, String> = HashMap()
                requestBody["medicine_id"] = medicine_id
                val response = medicineRemoteDataSource.delete_medicine_by_user(requestBody = requestBody)
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.medicine, response.body()!!.message)
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


    suspend fun fetch_medicines_by_user(): Result<CompositionObj<ArrayList<Medicine>, String>> {
        return withContext(Dispatchers.Default){
            try {
                val response = medicineRemoteDataSource.fetch_medicines_by_user()
                val body = response.body()
                if (body != null) {
                    val ab = CompositionObj(response.body()!!.medicines, response.body()!!.message)
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