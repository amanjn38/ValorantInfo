package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.valorantinfo.data.models.events.Event
import com.example.valorantinfo.databinding.FragmentEventDetailsBinding
import com.example.valorantinfo.ui.viewmodels.EventDetailsUiState
import com.example.valorantinfo.ui.viewmodels.EventDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class EventDetailsFragment : Fragment() {

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventDetailsViewModel by viewModels()
    private val args: EventDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.loadEvent(args.eventUuid)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is EventDetailsUiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE
                        binding.contentGroup.visibility = View.GONE
                    }
                    is EventDetailsUiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE
                        binding.contentGroup.visibility = View.VISIBLE
                        updateEventDetails(state.event)
                    }
                    is EventDetailsUiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.contentGroup.visibility = View.GONE
                        binding.tvError.text = state.message
                        binding.tvError.setOnClickListener {
                            viewModel.retry(args.eventUuid)
                        }
                    }
                }
            }
        }
    }

    private fun updateEventDetails(event: Event) {
        binding.apply {
            tvEventName.text = event.displayName
            tvEventShortName.text = event.shortDisplayName
            tvEventTime.text = formatEventTime(event.startTime, event.endTime)
            tvAssetPath.text = event.assetPath
        }
    }

    private fun formatEventTime(startTime: String, endTime: String): String {
        val start = Instant.parse(startTime)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        val end = Instant.parse(endTime)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
        return "${formatter.format(start)} - ${formatter.format(end)}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 