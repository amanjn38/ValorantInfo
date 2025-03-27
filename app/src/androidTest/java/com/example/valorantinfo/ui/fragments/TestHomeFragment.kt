package com.example.valorantinfo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valorantinfo.data.models.Category
import com.example.valorantinfo.databinding.FragmentHomeBinding
import com.example.valorantinfo.ui.adapters.CategoryAdapter

/**
 * Test version of HomeFragment without Hilt dependencies
 */
class TestHomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategoriesRecyclerView()
        loadCategories()
    }

    private fun setupCategoriesRecyclerView() {
        val categoryAdapter = CategoryAdapter { /* No navigation in tests */ }
        binding.rvCategories.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun loadCategories() {
        val categories = listOf(
            Category(
                title = "AGENTS",
                description = "View all agent details and abilities",
            ),
        )

        (binding.rvCategories.adapter as CategoryAdapter).submitList(categories)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
