package com.example.neocafeteae1prototype.view.bottom_navigation_items.branches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.databinding.FragmentMapBinding
import com.example.neocafeteae1prototype.view.adapters.MainRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.root.BaseFragmentWithErrorLiveData
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.view.tools.showToast
import com.example.neocafeteae1prototype.view_model.branches_vm.BranchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BranchFragment : BaseFragmentWithErrorLiveData<FragmentMapBinding>(),
    RecyclerItemClickListener {

    private val branchViewModel: BranchViewModel by viewModels()
    private val myAdapter by lazy { MainRecyclerAdapter(this) }
    private lateinit var socialMedia: AllModels.SocialMedia

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recyclerMap.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        branchViewModel.listOfBranches.observe(viewLifecycleOwner) {
            myAdapter.setList(it.filials)
            socialMedia = AllModels.SocialMedia(it.instagram, it.facebook)
        }
    }

    override fun itemClicked(item: AllModels?) {
        item as AllModels.Filial
        findNavController().navigate(BranchFragmentDirections.actionBranchFragmentToBranchesDetailFragment(item, socialMedia))
    }

    override fun setUpToolbar() {
        with(binding.include) {
            notification.setOnClickListener { findNavController().navigate(BranchFragmentDirections.actionBranchFragmentToNotification2()) }
            textView.text = resources.getText(R.string.mapping)
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentMapBinding {
        return FragmentMapBinding.inflate(inflater)
    }

    override fun errorListener(): LiveData<Boolean> = branchViewModel.errorLiveData
}