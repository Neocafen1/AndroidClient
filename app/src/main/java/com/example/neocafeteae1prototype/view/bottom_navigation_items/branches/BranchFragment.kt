package com.example.neocafeteae1prototype.view.bottom_navigation_items.branches

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.FragmentMapBinding
import com.example.neocafeteae1prototype.view.adapters.MainRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.view.tools.navigate
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view.tools.visible
import com.example.neocafeteae1prototype.view_model.branches_vm.BranchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BranchFragment : BaseFragment<FragmentMapBinding>(),
    RecyclerItemClickListener {

    private val branchViewModel: BranchViewModel by viewModels()
    private val myAdapter by lazy { MainRecyclerAdapter(this) }
    private lateinit var socialMedia: AllModels.SocialMedia

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Glide.with(requireActivity().applicationContext)
                .asGif()
                .load(R.drawable.progress_custom)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(binding.image)
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerMap.apply {
            adapter = myAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        branchViewModel.listOfBranches.observe(viewLifecycleOwner) {
            binding.recyclerMap.visible()
            binding.image.notVisible()
            myAdapter.setList(it.filials)
            socialMedia = AllModels.SocialMedia(it.instagram, it.facebook)
        }
    }

    override fun itemClicked(item: AllModels?) {
        item as AllModels.Filial
        navigate(BranchFragmentDirections.actionBranchFragmentToBranchesDetailFragment(item, socialMedia))
    }

    override fun setUpToolbar() {
        with(binding.include) {
            notification.setOnClickListener { navController.navigate(BranchFragmentDirections.actionBranchFragmentToNotification2()) }
            textView.text = resources.getText(R.string.mapping)
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentMapBinding {
        return FragmentMapBinding.inflate(inflater)
    }
}