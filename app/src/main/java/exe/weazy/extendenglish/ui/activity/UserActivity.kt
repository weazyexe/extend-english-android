package exe.weazy.extendenglish.ui.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.ui.dialog.TextDialog
import kotlinx.android.synthetic.main.activity_user.*


class UserActivity : AppCompatActivity(), TextDialog.TextDialogListener {

    private lateinit var level : String
    private lateinit var type : String

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        level = intent.getStringExtra("level")
    }

    override fun onStart() {
        super.onStart()

        text_username.text = if (auth.currentUser?.displayName.isNullOrEmpty()) {
            auth.currentUser?.email
        } else {
            auth.currentUser?.displayName
        }

        text_level.text = level
    }

    override fun applyText(text: String) {
        val bar = Snackbar.make(layout_user, R.string.loading, Snackbar.LENGTH_INDEFINITE)
        val contentLay = bar.view.findViewById<View>(R.id.snackbar_text).parent as LinearLayout
        val item = ProgressBar(this)

        contentLay.gravity = Gravity.CENTER_VERTICAL
        contentLay.addView(item, 70, 70)
        bar.show()

        if (type == "Email") {
            auth.currentUser?.updateEmail(text)?.addOnCompleteListener {
                bar.dismiss()
                Snackbar.make(layout_user, R.string.email_has_been_changed, Snackbar.LENGTH_LONG).show()
                updateUsername()
            }
        } else {
            val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(text).build()

            auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener {
                bar.dismiss()
                Snackbar.make(layout_user, R.string.username_has_been_changed, Snackbar.LENGTH_LONG).show()
                updateUsername()
            }
        }
    }

    fun onEmailChangeClickButton(view : View) {
        type = "Email"
        val dialog = TextDialog(type)
        dialog.show(supportFragmentManager, "Email change dialog")
    }

    fun onUsernameChangeClickButton(view : View) {
        type = "Username"
        val dialog = TextDialog(type)
        dialog.show(supportFragmentManager, "Username change dialog")
    }

    fun onPasswordChangeClickButton(view : View) {

    }

    fun onLogOutButtonClick(v: View) {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(R.string.logout_message)
            .setNegativeButton(R.string.no) { _, _ ->

            }
            .setPositiveButton(R.string.yes) { _, _ ->
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        dialog.show()
    }

    private fun updateUsername() {
        text_username.text = if (auth.currentUser?.displayName.isNullOrEmpty()) {
            auth.currentUser?.email
        } else {
            auth.currentUser?.displayName
        }
    }
}
