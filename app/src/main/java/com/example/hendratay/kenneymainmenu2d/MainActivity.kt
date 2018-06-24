package com.example.hendratay.kenneymainmenu2d

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN: Int = 123
    }

    private val providers: List<AuthUI.IdpConfig> = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.TwitterBuilder().build()
    )

    var backgroundMusic: BackgroundMusic? = null
    private val serviceConnection: ServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            backgroundMusic = (service as BackgroundMusic.ServiceBinder).getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            backgroundMusic = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(Intent(this, BackgroundMusic::class.java), serviceConnection, Context.BIND_AUTO_CREATE)

        setupMultiPlayerButton()
        setupOptionsButton()
    }

    override fun onResume() {
        super.onResume()
        startService(Intent(this, BackgroundMusic::class.java))
    }

    override fun onPause() {
        super.onPause()
        backgroundMusic?.pauseMusic()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, BackgroundMusic::class.java))
        unbindService(serviceConnection)
    }

    private fun setupMultiPlayerButton() {
        multiplayer.setOnClickListener {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.zombie_duck)
                            .setTheme(R.style.SignInTheme)
                            .setTosUrl("https://github.com")
                            .setPrivacyPolicyUrl("https://google.com")
                            .build(),
                    RC_SIGN_IN
            )
        }
    }

    private fun setupOptionsButton() {
        options.setOnClickListener {
            OptionsDialog().show(supportFragmentManager, "OptionsDialog")
        }
    }

}
