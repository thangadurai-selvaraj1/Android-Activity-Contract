package com.alvin.activitycontract.utils

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.alvin.activitycontract.ui.BaseActivity


/*

object Utils:DefaultLifecycleObserver {
    late init var getContent : ActivityResultLauncher<String>

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        owner.lifecycle.currentState{

        }
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            result = it
        }.launch(permission)
    }
}
fun AppCompatActivity.requestSinglePermission(permission: String): Boolean {
    var result = false
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        result = it
    }.launch(permission)
    return result
}
*/

/*inline fun <reified T : Any> BaseActivity.s(context: Context): ActivityResult? {
    var result: ActivityResult? = null
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result = it
    }.launch(Intent(context, T::class.java))
    return result
}*/


/**
 * Custom Contract
 * */
class ActivityContract<T : Any>(var mClass: T, var data: String) :
    ActivityResultContract<String, Intent?>() {

    override fun createIntent(context: Context, input: String): Intent {
        return Intent(context, mClass::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Intent? {
        return when (resultCode) {
            RESULT_OK -> intent
            else -> null
        }
    }
}

