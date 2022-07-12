package br.com.raveline.mystorephi.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.data.model.CategoryModel
import br.com.raveline.mystorephi.databinding.ItemAdapterHomeCardsBinding
import br.com.raveline.mystorephi.presentation.fragment.HomeFragmentDirections
import br.com.raveline.mystorephi.utils.ListDiffUtil
import com.bumptech.glide.Glide

class ItemAdapterHomeCards : RecyclerView.Adapter<ItemAdapterHomeCards.MyViewHolder>() {

    private var categoryList: List<CategoryModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemAdapterHomeCardsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = categoryList[position]
        holder.binding(category)
    }

    override fun getItemCount(): Int = categoryList.size


    class MyViewHolder(private val itemBinding: ItemAdapterHomeCardsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun binding(categoryModel: CategoryModel) {

            val circular =
                CircularProgressDrawable(itemBinding.imageViewAdapterHomeCards.context).apply {
                    strokeWidth = 10f
                    centerRadius = 50f
                    start()
                }

            itemBinding.apply {
                Glide.with(imageViewAdapterHomeCards)
                    .load(categoryModel.imageUrl)
                    .centerCrop()
                    .placeholder(circular)
                    .into(imageViewAdapterHomeCards)

                imageViewAdapterHomeCards.setOnClickListener {
                    val actions = HomeFragmentDirections.actionHomeFragmentToItemFragment().setType(categoryModel.type)
                    Navigation.findNavController(itemBinding.root).navigate(actions)
                }
            }


        }

    }

    fun setAdapter(categories: List<CategoryModel>) {
        val diffUtil = ListDiffUtil(categoryList, categories)
        val result = DiffUtil.calculateDiff(diffUtil)
        categoryList = categories
        result.dispatchUpdatesTo(this)
    }

}

