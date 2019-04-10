package exe.weazy.extendenglish.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import exe.weazy.extendenglish.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var firebaseAuth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        updateUI(firebaseAuth?.currentUser)
    }

    fun updateUI(user : FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", user)

            startActivity(intent)
        }
    }

    fun onLogInButtonClick(view : View) {
        val email = email_editText.text.toString()
        val password = password_editText.text.toString()

        firebaseAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                updateUI(firebaseAuth?.currentUser)
            } else {
                //Snackbar.make(coordinatorLayout, getString(R.string.wrong_email_or_password), Snackbar.LENGTH_LONG).show()
            }
        }
    }
}
