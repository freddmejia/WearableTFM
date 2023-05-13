package tm.wearable.wearabletfm.data.model

import org.json.JSONObject

class Health (
    var id: Int,
    var height: String,
    var weight: String,
    var birthay: String,
    var yearOld: Int
) {
    constructor() : this(0,"","","",0)
    constructor(jsonObject: JSONObject): this(
        jsonObject.getInt("id"),
        jsonObject.getString("height"),
        jsonObject.getString("weight"),
        jsonObject.getString("birthay"),
        jsonObject.getInt("yearOld")
    )
}
