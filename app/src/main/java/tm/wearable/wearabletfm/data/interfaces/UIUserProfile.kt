package tm.wearable.wearabletfm.data.interfaces

import android.os.Message
import tm.wearable.wearabletfm.data.model.User

interface UIUserProfile {
    fun save(user: User)
    fun showMessage(message: String)
}