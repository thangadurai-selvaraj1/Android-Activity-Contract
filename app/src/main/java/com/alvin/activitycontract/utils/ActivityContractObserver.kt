package com.alvin.activitycontract.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow

class ActivityContractObserver(private val registry: ActivityResultRegistry) :
    DefaultLifecycleObserver {

    private lateinit var getContentContract: ActivityResultLauncher<String>
    private lateinit var requestSinglePermissionContract: ActivityResultLauncher<String>
    private lateinit var requestMultiplePermissionContract: ActivityResultLauncher<Array<String>?>
    lateinit var startForResultContract: ActivityResultLauncher<Intent>


    private var requestSinglePermissionResult = MutableStateFlow<Boolean?>(null)
    private var requestMultiplePermissionResult = MutableStateFlow<Map<String, Boolean>?>(null)
    var startForResultResult = MutableStateFlow<ActivityResult?>(null)
    private var getContentResult = MutableStateFlow<Uri?>(null)

    override fun onCreate(owner: LifecycleOwner) {

        getContentContract = registry.register(GET_IMAGES_CONTRACT_KEY,
            owner,
            ActivityResultContracts.GetContent()) {
            getContentResult.value = it
        }

        startForResultContract = registry.register(START_FOR_RESULT_CONTRACT_KEY,
            owner,
            ActivityResultContracts.StartActivityForResult()) {
            startForResultResult.value = it
        }

        requestSinglePermissionContract =
            registry.register(REQUEST_SINGLE_PERMISSION_CONTRACT_KEY,
                owner,
                ActivityResultContracts.RequestPermission()) {
                requestSinglePermissionResult.value = it
            }

        requestMultiplePermissionContract =
            registry.register(REQUEST_MULTIPLE_PERMISSION_CONTRACT_KEY,
                owner,
                ActivityResultContracts.RequestMultiplePermissions()) {
                requestMultiplePermissionResult.value = it
            }
    }

    fun selectImage(): MutableStateFlow<Uri?> {
        getContentContract.launch("image/*")
        return getContentResult
    }

    fun requestSinglePermission(permission: String): MutableStateFlow<Boolean?> {
        requestSinglePermissionContract.launch(permission)
        return requestSinglePermissionResult
    }

    fun requestMultiplePermission(vararg permission: String): MutableStateFlow<Map<String, Boolean>?> {
        requestMultiplePermissionContract.launch(arrayOf(*permission))
        return requestMultiplePermissionResult
    }

    inline fun <reified T : Any> startForResult(context: Context): MutableStateFlow<ActivityResult?> {
        startForResultContract.launch(Intent(context, T::class.java))
        return startForResultResult
    }

    companion object {
        const val START_FOR_RESULT_CONTRACT_KEY = "START_FOR_RESULT_CONTRACT_KEY"
        const val REQUEST_SINGLE_PERMISSION_CONTRACT_KEY = "REQUEST_SINGLE_PERMISSION_CONTRACT_KEY"
        const val REQUEST_MULTIPLE_PERMISSION_CONTRACT_KEY =
            "REQUEST_MULTIPLE_PERMISSION_CONTRACT_KEY"
        const val GET_IMAGES_CONTRACT_KEY = "GET_IMAGES_CONTRACT_KEY"


        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        const val READ_EXTERNAL_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        const val WRITE_EXTERNAL_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS

    }
}

