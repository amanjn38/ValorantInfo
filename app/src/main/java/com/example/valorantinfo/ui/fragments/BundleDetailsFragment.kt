package com.example.valorantinfo.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.valorantinfo.databinding.FragmentBundleDetailsBinding
import com.example.valorantinfo.ui.viewmodels.BundleDetailsViewModel
import com.example.valorantinfo.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BundleDetailsFragment : Fragment() {

    private var _binding: FragmentBundleDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BundleDetailsViewModel by viewModels()
    private val args: BundleDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBundleDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initially hide all content elements
        hideContentElements()

        viewModel.fetchBundleDetails(args.bundleUuid)
        observeBundleDetails()
    }

    private fun hideContentElements() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvError.visibility = View.GONE

        // Hide all content sections until data is loaded
        binding.tvBundleName.visibility = View.GONE
        binding.cvBundleIcon.visibility = View.GONE
        binding.tvDescriptionLabel.visibility = View.GONE
        binding.tvBundleDescription.visibility = View.GONE
        binding.tvExtraLabel.visibility = View.GONE
        binding.tvExtraDescription.visibility = View.GONE
        binding.cvPromoImage.visibility = View.GONE
        binding.tvBundleUuid.visibility = View.GONE
    }

    private fun observeBundleDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.bundleDetails.collectLatest { result ->
                when (result) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.GONE

                        result.data?.let { bundle ->
                            // Show main elements except images (they'll show when loaded)
                            binding.tvBundleName.visibility = View.VISIBLE
                            binding.cvBundleIcon.visibility = View.VISIBLE
                            binding.tvDescriptionLabel.visibility = View.VISIBLE
                            binding.tvBundleDescription.visibility = View.VISIBLE
                            binding.cvPromoImage.visibility = View.VISIBLE
                            binding.tvBundleUuid.visibility = View.VISIBLE

                            // Set data
                            binding.tvBundleName.text = bundle.displayName
                            binding.tvBundleDescription.text = bundle.description
                            binding.tvBundleUuid.text = "UUID: ${bundle.uuid}"

                            // Load the bundle icon with loading state handling
                            Glide.with(requireContext())
                                .load(bundle.displayIcon)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .listener(object :
                                    RequestListener<android.graphics.drawable.Drawable> {

                                    override fun onLoadFailed(
                                        e: GlideException?,
                                        model: Any?,
                                        target: Target<Drawable>,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding.progressBarIcon.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(
                                        resource: Drawable,
                                        model: Any,
                                        target: Target<Drawable>?,
                                        dataSource: DataSource,
                                        isFirstResource: Boolean
                                    ): Boolean {
                                        binding.progressBarIcon.visibility = View.GONE
                                        binding.ivBundleIcon.visibility = View.VISIBLE
                                        return false
                                    }
                                })
                                .into(binding.ivBundleIcon)

                            // Load the promotional image with loading state handling
                            if (bundle.verticalPromoImage != null) {
                                Glide.with(requireContext())
                                    .load(bundle.verticalPromoImage)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .listener(object :
                                        RequestListener<android.graphics.drawable.Drawable> {
                                        override fun onLoadFailed(
                                            e: GlideException?,
                                            model: Any?,
                                            target: Target<Drawable>,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            binding.progressBarPromo.visibility = View.GONE
                                            return false
                                        }

                                        override fun onResourceReady(
                                            resource: Drawable,
                                            model: Any,
                                            target: Target<Drawable>?,
                                            dataSource: DataSource,
                                            isFirstResource: Boolean
                                        ): Boolean {
                                            binding.progressBarPromo.visibility = View.GONE
                                            binding.ivPromoImage.visibility = View.VISIBLE
                                            return false
                                        }
                                    })
                                    .into(binding.ivPromoImage)
                            } else {
                                binding.progressBarPromo.visibility = View.GONE
                                binding.cvPromoImage.visibility = View.GONE
                            }

                            // Show extra description if available
                            if (!bundle.extraDescription.isNullOrEmpty()) {
                                binding.tvExtraLabel.visibility = View.VISIBLE
                                binding.tvExtraDescription.visibility = View.VISIBLE
                                binding.tvExtraDescription.text = bundle.extraDescription
                            } else {
                                binding.tvExtraLabel.visibility = View.GONE
                                binding.tvExtraDescription.visibility = View.GONE
                            }
                        }
                    }

                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.tvError.visibility = View.VISIBLE
                        binding.tvError.text = result.message

                        // Hide content sections
                        binding.tvBundleName.visibility = View.GONE
                        binding.cvBundleIcon.visibility = View.GONE
                        binding.tvDescriptionLabel.visibility = View.GONE
                        binding.tvBundleDescription.visibility = View.GONE
                        binding.tvExtraLabel.visibility = View.GONE
                        binding.tvExtraDescription.visibility = View.GONE
                        binding.cvPromoImage.visibility = View.GONE
                        binding.tvBundleUuid.visibility = View.GONE
                    }

                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.tvError.visibility = View.GONE

                        // Keep content sections hidden during loading
                        binding.tvBundleName.visibility = View.GONE
                        binding.cvBundleIcon.visibility = View.GONE
                        binding.tvDescriptionLabel.visibility = View.GONE
                        binding.tvBundleDescription.visibility = View.GONE
                        binding.tvExtraLabel.visibility = View.GONE
                        binding.tvExtraDescription.visibility = View.GONE
                        binding.cvPromoImage.visibility = View.GONE
                        binding.tvBundleUuid.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 