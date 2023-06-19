package com.example.one_tap

import android.content.Context
import android.util.Log
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.util.*


class IdTokenVerifier(var context: Context?, var googleIdToken: String?) {

    fun verifier() {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val verifier = GoogleIdTokenVerifier.Builder(
                NetHttpTransport(),
                GsonFactory()
            ) // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList("677043548932-e2enrf2su0ap9m9vl9nsedd37d99a32g.apps.googleusercontent.com")) // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build()
            try {
                val idToken: GoogleIdToken = verifier.verify(googleIdToken)
                if (idToken != null) {
                    val payload: GoogleIdToken.Payload = idToken.payload

                    // Print user identifier
                    val userId: String = payload.subject
                    println("User ID: $userId")

                    // Get profile information from payload
                    val email: String = payload.email
                    val emailVerified: Boolean =
                        java.lang.Boolean.valueOf(payload.emailVerified)
                    val name = payload.get("name")
                    val pictureUrl = payload.get("picture")
                    val locale = payload.get("locale")
                    val familyName = payload.get("family_name")
                    val givenName = payload.get("given_name")

                    // Use or store profile information
                    // ...
                } else {
                    println("Invalid ID token.")
                }

            } catch (e: Exception) {
                Log.d("error", e.toString())
            }
        }

    }
}