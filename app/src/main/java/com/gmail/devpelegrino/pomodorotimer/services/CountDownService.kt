package com.gmail.devpelegrino.pomodorotimer.services

import android.app.*
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import com.gmail.devpelegrino.R
import com.gmail.devpelegrino.pomodorotimer.data.database.AppDatabase
import com.gmail.devpelegrino.pomodorotimer.data.repository.PomodoroDataSource
import com.gmail.devpelegrino.pomodorotimer.enums.PomodoroState
import com.gmail.devpelegrino.pomodorotimer.enums.TimerState
import com.gmail.devpelegrino.pomodorotimer.ui.pomodoro.*
import com.gmail.devpelegrino.pomodorotimer.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CountDownService : Service() {

    private var coroutineScope = CoroutineScope(Dispatchers.IO)
    private lateinit var notificationManager: NotificationManager
    private var pomodoroHandle: PomodoroHandle? = null
    private var countDownSecondsLeft: Int = 0
    private var timerState: TimerState = TimerState.STOP
    private var pomodoroState = PomodoroState.FOCUS

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createChannel()
        getNotificationManager()
        loadStateFlow()

        val database = AppDatabase.getDatabase(application.applicationContext)
        pomodoroHandle = PomodoroHandle(
            pomodoroRepository = PomodoroDataSource(database.pomodoroDao()),
            onTimerTicker = { allSeconds -> onTickerNotification(allSeconds) },
            onTimerFinish = { finishService() }
        )

        when (intent?.getStringExtra(Constants.COUNTDOWN_ACTION)) {
            Constants.MOVE_TO_FOREGROUND -> moveToForeground()
            Constants.MOVE_TO_BACKGROUND -> moveToBackground()
        }

        return START_STICKY
    }

    private fun loadStateFlow() {
        coroutineScope.launch {
            pomodoroHandle?.pomodoroState?.collect {
                pomodoroState = it
            }
        }
        coroutineScope.launch {
            pomodoroHandle?.timerState?.collect {
                timerState = it
            }
        }
    }

    private fun moveToForeground() {
        isBackground = false
        pomodoroHandle?.actionCountDown()
        startForeground(ID_NOTIFICATION, buildNotification())
        updateNotification(PomodoroHandle.countDownTimeSecondsLeft)
    }

    private fun moveToBackground() {
        isBackground = true
        if (PomodoroHandle.lastTimerState != TimerState.STOP) {
            pomodoroHandle?.actionCountDown()
        }
        stopForeground(true)
        sendStatus()
        pomodoroHandle = null
    }

    private fun onTickerNotification(time: Int) {
        if (!isBackground) {
            countDownSecondsLeft = time
            updateNotification()
        }
    }

    private fun finishService() {
        pomodoroHandle = null
        this.stopSelf()
        stopForeground(true)
    }

    private fun sendStatus() {
        val statusIntent = Intent()
        statusIntent.action = Constants.COUNTDOWN_STATUS
        statusIntent.putExtra(Constants.COUNTDOWN_STATE, timerState.name)
        sendBroadcast(statusIntent)
    }

    private fun createChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.setSound(null, null)
        notificationChannel.setShowBadge(true)
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun getNotificationManager() {
        notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
    }

    private fun buildNotification(time: Int? = null): Notification {

        val title: String
        var minutes: Int
        var seconds: Int

        minutes = countDownSecondsLeft.getMinuteByTotalSeconds()
        seconds = countDownSecondsLeft.getSecondsInMinuteByTotalSeconds()

        time?.let {
            minutes = it.getMinuteByTotalSeconds()
            seconds = it.getSecondsInMinuteByTotalSeconds()
        }

        val intent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val pomodoroStateUi: PomodoroStateUI =
            when (pomodoroState) {
                PomodoroState.FOCUS -> {
                    title = getString(R.string.notification_focus_title)
                    PomodoroStateUI(
                        name = getString(R.string.focus_name),
                        primaryColor = resources.getColor(
                            R.color.colorFocus,
                            applicationContext.theme
                        ),
                        secondaryColor = resources.getColor(
                            R.color.colorFocusSecondary,
                            applicationContext.theme
                        ),
                        tertiaryColor = resources.getColor(
                            R.color.colorFocusTertiary,
                            applicationContext.theme
                        ),
                        iconSrc = resources.getDrawable(
                            R.drawable.ic_focus,
                            applicationContext.theme
                        )
                    )
                }
                PomodoroState.SHORT_BREAK -> {
                    title = getString(R.string.notification_short_break_title)
                    PomodoroStateUI(
                        name = getString(R.string.short_break_name),
                        primaryColor = resources.getColor(
                            R.color.colorShortBreak,
                            applicationContext.theme
                        ),
                        secondaryColor = resources.getColor(
                            R.color.colorShortBreakSecondary,
                            applicationContext.theme
                        ),
                        tertiaryColor = resources.getColor(
                            R.color.colorShortBreakTertiary,
                            applicationContext.theme
                        ),
                        iconSrc = resources.getDrawable(
                            R.drawable.ic_coffee,
                            applicationContext.theme
                        )
                    )
                }
                PomodoroState.LONG_BREAK -> {
                    title = getString(R.string.notification_long_break_title)
                    PomodoroStateUI(
                        name = getString(R.string.long_break_name),
                        primaryColor = resources.getColor(
                            R.color.colorLongBreak,
                            applicationContext.theme
                        ),
                        secondaryColor = resources.getColor(
                            R.color.colorLongBreakSecondary,
                            applicationContext.theme
                        ),
                        tertiaryColor = resources.getColor(
                            R.color.colorLongBreakTertiary,
                            applicationContext.theme
                        ),
                        iconSrc = resources.getDrawable(
                            R.drawable.ic_coffee,
                            applicationContext.theme
                        )
                    )
                }
            }

        //                when (timerState) {
//                    TimerState.RUNNING -> _setStartIcon.value = false
//                    else -> _setStartIcon.value = true
//                }


        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setOngoing(true)
            .setContentText(
                "${"%02d".format(minutes)}:${"%02d".format(seconds)}"
            )
            .setColorized(true)
            .setColor(pomodoroStateUi.secondaryColor)
            .setSmallIcon(IconCompat.createWithBitmap(pomodoroStateUi.iconSrc.toBitmap()))
            .setOnlyAlertOnce(true)
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun updateNotification(time: Int? = null) {
        if (!isBackground) {
            notificationManager.notify(
                ID_NOTIFICATION,
                buildNotification(time)
            )
        }
    }

    companion object {
        private const val ID_NOTIFICATION = 1973
        private const val CHANNEL_ID = "COUNTDOWN_NOTIFICATIONS"
        private const val CHANNEL_NAME = "COUNTDOWN"
        private var isBackground = false
    }
}