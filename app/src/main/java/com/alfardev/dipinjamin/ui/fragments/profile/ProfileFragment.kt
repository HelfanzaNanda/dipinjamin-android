package com.alfardev.dipinjamin.ui.fragments.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.alfardev.dipinjamin.R
import com.alfardev.dipinjamin.models.User
import com.alfardev.dipinjamin.ui.login.LoginActivity
import com.alfardev.dipinjamin.ui.management_address.ManagementAddressActivity
import com.alfardev.dipinjamin.ui.my_orders.MyOrderActivity
import com.alfardev.dipinjamin.ui.update_password.UpdatePasswordActivity
import com.alfardev.dipinjamin.ui.update_profile.UpdateProfileActivity
import com.alfardev.dipinjamin.utils.Constants
import com.alfardev.dipinjamin.utils.extensions.gone
import com.alfardev.dipinjamin.utils.extensions.showToast
import com.alfardev.dipinjamin.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.unauthorized.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(){

    private val profileViewModel : ProfileViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (isLoggedIn()){
            return inflater.inflate(R.layout.fragment_profile, container, false)
        }
        return inflater.inflate(R.layout.unauthorized, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isLoggedIn()){
            observe()
            goToOtherActivity()
            logout()
        }else{
            requireView().btn_login.setOnClickListener {
                startActivity(Intent(requireActivity(), LoginActivity::class.java)
                        .putExtra("EXPECT_RESULT", false))
            }
        }
    }

    private fun logout() {
        requireView().btn_logout.setOnClickListener {
            AlertDialog.Builder(requireActivity()).apply {
                setMessage(getString(R.string.message_alert_logout))
                setPositiveButton("Ya"){dialog, _ ->
                    Constants.clearToken(requireActivity())
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                setNegativeButton("Tidak"){dialog, _ ->
                    dialog.dismiss()
                }
            }.show()
        }
    }

    private fun goToOtherActivity() {
        requireView().btn_update_password.setOnClickListener {
            startActivity(Intent(requireActivity(), UpdatePasswordActivity::class.java))
        }

        requireView().btn_update_profile.setOnClickListener {
            startActivity(Intent(requireActivity(), UpdateProfileActivity::class.java).apply {
                putExtra("CURRENT_USER", profileViewModel.listenToCurrentUser().value)
            })
        }
        requireView().btn_update_adresses.setOnClickListener {
            startActivity(Intent(requireActivity(), ManagementAddressActivity::class.java))
        }
        requireView().btn_book_borrow.setOnClickListener {
            startActivity(Intent(requireActivity(), MyOrderActivity::class.java)
                .putExtra("STATE", "BORROWER"))
        }
        requireView().btn_book_lend.setOnClickListener {
            startActivity(Intent(requireActivity(), MyOrderActivity::class.java)
                .putExtra("STATE", "OWNER"))
        }
    }

    private fun observe() {
        observeState()
        observeCurrentUser()
    }

    private fun observeState() = profileViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeCurrentUser() = profileViewModel.listenToCurrentUser().observe(viewLifecycleOwner, Observer { handleCurrentUser(it) })

    private fun handleCurrentUser(user: User?) {
        user?.let {
            requireView().profile_name.text = it.name
            requireView().profile_email.text = it.email
            requireView().profile_phone.text = if (it.phone.isNullOrEmpty()) "-" else it.phone
        }
    }

    private fun handleUiState(state: ProfileState?) {
        state?.let {
            when(it){
                is ProfileState.Loading -> handleLoading(it.state)
                is ProfileState.ShowToast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) = if (state) requireView().loading.visible() else requireView().loading.gone()

    private fun fetchCurrentUser() = profileViewModel.fetchCurrentUser(Constants.getToken(requireActivity()))
    private fun isLoggedIn() = !Constants.getToken(requireActivity()).equals(getString(R.string.undefined))

    override fun onResume() {
        super.onResume()
        if (isLoggedIn()) fetchCurrentUser()
    }
}