package tm.wearable.wearabletfm.data.model

class Device(
    var id: Int,
    var battery_level: String,
    var device_version: String,
    var id_dev_fitbit: String,
    var mac_address: String,
    var type: String,
    var last_sync_time: String
) {
    constructor(): this(0,"","","","","","")
}