package br.com.raveline.mystorephi.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.raveline.mystorephi.R
import br.com.raveline.mystorephi.databinding.FragmentAddAddressBinding
import br.com.raveline.mystorephi.databinding.FragmentAddressBinding

class AddAddressFragment : Fragment() {

    private var addressBinding: FragmentAddAddressBinding? = null
    private val binding get() = addressBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addressBinding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarAddAddressFragment.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        addressBinding = null
    }

}