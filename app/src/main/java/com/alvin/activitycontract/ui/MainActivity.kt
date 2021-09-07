package com.alvin.activitycontract.ui

import android.os.Bundle
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
                btnRequestPermission)
        }
    }

    override fun onClick(v: View) {
        binding.apply {
            when (v) {
                btnGetPhotos -> {
                    lifecycleScope.launchWhenResumed {
                        observer.selectImage().let { result ->
                            result.collect {
                                it?.let {
                                    showMessage(it.toString())
                                }
                            }
                        }
                    }
                }
                btnRequestPermission -> {
                    lifecycleScope.launchWhenResumed {
                        observer.requestSinglePermission(CAMERA_PERMISSION).let { result ->
                            result.collect {
                                it?.let {
                                    if (it)
                                        showMessage("Granted")
                                    else
                                        showMessage("Denied")
                                }
                            }
                        }
                    }
                }
                btnRequestMultiplePermission -> {
                    lifecycleScope.launchWhenResumed {
                        observer.requestMultiplePermission(CAMERA_PERMISSION,
                            READ_CONTACTS_PERMISSION).collect {
                            it?.let {
                                showMessage("Keys ${it.keys} : Values ${it.values}")
                            }
                        }
                    }
                }

                btnLaunchActivityResult -> {
                    lifecycleScope.launchWhenResumed {
                        observer.startForResult<SecondActivity>(this@MainActivity).collect {
                            it?.let {
                                if (it.resultCode == SecondActivity.RESULT_CODE) {
                                    showMessage("${it.data?.getStringExtra(SecondActivity.NAME)}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
