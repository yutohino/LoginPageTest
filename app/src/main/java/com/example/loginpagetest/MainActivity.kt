package com.example.loginpagetest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.loginpagetest.databinding.ActivityMainBinding
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

    lateinit var binding: ActivityMainBinding
    val userSubject = PublishSubject.create<String>()
    val passSubject = PublishSubject.create<String>()
    val compositeDisposable = CompositeDisposable()

    val userTextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(c: CharSequence?, p1: Int, p2: Int, p3: Int) {
            c?.let {
                observeOnUser(it)
                userSubject.onNext(it.toString())
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
                passSubject.onNext(it.toString())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var userList = ArrayList<String>()
    override fun onResume() {
        super.onResume()
        startActivity(Intent(this, WeatherActivity::class.java))
        return

        setSubscribe()

        binding.edUser.addTextChangedListener(userTextWatcher)

        binding.edPass.addTextChangedListener(passTextWatcher)

        binding.button.setOnClickListener {

        }

        prefs = getSharedPreferences(KEY_PREFS, Context.MODE_PRIVATE)
        editor = prefs.edit()
        binding.buttonAdd.setOnClickListener {
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
        binding.alertUser.text = if (c.length > 20) {
            "※20文字以内にしてください"
        } else {
            ""
        }
    }

    fun observeOnPass(c: CharSequence) {
        binding.alertPass.text = if (c.length < 8 || c.length > 16) {
            "※8〜16文字にしてください"
        } else {
            ""
        }
    }

    fun setClickableBtn() {
        if (binding.edUser.text.length <= 20 && binding.edPass.text.length >= 8 && binding.edPass.text.length <= 16 && binding.textUserAlert.text.isEmpty()) {
            binding.button.isClickable = true
            binding.button.background = ResourcesCompat.getDrawable(resources, R.drawable.ripple_button, null)
        } else {
            binding.button.isClickable = false
            binding.button.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_disable, null)
        }
    }

    fun searchUserSharedPref(s: String) {
        for (user in userList) {
            if (s == user) {
                binding.textUserAlert.text = "そのユーザー名はすでに使われています"
                binding.button.isClickable = false
                binding.button.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_button_disable, null)
                return
            }
            if (s != user && binding.textUserAlert.text.isNotEmpty()) {
                binding.textUserAlert.text = ""
                setClickableBtn()
            }
        }
    }

    fun searchPassSharedPref(s: String) {
        var count = 0
        for (i in s.iterator()) {
            if (i == 'e') {
                count++
            }
        }
        if (count > 0) {
            binding.textPassAlert.text = resources.getString(R.string.text_pass_alert, count)
        } else {
            binding.textPassAlert.text = ""
        }
    }

    fun setSubscribe() {
        val userDisposable = userSubject.distinctUntilChanged().debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                searchUserSharedPref(it)
            }
        val passDisposable = passSubject.distinctUntilChanged().debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                searchPassSharedPref(it)
            }
        compositeDisposable.add(userDisposable)
        compositeDisposable.add(passDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
