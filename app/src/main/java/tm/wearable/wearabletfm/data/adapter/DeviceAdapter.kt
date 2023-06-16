package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.model.Device
import tm.wearable.wearabletfm.databinding.DeviceItemBinding
import tm.wearable.wearabletfm.utils.Utils

class DeviceAdapter (val context: Context, var list: List<Device>):
    RecyclerView.Adapter<DeviceAdapter.holderAdapter>() {

    class holderAdapter(binding: DeviceItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding

        fun binData(device: Device, context: Context){
            binding.imgBatteryLevel.isVisible = true
            binding.nameDevice.text = device.device_version
            binding.macDevice.text = device.mac_address
            binding.lastSync.isVisible = true
            drawBatteryIcon(context = context, levelBattery = device.battery_level.toInt())
            try {
                binding.lastSync.text = Utils.convertDateFormat(device.last_sync_time)
            }catch (e: java.lang.Exception){
                binding.lastSync.isVisible = false
                e.printStackTrace()
            }

        }
        fun drawBatteryIcon(context: Context,levelBattery: Int) {
            binding.imgBatteryLevel.isVisible = false
            binding.batteryLevelPercentage.isVisible = false

            binding.imgBatteryLevel.batteryLevel = levelBattery
            binding.batteryLevelPercentage.text = levelBattery.toString() + context.resources.getString(R.string.percentage)
            binding.batteryLevelPercentage.setTextColor(Color.BLACK)
            binding.imgBatteryLevel.infillColor = Color.BLUE
            binding.imgBatteryLevel.borderColor = Color.BLACK

            if (levelBattery <= Utils.batteryLevelMin) {
                binding.imgBatteryLevel.infillColor = Color.RED
                binding.imgBatteryLevel.borderColor = Color.RED
                binding.batteryLevelPercentage.setTextColor(Color.RED)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderAdapter {
        val binding = DeviceItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.device_item,parent, false))
        return holderAdapter(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: holderAdapter, position: Int) {
        holder.binData(device = list[position], context = context)
    }

    override fun getItemCount() = list.size

    fun setNewData(arrayList: List<Device>) {
        list = arrayList
        notifyDataSetChanged()
    }
}