package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.data.interfaces.UICDay
import tm.wearable.wearabletfm.data.model.CDay
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.databinding.DayCalendarItemBinding

class CalendarAdapter (val context: Context, var listDays: List<CDay>, val observer: UICDay): RecyclerView.Adapter<CalendarAdapter.CAHolder>() {
    class CAHolder(binding: DayCalendarItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        val TAG= "CalendarAdapter"
        fun binData(cDay: CDay, observer: UICDay, context: Context){
            binding.tvDayText.text = cDay.dayText
            binding.tvNumberText.text = cDay.dayNumber.toString()
            binding.cvDay.setStrokeColor(context.resources.getColorStateList(R.color.color5))
            binding.cvDay.setCardBackgroundColor(context.resources.getColor(R.color.white))
            binding.tvDayText.setTextColor(context.resources.getColorStateList(R.color.black))
            binding.tvNumberText.setTextColor(context.resources.getColorStateList(R.color.black))

            if (cDay.isSelected){
                binding.cvDay.setStrokeColor(context.resources.getColorStateList(R.color.white))
                binding.cvDay.setCardBackgroundColor(context.resources.getColor(R.color.color4))
                binding.tvDayText.setTextColor(context.resources.getColorStateList(R.color.white))
                binding.tvNumberText.setTextColor(context.resources.getColorStateList(R.color.white))
                observer.dateChange(cDay = cDay)
            }
            binding.cvDay.setOnClickListener {
                observer.onClick(cDay = cDay)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CAHolder {
        val binding = DayCalendarItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.day_calendar_item,parent, false))
        return CAHolder(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: CAHolder, position: Int) {
        holder.binData(cDay = listDays[position], observer = observer, context = context)
    }

    override fun getItemCount() = listDays.size

    fun setNewData( newList: List<CDay>){
        listDays = newList
        try {
            notifyDataSetChanged()
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }

    }

}