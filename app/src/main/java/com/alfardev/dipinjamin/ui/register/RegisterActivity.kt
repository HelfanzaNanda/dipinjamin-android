package com.alfardev.dipinjamin.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.ui.fragments.MainActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.alertInfo
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.content_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var mSignInButton : View
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN: Int = 0
    private val registerViewModel : RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(findViewById(R.id.toolbar))
        mSignInButton = findViewById(R.id.btn_login_google);
        observe()
        register()
        setUpGoogleLogin()
    }

    private fun setUpGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mSignInButton.setOnClickListener {
            loginGoogle()
        }
    }

    private fun loginGoogle() {
        val intent : Intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun register() {
        btn_register.setOnClickListener {
            val name = input_name.text.toString().trim()
            val email = input_email.text.toString().trim()
            val password = input_password.text.toString().trim()
            val confirmPass = input_confirm_password.text.toString().trim()
            val phone = input_telp.text.toString().trim()
            if (registerViewModel.validate(name, email, password, confirmPass, phone)){
                val user = User(name = name, email = email, password = password, phone = phone)
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
                is RegisterState.SuccessLoginGoogle -> handleSuccessLoginGoogle(it.token)
            }
        }
    }

    private fun handleSuccessLoginGoogle(token: String) {
        Constants.setToken(this@RegisterActivity, "Bearer $token")
        startActivity(Intent(this@RegisterActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }).also { finish() }
    }

    private fun handleReset() {
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
        setConfirmPasswordError(null)
        setPhoneError(null)
    }

    private fun handleValidate(validate: RegisterState.Validate) {
        validate.name?.let { setNameError(it) }
        validate.email?.let { setEmailError(it) }
        validate.password?.let { setPasswordError(it) }
        validate.confirmPass?.let { setConfirmPasswordError(it) }
        validate.phone?.let { setPhoneError(it) }
    }

    private fun handleSuccess(email: String) {
        alertInfo(getString(R.string.success_register))
    }

    private fun handleLoading(state: Boolean) {
        btn_register.isEnabled = !state
        btn_login_google.isEnabled = !state
    }

    private fun setNameError(err : String?){ error_name.error = err }
    private fun setEmailError(err : String?){ error_email.error = err }
    private fun setPasswordError(err : String?){ error_password.error = err }
    private fun setConfirmPasswordError(err : String?){ error_confirm_password.error = err }
    private fun setPhoneError(err : String?){ error_telp.error = err }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)!!
            val user = User(
                    name = account.displayName,
                    email = account.email,
                    provider_id = account.id,
                    provider_name = getString(R.string.google_provider)
            )
            registerViewModel.loginProvider(user)
        } catch (e: ApiException) {
            println("login google err: ${e}")
            Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
        }
    }
}