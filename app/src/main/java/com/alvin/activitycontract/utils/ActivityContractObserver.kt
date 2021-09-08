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

    private lateinit var pickImageContract: ActivityResultLauncher<String>
    private lateinit var requestSinglePermissionContract: ActivityResultLauncher<String>
    private lateinit var requestMultiplePermissionContract: ActivityResultLauncher<Array<String>?>
    lateinit var startForResultContract: ActivityResultLauncher<Intent>
    lateinit var takePictureContract: ActivityResultLauncher<Intent>
    private lateinit var pickVideoContract: ActivityResultLauncher<String>

    private var requestSinglePermissionResult = MutableStateFlow<Boolean?>(null)
    private var requestMultiplePermissionResult = MutableStateFlow<Map<String, Boolean>?>(null)
    var startForResultResult = MutableStateFlow<ActivityResult?>(null)
    private var pickImageResult = MutableStateFlow<Uri?>(null)
    var takePictureResult = MutableStateFlow<ActivityResult?>(null)
    private var pickVideoResult = MutableStateFlow<Uri?>(null)

    override fun onCreate(owner: LifecycleOwner) {

        pickImageContract = registry.register(GET_IMAGES_CONTRACT_KEY,
            owner,
            ActivityResultContracts.GetContent()) {
            pickImageResult.value = it
        }

        pickVideoContract = registry.register(GET_VIDEO_CONTRACT_KEY,
            owner,
            ActivityResultContracts.GetContent()) {
            pickVideoResult.value = it
        }

        takePictureContract = registry.register(TAKE_PICTURE_CONTRACT_KEY,
            owner,
            ActivityResultContracts.StartActivityForResult()) {
            takePictureResult.value = it
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

    fun pickImage(): MutableStateFlow<Uri?> {
        pickImageResult.value = null
        pickImageContract.launch("image/*")
        return pickImageResult
    }

    fun pickVideo(): MutableStateFlow<Uri?> {
        pickVideoResult.value = null
        pickVideoContract.launch("video/*")
        return pickVideoResult
    }

    fun takePicture(intent: Intent): MutableStateFlow<ActivityResult?> {
        takePictureResult.value = null
        takePictureContract.launch(intent)
        return takePictureResult
    }

    fun requestSinglePermission(permission: String): MutableStateFlow<Boolean?> {
        requestSinglePermissionResult.value = null
        requestSinglePermissionContract.launch(permission)
        return requestSinglePermissionResult
    }

    fun requestMultiplePermission(vararg permission: String): MutableStateFlow<Map<String, Boolean>?> {
        requestMultiplePermissionResult.value = null
        requestMultiplePermissionContract.launch(arrayOf(*permission))
        return requestMultiplePermissionResult
    }

    inline fun <reified T : Any> startForResultForActivity(context: Context): MutableStateFlow<ActivityResult?> {
        startForResultResult.value = null
        startForResultContract.launch(Intent(context, T::class.java))
        return startForResultResult
    }


    companion object {
        const val START_FOR_RESULT_CONTRACT_KEY = "START_FOR_RESULT_CONTRACT_KEY"
        const val TAKE_PICTURE_CONTRACT_KEY = "TAKE_PICTURE_CONTRACT_KEY"
        const val REQUEST_SINGLE_PERMISSION_CONTRACT_KEY = "REQUEST_SINGLE_PERMISSION_CONTRACT_KEY"
        const val REQUEST_MULTIPLE_PERMISSION_CONTRACT_KEY =
            "REQUEST_MULTIPLE_PERMISSION_CONTRACT_KEY"
        const val GET_IMAGES_CONTRACT_KEY = "GET_IMAGES_CONTRACT_KEY"
        const val GET_VIDEO_CONTRACT_KEY = "GET_VIDEO_CONTRACT_KEY"


        const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        const val READ_EXTERNAL_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
        const val WRITE_EXTERNAL_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val READ_CONTACTS_PERMISSION = Manifest.permission.READ_CONTACTS

    }
}

