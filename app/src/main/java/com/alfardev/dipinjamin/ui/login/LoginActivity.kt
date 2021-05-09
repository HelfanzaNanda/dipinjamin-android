package com.alfardev.dipinjamin.ui.login

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.ui.fragments.MainActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.alertInfo
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.content_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val loginViewModel : LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))
        observe()
        login()
    }

    private fun login(){
        btn_login.setOnClickListener {
            val email = input_email.text.toString().trim()
            val password = input_password.text.toString().trim()
            if (loginViewModel.validate(email, password)){
                loginViewModel.login(email, password)
            }
        }
    }

    private fun observe() = loginViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleUiState(loginState: LoginState?) {
        loginState?.let {
            when(it){
                is LoginState.Loading -> handleLoading(it.state)
                is LoginState.ShowToast -> showToast(it.message)
                is LoginState.ShowAlert -> alertInfo(it.message)
                is LoginState.Success -> handleSuccess(it.token)
                is LoginState.Validate -> handleValidate(it)
                is LoginState.Reset -> handleReset()
            }
        }
    }

    private fun handleReset() {
        setErrorEmail(null)
        setErrorPassword(null)
    }

    private fun handleValidate(validate: LoginState.Validate) {
        validate.email?.let { setErrorEmail(it) }
        validate.password?.let { setErrorEmail(it) }
    }

    private fun handleSuccess(token: String) {
        Constants.setToken(this@LoginActivity, "Bearer $token")
        if (getExpectResult()){
            finish()
        }else{
            startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }).also { finish() }
        }
    }

    private fun handleLoading(state: Boolean) {
        btn_login_facebook.isEnabled = !state
        btn_register_now.isEnabled = !state
        btn_forgot_password.isEnabled = !state
        btn_login_google.isEnabled = !state
        btn_login.isEnabled = !state
        if (state) loading.visible() else loading.gone()
    }

    private fun getExpectResult() = intent.getBooleanExtra("EXPECT_RESULT", false)
    private fun setErrorEmail(err : String?) { error_email.error = err }
    private fun setErrorPassword(err : String?) { error_password.error = err }

    override fun onResume() {
        super.onResume()
        if (!Constants.getToken(this@LoginActivity).equals("UNDEFINED")){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}