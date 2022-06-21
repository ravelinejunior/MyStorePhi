package br.com.raveline.mystorephi.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.raveline.mystorephi.databinding.FragmentPaymentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentFragment : Fragment() {

    private var _itemBinding: FragmentPaymentBinding? = null
    private val itemBinding get() = _itemBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _itemBinding = FragmentPaymentBinding.inflate(layoutInflater, container, false)

        return itemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemBinding.apply {
            toolbarItemDetailFragment.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _itemBinding = null
    }
}