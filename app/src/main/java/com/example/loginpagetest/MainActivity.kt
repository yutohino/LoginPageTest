package com.example.loginpagetest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.example.loginpagetest.databinding.ActivityMainBinding
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    val KEY_PREFS = "KEY_PREFS"
    val KEY_STRING1 = "KEY_STRING1"
    val KEY_STRING2 = "KEY_STRING2"
    val KEY_STRING3 = "KEY_STRING3"
    val KEY_STRING4 = "KEY_STRING4"

    lateinit var mBinding: ActivityMainBinding
    val subject = PublishSubject.create<CharSequence>()
    val compositeDisposable = CompositeDisposable()

    val userTextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
            c?.let {
                observeOnUser(it)
                subject.onNext(it.toString())
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
                subject.onNext(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var userList = ArrayList<String>()
    override fun onResume() {
        super.onResume()
        val userDisposable = subject.distinctUntilChanged()
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            searchUserSharedPref(it)
        }
        compositeDisposable.add(userDisposable)

        mBinding.edUser.addTextChangedListener(userTextWatcher)

        mBinding.edPass.addTextChangedListener(passTextWatcher)

        mBinding.button.setOnClickListener {

        }

        prefs = getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        editor = prefs.edit()
        mBinding.buttonAdd.setOnClickListener {
            editor.putString(KEY_STRING1, "ひの")
            editor.putString(KEY_STRING2, "ゆうと")
            editor.putString(KEY_STRING3, "yuto")
            editor.putString(KEY_STRING4, "hino")
            editor.commit()

            addUserList()
        }

        addUserList()
    }

    fun addUserList() {
        prefs.getString(KEY_STRING1, null)?.let { userList.add(it) }
        prefs.getString(KEY_STRING2, null)?.let { userList.add(it) }
        prefs.getString(KEY_STRING3, null)?.let { userList.add(it) }
        prefs.getString(KEY_STRING4, null)?.let { userList.add(it) }
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

    fun searchUserSharedPref(c: CharSequence) {
        for (user in userList) {
            if (c.toString() == user) {
                mBinding.textAlert.text = "そのユーザー名はすでに使われています"
                mBinding.button.isClickable = false
                mBinding.button.background = resources.getDrawable(R.drawable.bg_button_disable)
                return
            }
            if (c.toString() != user && mBinding.textAlert.text.isNotEmpty()) {
                mBinding.textAlert.text = ""
                setClickableBtn()
            }
        }
    }

    fun searchPassSharedPref(c: CharSequence) {

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
