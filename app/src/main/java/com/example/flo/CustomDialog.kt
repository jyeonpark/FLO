import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.WindowManager
import android.widget.EditText
import androidx.core.content.ContextCompat.startActivity
import com.example.flo.LoginActivity
import com.example.flo.R
import kotlinx.android.synthetic.main.dialog_custom.*

class CustomDialog(context: Context)
{
    private val dialog = Dialog(context)

    fun showDialog(context: Context)
    {
        dialog.setContentView(R.layout.dialog_custom)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        dialog.dialog_negative_tv.setOnClickListener {
            dialog.dismiss()
        }

        dialog.dialog_positive_tv.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

    }


}