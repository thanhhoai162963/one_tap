package com.example.one_tap

import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.one_tap.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.identity.*
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var signInClient: SignInClient
    private lateinit var beginSignInRequest: BeginSignInRequest
    private var username: String = "thanh"
    private var password: String = "123"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signInClient = Identity.getSignInClient(this)
        beginSignInRequest = BeginSignInRequest.builder().setPasswordRequestOptions(
            BeginSignInRequest.PasswordRequestOptions.builder().setSupported(true).build()
        ).setGoogleIdTokenRequestOptions(
            GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setServerClientId(getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(true).build()
        ).setAutoSelectEnabled(true).build()


        val login =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
                if (it.resultCode == RESULT_OK) {
                    try {
                        val certificate = signInClient.getSignInCredentialFromIntent(it.data)
                        val googleIdToken = certificate.googleIdToken
                        IdTokenVerifier(this, googleIdToken).verifier()
                        Log.d("bbb", googleIdToken.toString())
                    } catch (e: ApiException) {
                        when (e.statusCode) {
                            CommonStatusCodes.CANCELED -> {

                            }
                            CommonStatusCodes.NETWORK_ERROR -> {

                            }
                            else -> {

                            }
                        }
                    }
                }
            }
        val savePasswordClient = Identity.getCredentialSavingClient(this)
        val signInPassword = SignInPassword(username, password)
        val savePasswordRequest =
            SavePasswordRequest.builder().setSignInPassword(signInPassword).build()

        val signInHandler =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {

            }
        binding.btn.setOnClickListener {
            savePasswordClient.savePassword(savePasswordRequest).addOnSuccessListener {
                signInHandler.launch(
                    IntentSenderRequest.Builder(it.pendingIntent.intentSender).build()
                )
            }
        }

        binding.btn2.setOnClickListener {
            signInClient.beginSignIn(beginSignInRequest).addOnSuccessListener {
                login.launch(IntentSenderRequest.Builder(it.pendingIntent.intentSender).build())
            }
        }
    }

}