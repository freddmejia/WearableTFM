package tm.wearable.wearabletfm.data.interfaces

import tm.wearable.wearabletfm.data.model.Health
import tm.wearable.wearabletfm.data.model.User

interface UIUserHealth {
    fun save(health: Health)
    fun showMessage(message: String)
}