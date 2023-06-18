package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.model.Metrics
import tm.wearable.wearabletfm.databinding.LoadingItemBinding
import tm.wearable.wearabletfm.databinding.MetricsDetailTypeItemBinding
import tm.wearable.wearabletfm.utils.TypeMetrics

class MetricsDetailAdapter (val context: Context, var list: ArrayList<Metrics?>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    lateinit var mcontext: Context
    val VIEW_TYPE_ITEM = 0
    val VIEW_TYPE_LOADING = 1


    class DevSpecSensorHolder(binding: MetricsDetailTypeItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        fun binData(metrics: Metrics?, context: Context){
            binding.tvValue.text = metrics!!.value + " " + metrics.measure
            binding.linear.isVisible = false
            binding.linear1.isVisible = false
            try {

                if (metrics.type == TypeMetrics.sleep_name){
                    /*binding.linear1.isVisible = true
                    binding.totalSleep.text = context.resources.getString(R.string.from)+" "+ deviceData.created_at.split(" ")[1] +" "+
                            context.resources.getString(R.string.to)+" "+
                            Utils.addtMinutesFromTime(
                                datetime = deviceData.created_at,
                                minutes = deviceData.value_metric.toInt()
                            ).split(" ")[1]
                    val adapter = DeviceSpecSensorSleepDataAdapter(context = context, list = deviceData.detailDataMetrics.sortedBy { it.sensor_type }.reversed())
                    binding.rvDetailsDataMetrics.layoutManager =  GridLayoutManager(context, 2)
                    binding.rvDetailsDataMetrics.adapter = adapter*/
                }
                else{
                    Log.e("", "binData: show data other types", )
                    binding.linear.isVisible = true
                }
                binding.tvDatetime.text = metrics.datetime
            }catch (e: java.lang.Exception){
                binding.tvDatetime.text = "11:00:00"
            }
        }
    }

    class LoadingHolder(binding: LoadingItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        fun binData(){
            binding.progressBar.isVisible = true
        }
    }

    fun getData () =  this.list

    fun setNewD(){
        list = arrayListOf()
        notifyDataSetChanged()
    }
    fun setData(newList: ArrayList<Metrics?>){
        //list = newList.sortedBy { it?.created_at }.reversed() as ArrayList<DataMetrics?>
        list = newList
        notifyDataSetChanged()
    }

    fun setNewData(){
        this.list = arrayListOf()
    }
    fun addData(newList: ArrayList<Metrics?>) {
        this.list.addAll(newList)
        notifyDataSetChanged()
    }

    fun isData() = this.list.size > 0

    fun getItemAtPosition(position: Int): Metrics? {
        return list[position]
    }

    override fun getItemId(position: Int): Long {

        val dataMetrics: Metrics? = list.get(position)
        if (dataMetrics != null) {
            return dataMetrics.id.toLong()
        }
        return 0
    }

    fun addLoadingView() {
        //Add loading item
        android.os.Handler().post {
            list.add(null)
            notifyItemInserted(list.size - 1)
        }
    }


    fun removeLoadingView() {
        //Remove loading item
        if (list.size != 0) {
            list.removeAt(list.size - 1)
        }
    }

    fun removeNull(){
        try {
            this.list.forEachIndexed { index, dataMetrics ->
                if (dataMetrics == null){
                    list.removeAt(index)
                }
            }
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == VIEW_TYPE_ITEM) {
            val binding = MetricsDetailTypeItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.metrics_detail_type_item,parent, false))
            DevSpecSensorHolder(binding)
        } else {
            val binding = LoadingItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.loading_item,parent, false))
            LoadingHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (list[position] == null) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_ITEM) {
            val newholder = holder as DevSpecSensorHolder
            newholder.binData(metrics = list[position], context = context)
            //holder.itemView.itemtextview.text = itemsCells[position]
        }
    }
}
