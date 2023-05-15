package tm.wearable.wearabletfm.data.interfaces

import tm.wearable.wearabletfm.data.model.Medicine

interface UIMed {
    fun edit(medicine: Medicine)
    fun delete(medicine: Medicine)
}