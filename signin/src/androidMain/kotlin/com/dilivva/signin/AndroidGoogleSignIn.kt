/*
 * Copyright (C) 2024, Send24.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.dilivva.signin

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AndroidGoogleSignIn(
    private val coroutineScope: CoroutineScope,
    private val context: Context,
    private val onResult: (GoogleSignInResult) -> Unit
): GoogleSignIn {

    private val googleIdOption = GetGoogleIdOption.Builder()
        .setServerClientId(GoogleSignInConfig.serverClientId.orEmpty())
        .setNonce(GoogleSignInConfig.nonce)
    private val credentialManager = CredentialManager.create(context)

    override fun signIn() {
        val signInRequest = googleIdOption.setFilterByAuthorizedAccounts(false).build()
        requestSignIn(signInRequest)
    }



    override fun restorePreviousSignIn() {
        val signInRequest = googleIdOption.setFilterByAuthorizedAccounts(true).build()
        requestSignIn(signInRequest)
    }

    override fun signOut() {
        coroutineScope.launch {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
    }

    private fun requestSignIn(signInOptions:  GetGoogleIdOption) {
        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(signInOptions)
            .build()
        coroutineScope.launch {
            try {
                val credential = credentialManager.getCredential(
                    request = request,
                    context = context,
                ).credential
                handleSignInCredential(credential)
            } catch (e: GetCredentialException) {
                onResult(GoogleSignInResult.Error(e.message.orEmpty()))
            }
        }
    }

    private fun handleSignInCredential(credential: Credential){
        if (credential !is CustomCredential && credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
            onResult(GoogleSignInResult.NoResult)
            return
        }

        try {
            val profile = GoogleIdTokenCredential.createFrom(credential.data)
            val googleSignInUser = GoogleSignInUser(
                email = profile.id,
                idToken = profile.idToken,
                name = profile.displayName,
                familyName = profile.familyName,
                givenName = profile.givenName,
                profilePictureUri = profile.profilePictureUri?.toString()
            )
            onResult(googleSignInUser)
        } catch (e: GoogleIdTokenParsingException) {
            onResult(GoogleSignInResult.NoResult)
        }
    }
}