package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.content.SyncStatusObserver
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.interfaces.UIMetric
import tm.wearable.wearabletfm.data.model.Device
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.databinding.DeviceItemBinding
import tm.wearable.wearabletfm.databinding.MetricsGenItemBinding
import tm.wearable.wearabletfm.utils.TypeMetrics
import tm.wearable.wearabletfm.utils.Utils

class MetricsGeneralAdapter (val context: Context, var list: List<Metrics>, val observer: UIMetric? = null):
    RecyclerView.Adapter<MetricsGeneralAdapter.holderAdapter>() {

    class holderAdapter(binding: MetricsGenItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding

        fun binData(metrics: Metrics, context: Context, observer: UIMetric?=null){

            Log.e("", "binData: "+metrics.type )
            binding.valueMetric.text = metrics.value +" \n"+metrics.measure
            binding.imageType.setImageResource(TypeMetrics.getIconSensor(type = metrics.type))
            val color = ContextCompat.getColor(context, TypeMetrics.getColorDetailDataMetrics(type = metrics.type))
            binding.cvMetricColor.setStrokeColor(color)
            binding.cvMetricColor.setOnClickListener {
                observer?.onClick(metrics = metrics)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderAdapter {
        val binding = MetricsGenItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.metrics_gen_item,parent, false))
        return holderAdapter(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: holderAdapter, position: Int) {
        holder.binData(metrics = list[position], context = context, observer = observer)
    }

    override fun getItemCount() = list.size

    fun setNewData(arrayList: List<Metrics>) {
        list = arrayList
        notifyDataSetChanged()
    }
}