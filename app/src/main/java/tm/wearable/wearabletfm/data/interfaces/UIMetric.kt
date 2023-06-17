package tm.wearable.wearabletfm.data.interfaces

import tm.wearable.wearabletfm.data.model.Metrics

interface UIMetric {
    fun onClick(metrics: Metrics)
}