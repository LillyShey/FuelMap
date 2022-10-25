package com.hpk.fuelmap.common.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.hpk.fuelmap.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import splitties.alertdialog.appcompat.alertDialog
import splitties.alertdialog.appcompat.message
import splitties.alertdialog.appcompat.positiveButton
import splitties.alertdialog.appcompat.title

typealias OnPermissionGrantedListener =
            (PermissionGrantedResponse) -> Unit

typealias OnPermissionDeniedListener =
            (PermissionDeniedResponse) -> Unit

typealias OnPermissionRationaleListener =
            (PermissionRequest, PermissionToken) -> Unit

inline fun Context.requestAppPermission(permission: Permission.() -> Unit) {
    val dexter = Dexter.withContext(this)
    Permission(dexter).apply(permission)
}

class Permission(
    private val dexter: DexterBuilder.Permission,
) {

    fun permission(vararg permission: String, listener: Listener.() -> Unit) {
        dexter.withPermissions(permission.asList())
            .withListener(PermissionListenerImpl(listener)).check()
    }
}

class Listener(
    var granted: OnPermissionGrantedListener? = null,
    var denied: OnPermissionDeniedListener? = null,
    var rationaleShouldBeShown: OnPermissionRationaleListener? = null,
) {

    fun granted(onGranted: OnPermissionGrantedListener) {
        granted = onGranted
    }

    fun denied(onDenied: OnPermissionDeniedListener) {
        denied = onDenied
    }

    fun rationaleShouldBeShown(onRationaleShouldBeShown: OnPermissionRationaleListener) {
        rationaleShouldBeShown = onRationaleShouldBeShown
    }
}

private class PermissionListenerImpl(
    listener: Listener.() -> Unit,
) : MultiplePermissionsListener {

    private val _listener = Listener().apply(listener)

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        report?.let {
            if (!it.areAllPermissionsGranted()) {
                _listener.denied?.invoke(it.deniedPermissionResponses.first())
            } else {
                _listener.granted?.invoke(it.grantedPermissionResponses.first())
            }
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        permissions: List<PermissionRequest>,
        token: PermissionToken,
    ) {
        if (_listener.rationaleShouldBeShown == null) {
            token.continuePermissionRequest()
        } else {
            _listener.rationaleShouldBeShown?.invoke(permissions.first(), token)
        }
    }
}

fun Context.showPermissionRequiredDialog(message: String) {
    alertDialog {
        title = getString(R.string.permission_required_title)
        this.message = message
        positiveButton(R.string.permission_required_ok) {
            startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
            )
        }
        show()
    }
}
