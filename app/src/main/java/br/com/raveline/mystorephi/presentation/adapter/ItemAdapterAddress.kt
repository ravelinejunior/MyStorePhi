package br.com.raveline.mystorephi.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.raveline.mystorephi.data.model.AddressModel
import br.com.raveline.mystorephi.databinding.ItemAdapterAddressBinding
import br.com.raveline.mystorephi.utils.ListDiffUtil

class ItemAdapterAddress : RecyclerView.Adapter<ItemAdapterAddress.MyViewHolder>() {

    private var addressList = listOf<AddressModel>()
    private var radioButton:RadioButton? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemAdapterAddressBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val address = addressList[position]
        holder.binding(address)
    }

    override fun getItemCount(): Int = addressList.size


    inner class MyViewHolder(private val aBinding: ItemAdapterAddressBinding) :
        RecyclerView.ViewHolder(aBinding.root) {

        fun binding(addressModel: AddressModel) {
            aBinding.textViewAdapterAddressName.text = addressModel.address

            aBinding.radioButtonAdapterAddress.setOnClickListener {
                setSelectedRadioButton(it,bindingAdapterPosition)
            }

        }

    }

    private fun setSelectedRadioButton(v: View, position: Int){

        for (model in addressList){
            model.isSelected = false
        }

        addressList[position].isSelected = true
        if(radioButton != null){
            radioButton?.isChecked = false
        }

        radioButton = v  as RadioButton
        radioButton?.isChecked = true


    }

    fun setData(addresses: List<AddressModel>) {
        val diffUtil = ListDiffUtil(addressList, addresses)
        val result = DiffUtil.calculateDiff(diffUtil)
        addressList = addresses
        result.dispatchUpdatesTo(this)
    }

}