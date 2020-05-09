package com.ama.configuration

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.ama.R
import com.ama.core.getLocationPermissions
import com.ama.core.hideSoftKeyboard
import com.ama.core.requestPermission
import com.ama.core.toast
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
    ): View = inflater.inflate(R.layout.fragment_configuration, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupConfigurationButton()
        setupSuccessObserver()
        setupErrorObserver()
        setupIsLoadingObserver()
    }

    private fun setupConfigurationButton() {
        btn_configuration.setOnClickListener {
            context?.hideSoftKeyboard(view)
            et_configuration_id.clearFocus()
            val configurationId = et_configuration_id.text.toString()
            viewModel.loadConfiguration(configurationId)
        }
    }

    private fun setupSuccessObserver() {
        viewModel.success.observe(viewLifecycleOwner) { configurationId ->
            activity?.let {
                val permissions = getLocationPermissions() + Manifest.permission.READ_PHONE_STATE
                it.requestPermission(
                    permissions,
                    onGranted = {
                        viewModel.savePermissionEvent(true)
                        openLocationFragment(configurationId)
                    },
                    onDenied = {
                        viewModel.savePermissionEvent(false)
                        it.toast("Permission not granted")
                    }
                )
            }
        }
    }

    private fun openLocationFragment(configurationId: String) {
        val action = ConfigurationFragmentDirections.openLocationFragment(configurationId)
        findNavController().navigate(action)
    }

    private fun setupErrorObserver() {
        viewModel.error.observe(viewLifecycleOwner) { error ->
            context?.let {
                val errorMessage = when (error) {
                    is ConfigurationViewModel.Error.ConfigurationIdEmpty -> {
                        it.getString(R.string.configuration_id_empty_error)
                    }
                    is ConfigurationViewModel.Error.Remote -> error.message
                }
                it.toast(errorMessage)
            }
        }
    }

    private fun setupIsLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            pb_loading.isVisible = it
            btn_configuration.isEnabled = !it
            et_configuration_id.isEnabled = !it
        }
    }
}