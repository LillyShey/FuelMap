package com.hpk.fuelmap.common.ui.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.hpk.fuelmap.common.arch.SingleLiveEvent
import com.hpk.fuelmap.common.extensions.hide
import com.hpk.fuelmap.common.extensions.show
import com.hpk.fuelmap.common.extensions.showConnectionErrorSnackBar
import com.hpk.fuelmap.common.ui.error.ErrorItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.toast.UnreliableToastApi
import splitties.toast.toast

@OptIn(UnreliableToastApi::class)
abstract class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {

    var loadingContainer: View? = null
    var errorContainer: ErrorItem? = null

    val navController: NavController?
        get() {
            return (activity as? BaseActivity)?.navController
        }

    fun observeConnectionError(
        connectionError: SingleLiveEvent<Boolean>,
        mainContainer: View,
        onRetry: () -> Unit
    ) {
        connectionError.observe(viewLifecycleOwner) {
            if (it == true) {
                showConnectionErrorSnackBar(mainContainer) { onRetry.invoke() }
            }
        }
    }

    protected fun observeLoading(loading: MutableLiveData<Boolean>) {
        loading.observe(this) {
            if (it == true) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    protected fun observeErrorMessage(
        errorContainer: ErrorItem? = null,
        errorMessage: SingleLiveEvent<String?>
    ) {
        this.errorContainer = errorContainer
        errorMessage.observe(this) {
            it?.let { message ->
                if (this.errorContainer != null) {
                    lifecycleScope.launch {
                        this@BaseFragment.errorContainer?.show()
                        this@BaseFragment.errorContainer?.errorMessage = message
                        delay(1500)
                        this@BaseFragment.errorContainer?.hide()
                    }
                } else {
                    toast(message)
                }
            }
        }
    }

    private fun showLoading() {
        loadingContainer?.show()
    }

    private fun hideLoading() {
        loadingContainer?.hide()
    }
}
