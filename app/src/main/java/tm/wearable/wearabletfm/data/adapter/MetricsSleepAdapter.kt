package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.model.DetailMetrics
import tm.wearable.wearabletfm.databinding.SleepTimeBinding
import tm.wearable.wearabletfm.utils.TypeMetrics

class MetricsSleepAdapter (val context: Context, var list: List<DetailMetrics>):
    RecyclerView.Adapter<MetricsSleepAdapter.holderAdapter>() {

    class holderAdapter(binding: SleepTimeBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        fun binData(metrics: DetailMetrics){
            binding.valueMetric.text = metrics.value
            binding.sensorType.text = TypeMetrics.getNameSensor(type = metrics.type)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderAdapter {
        val binding = SleepTimeBinding.bind(LayoutInflater.from(context).inflate(R.layout.sleep_time,parent, false))
        return holderAdapter(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: holderAdapter, position: Int) {
        holder.binData(metrics = list[position])
    }

    override fun getItemCount() = list.size

    fun setNewData(arrayList: List<DetailMetrics>) {
        list = arrayList
        notifyDataSetChanged()
    }

    fun deleteElement(position: Int){
        notifyItemRemoved(position)
    }

}