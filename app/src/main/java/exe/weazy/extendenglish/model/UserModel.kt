package exe.weazy.extendenglish.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.arch.UserContract
import exe.weazy.extendenglish.presenter.UserPresenter

class UserModel(private var presenter : UserPresenter) : UserContract.Model {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun writeAvatar(path: String) {
        firestore.document("users/${auth.currentUser?.uid}").update("avatar", path)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    presenter.onAvatarUpdateFinished()
                } else {
                    presenter.onAvatarUpdateFailure()
                }
            }
    }

    override fun writeUsername(username: String) {
        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()

        auth.currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener {
            if (it.isSuccessful) {
                presenter.onUsernameUpdateFinished()
            } else {
                presenter.onUsernameUpdateFailure()
            }
        }
    }

    override fun writeEmail(email: String) {
        auth.currentUser?.updateEmail(email)?.addOnCompleteListener {
            if (it.isSuccessful) {
                presenter.onEmailUpdateFinished()
            } else {
                presenter.onEmailUpdateFailure()
            }
        }
    }

    override fun writePassword(password: String) {
        auth.currentUser?.updatePassword(password)?.addOnCompleteListener {
            if (it.isSuccessful) {
                presenter.onPasswordUpdateFinished()
            } else {
                presenter.onPasswordUpdateFailure()
            }
        }
    }
}