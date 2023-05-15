package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.data.interfaces.UIMedicine
import tm.wearable.wearabletfm.data.model.MedicineDetail
import tm.wearable.wearabletfm.databinding.AddMedicineItemBinding
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.utils.Utils

class AddMedicineAdapter  (val context: Context, var list: List<MedicineDetail>, val observer: UIMedicine):
    RecyclerView.Adapter<AddMedicineAdapter.holderAdapter>() {

    class holderAdapter(binding: AddMedicineItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        //private val context = context
        fun binData(medicineDetail: MedicineDetail, observer: UIMedicine){
            binding.dateDetailMedicine.setText(medicineDetail.hour)

            binding.dateDetailMedicine.setOnClickListener {
                observer.chooseDate(medicineDetail = medicineDetail)
            }
            binding.deleteDay.setOnClickListener {
                observer.deleteDay(medicineDetail = medicineDetail)
            }

            resetCardViews(cardView =  binding.cvMonday, textView = binding.tvMonday)
            resetCardViews(cardView =  binding.cvTuesday, textView = binding.tvTuesday)
            resetCardViews(cardView =  binding.cvWednesday, textView = binding.tvWednesday)
            resetCardViews(cardView =  binding.cvThursday, textView = binding.tvThursday)
            resetCardViews(cardView =  binding.cvFriday, textView = binding.tvFriday)
            resetCardViews(cardView =  binding.cvSaturday, textView = binding.tvSaturday)
            resetCardViews(cardView =  binding.cvSunday, textView = binding.tvSunday)

            selectedCardView(
                isSelected = medicineDetail.isSelected,
                day = medicineDetail.day
            )


            binding.cvMonday.setOnClickListener {
                //medicineDetail.isSelected = !medicineDetail.isSelected
                observer.chooseDay(medicineDetail = medicineDetail, day = Utils.monday)
            }
            binding.cvTuesday.setOnClickListener {
                //medicineDetail.isSelected = !medicineDetail.isSelected
                observer.chooseDay(medicineDetail = medicineDetail, day = Utils.tuesday)
            }
            binding.cvWednesday.setOnClickListener {
                //medicineDetail.isSelected = !medicineDetail.isSelected
                observer.chooseDay(medicineDetail = medicineDetail, day = Utils.wednesday)
            }
            binding.cvThursday.setOnClickListener {
                //medicineDetail.isSelected = !medicineDetail.isSelected
                observer.chooseDay(medicineDetail = medicineDetail, day = Utils.thursday)
            }
            binding.cvFriday.setOnClickListener {
                //medicineDetail.isSelected = !medicineDetail.isSelected
                observer.chooseDay(medicineDetail = medicineDetail, day = Utils.friday)
            }
            binding.cvSaturday.setOnClickListener {
                //medicineDetail.isSelected = !medicineDetail.isSelected
                observer.chooseDay(medicineDetail = medicineDetail, day = Utils.saturday)
            }
            binding.cvSunday.setOnClickListener {
                //medicineDetail.isSelected = !medicineDetail.isSelected
                observer.chooseDay(medicineDetail = medicineDetail, day = Utils.sunday)
            }
        }

        fun resetCardViews(cardView: CardView, textView: TextView){
            cardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
            textView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
        }

        fun selectCardView(cardView: CardView, textView: TextView) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.blue_f4))
            textView.setTextColor(ContextCompat.getColor(binding.root.context, R.color.white))
        }

        fun selectedCardView(isSelected: Boolean, day: Int){
            //if (isSelected) {
                when(day ){
                    Utils.monday ->
                        selectCardView(cardView = binding.cvMonday, textView = binding.tvMonday)
                    Utils.tuesday ->
                        selectCardView(cardView = binding.cvTuesday, textView = binding.tvTuesday)
                    Utils.wednesday ->
                        selectCardView(cardView = binding.cvWednesday, textView = binding.tvWednesday)
                    Utils.thursday ->
                        selectCardView(cardView = binding.cvThursday, textView = binding.tvThursday)
                    Utils.friday ->
                        selectCardView(cardView = binding.cvFriday, textView = binding.tvFriday)
                    Utils.saturday ->
                        selectCardView(cardView = binding.cvSaturday, textView = binding.tvSaturday)
                    Utils.sunday ->
                        selectCardView(cardView = binding.cvSunday, textView = binding.tvSunday)
                }
            //}

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderAdapter {
        val binding = AddMedicineItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.add_medicine_item,parent, false))
        return holderAdapter(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: holderAdapter, position: Int) {
        holder.binData(medicineDetail = list[position], observer = observer)
    }

    override fun getItemCount() = list.size

    fun setNewData(arrayList: List<MedicineDetail>) {
        list = arrayList
        notifyDataSetChanged()
    }
}