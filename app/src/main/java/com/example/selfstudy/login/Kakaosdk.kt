package com.example.selfstudy.login

import android.app.Application
import com.example.selfstudy.R
import com.kakao.sdk.common.KakaoSdk

class Kakaosdk : Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))    }
}