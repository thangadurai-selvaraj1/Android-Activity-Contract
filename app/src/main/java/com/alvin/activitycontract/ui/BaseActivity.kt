package com.alvin.activitycontract.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alvin.activitycontract.utils.ActivityContractObserver

open class BaseActivity : AppCompatActivity() {

    lateinit var observer: ActivityContractObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer = ActivityContractObserver(activityResultRegistry)
        lifecycle.addObserver(observer)
    }

    fun showMessage(msg: String) {
        Toast.makeText(this,
            msg,
            Toast.LENGTH_SHORT).show()
    }
}