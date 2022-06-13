package br.com.raveline.mystorephi.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.data.model.SizesModel
import br.com.raveline.mystorephi.databinding.ItemAdapterButtonSizesBinding

class ItemAdapterButtonSizes(
    private val fragment: Fragment
) : RecyclerView.Adapter<ItemAdapterButtonSizes.MyViewHolder>() {

    private var sizeList = setNewSizeList()
    private var lastPositionSelected = -1
    private var isSelected = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAdapterButtonSizesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val size = sizeList[position]
        holder.bind(size)
    }

    override fun getItemCount(): Int = sizeList.size


    inner class MyViewHolder(val binding: ItemAdapterButtonSizesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(size: SizesModel) {
            binding.apply {
                radioButtonAdapterButtonSize.text = size.selectedSize
            }

            binding.radioButtonAdapterButtonSize.setOnClickListener {
                handleRadioCheck(bindingAdapterPosition)
            }

            if (size.isSelected) {
                binding.radioButtonAdapterButtonSize.isChecked =
                    sizeList[lastPositionSelected].isSelected

                binding.cardViewAdapterButtonSize.setCardBackgroundColor(R.color.standart_purple)
                binding.radioButtonAdapterButtonSize.setBackgroundColor(R.color.standart_purple)

                binding.radioButtonAdapterButtonSize.setTextColor(
                    fragment.requireContext().getColor(R.color.white)
                )

                binding.radioButtonAdapterButtonSize.setBackgroundColor(
                    android.R.color.transparent
                )

            } else {
                binding.cardViewAdapterButtonSize.setCardBackgroundColor(R.color.mercury)
                binding.radioButtonAdapterButtonSize.setBackgroundColor(
                    R.color.mercury
                )
                binding.radioButtonAdapterButtonSize.setTextColor(
                    R.color.black
                )
            }


        }

        private fun handleRadioCheck(position: Int) {

            if (lastPositionSelected == -1) {
                isSelected = true
                lastPositionSelected = position
                sizeList[position].isSelected = true
            } else if (position != lastPositionSelected) {
                isSelected = true
                sizeList[lastPositionSelected].isSelected = false
                sizeList[position].isSelected = true
                lastPositionSelected = position
            }
            notifyDataSetChanged()

        }


    }


    private fun setNewSizeList(): List<SizesModel> = listOf(
        SizesModel(selectedSize = "S"),
        SizesModel(selectedSize = "M"),
        SizesModel(selectedSize = "L"),
        SizesModel(selectedSize = "X"),
        SizesModel(selectedSize = "XL"),
        SizesModel(selectedSize = "XXL"),
    )

    /*]

    1) primeiro -> nenhum selecionado
                    -> lastPosition = position
                    -> altero valor na lista no indice clicado
                    -> verificar se lastPosition = -1
    2) segundo -> ja existe elemento associadp
                    -> elemento clicado é igual lastPosition?


    *  if (b) {
                    controlIndex = this.bindingAdapterPosition
                    binding.radioButtonAdapterButtonSize.background = ContextCompat.getDrawable(
                        fragment.requireContext(), R.drawable.bg_value_item_card_button
                    )
                    binding.radioButtonAdapterButtonSize.setTextColor(
                        R.color.white
                    )
                } else {
                    binding.radioButtonAdapterButtonSize.setBackgroundColor(
                        R.color.mercury
                    )
                    binding.radioButtonAdapterButtonSize.setTextColor(
                        R.color.black
                    )
                }
    * */


}




