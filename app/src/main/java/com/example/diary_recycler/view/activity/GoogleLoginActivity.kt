package com.example.diary_recycler.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diary_recycler.*
import com.example.diary_recycler.dataClass.SignUp
import com.example.diary_recycler.databinding.ActivityLoginBinding
import com.example.diary_recycler.view.RetrofitClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility


class GoogleLoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    val GOOGLE_REQUEST_CODE = 99
    val TAG = "googleLogin"
    private lateinit var googleSignInClient: GoogleSignInClient
    val retrofit1 = RetrofitClient.getClient()
    var server = retrofit1?.create(ServerInterface::class.java)
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //구글로그인 버튼
        binding.login.setOnClickListener {
            signIn()
        }
        //카카오 로그인 버튼
        binding.kakaologin.setOnClickListener {
            if (LoginClient.instance.isKakaoTalkLoginAvailable(this)) {
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.idToken)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information

                    val user = auth!!.currentUser
                    val input = HashMap<String?,Any?>()
                    input.put("email", user?.email!!)
                    input.put("nickname", user?.displayName!!)
                    input.put("token", idToken)
                    input.put("img", user.photoUrl.toString())
                    Log.e("input", input.get("email").toString())
                    //idToken, user?.email!!, user?.displayName!!, user.photoUrl.toString()
                    val preferences = this.getPreferences(0)
                    val editor = preferences.edit()
                    editor.putString("token", idToken)
                    editor.apply()
                    Log.e("tokenLogin", idToken)

                    val keys: Map<String, *> = preferences.getAll()
                    for ((key, value) in keys) {
                        Log.d(
                            "map values1", key + ": " +
                                    value.toString()
                        )
                    }
                    server?.postSignUp(input)?.enqueue((object: retrofit2.Callback<SignUp> {
                           override fun onFailure(call: retrofit2.Call<SignUp>, t: Throwable?) {
                               Log.e(
                                   "signup",
                                   "서버연결실패")
                           }

                           override fun onResponse(call: retrofit2.Call<SignUp>, response: retrofit2.Response<SignUp>){

                               if (response.isSuccessful()) {
                                   val signup: SignUp? = response.body()
                                   val flag = signup?.code
                                   if (flag == 200) { //보내기 성공
                                       Log.e(
                                           "signup",
                                           "회원가입에 성공했습니다")
                                       Toast.makeText(
                                           applicationContext,
                                           "회원가입에 성공했습니다",
                                           Toast.LENGTH_SHORT
                                       ).show()
                                       loginSuccess()
                                   } else if (flag == 308) { //이메일 중복
                                       Log.e(
                                           "signup",
                                           "이미 회원가입한 계정입니다", //로그인으로 넘어가기
                                       )
                                       Toast.makeText(
                                           applicationContext,
                                           "이미 회원가입한 계정입니다",
                                           Toast.LENGTH_SHORT
                                       ).show()
                                       loginSuccess()
                                   }
                               } else println(response.toString())
                           }


                       }))

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun loginSuccess(){

        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    public override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    fun moveMainPage(user: FirebaseUser?){
        if( user!= null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    //---------------------------카톡 로그인----------------------------------

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        Log.e( "callback", "되나")
        if (error != null) {
            when {
                error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                    Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                    Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                    Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                    Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                    Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                    Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.ServerError.toString() -> {
                    Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                }
                error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                    Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                }
                else -> { // Unknown
                    Log.e( "error", error.toString())
                }
            }
        }
        else if (token != null) {
            Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
        else Log.e( "error", "로그인에러")
    }


    // 로그인 버튼


}