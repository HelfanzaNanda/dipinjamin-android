package com.alfardev.dipinjamin.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.ui.fragments.MainActivity
import com.alfardev.dipinjamin.ui.register.RegisterActivity
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
import kotlinx.android.synthetic.main.content_login.*
import kotlinx.android.synthetic.main.content_login.btn_login_google
import kotlinx.android.synthetic.main.content_login.error_email
import kotlinx.android.synthetic.main.content_login.error_password
import kotlinx.android.synthetic.main.content_login.input_email
import kotlinx.android.synthetic.main.content_login.input_password
import kotlinx.android.synthetic.main.content_login.loading
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    private lateinit var mSignInButton : View
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN: Int = 0
    private val loginViewModel : LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))
        mSignInButton = findViewById(R.id.btn_login_google);
        observe()
        setUpGoogleLogin()
        login()
        goToRegisterActivity()
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
        val intent :Intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun goToRegisterActivity() {
        btn_register_now.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
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
            loginViewModel.loginProvider(user)
        } catch (e: ApiException) {
            println("login google err: ${e}")
            Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleUiState(loginState: LoginState?) {
        loginState?.let {
            when(it){
                is LoginState.Loading -> handleLoading(it.state)
                is LoginState.ShowToast -> showToast(it.message)
                is LoginState.ShowAlert -> alertInfo(it.message)
                is LoginState.Success -> handleSuccess(it.token, it.name)
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
        validate.password?.let { setErrorPassword(it) }
    }

    private fun handleSuccess(token: String, name : String) {
        Constants.setToken(this@LoginActivity, "Bearer $token")
        if (getExpectResult()){
            finish()
        }else{
            startActivity(Intent(this@LoginActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("IS_LOGGED_IN", true)
                putExtra("USER_NAME", name)
            }).also { finish() }
        }
    }

    private fun handleLoading(state: Boolean) {
        btn_register_now.isEnabled = !state
        btn_forgot_password.isEnabled = !state
        //mSignInButton.isEnabled = !state
        btn_login.isEnabled = !state
        if (state) loading.visible() else loading.gone()
    }

    private fun getExpectResult() = intent.getBooleanExtra("EXPECT_RESULT", false)
    private fun setErrorEmail(err : String?) { error_email.error = err }
    private fun setErrorPassword(err : String?) { error_password.error = err }

    override fun onResume() {
        super.onResume()
        if (!Constants.getToken(this@LoginActivity).equals(getString(R.string.undefined))){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }
}