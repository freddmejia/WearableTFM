package tm.wearable.wearabletfm.data.repository.datasource.remote

import tm.wearable.wearabletfm.data.repository.api.MedicineServiceRemote
import javax.inject.Inject

class MedicineRemoteDataSource @Inject constructor(
    private val medicineServiceRemote: MedicineServiceRemote
) {
    suspend fun store_medicine(requestBody: MutableMap<String, String>) = medicineServiceRemote.store_medicine(requestBody)
    suspend fun update_medicine(requestBody: MutableMap<String, String>) = medicineServiceRemote.update_medicine(requestBody)
    suspend fun fetch_three_last_medicines_by_user() = medicineServiceRemote.fetch_three_last_medicines_by_user()
    suspend fun delete_medicine_by_user(requestBody: MutableMap<String, String>) = medicineServiceRemote.delete_medicine_by_user(requestBody)
    suspend fun fetch_medicines_by_user() = medicineServiceRemote.fetch_medicines_by_user()
}