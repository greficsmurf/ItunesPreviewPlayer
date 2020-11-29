package com.example.itunesapiapptest.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.domain.model.Song
import com.example.itunesapiapptest.MainActivity
import com.example.itunesapiapptest.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.lang.Exception


@OptIn(ExperimentalCoroutinesApi::class)
class PlayerService : Service() {
    companion object{
        const val CHANNEL_ID = "0"
        const val CHANNEL_NAME = "PlayerService"
        const val FOREGROUND_ID = 1
        const val PREV_CLICK = "prev"
        const val NEXT_CLICK = "next"
        const val PLAY_STOP_CLICK = "play_stop"
        const val NOTIFICATION_CLICK = "notification_click"
        const val CLEAR_CLICK = "clear"
        var isRunning = false
    }
    lateinit var remoteView: RemoteViews
    lateinit var notificationManager: NotificationManager
    lateinit var notification: Notification

    private val binder = PlayerBinder()
    private val playList: MutableList<Song> = mutableListOf()
    private val _currentSongIndex = MutableStateFlow(0)
    private val _currentSong: MutableStateFlow<Song?> = MutableStateFlow(null)
    val currentSong: StateFlow<Song?>
        get() = _currentSong
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean>
        get() = _isPlaying
    private val _isPrepared = MutableStateFlow(false)
    val isPrepared: StateFlow<Boolean>
        get() = _isPrepared
    private val _isLeftEnd = MutableStateFlow(false)
    val isLeftEnd: StateFlow<Boolean>
        get() = _isLeftEnd
    private val _isRightEnd = MutableStateFlow(false)
    val isRightEnd: StateFlow<Boolean>
        get() = _isRightEnd
    private val _isDestroyed = MutableStateFlow(false)
    val isDestroyed: StateFlow<Boolean>
        get() = _isDestroyed

    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
                AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        )

        setOnCompletionListener {
            _isPlaying.value = false
            nextSong()
            _isPrepared.value = false
        }
        setOnPreparedListener {
            _isPrepared.value = true
            resumeSong()
            _isPlaying.value = true
        }
    }

    fun setSongs(songs: List<Song>, startIndex: Int = 0){
        playList.clear()
        playList.addAll(songs)
        setCurrentSongIndex(startIndex)
    }

    val player: MediaPlayer
        get() = mediaPlayer

    fun nextSong(){
        setCurrentSongIndex(_currentSongIndex.value.plus(1))
    }

    fun prevSong(){
        setCurrentSongIndex(_currentSongIndex.value.minus(1))
    }

    fun seekTo(progress: Int){
        mediaPlayer.seekTo(progress)
    }

    fun playPauseSong(){
        if(mediaPlayer.isPlaying){
            pauseSong()
        }
        else
        {
            resumeSong()
        }
    }

    fun pauseSong(){
        mediaPlayer.pause()
        _isPlaying.value = false
        modifyNotification {
            remoteView.setInt(
                R.id.play_stop_button, "setBackgroundResource", R.drawable.ic_play_arrow_24px__1__notification
            )
        }
    }
    fun resumeSong(){
        mediaPlayer.start()
        _isPlaying.value = true
        modifyNotification {
            remoteView.setInt(
                R.id.play_stop_button, "setBackgroundResource", R.drawable.ic_stop_24px_notification
            )
        }
    }

    private fun modifyNotification(call: () -> Unit){
        call.invoke()
        notificationManager.notify(FOREGROUND_ID, notification)
    }


    val songPos: Int
        get() = mediaPlayer.currentPosition

    val duration: Int
        get() = mediaPlayer.duration

    private fun setCurrentSongIndex(index: Int){
        if(index >= 0 && index <= playList.lastIndex)
        {
            _currentSongIndex.value = index
            _currentSong.value = playList[index]
        }

        _isRightEnd.value = index >= playList.lastIndex
        _isLeftEnd.value = index <= 0
    }

    init {
        CoroutineScope(Dispatchers.Main).launch {
            _currentSong.collect { song ->
                song?.let {
                    initMediaPlayer(song)
                }
            }
        }
    }


    private fun initMediaPlayer(song: Song){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mediaPlayer.apply {
                    stop()
                    reset()
                    setDataSource(this@PlayerService, Uri.parse(song.previewUrl))
                    prepare()
                }

            }catch (e: Exception){
            }
        }
        modifyNotification {
            remoteView.setTextViewText(
                R.id.song_title, song.trackName
            )
            remoteView.setTextViewText(
                R.id.song_artist, song.artist.artistName
            )
            if(mediaPlayer.isPlaying){
                modifyNotification {
                    remoteView.setInt(
                            R.id.play_stop_button, "setBackgroundResource", R.drawable.ic_stop_24px_notification
                    )
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class PlayerBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }
    inner class PrevClickBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action ?: ""
            when(action) {
                PREV_CLICK -> this@PlayerService.prevSong()
                NEXT_CLICK -> this@PlayerService.nextSong()
                PLAY_STOP_CLICK -> this@PlayerService.playPauseSong()
                NOTIFICATION_CLICK -> {
                    val intent = Intent(applicationContext, MainActivity::class.java).apply {
                        putExtra("songId", currentSong.value?.trackId)
                        putExtra("albumId", currentSong.value?.collectionId)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                }
                CLEAR_CLICK -> this@PlayerService.apply {
                    _isDestroyed.value = true
                    stopForeground(true)
                    stopSelf()
                }
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startPlayerForeground()

        return START_NOT_STICKY
    }

    private fun startPlayerForeground(){
        val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel(CHANNEL_ID, CHANNEL_NAME)
                } else {
                    CHANNEL_ID
                }


        val pendingIntent: PendingIntent =
                Intent(this, MainActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(this, 0, notificationIntent, 0)
                }
        remoteView = RemoteViews(applicationContext.packageName, R.layout.notification_player)
        remoteView.apply {
            setOnClickPendingIntent(
                    R.id.prev_button, PendingIntent.getBroadcast(applicationContext, 0,
                    Intent(PREV_CLICK), 0)
            )
            setOnClickPendingIntent(
                    R.id.play_stop_button, PendingIntent.getBroadcast(applicationContext, 0,
                    Intent(PLAY_STOP_CLICK), 0)
            )
            setOnClickPendingIntent(
                    R.id.next_button, PendingIntent.getBroadcast(applicationContext, 0,
                    Intent(NEXT_CLICK), 0)
            )
            setOnClickPendingIntent(
                    R.id.dismiss_button, PendingIntent.getBroadcast(applicationContext, 0,
                    Intent(CLEAR_CLICK), 0)
            )
        }
        registerReceiver(PrevClickBroadcastReceiver(), IntentFilter().apply {
            addAction(PREV_CLICK)
            addAction(NEXT_CLICK)
            addAction(PLAY_STOP_CLICK)
            addAction(NOTIFICATION_CLICK)
            addAction(CLEAR_CLICK)
        })
        notification = NotificationCompat.Builder(this, channelId)
                .setColorized(true)
                .setColor(Color.parseColor("#ffffff"))
                .setCustomContentView(remoteView)
                .setContentTitle(getText(R.string.notification_player_title))
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setSmallIcon(R.drawable.ic_music_note_24px)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setContentIntent(
                        PendingIntent.getBroadcast(
                                applicationContext,
                                0,
                                Intent(
                                        NOTIFICATION_CLICK
                                ), 0
                        )
                )
                .build()
        notification.priority = NotificationCompat.PRIORITY_DEFAULT
        notification.defaults = 0
        notification.sound = null
        startForeground(FOREGROUND_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_DEFAULT)
        chan.setSound(null, null)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    override fun onDestroy() {
        mediaPlayer.release()
        isRunning = false
        super.onDestroy()
    }
}