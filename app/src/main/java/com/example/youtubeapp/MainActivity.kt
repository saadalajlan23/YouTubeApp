package com.example.youtubeapp

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MainActivity : AppCompatActivity() {
    private val videos: Array<Array<String>> = arrayOf(
        arrayOf("SPIDER-MAN: NO WAY HOME", "JfVOs4VSpmA"),
        arrayOf("Dune ", "8g18jFHCLXk"),
        arrayOf("VENOM: LET THERE BE CARNAGE", "-FmWuCgJmxo"),
        arrayOf("TOY STORY", "rNk1Wi8SvNc"),
        arrayOf("The Shawshank Redemption", "PLl99DlL6b4"),
        arrayOf("The Lion King", "lFzVJEksoDY"),
        arrayOf("The Conjuring", "k10ETZ41q5o") ,
        arrayOf("The Godfather", "sY1S34973zA"))


    private lateinit var youTubePlayerView: YouTubePlayerView
    private lateinit var player: YouTubePlayer
    private var currentVideo = 0
    private var timeStamp = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkInternet()
        title = "Trailer Movies"
        youTubePlayerView = findViewById(R.id.ytPlayer)
        youTubePlayerView.addYouTubePlayerListener(object: AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                player = youTubePlayer
                player.loadVideo(videos[currentVideo][1], timeStamp)
                initializeRV()
            }
        })

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            youTubePlayerView.enterFullScreen()
        } else if (newConfig.orientation === Configuration.ORIENTATION_PORTRAIT) {
            youTubePlayerView.exitFullScreen()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt("currentVideo", currentVideo)
        outState.putFloat("timeStamp", timeStamp)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        currentVideo = savedInstanceState.getInt("currentVideo", 0)
        timeStamp = savedInstanceState.getFloat("timeStamp", 0f)
    }

    private fun initializeRV(){
        val recyclerView: RecyclerView = findViewById(R.id.rvVideos)
        recyclerView.adapter = RecycelViewAdapter(videos, player)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    private fun checkInternet(){
        if(!connectedToInternet()){
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Internet Connection Not Found")
                .setPositiveButton("RETRY"){_, _ -> checkInternet()}
                .show()
        }
    }

    private fun connectedToInternet(): Boolean{
        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}