package com.alfardev.dipinjamin.ui.update_profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_update_profile.*
import kotlinx.android.synthetic.main.content_update_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdateProfileActivity : AppCompatActivity() {

    private val updateProfileViewModel : UpdateProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        observe()
        update()
    }

    private fun observe() {
        observeState()
    }

    private fun observeState() = updateProfileViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleUiState(profileState: UpdateProfileState?) {
        profileState?.let {
            when(it){
                is UpdateProfileState.Loading -> handleLoading(it.state)
                is UpdateProfileState.ShowToast -> showToast(it.message)
                is UpdateProfileState.Success -> handleSuccess()
                is UpdateProfileState.Validate -> handleValidate(it)
                is UpdateProfileState.Reset -> handleReset()
            }
        }
    }

    private fun update(){
        btn_save.setOnClickListener {
            val firstName = input_first_name.text.toString().trim()
            val lastName = input_last_name.text.toString().trim()
            val email = input_email.text.toString().trim()
            val phone = input_phone.text.toString().trim()
            if (updateProfileViewModel.validate(firstName, lastName, email, phone)){
                updateProfileViewModel.updateProfile(
                        Constants.getToken(this@UpdateProfileActivity),
                firstName, lastName, email, phone)
            }
        }
    }

    private fun handleReset() {
        setErrorFirstName(null)
        setErrorLastName(null)
        setErrorEmail(null)
        setErrorPhone(null)
    }

    private fun handleValidate(validate: UpdateProfileState.Validate) {
        validate.firstName?.let { setErrorFirstName(it) }
        validate.lastName?.let { setErrorLastName(it) }
        validate.email?.let { setErrorEmail(it) }
        validate.phone?.let { setErrorPhone(it) }
    }

    private fun handleSuccess() {
        finish()
    }

    private fun handleLoading(state: Boolean) {
        btn_save.isEnabled = !state
        if (state) loading.visible() else loading.gone()
    }

    private fun setErrorFirstName(err : String?) { error_first_name.error = err }
    private fun setErrorLastName(err : String?) { error_last_name.error = err }
    private fun setErrorEmail(err : String?) { error_email.error = err }
    private fun setErrorPhone(err : String?) { error_phone.error = err }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}