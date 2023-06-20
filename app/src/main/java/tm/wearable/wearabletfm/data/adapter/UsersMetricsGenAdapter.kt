package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.interfaces.UIMetricFriend
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.data.model.User
import tm.wearable.wearabletfm.databinding.UsersMetricsGenItemBinding
import tm.wearable.wearabletfm.utils.CompositionObj


class UsersMetricsGenAdapter (val context: Context, var list: List<CompositionObj<User, ArrayList<Metrics>>>, val observer: UIMetricFriend):
    RecyclerView.Adapter<UsersMetricsGenAdapter.holderAdapter>() {

    class holderAdapter(binding: UsersMetricsGenItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding

        fun binData(compositionObj: CompositionObj<User, ArrayList<Metrics>>, context: Context, observer: UIMetricFriend){
            binding.nameFriend.text = compositionObj.data.name
            binding.noData.isVisible = true
            binding.cvAccessData.isVisible = false
            if (compositionObj.message.isNotEmpty()) {
                binding.noData.isVisible = false
                binding.rvMetrics.isVisible = true
                binding.cvAccessData.isVisible = true
                binding.cvAccessData.setOnClickListener {
                    observer.onClick(friendMetric = compositionObj)
                }
                val metricsGeneralAdapter =
                    MetricsGeneralAdapter(context = context, list = compositionObj.message)
                binding.rvMetrics.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                binding.rvMetrics.adapter = metricsGeneralAdapter
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderAdapter {
        val binding = UsersMetricsGenItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.users_metrics_gen_item,parent, false))
        return holderAdapter(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: holderAdapter, position: Int) {
        holder.binData(compositionObj = list[position], context = context, observer = observer)
    }

    override fun getItemCount() = list.size

    fun setNewData(arrayList: List<CompositionObj<User, ArrayList<Metrics>>>) {
        list = arrayList
        notifyDataSetChanged()
    }
}