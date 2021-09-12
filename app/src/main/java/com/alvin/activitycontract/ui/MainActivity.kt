package com.alvin.activitycontract.ui

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.alvin.activitycontract.databinding.ActivityMainBinding
import com.alvin.activitycontract.extensions.registerListeners
import com.alvin.activitycontract.utils.ActivityContractObserver.Companion.CAMERA_PERMISSION
import com.alvin.activitycontract.utils.ActivityContractObserver.Companion.READ_CONTACTS_PERMISSION
import kotlinx.coroutines.flow.collect


class MainActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            this@MainActivity.registerListeners(btnLaunchActivityResult,
                btnRequestMultiplePermission,
                btnGetPhotos,
                btnCapturePhoto,
                btnGetVideos,
                btnRequestPermission)
        }
    }

    override fun onClick(v: View) {
        binding.apply {
            when (v) {
                btnGetPhotos -> {
                    lifecycleScope.launchWhenResumed {
                        observer.apply {
                            pickImage()
                            pickImageResult.collect {
                                it?.let {
                                    showMessage(it.toString())
                                    setEmpty()
                                }
                            }
                        }
                    }
                }

                btnGetVideos -> {
                    lifecycleScope.launchWhenResumed {
                        observer.apply {
                            pickVideo()
                            pickVideoResult.collect {
                                it?.let {
                                    showMessage(it.toString())
                                    setEmpty()
                                }
                            }
                        }
                    }
                }

                btnCapturePhoto -> {
                    lifecycleScope.launchWhenResumed {
                        observer.apply {
                            requestSinglePermission(CAMERA_PERMISSION)
                            requestSinglePermissionResult.collect { requestSinglePermissionResult ->
                                requestSinglePermissionResult?.let {
                                    if (it) {
                                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        takePicture(cameraIntent)
                                        takePictureResult
                                            .collect { takePictureResult ->
                                                takePictureResult?.let {
                                                    showMessage("$takePictureResult")
                                                    setEmpty()
                                                }
                                            }
                                    } else
                                        showMessage("Need Camera Permission")
                                    setEmpty()
                                }
                            }
                        }
                    }
                }

                btnRequestPermission -> {
                    lifecycleScope.launchWhenResumed {
                        observer.apply {
                            requestSinglePermission(CAMERA_PERMISSION)
                            requestSinglePermissionResult.collect {
                                it?.let {
                                    showMessage(it.toString())
                                    setEmpty()
                                }
                            }
                        }
                    }
                }
                btnRequestMultiplePermission -> {
                    lifecycleScope.launchWhenResumed {
                        observer.apply {
                            requestMultiplePermission(CAMERA_PERMISSION,
                                READ_CONTACTS_PERMISSION)
                            requestMultiplePermissionResult.collect {
                                it?.let {
                                    showMessage(it.toString())
                                    setEmpty()
                                }
                            }
                        }
                    }
                }

                btnLaunchActivityResult -> {
                    lifecycleScope.launchWhenResumed {
                        observer.apply {
                            startForResultForActivity<SecondActivity>(this@MainActivity)
                            startForResultResult.collect {
                                it?.let {
                                    showMessage(it.toString())
                                    setEmpty()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
