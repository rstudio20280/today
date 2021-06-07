package com.study.today.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.SystemClock
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**권한이 있어야만 수행 가능한 작업을 이 클래스를 통해 편하게 실행시킬 수 있다.
 * @param componentActivity Fragment에서 사용할 경우 requireActivity()를 쓰고 Activity에서 사용시 this 사용
 * @param permissions 해당 작업을 수행하는데 필요한 권한 목록*/
class RunWithPermission {
    constructor(activity: AppCompatActivity, vararg permissions: String) {
        this.permissions = LinkedHashSet(permissions.toList())
        componentActivity = activity
        context = activity

        permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            callback
        )

        intentLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (isGranted()) actionWhenGranted?.invoke()
                else actionWhenDenied?.invoke(this)
            }
    }

    constructor(fragment: Fragment, vararg permissions: String) {
        this.permissions = LinkedHashSet(permissions.toList())
        componentActivity = fragment.requireActivity()
        context = fragment.requireContext()

        permissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            callback
        )

        intentLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (isGranted()) actionWhenGranted?.invoke()
                else actionWhenDenied?.invoke(this)
            }
    }

    private var intentLauncher: ActivityResultLauncher<Intent>
    private var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var permissions: LinkedHashSet<String>
    private var context: Context
    private var componentActivity: ComponentActivity

    private var lastRequestTime = 0L
    private val timeLimit = 500L    //권한 요청 후 거절하기까지 시간이 timeLimit 보다 작으면 팝업창이 뜨지 않은것으로 간주

    private val callback = { granted: Map<String, Boolean> ->
        if (granted.values.contains(false)) {
            val diff = SystemClock.uptimeMillis() - lastRequestTime
//            Timber.i("거절까지 걸린 시간: $diff")
            if (diff < timeLimit) {
                if (requestCount == 1) actionWhenDenied?.invoke(this)   //첫 로딩에서 팝업이 뜨지 않을 경우 actionWhenDenied 수행
                else actionInsteadPopup?.invoke(this)
            } else actionWhenDenied?.invoke(this)
        } else {
            actionWhenGranted?.invoke()
        }
        Unit
    }

    private var actionWhenGranted: (() -> Unit)? = null
    private var actionWhenDenied: ((RunWithPermission) -> Unit)? = null

    //다시 보지않기 체크 혹은 거절을 두번 당했을 때 (Android 11에서는 두번 거절당하면 더이상 팝업창이 안뜬다. 이때 처리할 작업)
    private var actionInsteadPopup: ((RunWithPermission) -> Unit)? = null


    /**권한이 승인되었을 때 작업*/
    fun setActionWhenGranted(action: (() -> Unit)): RunWithPermission {
        actionWhenGranted = action
        return this
    }

    /**권한 요청 팝업이 떴지만 승인하지 않았거나 옵션 변경 화면에서 권한을 승인하지 않았을 때 작업*/
    fun setActionWhenDenied(action: ((RunWithPermission) -> Unit)): RunWithPermission {
        actionWhenDenied = action
        return this
    }

    /**다시보지않기를 체크했거나 안드로이드 11버전 이상에서 2회 이상 거부를 눌렀을 경우 권한을 요청해도 팝업창이 안뜨는데 이때 취할 작업*/
    fun setActionInsteadPopup(action: ((RunWithPermission) -> Unit)): RunWithPermission {
        actionInsteadPopup = action
        return this
    }

    fun run() {
        if (isGranted()) {
            actionWhenGranted?.invoke()
        } else {

            var isUserClickDenied = false
            for (p in permissions) {
                if (componentActivity.shouldShowRequestPermissionRationale(p)) {
                    isUserClickDenied = true
                    break
                }
            }
            if (isUserClickDenied) {
                actionWhenDenied?.invoke(this)
            } else if (lastRequestTime == 0L)
                requestPermission()
        }
    }

    fun isGranted(): Boolean {
        for (p in permissions) {
            if (ContextCompat.checkSelfPermission(context, p)
                != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private var requestCount = 0
    fun requestPermission() {
        requestCount++
        lastRequestTime = SystemClock.uptimeMillis()
        permissionLauncher.launch(permissions.toTypedArray())
    }


    /**팝업창이 안뜰때 대신 사용하면 좋은 기능 (해당 앱의 환결설정 페이지로 넘어간다.)*/
    fun startPermissionIntent() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:" + context.packageName)
        )
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intentLauncher.launch(intent)
    }
}