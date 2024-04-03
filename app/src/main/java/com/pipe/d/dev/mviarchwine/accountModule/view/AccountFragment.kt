package com.pipe.d.dev.mviarchwine.accountModule.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.pipe.d.dev.mviarchwine.commonModule.utils.Constants
import com.pipe.d.dev.mviarchwine.commonModule.dataAccess.local.FakeFirebaseAuth
import com.pipe.d.dev.mviarchwine.mainModule.MainActivity
import com.pipe.d.dev.mviarchwine.R
import com.pipe.d.dev.mviarchwine.accountModule.AccountViewModel
import com.pipe.d.dev.mviarchwine.accountModule.AccountViewModelFactory
import com.pipe.d.dev.mviarchwine.accountModule.intent.AccountIntent
import com.pipe.d.dev.mviarchwine.accountModule.model.AccountRepository
import com.pipe.d.dev.mviarchwine.accountModule.model.AccountState
import com.pipe.d.dev.mviarchwine.commonModule.entities.FirebaseUser
import com.pipe.d.dev.mviarchwine.databinding.FragmentAccountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var vm: AccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupUserUI()
        setupViewModel()
        setupObservers()
        setupButtons()
    }

    private fun setupViewModel() {
        vm = ViewModelProvider(this,
            AccountViewModelFactory(
                AccountRepository(FakeFirebaseAuth())))[AccountViewModel::class.java]
    }

    private fun setupUserUI(user: FirebaseUser) {
        //val auth = FakeFirebaseAuth()
        Log.i("AccountFragment", "Entró")
        with(binding) {
            tvName.text = user.displayName
            tvEmail.text = user.email
            tvPhone.text = user.phone

            Glide.with(requireContext())
                .load(user.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imgProfile)
        }
        setupIntents()
    }

    private fun setupIntents() {
        binding.tvEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse(Constants.DATA_MAIL_TO)
                putExtra(Intent.EXTRA_EMAIL, arrayOf(binding.tvEmail.text.toString()))
                putExtra(Intent.EXTRA_SUBJECT, "From kotlin architectures course")
            }
            launchIntent(intent)
        }

        binding.tvPhone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                val phone = (it as TextView).text
                data = Uri.parse("${Constants.DATA_TEL}$phone")
            }
            launchIntent(intent)
        }
    }

    private fun launchIntent(intent: Intent){
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireActivity(), getString(R.string.account_error_no_resolve), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.btnSignOut.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                vm.channel.send(AccountIntent.SignOut)

            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            //val state = AccountState

            vm.state.collect { state ->
                when(state) {
                    is AccountState.Init -> {}
                    is AccountState.ShowProgress -> showProgress(true)
                    is AccountState.HideProgress -> showProgress(false)
                    is AccountState.SignOutSuccess -> {
                        (requireActivity() as MainActivity).apply {
                            setupNavView(false)
                            launchLoginUI()
                        }
                        showProgress(false)
                    }
                    is AccountState.RequestUserSuccess -> setupUserUI(state.user)
                    is AccountState.Fail -> showMsg(state.msgRes)
                }
            }

        }
    }

    private fun showMsg(msgRes: Int) {
        Snackbar.make(binding.root, msgRes, Snackbar.LENGTH_SHORT).show()
    }

    private fun showProgress(isVisible: Boolean) {
        binding.contentProgress.root.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.IO) {
            vm.channel.send(AccountIntent.RequestUser)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}