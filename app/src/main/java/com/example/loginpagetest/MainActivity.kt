package com.example.loginpagetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.loginpagetest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityMainBinding
    val addressTextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
            c?.let {
                observeOnUser(it)
                setClickableBtn()
            }
        }
    }

    val passTextWatcher = object : TextWatcher {
        override fun afterTextChanged(e: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
            c?.let {
                observeOnPass(it)
                setClickableBtn()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        mBinding.edUser.addTextChangedListener(addressTextWatcher)

        mBinding.edPass.addTextChangedListener(passTextWatcher)

        mBinding.button.setOnClickListener {

        }
    }

    fun observeOnUser(c: CharSequence) {
        mBinding.alertUser.text = if (c.length > 20) {
            "※20文字以内にしてください"
        } else {
            ""
        }
    }

    fun observeOnPass(c: CharSequence) {
        mBinding.alertPass.text = if (c.length < 8 || c.length > 16) {
            "※8〜16文字にしてください"
        } else {
            ""
        }
    }

    fun setClickableBtn() {
        if (mBinding.edUser.text.length <= 20 && mBinding.edPass.text.length >= 8 && mBinding.edPass.text.length <= 16) {
            mBinding.button.isClickable = true
            mBinding.button.background = resources.getDrawable(R.drawable.ripple_button)
        } else {
            mBinding.button.isClickable = false
            mBinding.button.background = resources.getDrawable(R.drawable.bg_button_disable)
        }
    }
}
