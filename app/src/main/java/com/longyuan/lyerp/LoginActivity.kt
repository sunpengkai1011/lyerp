package com.longyuan.lyerp

import android.widget.Toast
import com.longyuan.lyerp.network.SignInRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(){

    override fun initView() {
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener{
            SignInRequest(this, etUsername.text.toString(), etPassword.text.toString())
                .getData().subscribe ( {
                    Toast.makeText(this, "${it.username} login success", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show()
                }  )
        }
    }

    override fun initData() {
    }
}