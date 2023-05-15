package tm.wearable.wearabletfm.data.interfaces

import tm.wearable.wearabletfm.data.model.MedicineDetail

interface UIMedicine {
    fun chooseDate(medicineDetail: MedicineDetail)
    fun chooseDay(medicineDetail: MedicineDetail, day: Int)
    fun editDate(medicineDetail: MedicineDetail)
    fun deleteDay(medicineDetail: MedicineDetail)
}