package tm.wearable.wearabletfm.data.repository.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.POST
import tm.wearable.wearabletfm.data.model.Health
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.utils.Utils

interface MedicineServiceRemote {
    @POST(Utils.store_medicine)
    suspend fun store_medicine(@Body requestBody: Map<String,String>): retrofit2.Response<MedicineResponseApi>

    @POST(Utils.updateMedicine)
    suspend fun update_medicine(@Body requestBody: Map<String,String>): retrofit2.Response<MedicineResponseApi>

    @POST(Utils.fetch_three_last_medicines_by_user)
    suspend fun fetch_three_last_medicines_by_user(): retrofit2.Response<MedicinesResponseApi>

    @POST(Utils.delete_medicine_by_user)
    suspend fun delete_medicine_by_user(@Body requestBody: Map<String,String>): retrofit2.Response<MedicineResponseApi>

    @POST(Utils.fetch_medicines_by_user)
    suspend fun fetch_medicines_by_user(): retrofit2.Response<MedicinesResponseApi>
}


class MedicineResponseApi {
    @SerializedName("medicine") var medicine: Medicine = Medicine()
    @SerializedName("message") var message: String = ""
}

class MedicinesResponseApi {
    @SerializedName("medicines") var medicines: ArrayList<Medicine> = arrayListOf()
    @SerializedName("message") var message: String = ""
}