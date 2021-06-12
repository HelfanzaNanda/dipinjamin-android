package com.alfardev.dipinjamin.ui.update_password

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_update_password.*
import kotlinx.android.synthetic.main.content_update_password.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdatePasswordActivity : AppCompatActivity() {

    private val updatePasswordViewModel : UpdatePasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observe()
        update()
    }

    private fun observe() {
        observeState()
    }

    private fun observeState() = updatePasswordViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleUiState(profileState: UpdatePasswordState?) {
        profileState?.let {
            when(it){
                is UpdatePasswordState.Loading -> handleLoading(it.state)
                is UpdatePasswordState.ShowToast -> showToast(it.message)
                is UpdatePasswordState.Success -> handleSuccess()
                is UpdatePasswordState.Validate -> handleValidate(it)
                is UpdatePasswordState.Reset -> handleReset()
            }
        }
    }

    private fun update(){
        btn_save.setOnClickListener {
            val pass = input_password.text.toString().trim()
            val confirmPass = input_confirm_password.text.toString().trim()
            if (updatePasswordViewModel.validate(pass, confirmPass)){
                updatePasswordViewModel.updatePassword(Constants.getToken(this@UpdatePasswordActivity), pass)
            }
        }
    }

    private fun handleReset() {
        setErrorPassword(null)
        setErrorConfirmPassword(null)
    }

    private fun handleValidate(validate: UpdatePasswordState.Validate) {
        validate.password?.let { setErrorPassword(it) }
        validate.confirm_password?.let { setErrorConfirmPassword(it) }
    }

    private fun handleSuccess() {
        finish()
    }

    private fun handleLoading(state: Boolean) {
        btn_save.isEnabled = !state
        if (state) loading.visible() else loading.gone()
    }

    private fun setErrorPassword(err : String?) { error_password.error = err }
    private fun setErrorConfirmPassword(err : String?) { error_confirm_password.error = err }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}