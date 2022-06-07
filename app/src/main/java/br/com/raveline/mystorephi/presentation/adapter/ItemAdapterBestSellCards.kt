package br.com.raveline.mystorephi.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.data.model.BestSellModel
import br.com.raveline.mystorephi.databinding.ItemAdapterBestSellCardsBinding
import br.com.raveline.mystorephi.utils.ListDiffUtil
import br.com.raveline.mystorephi.utils.SystemFunctions.replaceDotToComma
import com.bumptech.glide.Glide
import java.util.*

class ItemAdapterBestSellCards : RecyclerView.Adapter<ItemAdapterBestSellCards.MyViewHolder>() {

    private var bestSellList: List<BestSellModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemAdapterBestSellCardsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val feature = bestSellList[position]
        holder.binding(feature)
    }

    override fun getItemCount(): Int = bestSellList.size


    class MyViewHolder(private val itemBinding: ItemAdapterBestSellCardsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun binding(bestSellModel: BestSellModel) {

            val circular =
                CircularProgressDrawable(itemBinding.imageViewAdapterBestSellCards.context).apply {
                    strokeWidth = 10f
                    centerRadius = 50f
                    start()
                }

            itemBinding.apply {
                Glide.with(imageViewAdapterBestSellCards)
                    .load(bestSellModel.imageUrl)
                    .centerCrop()
                    .placeholder(circular)
                    .into(imageViewAdapterBestSellCards)

                imageViewAdapterBestSellCards.setOnClickListener {
                    Navigation.findNavController(itemBinding.root)
                        .navigate(R.id.action_homeFragment_to_itemFragment)
                }

                textViewAdapterBestSellCardsName.text =
                    replaceDotToComma(bestSellModel.price)
                textViewAdapterBestSellCardsPrice.text = bestSellModel.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }


        }

    }

    fun setAdapter(bestSell: List<BestSellModel>) {
        val diffUtil = ListDiffUtil(bestSellList, bestSell)
        val result = DiffUtil.calculateDiff(diffUtil)
        bestSellList = bestSell
        result.dispatchUpdatesTo(this)
    }

}

