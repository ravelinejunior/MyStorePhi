package br.com.raveline.mystorephi.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.data.model.FeaturesModel
import br.com.raveline.mystorephi.databinding.ItemAdapterFeaturesCardsBinding
import br.com.raveline.mystorephi.presentation.fragment.HomeFragmentDirections
import br.com.raveline.mystorephi.utils.ListDiffUtil
import br.com.raveline.mystorephi.utils.SystemFunctions.replaceDotToComma
import com.bumptech.glide.Glide
import java.util.*

class ItemAdapterFeaturesCards(
    private val fragment: Fragment
) : RecyclerView.Adapter<ItemAdapterFeaturesCards.MyViewHolder>() {

    private var featureList: List<FeaturesModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemAdapterFeaturesCardsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val feature = featureList[position]
        holder.binding(feature)


        holder.itemView.setOnClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToItemDetailFragment(feature)
            fragment.findNavController().navigate(action)
        }


    }

    override fun getItemCount(): Int = featureList.size


    class MyViewHolder(private val itemBinding: ItemAdapterFeaturesCardsBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun binding(featuresModel: FeaturesModel) {

            val circular =
                CircularProgressDrawable(itemBinding.imageViewAdapterFeaturesCards.context).apply {
                    strokeWidth = 10f
                    centerRadius = 50f
                    start()
                }

            itemBinding.apply {
                Glide.with(imageViewAdapterFeaturesCards)
                    .load(featuresModel.imageUrl)
                    .centerCrop()
                    .placeholder(circular)
                    .into(imageViewAdapterFeaturesCards)

                textViewAdapterFeaturesCardsPrice.text =
                    replaceDotToComma(featuresModel.price)
                textViewAdapterFeaturesCardsName.text = featuresModel.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }

            }

        }

    }

    fun setAdapter(features: List<FeaturesModel>) {
        val diffUtil = ListDiffUtil(featureList, features)
        val result = DiffUtil.calculateDiff(diffUtil)
        featureList = features
        result.dispatchUpdatesTo(this)
    }

}

