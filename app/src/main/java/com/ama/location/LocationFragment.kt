package com.ama.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ama.R
import com.ama.configuration.ConfigurationViewModel
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_location.*
import javax.inject.Inject

class LocationFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<LocationViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChangeConfigurationButton()
    }

    private fun setupChangeConfigurationButton() {
        btn_change_configuration.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}