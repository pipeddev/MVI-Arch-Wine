package com.pipe.d.dev.mviarchwine.loginModule.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.pipe.d.dev.mviarchwine.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.pipe.d.dev.mviarchwine.commonModule.dataAccess.local.FakeFirebaseAuth
import com.pipe.d.dev.mviarchwine.mainModule.view.MainActivity
import com.pipe.d.dev.mviarchwine.loginModule.LoginViewModel
import com.pipe.d.dev.mviarchwine.loginModule.LoginViewModelFactory
import com.pipe.d.dev.mviarchwine.loginModule.intent.LoginIntent
import com.pipe.d.dev.mviarchwine.loginModule.model.LoginRepository
import com.pipe.d.dev.mviarchwine.loginModule.model.LoginState
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
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var vm: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupButtons()
        setupObservers()
    }

    private fun setupViewModel() {
        vm = ViewModelProvider(this,
            LoginViewModelFactory(
                LoginRepository(FakeFirebaseAuth())))[LoginViewModel::class.java]
    }

    private fun checkAuth() {
        lifecycleScope.launch {
            vm.channel.send(LoginIntent.CheckAuth)
            /*showProgress(true)
            delay(2_500)
            val auth = FakeFirebaseAuth()
            if (auth.isValidAuth()) {
                closeLoginUI()
            } else {
                showForm(true)
            }
            showProgress(false)
             */
        }
    }

    private fun setupButtons() {
        with(binding) {
            btnLogin.setOnClickListener {
                lifecycleScope.launch {
                    vm.channel.send(LoginIntent.SignIn(etUsername.text.toString(), etPin.text.toString()))
                    /*showProgress(true)
                    showForm(false)
                    val auth = FakeFirebaseAuth()
                    if (auth.login(etUsername.text.toString(), etPin.text.toString()))
                        closeLoginUI()
                    else {
                        showProgress(false)
                        showMsg(R.string.login_login_fail)
                        showForm(true)
                    }*/
                }
            }
        }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            vm.state.collect { state ->
                when(state) {
                    is LoginState.Init -> checkAuth()
                    is LoginState.ShowProgress -> {
                        showProgress(true)
                        showForm(false)
                    }
                    is LoginState.HideProgress -> showProgress(false)
                    /*is LoginState.AuthValid -> closeLoginUI()
                    is LoginState.LoginSuccess -> closeLoginUI()*/
                    is LoginState.AuthValid,
                    is LoginState.LoginSuccess -> closeLoginUI()
                    is LoginState.Fail -> {
                        showMsg(state.msgRes)
                        showForm(true)
                    }
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

    private fun showForm(isVisible: Boolean) {
        binding.contentLogin.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun closeLoginUI() {
        (requireActivity() as MainActivity).setupNavView(true)
        requireActivity().supportFragmentManager.beginTransaction().apply {
            remove(this@LoginFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}