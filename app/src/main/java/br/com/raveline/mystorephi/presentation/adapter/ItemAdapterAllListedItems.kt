package br.com.raveline.mystorephi.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.raveline.mystorephi.data.model.AllListedItemsModel
import br.com.raveline.mystorephi.databinding.ItemAdapterFromAllItemsBinding
import br.com.raveline.mystorephi.presentation.viewmodel.HomeViewModel
import br.com.raveline.mystorephi.utils.ListDiffUtil
import br.com.raveline.mystorephi.utils.SystemFunctions
import com.bumptech.glide.Glide

class ItemAdapterAllListedItems(private val homeViewModel: HomeViewModel) :
    RecyclerView.Adapter<ItemAdapterAllListedItems.MyViewHolder>() {

    private var allItems = listOf<AllListedItemsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemAdapterFromAllItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = allItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = allItems.size

    inner class MyViewHolder(private val itemBinding: ItemAdapterFromAllItemsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: AllListedItemsModel) {
            itemBinding.apply {
                textViewAdapterAllItemsName.text = item.name
                textViewAdapterAllItemsPrice.text = SystemFunctions.replaceDotToComma(
                    item.price
                )
                val circular =
                    CircularProgressDrawable(imageViewAdapterAllItems.context).apply {
                        strokeWidth = 10f
                        centerRadius = 50f
                        start()
                    }

                itemBinding.apply {
                    Glide.with(imageViewAdapterAllItems)
                        .load(item.imageUrl)
                        .placeholder(circular)
                        .into(imageViewAdapterAllItems)
                }
            }
        }
    }

    fun setData(listItems: List<AllListedItemsModel>) {
        val diffUtil = ListDiffUtil(allItems, listItems)
        val result = DiffUtil.calculateDiff(diffUtil)
        allItems = listItems
        result.dispatchUpdatesTo(this)
    }


}