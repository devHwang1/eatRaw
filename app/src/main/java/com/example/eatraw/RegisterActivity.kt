package com.example.eatraw



import NickFragment
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.eatraw.data.Users
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonReg: Button
    private lateinit var mAuth: FirebaseAuth


    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var googleRgLogin: ImageButton
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var backButton: ImageButton

    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    private lateinit var gso: GoogleSignInOptions





    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        buttonReg = findViewById(R.id.btn_register)
        backButton = findViewById(R.id.backButton)
        googleRgLogin = findViewById(R.id.google_rg_login)
        backButton.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.your_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        googleRgLogin.setOnClickListener {
            signUp()
        }
        buttonReg.setOnClickListener {

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }



            // 다음과 같이 신규 사용자의 이메일 주소와 비밀번호를 createUserWithEmailAndPassword에 전달하여 신규 계정을 생성합니다.
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 사용자가 성공적으로 생성되었으므로, 사용자 정보를 데이터베이스에 저장
                        val currentUser = mAuth.currentUser

                        val user = currentUser?.let { it -> Users(email, nickname ="", aouthLogin = false,
                            admin = false,
                            imageUrl ="", userId = it.uid,likeMarket = null) } // 사용자 정보 생성

                        // 또는 Cloud Firestore에 사용자 정보 저장
                        if (currentUser != null) {
                            if (user != null) {
                                usersCollection.document(currentUser.uid).set(user)
                            }
                        }

                        val nickFragment = NickFragment().apply {
                            arguments = Bundle().apply {
                                putString("email", email)
                            }
                        }

                        // Add the fragment to the 'fragment_container' FrameLayout
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, nickFragment)
                            .commit()

                    } else {
                        // If sign-in fails, display a message to the user.
                        Toast.makeText(
                            this@RegisterActivity,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.your_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .build()



    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun signUp() {
        val signInIntent = mGoogleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val idToken = account?.idToken

            val credential = GoogleAuthProvider.getCredential(idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 사용자가 성공적으로 로그인되었으므로, 사용자 정보를 데이터베이스에 저장
                        val currentUser = mAuth.currentUser

                        val user = currentUser?.let { Users(account?.email!!, nickname ="",
                            aouthLogin = true,
                            admin = false,
                            imageUrl ="", userId = it.uid,likeMarket = null) } // 사용자 정보 생성


                        // 또는 Cloud Firestore에 사용자 정보 저장
                        if (currentUser != null) {
                            if (user != null) {
                                usersCollection.document(currentUser.uid).set(user)
                            }
                        }
                        // Create a new Fragment to be placed in the activity layout
                        val nickFragment = NickFragment().apply {
                            arguments = Bundle().apply {
                                putString("email", account?.email!!)
                            }
                        }

                        // Add the fragment to the 'fragment_container' FrameLayout
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, nickFragment)
                            .commit()

                    } else {
                        // If sign-in fails, display a message to the user
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Toast.makeText(this@RegisterActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {
            Log.w("failed", "signInResult:failed code=" + e.statusCode)
        }
    }
}