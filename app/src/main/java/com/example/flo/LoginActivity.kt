package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    private var pwd_visible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginCloseIv.setOnClickListener {
            finish()
        }

        binding.loginGotoSignupTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.loginGotoLoginBtn.setOnClickListener {
            if(login()) {
                Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()
                startMainActivity()
            }
        }

        binding.loginInputPasswordIv.setOnClickListener {
            if (pwd_visible){ // 비밀번호 안보이게
                binding.loginInputPasswordIv.setImageResource(R.drawable.btn_input_password_off)
                binding.loginPasswordEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                pwd_visible = false
            } else { // 비밀번호 보이게
                binding.loginInputPasswordIv.setImageResource(R.drawable.btn_input_password)
                binding.loginPasswordEt.inputType = InputType.TYPE_CLASS_TEXT
                pwd_visible = true
            }
        }
    }

    private fun login(): Boolean{
        if(binding.loginIdEt.text.toString().isEmpty() ||  binding.loginEmailAddrEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.loginPasswordEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginEmailAddrEt.text.toString()
        val pwd: String = binding.loginPasswordEt.text.toString()

        val songDB = SongDatabase.getInstance(this)!!
        val user = songDB.userDao().getUser(email, pwd)

        user?.let {
            Log.d("LOGINACT/GET_USER", "userId : ${user.id}, $user")
            // 발급받은 jwt 를 저장해주는 함수
            saveJwt(user.id)
            return true
        }

        Toast.makeText(this, "회원정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        binding.loginIdEt.text = null
        binding.loginEmailAddrEt.text = null
        binding.loginPasswordEt.text = null
        return false
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun saveJwt(jwt : Int){
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()

    }
}