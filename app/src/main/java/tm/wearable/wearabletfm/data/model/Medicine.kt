package tm.wearable.wearabletfm.data.model

class Medicine (
    var id: Int,
    var name: String,
    var date_from: String,
    var date_until: String,
    var details: ArrayList<MedicineDetail>
        ){
    constructor():this(0,"","","", arrayListOf())
}