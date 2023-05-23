package com.example.selfstudy.login.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.selfstudy.databinding.ActivitySignUpBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Url
import java.io.IOException
import java.util.regex.Pattern

class SignUp : AppCompatActivity() {
    private val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,15}\$"
    var checkID_finish = false
    private val idPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})\$"
    lateinit var binding : ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.inputPassword.addTextChangedListener(object : TextWatcher {           // 비밀번호 기입란
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {              // 비밀번호 조건 안 맞는 경우
                val password = p0?.trim().toString() // 공백 제거
                binding.IsTextPassword.text = when (patternCheckPw(password)) {
                    true -> "사용 가능한 비밀번호"
                    else -> "양식이 맞지 않는 비밀번호"
                }
                binding.IsTextPassword.text = ""
            }
        })
        var samePW : Boolean
        binding.inputRePassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(checkPassword: Editable?) {
                samePW = binding.inputPassword.text.toString() == binding.inputRePassword.text.toString()
                if (samePW) {
                    binding.textPassword.setText("비밀번호가 일치합니다")

                } else {
                    binding.textPassword.setText("비밀번호가 일치하지 않습니다")
                }
                if (binding.inputPassword.length() < 8) {
                    binding.textPassword.setText("")
                }
            }
        })
        binding.checkName.setOnClickListener {
            val userID = binding.inputId.text.toString()
            val information = UserInfo(userID,"",null)
            postCheckId(information)
            binding.inputId.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    checkID_finish = false
                    binding.textCheckName.setText("중복확인을 해주세요")
                }
            })
        }

        binding.UserInfoComplete.setOnClickListener {
            samePW = binding.inputPassword.text.toString()==(binding.inputRePassword.text.toString())
            if((patternCheckPw(binding.inputPassword.text.toString()))&& samePW && (checkID_finish)
                && (binding.inputId.length()>=2)) { // && (inputNumber.length() == 11)
                val userID = binding.inputId.getText().toString()
                val userPW = binding.inputPassword.getText().toString()
                val information = UserInfo(userID, userPW, null)
                Log.d("information",information.toString())
                postUser(information)
            }else if(!checkID_finish) {
                Toast.makeText(applicationContext,"아이디 중복확인을 해주세요",Toast.LENGTH_SHORT).show()
            }
            else{
                    binding.textCheckName.setText("")
                    if(!patternCheckPw(binding.inputPassword.text.toString())){
                        Toast.makeText(applicationContext,"checkPw",Toast.LENGTH_SHORT).show()
                    }
                    else if(samePW == false){
                        Toast.makeText(applicationContext,"samePw",Toast.LENGTH_SHORT).show()
                    }

                Toast.makeText(applicationContext,"가입 조건에 맞게 입력해 주세요",Toast.LENGTH_SHORT).show()
            }
        }
        binding.UserInfoCancel.setOnClickListener{
            finish()
        }

    }



    fun patternCheckPw(pw : String):Boolean{
        val p = Pattern.matches(pwPattern, pw)      // 패턴 일치여부 확인
        return p
    }
    fun patternCheckId(id : String) :Boolean {
        val d = Pattern.matches(idPattern, id)      // 패턴 일치여부 확인
        return d
    }
    fun postUser(information: UserInfo) {
        val gson = GsonBuilder().create()
        val str = gson.toJson(information)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
//                Toast.makeText(applicationContext,"회원가입 실패",Toast.LENGTH_SHORT).show()
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"실패", Toast.LENGTH_SHORT).show()
                }
                Log.e("fail",e.message.toString())
                Log.e("fail",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                // Toast.makeText(applicationContext,"회원가입 성공",Toast.LENGTH_SHORT).show()
                if(response.isSuccessful) {
                    Log.i("Success", response.message)
                    Log.i("Success", response.toString())
                    finish()
                }
                else{
                    Log.e("connection error",response.body.toString())
                }
            }
        })
    }
    fun postCheckId(information: UserInfo) {
        val gson = GsonBuilder().create()
        val str = gson.toJson(information)
        val okHttpClient = OkHttpClient()
        val requestBody = str.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.Main).launch{
                    Toast.makeText(applicationContext,"연결 실패", Toast.LENGTH_SHORT).show()
                }
                Log.e("fail",e.message.toString())
                Log.e("fail",e.toString())
            }
            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful) {
                    Log.i("Success", response.message)
                    Log.i("Success", response.toString())
                    val final_Id = binding.inputId.text.toString().trim() // 공백제거
                    CoroutineScope(Dispatchers.Main).launch {
                        when (patternCheckId(final_Id)) {
                            true -> {
                                binding.textCheckName.text = "사용 가능한 이메일"
                                checkID_finish = true
                            }
                            else -> {
                                binding.textCheckName.text = "올바른 형식의 이메일을 입력해주세요"
                                checkID_finish = false
                            }
                        }
                    }
                }
                else{
                    Log.e("connection error",response.body.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.textCheckName.text = "사용중인 이메일입니다"
                        checkID_finish = false
                    }
                }
            }
        })
    }
}
data class UserInfo(var userId: String,
                       var password: String,
                       var userImage : Url?,
                       ) {
}