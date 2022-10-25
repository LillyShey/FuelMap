package com.hpk.fuelmap.common.ui.base

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
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
import splitties.toast.toast

abstract class BaseActivity(@LayoutRes layoutId: Int) : AppCompatActivity(layoutId) {
    private val authReceiver = AuthReceiver()

    var loadingContainer: View? = null
    var errorContainer: ErrorItem? = null
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authReceiver.init(this)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        try {
            this.unregisterReceiver(authReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
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
                        this@BaseActivity.errorContainer?.show()
                        this@BaseActivity.errorContainer?.errorMessage = message
                        delay(2500)
                        this@BaseActivity.errorContainer?.hide()
                    }
                } else {
                    toast(message)
                }
            }
        }
    }

    protected fun observeConnectionError(
        connectionError: SingleLiveEvent<Boolean>,
        mainContainer: View,
        onRetry: () -> Unit
    ) {
        connectionError.observe(this) {
            if (it == true) {
                showConnectionErrorSnackBar(mainContainer) { onRetry.invoke() }
            }
        }
    }

    private fun showLoading() {
        loadingContainer?.show()
    }

    private fun hideLoading() {
        loadingContainer?.hide()
    }

    class AuthReceiver : BroadcastReceiver() {

        private var activity: Activity? = null

        override fun onReceive(context: Context, intent: Intent) {
            //TODo Open auth screen
            /*activity?.let {
                val homeIntent = SplashFragment.getIntent(it)
                homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                it.startActivity(homeIntent)
                it.finish()
            }*/
        }

        fun init(activity: Activity) {
            this.activity = activity
        }
    }

}
