package tm.wearable.wearabletfm.data.model

class Notification(
    var id: Int,
    var user_id: Int,
    var title: String,
    var message: String,
    var created_at: String,
) {
    constructor(): this(0,0,"","","")
}