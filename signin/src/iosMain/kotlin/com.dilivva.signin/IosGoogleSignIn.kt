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

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSError
import platform.UIKit.UIViewController
import signin.GoogleSignIn.GIDGoogleUser
import signin.GoogleSignIn.GIDSignIn

@OptIn(ExperimentalForeignApi::class)
class IosGoogleSignIn(
    private val presenter: UIViewController,
    private val onResult: (GoogleSignInResult) -> Unit
): GoogleSignIn {

    private val googleInstance = GIDSignIn.sharedInstance
    override fun signIn() {
        googleInstance.signInWithPresentingViewController(presenter){ gidSignInResult, nsError ->
            signInRequest(nsError, gidSignInResult?.user)
        }
    }
    override fun restorePreviousSignIn() {
        googleInstance.restorePreviousSignInWithCompletion { gidGoogleUser, nsError ->
            signInRequest(nsError, gidGoogleUser)
        }
    }

    override fun signOut() {
        googleInstance.signOut()
    }

    private fun signInRequest(
        nsError: NSError?,
        user: GIDGoogleUser?
    ) {
        if (nsError != null) {
            onResult(GoogleSignInResult.Error(nsError.localizedDescription))
            return
        }
        if (user?.idToken == null || user.profile == null) {
            onResult(GoogleSignInResult.NoResult)
            return
        }

        val idToken = user.idToken?.tokenString ?: return
        val profile = user.profile() ?: return

        val googleSignInUser = GoogleSignInUser(
            email = profile.email,
            idToken = idToken,
            name = profile.name,
            familyName = profile.familyName,
            givenName = profile.givenName,
            profilePictureUri = profile.imageURLWithDimension(320u)?.absoluteString
        )
        onResult(googleSignInUser)
    }
}