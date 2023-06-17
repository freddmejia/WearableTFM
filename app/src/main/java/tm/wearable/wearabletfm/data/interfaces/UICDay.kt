package tm.wearable.wearabletfm.data.interfaces

import tm.wearable.wearabletfm.data.model.CDay

interface UICDay {
    fun onClick(cDay: CDay)
    fun dateChange(cDay: CDay)
}