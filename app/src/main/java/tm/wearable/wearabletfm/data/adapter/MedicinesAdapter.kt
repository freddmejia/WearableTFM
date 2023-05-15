package tm.wearable.wearabletfm.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tm.wearable.wearabletfm.R
import tm.wearable.wearabletfm.data.interfaces.UIMed
import tm.wearable.wearabletfm.data.model.Medicine
import tm.wearable.wearabletfm.databinding.MedicineItemBinding

class MedicinesAdapter (val context: Context, var list: List<Medicine>, val observer: UIMed):
    RecyclerView.Adapter<MedicinesAdapter.holderAdapter>() {

    class holderAdapter(binding: MedicineItemBinding): RecyclerView.ViewHolder(binding.root){
        private val binding = binding
        fun binData(medicine: Medicine, observer: UIMed){
            binding.name.text = medicine.name
            binding.edit.setOnClickListener {
                observer.edit(medicine = medicine)
            }
            binding.delete.setOnClickListener {
                observer.delete(medicine = medicine)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holderAdapter {
        val binding = MedicineItemBinding.bind(LayoutInflater.from(context).inflate(R.layout.medicine_item,parent, false))
        return holderAdapter(
            binding = binding
        )
    }

    override fun onBindViewHolder(holder: holderAdapter, position: Int) {
        holder.binData(medicine = list[position], observer = observer)
    }

    override fun getItemCount() = list.size

    fun setNewData(arrayList: List<Medicine>) {
        list = arrayList
        notifyDataSetChanged()
    }
}