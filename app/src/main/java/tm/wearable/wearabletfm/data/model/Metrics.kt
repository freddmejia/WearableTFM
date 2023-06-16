package tm.wearable.wearabletfm.data.model

class Metrics (
    var id: Int,
    var type: String,
    var value: String,
    var measure: String,
    var datetime: String,
    var id_device: String
) {
    constructor(): this(0,"","","","","")
}