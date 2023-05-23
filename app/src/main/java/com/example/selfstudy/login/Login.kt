package com.example.selfstudy.login

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.selfstudy.MainActivity
import com.example.selfstudy.databinding.ActivityLoginBinding
import com.example.selfstudy.login.signup.MySharedPreferences
import com.example.selfstudy.login.signup.SignUp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.GsonBuilder
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Login : AppCompatActivity() {

    lateinit var binding : ActivityLoginBinding
    var mGoogleSignInClient : GoogleSignInClient? = null
    var googleLoginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == -1) {
            val data = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            getGoogleInfo(task)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.kakaoBtn.setOnClickListener(){
            kakaologin(this)
        }

        binding.googleBtn.setOnClickListener(){
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            googleLogin()
        }
        binding.signUpBtn.setOnClickListener(){
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener{
            finish()
            //Login()
        }

    }
    fun Login() {
        val userID = binding.inputId.text.toString()
        val userPW = binding.inputPassword.text.toString()
        val loginInfo = LoginInfo(userID, userPW)
        postLogin(loginInfo)
    }
    fun kakaologin(context: Context){
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(ContentValues.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                finish()
            }
        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(ContentValues.TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(ContentValues.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    finish()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
    fun googleLogin() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        googleLoginLauncher.launch(signInIntent)
    }
    fun getGoogleInfo(completedTask: Task<GoogleSignInAccount>) {
        try {
            val TAG = "구글 로그인 결과"
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, account.id!!)
            Log.d(TAG, account.familyName!!)
            Log.d(TAG, account.givenName!!)
            Log.d(TAG, account.email!!)
        }
        catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    fun postLogin(loginInfo: LoginInfo) {
        val gson = GsonBuilder().create()
        val str = gson.toJson(loginInfo)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"로그인 실패", Toast.LENGTH_SHORT).show()
                }
                Log.e("fail to login",e.message.toString())
                Log.e("fail to login",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {

                if(response.isSuccessful) {
                    val tmpname = response.body.string()
                    val username = gson.fromJson(tmpname, String::class.java)

                    MySharedPreferences.setUserId(applicationContext, binding.inputId.text.toString())
                    MySharedPreferences.setUserPw(applicationContext, binding.inputPassword.text.toString())

                    Log.i("Success", response.message)
                    Log.i("Success", response.toString())
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Log.e("connection error",response.body.toString())
                }
            }
        })
    }
}
data class LoginInfo(val userId: String, var password: String) {
}