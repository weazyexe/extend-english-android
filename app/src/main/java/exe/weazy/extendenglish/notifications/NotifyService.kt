package exe.weazy.extendenglish.notifications

import android.app.IntentService
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.tools.AppHelper
import exe.weazy.extendenglish.tools.UiHelper

class NotifyService : IntentService("NotifyService") {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onHandleIntent(intent: Intent?) {
        val time = intent?.getLongExtra("timestamp", 0L)

        if (time == -1L) {
            loadLastActivity()
        } else {
            UiHelper.showNotification(this, getString(R.string.words_words), getString(R.string.lets_repeat_and_learn))
        }
    }

    private fun loadLastActivity() {
        firestore.document("users/${auth.currentUser?.uid}").get().addOnCompleteListener { querySnapshot ->
            if (querySnapshot.isSuccessful) {
                val result = querySnapshot.result
                val format = SimpleDateFormat("MM dd yyyy HH:mm")
                format.timeZone = TimeZone.GMT_ZONE

                val time = format.parse(result?.get("lastActivity").toString())
                AppHelper.setupNotification(this, time.time)
            }
        }
    }
}
