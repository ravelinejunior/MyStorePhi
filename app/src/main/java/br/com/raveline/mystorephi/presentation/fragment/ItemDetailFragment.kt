package br.com.raveline.mystorephi.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.FragmentItemDetailBinding
import br.com.raveline.mystorephi.presentation.adapter.ItemAdapterButtonSizes
import br.com.raveline.mystorephi.utils.SystemFunctions
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private var _itemBinding: FragmentItemDetailBinding? = null
    private val itemBinding get() = _itemBinding!!

    private val args: ItemDetailFragmentArgs by navArgs()

    private val itemAdapter: ItemAdapterButtonSizes by lazy {
        ItemAdapterButtonSizes(requireParentFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _itemBinding = FragmentItemDetailBinding.inflate(inflater, container, false)

        return itemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayData()
    }

    private fun displayData() {
        val feature = args.selectedFeature
        val circular =
            CircularProgressDrawable(requireContext()).apply {
                strokeWidth = 10f
                centerRadius = 50f
                start()
            }

        itemBinding.apply {
            textViewItemDetailFragmentTitle.text = feature.name
            textViewItemDetailFragmentDescription.text = feature.description
            textViewItemDetailFragmentPrice.text = SystemFunctions.replaceDotToComma(feature.price)
            textViewItemDetailFragmentRatingText.text = setRatingValue(feature.rating)
            buttonItemDetailFragmentRatingValue.text = feature.rating.toString()
            Glide.with(imageViewItemDetailFragment)
                .load(feature.imageUrl)
                .centerCrop()
                .placeholder(circular)
                .into(imageViewItemDetailFragment)
        }

        itemBinding.buttonItemDetailFragmentBuy.setOnClickListener {
            findNavController().navigate(R.id.action_itemDetailFragment_to_addressFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        itemBinding.recyclerViewItemDetailFragmentSizes.apply {
            setHasFixedSize(true)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = itemAdapter
        }
    }

    private fun setRatingValue(value: Int): String {
        return when (value) {
            in 1..2 -> {
                getString(R.string.not_good_string)
            }
            in 3..4 -> {
                getString(R.string.good_string)
            }
            else -> {
                getString(R.string.excellent_string)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _itemBinding = null
    }

}