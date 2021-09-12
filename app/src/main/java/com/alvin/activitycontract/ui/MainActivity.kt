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
                        observer.pickImage().collect {
                            it?.let {
                                showMessage(it.toString())
                            }
                        }
                    }
                }

                btnGetVideos -> {
                    lifecycleScope.launchWhenResumed {
                        observer.pickVideo().collect {
                            it?.let {
                                showMessage(it.toString())
                            }
                        }

                    }
                }

                btnCapturePhoto -> {
                    lifecycleScope.launchWhenResumed {
                        observer.apply {
                            requestSinglePermission(CAMERA_PERMISSION).collect { requestSinglePermissionResult ->
                                requestSinglePermissionResult?.let {
                                    if (it) {
                                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        takePicture(cameraIntent)
                                            .collect { takePictureResult ->
                                                takePictureResult?.let {
                                                    showMessage("$takePictureResult")
                                                }
                                            }
                                    } else
                                        showMessage("Need Camera Permission")
                                }
                            }
                        }
                    }
                }

                btnRequestPermission -> {
                    lifecycleScope.launchWhenResumed {
                        observer.requestSinglePermission(CAMERA_PERMISSION)
                            .collect {
                                it?.let {
                                    showMessage(it.toString())
                                }
                            }

                    }
                }
                btnRequestMultiplePermission -> {
                    lifecycleScope.launchWhenResumed {
                        observer.requestMultiplePermission(CAMERA_PERMISSION,
                            READ_CONTACTS_PERMISSION)
                            .collect {
                                it?.let {
                                    showMessage(it.toString())
                                }
                            }

                    }
                }

                btnLaunchActivityResult -> {
                    lifecycleScope.launchWhenResumed {
                        observer
                            .startForResultForActivity<SecondActivity>(this@MainActivity)
                            .collect {
                                it?.let {
                                    showMessage(it.toString())
                                }
                            }
                    }
                }
            }
        }
    }
}
