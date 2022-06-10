package br.com.raveline.mystorephi.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.raveline.mystorephi.databinding.FragmentItemDetailBinding
import br.com.raveline.mystorephi.utils.SystemFunctions
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private var _itemBinding: FragmentItemDetailBinding? = null
    private val itemBinding get() = _itemBinding!!

    private val args: ItemDetailFragmentArgs by navArgs()

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
            textViewItemDetailFragmentRatingText.text = feature.rating.toString()
            buttonItemDetailFragmentRatingValue.text = feature.rating.toString()
            Glide.with(imageViewItemDetailFragment)
                .load(feature.imageUrl)
                .centerCrop()
                .placeholder(circular)
                .into(imageViewItemDetailFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _itemBinding = null
    }

}