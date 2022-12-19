package com.example.starpin.common.Services


import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.AudioAttributes
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.starpin.*
import com.example.starpin.common.PlayBroadcastReceiver


interface PlayBind {
    fun onStart()

}

var playBind: (service: IntentService) -> Unit = {}
var inted = false

class PlayService : Service() {
    lateinit var mediaPlayer: MediaPlayer
    val CHANNEL_ID = "playing_service"
    lateinit var track: Track
    lateinit var mediaSession: MediaSessionCompat


    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        Thread {
            while (true) {

                try {
                    if (Managers.musicManager.playingState == MusicManager.PLAYING) {
                        Managers.musicManager.onProgress.onProgressListener(getPosition(), getMax())
                        Managers.musicManager.progress = getPosition()
                    }
                } catch (e: NullPointerException) {
                    Log.e("ErrorPosition", e.message.toString())
                }

            }
        }.start()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        val mode = intent!!.action


        if (mode == "new") {


            play(Managers.musicManager.currentTrack!!.url)


        } else if (mode == "pause") {
            mediaPlayer.pause()

        } else if (mode == "unpause") {
            mediaPlayer.start()

        } else if (mode == "seekTo") {

            setPosition(intent.getIntExtra("position", 0))

        }

        showNotification(
            Managers.musicManager.playingState,
            Managers.musicManager.currentTrack!!.name,
            Managers.musicManager.currentTrack!!.artist,
            Managers.musicManager.currentTrack!!.avatar
        )
        return super.onStartCommand(intent, flags, startId)
    }

    fun getPosition(): Int {
        var out: Int = 0

        if (::mediaPlayer.isInitialized && is_prepared) {
            try {
                out = mediaPlayer.currentPosition
            } catch (e: IllegalStateException) {
                out = 0
            }
        }

        return out
    }

    fun setPosition(sec: Int) {
        if (::mediaPlayer.isInitialized && is_prepared) {
            mediaPlayer.seekTo(sec)

        }
    }

    fun getMax(): Int {
        var out: Int = 0
        if (::mediaPlayer.isInitialized && is_prepared) {
            try {
                out = mediaPlayer.duration
            } catch (e: IllegalStateException) {
                out = 0
            }
        }
        return out
    }

    fun play(url: String) {

        try {

            Managers.musicManager.onLoading.onStartLoading()

        } catch (e: Exception) {
            Log.e("onStartLoading", e.message.toString())
        }

        if (::mediaPlayer.isInitialized) {

            mediaPlayer.release()

            is_prepared = false
        }
        mediaPlayer = MediaPlayer()


        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        mediaPlayer.setDataSource(url)

        //mediaPlayer.isLooping = false
        mediaPlayer.setOnPreparedListener {

            is_prepared = true
            try {
                Screens.activity.runOnUiThread {
                    Managers.musicManager.onLoading.onStopLoading()
                }
            } catch (e: Exception) {
                Log.e("onStopLoading", e.message.toString())
            }
            Log.e("prepareState", is_prepared.toString())
            if (Managers.musicManager.playingState == MusicManager.PLAYING) {
                mediaPlayer.start()

            }
            Managers.musicManager.maxPosition = getMax()
            Log.e("Max", getMax().toString())
            showNotification(
                Managers.musicManager.playingState,
                Managers.musicManager.currentTrack!!.name,
                Managers.musicManager.currentTrack!!.artist,
                Managers.musicManager.currentTrack!!.avatar
            )
        }
        mediaPlayer.setOnCompletionListener {

            //stopSelf()

            //Thread {

            val track = Managers.musicManager.nextTrack()
            play(track.url)
            //}.start()
            //if (getPosition() == getMax()) {
            //nextTrack()
            //}

        }
        mediaPlayer.prepareAsync()


    }


    @SuppressLint("LaunchActivityFromNotification")
    private fun showNotification(state: Int, name: String, artist: String, avatar: String) {
        mediaSession = MediaSessionCompat(this, "My music")
        val toActivity = Intent(this, MainScreenActivity::class.java)
        val toActivityPendingIntent = PendingIntent.getActivity(this, 0, toActivity, FLAG_IMMUTABLE)

        val previous = Intent(this, PlayBroadcastReceiver::class.java)
        previous.action = "previous"
        val previousPendingIntent = PendingIntent.getBroadcast(this, 0, previous, FLAG_IMMUTABLE)

        val pauseIntent = Intent(this, PlayBroadcastReceiver::class.java)
        pauseIntent.action = "pause"
        val pausePendingIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, FLAG_IMMUTABLE)

        val unpauseIntent = Intent(this, PlayBroadcastReceiver::class.java)
        unpauseIntent.action = "unpause"
        val unpausePendingIntent =
            PendingIntent.getBroadcast(this, 0, unpauseIntent, FLAG_IMMUTABLE)

        val nextIntent = Intent(this, PlayBroadcastReceiver::class.java)
        nextIntent.action = "next"
        val nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, FLAG_IMMUTABLE)


        var notification = NotificationCompat.Builder(this, CHANNEL_ID)
            //.setContentText("$artist - $name")
            .setSmallIcon(R.drawable.ic_baseline_audiotrack_24)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )


            .setCategory(Notification.CATEGORY_SERVICE)
            .setOngoing(true)

        //Log.e("getPosition().toLong()", mediaPlayer.duration.toLong().toString())
        mediaSession.setCallback(object: MediaSessionCompat.Callback(){
            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
                setPosition(pos.toInt())
                showNotification(state, name, artist, avatar)
            }
        })

        if (state == MusicManager.PLAYING) {
            val previousAction = NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_skip_previous_24,
                "ggg",
                previousPendingIntent
            ).build()
            val pauseAction = NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_pause_24,
                "ggg",
                pausePendingIntent
            ).build()
            val nextAction = NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_skip_next_24,
                "ggg",
                nextPendingIntent
            ).build()
            notification = notification
                .addAction(previousAction)
                .addAction(pauseAction)
                .addAction(nextAction)


            val mPlaybackState = PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, getPosition().toLong(), 1.0f)

                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
            mediaSession.setPlaybackState(mPlaybackState.build())


        } else {
            val previousAction = NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_skip_previous_24,
                "ggg",
                previousPendingIntent
            ).build()
            val pauseAction = NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_play_arrow_24,
                "ggg",
                unpausePendingIntent
            ).build()
            val nextAction = NotificationCompat.Action.Builder(
                R.drawable.ic_baseline_skip_next_24,
                "ggg",
                nextPendingIntent
            ).build()
            notification = notification
                .addAction(previousAction)
                .addAction(pauseAction)
                .addAction(nextAction)
            val mPlaybackState = PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PAUSED, getPosition().toLong(), 1.0f)

                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)

            mediaSession.setPlaybackState(mPlaybackState.build())

        }
        Glide.with(applicationContext)
            .asBitmap()
            .load(avatar)

            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    Log.e("Max", getMax().toLong().toString())
                    if ("no-cover" in avatar) {
                        mediaSession.setMetadata(
                            MediaMetadataCompat.Builder()


                                .putString(MediaMetadata.METADATA_KEY_ARTIST, artist)
                                .putBitmap(
                                    MediaMetadata.METADATA_KEY_ART,
                                    BitmapFactory.decodeResource(resources, R.drawable.no_avatar)
                                )

                                .putLong(MediaMetadata.METADATA_KEY_DURATION, getMax().toLong())
                                //.putString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI, avatar)
                                .putString(MediaMetadata.METADATA_KEY_TITLE, name)
                                .build()
                        )
                    } else {
                        mediaSession.setMetadata(
                            MediaMetadataCompat.Builder()


                                .putString(MediaMetadata.METADATA_KEY_ARTIST, artist)
                                .putBitmap(MediaMetadata.METADATA_KEY_ART, resource)

                                .putLong(MediaMetadata.METADATA_KEY_DURATION, getMax().toLong())
                                //.putString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI, avatar)
                                .putString(MediaMetadata.METADATA_KEY_TITLE, name)
                                .build()
                        )

                    }

                    startForeground(123, notification.build())

                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    mediaSession.setMetadata(
                        MediaMetadataCompat.Builder()


                            .putString(MediaMetadata.METADATA_KEY_ARTIST, artist)
                            .putBitmap(
                                MediaMetadata.METADATA_KEY_ART,
                                BitmapFactory.decodeResource(resources, R.drawable.no_avatar)
                            )

                            .putLong(MediaMetadata.METADATA_KEY_DURATION, getMax().toLong())
                            //.putString(MediaMetadata.METADATA_KEY_ALBUM_ART_URI, avatar)
                            .putString(MediaMetadata.METADATA_KEY_TITLE, name)
                            .build()
                    )
                    startForeground(123, notification.build())
                }
            })


    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            serviceChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented");
    }


}

class PlayerMediaSessionCallback(val mediaPlayer: MediaPlayer?) : MediaSessionCompat.Callback() {

    // other callback methods
    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)

        mediaPlayer?.seekTo(pos.toInt())

    }


}