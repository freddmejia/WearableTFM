package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.interfaces.UIMedicine
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.databinding.MedicinelittleItemBinding
import tm.wearable.wearabletfm.utils.Utils

class MedicinesLittleAdapter(val context: Context, var list: List<Medicine>):
    RecyclerView.Adapter<MedicinesLittleAdapter.holderAdapter>() {

    class holderAdapter(binding: MedicinelittleItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        fun binData(medicine: Medicine){
            binding.name.text = medicine.name
            binding.days.text = getDays(medicine = medicine)
            Log.e("", "MEDICINE: "+medicine.details.joinToString { it.hour+" "+it.day.toString() } )
        }

        fun getDays(medicine: Medicine): String {
            val map = mapOf(
                Utils.monday to "L",
                Utils.tuesday to "M",
                Utils.wednesday to "X",
                Utils.thursday to "J",
                Utils.friday to "V",
                Utils.saturday to "S",
                Utils.sunday to "D"
            )
            val days = medicine.details.sortedBy { it.day }.map { it.day }
            return days.map { map[it] }.joinToString(",")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderAdapter {
        val binding = MedicinelittleItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.medicinelittle_item,parent, false))
        return holderAdapter(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: holderAdapter, position: Int) {
        holder.binData(medicine = list[position])
    }

    override fun getItemCount() = list.size

    fun setNewData(arrayList: List<Medicine>) {
        list = arrayList
        notifyDataSetChanged()
    }


}
