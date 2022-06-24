package com.gmail.devpelegrino.ui

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.*
import com.gmail.devpelegrino.util.Constants
import java.lang.IllegalArgumentException

class MainViewModel(application: Application) : AndroidViewModel(application), DefaultLifecycleObserver {

    private var countDownTotal: Long = 0
    private var countDownTimer: CountDownTimer? = null

    private var _countDownMinutes = MutableLiveData<Int>().apply { value = 0 }
    val countDownMinutes: LiveData<Int>
        get() = _countDownMinutes

    private var _countDownSeconds = MutableLiveData<Int>().apply { value = 0 }
    val countDownSeconds: LiveData<Int>
        get() = _countDownSeconds

    private var isCountDownFinished: Boolean = false

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        load()
    }

    private fun load() {
        countDownTimer = getCountDownTimer(3620000L).start()
    }

    private fun getCountDownTimer(timeSeconds: Long): CountDownTimer {
        return object : CountDownTimer(timeSeconds, Constants.MILLISECONDS_TO_ONE_SECOND_LONG) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemains = (millisUntilFinished / Constants.MILLISECONDS_TO_ONE_SECOND_LONG).toInt()
                _countDownSeconds.value = secondsRemains.mod(Constants.SECONDS_TO_ONE_MINUTE)
                _countDownMinutes.value = secondsRemains.div(Constants.SECONDS_TO_ONE_MINUTE)
            }

            override fun onFinish() {
                isCountDownFinished = true
            }
        }
    }

    class MainViewModelFactory constructor(
        private val application: Application
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(MainViewModel::class.java) -> MainViewModel(application)
                    else -> throw IllegalArgumentException("Class not found")
                }
            } as T
    }
}