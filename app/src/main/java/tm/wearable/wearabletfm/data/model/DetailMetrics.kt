package tm.wearable.wearabletfm.data.model

class DetailMetrics(
    var type: String,
    var value: String,
    var measure: String,
    var datetime: String
) {
    constructor(): this("","","","")
}