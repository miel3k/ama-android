package com.ama.configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ama.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_configuration.*
import javax.inject.Inject

class ConfigurationFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ConfigurationViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_configuration,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupConfigurationButton()
        setupSuccess()
        setupError()
    }

    private fun setupConfigurationButton() {
        btn_configuration.setOnClickListener {
            val configurationId = et_configuration_id.text.toString()
            viewModel.loadConfiguration(configurationId)
        }
    }

    private fun setupSuccess() {
        viewModel.success.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(R.id.action_configuration_fragment)
        })
    }

    private fun setupError() {
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            context?.let {
                val errorMessage = when (error) {
                    is ConfigurationViewModel.Error.ConfigurationIdEmpty -> {
                        it.getString(R.string.configuration_id_empty_error)
                    }
                    is ConfigurationViewModel.Error.Remote -> error.message
                }
                Toast.makeText(it, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}