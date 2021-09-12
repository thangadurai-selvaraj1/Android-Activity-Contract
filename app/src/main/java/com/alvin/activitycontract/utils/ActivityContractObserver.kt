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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

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
    private var takePictureResult = MutableStateFlow<ActivityResult?>(null)
    private var pickVideoResult = MutableStateFlow<Uri?>(null)

    override fun onCreate(owner: LifecycleOwner) {

        pickImageContract = registry.register(GET_IMAGES_CONTRACT_KEY,
            owner,
            ActivityResultContracts.GetContent()) {
            pickImageResult.value = it
            CoroutineScope(Dispatchers.IO).launch {
                delay(20L)
                pickImageResult = MutableStateFlow(null)
            }
        }

        pickVideoContract = registry.register(GET_VIDEO_CONTRACT_KEY,
            owner,
            ActivityResultContracts.GetContent()) {
            pickVideoResult.value = it
            CoroutineScope(Dispatchers.IO).launch {
                delay(20L)
                pickVideoResult = MutableStateFlow(null)
            }


        }

        takePictureContract = registry.register(TAKE_PICTURE_CONTRACT_KEY,
            owner,
            ActivityResultContracts.StartActivityForResult()) {
            takePictureResult.value = it
            CoroutineScope(Dispatchers.IO).launch {
                delay(20L)
                takePictureResult = MutableStateFlow(null)
            }
        }

        startForResultContract = registry.register(START_FOR_RESULT_CONTRACT_KEY,
            owner,
            ActivityResultContracts.StartActivityForResult()) {
            startForResultResult.value = it
            CoroutineScope(Dispatchers.IO).launch {
                delay(20L)
                startForResultResult = MutableStateFlow(null)
            }
        }

        requestSinglePermissionContract =
            registry.register(REQUEST_SINGLE_PERMISSION_CONTRACT_KEY,
                owner,
                ActivityResultContracts.RequestPermission()) {
                requestSinglePermissionResult.value = it
                CoroutineScope(Dispatchers.IO).launch {
                    delay(20L)
                    requestSinglePermissionResult = MutableStateFlow(null)
                }
            }

        requestMultiplePermissionContract =
            registry.register(REQUEST_MULTIPLE_PERMISSION_CONTRACT_KEY,
                owner,
                ActivityResultContracts.RequestMultiplePermissions()) {
                requestMultiplePermissionResult.value = it
                CoroutineScope(Dispatchers.IO).launch {
                    delay(20L)
                    requestMultiplePermissionResult = MutableStateFlow(null)
                }
            }
    }

    fun pickImage(): MutableStateFlow<Uri?> {
        pickImageContract.launch("image/*")
        return pickImageResult
    }

    fun pickVideo(): MutableStateFlow<Uri?> {
        pickVideoContract.launch("video/*")
        return pickVideoResult
    }

    fun takePicture(intent: Intent): MutableStateFlow<ActivityResult?> {
        takePictureContract.launch(intent)
        return takePictureResult
    }

    fun requestSinglePermission(permission: String): MutableStateFlow<Boolean?> {
        requestSinglePermissionContract.launch(permission)
        return requestSinglePermissionResult
    }

    fun requestMultiplePermission(vararg permission: String): MutableStateFlow<Map<String, Boolean>?> {
        requestMultiplePermissionContract.launch(arrayOf(*permission))
        return requestMultiplePermissionResult
    }

    inline fun <reified T : Any> startForResultForActivity(context: Context): MutableStateFlow<ActivityResult?> {
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

    /**
     *
     * */
    fun setEmpty() {
        requestSinglePermissionResult.value = null
        requestMultiplePermissionResult.value = null
        startForResultResult.value = null
        pickImageResult.value = null
        takePictureResult.value = null
        pickVideoResult.value = null
    }

}

