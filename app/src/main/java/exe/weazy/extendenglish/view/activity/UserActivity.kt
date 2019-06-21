package exe.weazy.extendenglish.view.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.tools.GlideApp
import exe.weazy.extendenglish.view.dialog.AvatarDialog
import exe.weazy.extendenglish.view.dialog.NewPasswordDialog
import exe.weazy.extendenglish.view.dialog.TextDialog
import kotlinx.android.synthetic.main.activity_user.*


class UserActivity : AppCompatActivity(), TextDialog.TextDialogListener, NewPasswordDialog.NewPasswordDialogListener,
        AvatarDialog.AvatarDialogListener {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var level : String
    private lateinit var type : String
    private var avatarPath = "default_avatars/placeholder.png"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        level = intent.getStringExtra("level")
        avatarPath = intent.getStringExtra("avatar_path")
    }

    override fun onStart() {
        super.onStart()

        text_username.text = if (auth.currentUser?.displayName.isNullOrEmpty()) {
            auth.currentUser?.email
        } else {
            auth.currentUser?.displayName
        }

        text_level.text = level

        setAvatar()

        card_avatar.setOnClickListener {
            val dialog = AvatarDialog()
            dialog.show(supportFragmentManager, "Avatar change dialog")
        }
    }

    override fun applyPassword(password : String, repeat : String) {
        if (password == repeat) {
            val bar = Snackbar.make(layout_user, R.string.loading, Snackbar.LENGTH_INDEFINITE)
            val contentLay = bar.view.findViewById<View>(R.id.snackbar_text).parent as LinearLayout
            val item = ProgressBar(this)

            contentLay.gravity = Gravity.CENTER_VERTICAL
            contentLay.addView(item, 70, 70)
            bar.show()

            auth.currentUser?.updatePassword(password)?.addOnCompleteListener {
                bar.dismiss()
                Snackbar.make(layout_user, R.string.password_has_been_changed, Snackbar.LENGTH_LONG).show()
            }
        } else {
            Snackbar.make(layout_user, R.string.passwords_do_not_match, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun applyText(text : String) {
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
        type = "Password"
        val dialog = NewPasswordDialog()
        dialog.show(supportFragmentManager, "Password change dialog")
    }

    fun onLogOutButtonClick(v: View) {
        val dialog = AlertDialog.Builder(this)
        dialog
            .setTitle(R.string.logout)
            .setMessage(R.string.logout_message)
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

    fun onBackButtonClick(view : View) {
        onBackPressed()
    }

    private fun updateUsername() {
        text_username.text = if (auth.currentUser?.displayName.isNullOrEmpty()) {
            auth.currentUser?.email
        } else {
            auth.currentUser?.displayName
        }
    }

    private fun setAvatar() {

        val ref = storage.getReference(avatarPath)

        GlideApp.with(this)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(imageview_avatar)

    }

    override fun applyAvatar(chosen: String) {
        val ref = storage.getReference(chosen)

        GlideApp.with(this)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(imageview_avatar)

        firestore.document("users/${auth.currentUser?.uid}").update("avatar", chosen)
    }
}
