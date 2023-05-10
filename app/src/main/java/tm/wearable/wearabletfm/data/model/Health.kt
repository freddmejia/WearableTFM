package tm.wearable.wearabletfm.data.model

class Health (
    var id: Int,
    var height: String,
    var weight: String,
    var birthay: String,
    var yearOld: Int
) {
    constructor() : this(0,"","","",0)
}
