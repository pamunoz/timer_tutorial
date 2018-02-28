package com.pfariasmunoz.timertutorial.timer

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.ads.AdRequest
import com.pfariasmunoz.timertutorial.R
import com.pfariasmunoz.timertutorial.di.Injector
import com.pfariasmunoz.timertutorial.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*

class TimerActivity : AppCompatActivity(), TimerContract.View {

    override val presenter: TimerContract.Presenter

    init {
        this.presenter = Injector.get().timerPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setIcon(R.drawable.ic_timer)
            title = getString(R.string.timer_action_bar_title)
        }
        presenter.setView(this)
        // add functionality to the fabButtons
        fab_start.setOnClickListener { presenter.startTimer() }
        fab_pause.setOnClickListener { presenter.pauseTimer() }
        fab_stop.setOnClickListener { presenter.stopTimer() }

        val adRequest: AdRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPauseView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun updateCountdownUI() {
        textView_countdown.text = presenter.timerText
        progress_countdown.progress = presenter.progress
    }

    override fun updateButtons(timerState: TimerState) {
        when(timerState) {
            TimerState.RUNNING -> {
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
                fab_start.isEnabled = false
            }
            TimerState.STOPPED -> {
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
                fab_start.isEnabled = true
            }
            TimerState.PAUSED -> {
                fab_start.isEnabled = true
                fab_stop.isEnabled = true
                fab_pause.isEnabled = false
            }
        }
    }

    override fun resetProgressCountdown() { progress_countdown.progress = 0 }

    override fun setMaxProgressCountdown(max: Int) { progress_countdown.max = max }

}
