package com.pipe.d.dev.mviarchwine.favoriteModule.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pipe.d.dev.mviarchwine.commonModule.utils.Constants
import com.pipe.d.dev.mviarchwine.commonModule.utils.OnClickListener
import com.pipe.d.dev.mviarchwine.R
import com.pipe.d.dev.mviarchwine.updateModule.UpdateDialogFragment
import com.pipe.d.dev.mviarchwine.commonModule.entities.Wine
import com.pipe.d.dev.mviarchwine.WineApplication
import com.pipe.d.dev.mviarchwine.accountModule.model.AccountState
import com.pipe.d.dev.mviarchwine.commonModule.view.WineBaseFragment
import com.pipe.d.dev.mviarchwine.favoriteModule.FavoriteViewModel
import com.pipe.d.dev.mviarchwine.favoriteModule.FavoriteViewModelFactory
import com.pipe.d.dev.mviarchwine.favoriteModule.intent.FavoriteIntent
import com.pipe.d.dev.mviarchwine.favoriteModule.model.FavoriteRepository
import com.pipe.d.dev.mviarchwine.favoriteModule.model.FavoriteState
import com.pipe.d.dev.mviarchwine.favoriteModule.model.RoomDatabase
import com.pipe.d.dev.mviarchwine.mainModule.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/****
 * Project: Wines
 * From: com.cursosant.wines
 * Created by Alain Nicolás Tello on 06/02/24 at 20:23
 * All rights reserved 2024.
 *
 * All my Udemy Courses:
 * https://www.udemy.com/user/alain-nicolas-tello/
 * And Frogames formación:
 * https://cursos.frogamesformacion.com/pages/instructor-alain-nicolas
 *
 * Coupons on my Website:
 * www.alainnicolastello.com
 ***/
class FavouriteFragment : WineBaseFragment(), OnClickListener {

    private lateinit var adapter: WineFavListAdapter
    private lateinit var vm: FavoriteViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupAdapter()
        setupRecyclerView()
        setupSwipeRefresh()
        setupObservers()
    }
    private fun setupViewModel() {
        vm = ViewModelProvider(this, FavoriteViewModelFactory(
            FavoriteRepository(RoomDatabase())))[FavoriteViewModel::class.java]
    }

    private fun setupAdapter() {
        adapter = WineFavListAdapter()
        adapter.setOnClickListener(this)
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = this@FavouriteFragment.adapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.srlResults.setOnRefreshListener {
            getWines()
        }
    }

    private fun getWines() {
        lifecycleScope.launch(Dispatchers.IO) {
            vm.channel.send(FavoriteIntent.RequestWines)
            /*try {
                val wines = WineApplication.database.wineDao().getAllWines()
                withContext(Dispatchers.Main){
                    if (wines.isNotEmpty()) {
                        showNoDataView(false)
                        showRecyclerView(true)
                        adapter.submitList(wines)
                    } else {
                        showRecyclerView(false)
                        showNoDataView(true)
                    }
                }
            } catch (e: Exception) {
                showMsg(R.string.room_request_fail)
            } finally {
                showProgress(false)
            }*/
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            //val state = AccountState

            vm.state.collect { state ->
                when(state) {
                    is FavoriteState.Init -> {}
                    is FavoriteState.ShowProgress -> showProgress(true)
                    is FavoriteState.HideProgress -> showProgress(false)
                    is FavoriteState.RequestWinesSuccess -> {
                        adapter.submitList(state.list)
                        showNoDataView(state.list.isEmpty())
                        showRecyclerView(state.list.isNotEmpty())
                    }
                    is FavoriteState.AddWineSuccess -> showMsg(state.msgRes)
                    is FavoriteState.DeleteWineSuccess -> showMsg(state.msgRes)
                    is FavoriteState.Fail -> showMsg(state.msgRes)
                }
            }

        }
    }

    private fun showMsg(msgRes: Int) {
        Snackbar.make(binding.root, msgRes, Snackbar.LENGTH_SHORT).show()
    }

    private fun showProgress(isVisible: Boolean) {
        binding.srlResults.isRefreshing = isVisible
    }

    private fun showNoDataView(isVisible: Boolean) {
        binding.tvNoData.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun showRecyclerView(isVisible: Boolean) {
        binding.recyclerView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        getWines()
    }

    /*
    * OnClickListener
    * */
    override fun onFavorite(wine: Wine) {
        wine.isFavorite = !wine.isFavorite
        lifecycleScope.launch(Dispatchers.IO) {
            if (wine.isFavorite){
                vm.channel.send(FavoriteIntent.AddWine(wine))
            } else {
                vm.channel.send(FavoriteIntent.DeleteWine(wine))
            }
        }
    }

    override fun onLongClick(wine: Wine) {
        val fragmentManager = childFragmentManager
        val fragment = UpdateDialogFragment()
        val args = Bundle()
        args.putDouble(Constants.ARG_ID, wine.id)
        fragment.arguments = args
        fragment.show(fragmentManager, UpdateDialogFragment::class.java.simpleName)
        fragment.setOnUpdateListener {
            binding.srlResults.isRefreshing = true
            getWines()
        }
    }
}