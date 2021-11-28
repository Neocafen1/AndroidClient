package com.example.neocafeteae1prototype.view.notification

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.Consts
import com.example.neocafeteae1prototype.databinding.FragmentNotificationBinding
import com.example.neocafeteae1prototype.view.adapters.MainRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view_model.notification_vm.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class Notification : BaseFragment<FragmentNotificationBinding>() {

    private val viewModel: NotificationViewModel by viewModels()
    private val recyclerAdapter by lazy {MainRecyclerAdapter(null)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        setUpSwipeCallback()
    }

    private fun setUpSwipeCallback() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(Color.parseColor(Consts.ENABLE_COLOR))
                        .addActionIcon(R.drawable.ic_trash)
                        .create()
                        .decorate()

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        viewModel.list.removeAt(position)
                        recyclerAdapter.notifyItemRemoved(position)
                        viewModel.deleteNotificationsById(position)
                    }
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.notificationRecycler)
    }

    private fun setUpRecycler() {
        binding.notificationRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        recyclerAdapter.setList(viewModel.list)
    }

    override fun setUpToolbar() {
        binding.back.setOnClickListener { navController.navigateUp() }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentNotificationBinding {
        return FragmentNotificationBinding.inflate(inflater)
    }
}