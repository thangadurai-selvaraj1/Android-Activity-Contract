package com.alvin.activitycontract.ui

import android.content.Intent
import android.os.Bundle
import com.alvin.activitycontract.databinding.ActivitySecondBinding

class SecondActivity : BaseActivity() {

    companion object {
        const val NAME = "name"
        const val RESULT_CODE = 1002
    }

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSetResult.setOnClickListener {
            setResult(RESULT_CODE, Intent().apply {
                putExtra(NAME, "alvin")
            })
            finish()
        }

    }
}