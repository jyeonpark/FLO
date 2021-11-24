package com.example.flo

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    private var pwd_visible: Boolean = false
    private var pwd_check_visible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupGotoSignupBtn.setOnClickListener {
            if(signUp()) {
                Toast.makeText(this, "회원가입에 성공했습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.signupInputPasswordIv.setOnClickListener {
            if (pwd_visible){ // 비밀번호 안보이게
                binding.signupInputPasswordIv.setImageResource(R.drawable.btn_input_password_off)
                binding.signupPasswordEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                pwd_visible = false
            } else { // 비밀번호 보이게
                binding.signupInputPasswordIv.setImageResource(R.drawable.btn_input_password)
                binding.signupPasswordEt.inputType = InputType.TYPE_CLASS_TEXT
                pwd_visible = true
            }
        }

        binding.signupInputPasswordCheckIv.setOnClickListener {
            if (pwd_check_visible){ // 비밀번호 안보이게
                binding.signupInputPasswordCheckIv.setImageResource(R.drawable.btn_input_password_off)
                binding.signupPasswordCheckEt.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                pwd_check_visible = false
            } else { // 비밀번호 보이게
                binding.signupInputPasswordCheckIv.setImageResource(R.drawable.btn_input_password)
                binding.signupPasswordCheckEt.inputType = InputType.TYPE_CLASS_TEXT
                pwd_check_visible = true
            }
        }

    }

    private fun getUser(): User {
        val email: String = binding.signupIdEt.text.toString() + "@" + binding.signupEmailAddrEt.text.toString()
        val pwd: String = binding.signupPasswordEt.text.toString()
        val name: String = binding.signupUsernameEt.text.toString()

        return User(email, pwd, name)
    }

    private fun signUp(): Boolean{
        if(binding.signupIdEt.text.toString().isEmpty() ||  binding.signupEmailAddrEt.text.toString().isEmpty()){
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.signupPasswordEt.text.toString().isEmpty() ||  binding.signupPasswordCheckEt.text.toString().isEmpty()){
            Toast.makeText(this, "비밀번호 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.signupPasswordEt.text.toString() !=  binding.signupPasswordCheckEt.text.toString()){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            return false
        }

        if(binding.signupUsernameEt.text.toString().isEmpty()){
            Toast.makeText(this, "사용자 이름 형식이 잘못되었습니다.",Toast.LENGTH_SHORT).show()
            return false
        }

        val userDB = SongDatabase.getInstance(this)!!
        userDB.userDao().insert(getUser())

        val users = userDB.userDao().getUsers()
        Log.d("Sign Up Act", users.toString())

        return true
    }
}