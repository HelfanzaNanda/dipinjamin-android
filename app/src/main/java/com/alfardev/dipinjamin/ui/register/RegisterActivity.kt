package com.alfardev.dipinjamin.ui.register

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.utils.extensions.alertInfo
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.content_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel : RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(findViewById(R.id.toolbar))
        observe()
        register()
    }

    private fun register() {
        btn_register.setOnClickListener {
            val name = input_name.text.toString().trim()
            val email = input_email.text.toString().trim()
            val password = input_password.text.toString().trim()
            val confirmPass = input_confirm_password.text.toString().trim()
            if (registerViewModel.validate(name, email, password, confirmPass)){
                val user = User(name = name, email = email, password = password)
                registerViewModel.register(user)
            }
        }
    }

    private fun observe(){
        observeState()
    }

    private fun observeState() = registerViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleUiState(registerState: RegisterState?) {
        registerState?.let {
            when(it){
                is RegisterState.Loading -> handleLoading(it.state)
                is RegisterState.ShowToast -> showToast(it.message)
                is RegisterState.ShowAlert -> alertInfo(it.message)
                is RegisterState.Success -> handleSuccess(it.email)
                is RegisterState.Validate -> handleValidate(it)
                is RegisterState.Reset -> handleReset()
            }
        }
    }

    private fun handleReset() {
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
        setConfirmPasswordError(null)
    }

    private fun handleValidate(validate: RegisterState.Validate) {
        validate.name?.let { setNameError(it) }
        validate.email?.let { setEmailError(it) }
        validate.password?.let { setPasswordError(it) }
        validate.confirmPass?.let { setConfirmPasswordError(it) }
    }

    private fun handleSuccess(email: String) {
        alertInfo(getString(R.string.success_register))
    }

    private fun handleLoading(state: Boolean) {
        btn_register.isEnabled = !state
        btn_login_facebook.isEnabled = !state
        btn_login_google.isEnabled = !state
    }

    private fun setNameError(err : String?){ error_name.error = err }
    private fun setEmailError(err : String?){ error_email.error = err }
    private fun setPasswordError(err : String?){ error_password.error = err }
    private fun setConfirmPasswordError(err : String?){ error_confirm_password.error = err }
}