package tm.wearable.wearabletfm.data.model

import com.google.gson.annotations.SerializedName

class Metrics (
    var id: Int,
    var type: String,
    var value: String,
    var measure: String,
    var datetime: String,
    var enddatetime: String,
    var id_device: String,
    @SerializedName("details") var details: ArrayList<DetailMetrics>
) {
    constructor(): this(0,"","","","","","", arrayListOf<DetailMetrics>())
}
