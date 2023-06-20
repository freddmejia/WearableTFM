package tm.wearable.wearabletfm.data.interfaces

import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.utils.CompositionObj

interface UIMetricFriend {
    fun onClick(friendMetric: CompositionObj<User, ArrayList<Metrics>>)
}