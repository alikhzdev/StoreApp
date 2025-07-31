package com.StoreApp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.StoreApp.databinding.ActivityLoginBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.StoreApp.KotlinActivity
import com.StoreApp.R
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var credentialManager: CredentialManager
    private val TAG = "LoginActivity"

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            lifecycleScope.launch {
                try {
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(true)
                        .setServerClientId(getString(R.string.web_client_id))
                        .setNonce(generateNonce())
                        .build()

                    val request = GetCredentialRequest(listOf(googleIdOption))
                    val credentialResponse = credentialManager.getCredential(
                        context = this@LoginActivity,
                        request = request
                    )
                    handleSignInResult(credentialResponse)
                } catch (e: GetCredentialException) {
                    Log.e(TAG, "Google Sign-In failed: ${e.message}")
                    Toast.makeText(this@LoginActivity, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.e(TAG, "Google Sign-In canceled")
            Toast.makeText(this@LoginActivity, "Google Sign-In canceled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Credential Manager
        credentialManager = CredentialManager.create(this)

        // Try to retrieve saved credentials on launch
        lifecycleScope.launch {
            tryRetrieveCredentials()
        }

        // Log In button (Username/Password)
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    if (validateCredentials(email, password)) {
                        saveCredentials(email, password)
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Google Sign-In button
        binding.googleLoginButton.setOnClickListener {
            initiateGoogleSignIn()
        }

        // Forgot Password (Placeholder)
        binding.forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Forgot Password clicked", Toast.LENGTH_SHORT).show()
            // TODO: Implement forgot password logic
        }

        // Create Account (Placeholder)
        binding.createAccountText.setOnClickListener {
            Toast.makeText(this, "Create Account clicked", Toast.LENGTH_SHORT).show()
            // TODO: Implement account creation
        }
    }

    private suspend fun tryRetrieveCredentials() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId(getString(R.string.web_client_id))
            .setNonce(generateNonce())
            .build()

        val passwordOption = GetPasswordOption()

        val request = GetCredentialRequest(listOf(googleIdOption, passwordOption))

        try {
            val result = credentialManager.getCredential(this@LoginActivity, request)
            handleSignInResult(result)
        } catch (e: NoCredentialException) {
            Log.i(TAG, "No saved credentials found")
            // Show login UI
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Credential retrieval failed: ${e.message}")
            Toast.makeText(this, "Failed to retrieve credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSignInResult(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is PasswordCredential -> {
                val email = credential.id
                val password = credential.password
                binding.emailEditText.setText(email)
                binding.passwordEditText.setText(password)
                lifecycleScope.launch {
                    if (validateCredentials(email, password)) {
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid saved credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is GoogleIdTokenCredential -> {
                val idToken = credential.idToken
                lifecycleScope.launch {
                    if (validateGoogleIdToken(idToken)) {
                        Log.i(TAG, "Google Sign-In successful, ID Token: $idToken")
                        navigateToMainActivity()
                    } else {
                        Log.e(TAG, "Google ID token validation failed")
                        Toast.makeText(this@LoginActivity, "Google Sign-In validation failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            is CustomCredential -> {
                Log.w(TAG, "Unexpected credential type: ${credential.type}")
                Toast.makeText(this, "Unsupported credential type", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Log.w(TAG, "Unknown credential type")
                Toast.makeText(this, "Unknown credential type", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun saveCredentials(email: String, password: String) {
        try {
            val createRequest = CreatePasswordRequest(email, password)
            credentialManager.createCredential(this@LoginActivity, createRequest)
            Log.i(TAG, "Credentials saved successfully")
            Toast.makeText(this, "Credentials saved", Toast.LENGTH_SHORT).show()
        } catch (e: CreateCredentialException) {
            Log.e(TAG, "Failed to save credentials: ${e.message}")
            Toast.makeText(this, "Failed to save credentials", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initiateGoogleSignIn() {
        lifecycleScope.launch {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(getString(R.string.web_client_id))
                .setNonce(generateNonce())
                .build()

            val request = GetCredentialRequest(listOf(googleIdOption))

            try {
                val result = credentialManager.getCredential(this@LoginActivity, request)
                handleSignInResult(result)
            } catch (e: NoCredentialException) {
                // No authorized accounts, try with all accounts
                val googleIdOptionRetry = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(getString(R.string.web_client_id))
                    .setNonce(generateNonce())
                    .build()

                val retryRequest = GetCredentialRequest(listOf(googleIdOptionRetry))

                try {
                    val result = credentialManager.getCredential(this@LoginActivity, retryRequest)
                    handleSignInResult(result)
                } catch (e: GetCredentialException) {
                    Log.e(TAG, "Google Sign-In failed: ${e.message}")
                    Toast.makeText(this@LoginActivity, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                }
            } catch (e: GetCredentialException) {
                Log.e(TAG, "Google Sign-In failed: ${e.message}")
                Toast.makeText(this@LoginActivity, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    private suspend fun validateCredentials(email: String, password: String): Boolean {
        // Placeholder: Replace with actual backend validation (e.g., Firebase Authentication)
        return email.isNotEmpty() && password.isNotEmpty() // Mock validation
    }

    private suspend fun validateGoogleIdToken(idToken: String): Boolean {
        // Placeholder: Replace with actual backend validation (e.g., Firebase Authentication)
        return idToken.isNotEmpty() // Mock validation
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, KotlinActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}