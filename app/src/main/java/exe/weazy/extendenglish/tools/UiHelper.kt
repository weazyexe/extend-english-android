package exe.weazy.extendenglish.tools

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.NotificationCompat
import exe.weazy.extendenglish.R

class UiHelper {
    companion object {
        /**
         * Hide keyboard
         */
        fun hideKeyboard(view: View?, context: Context?) {
            val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        /**
         * Set view's visibility to GONE with cross fade animation
         */
        fun hideView(view : View?) {
            view?.alpha = 1f
            view?.animate()
                ?.alpha(0f)
                ?.setDuration(500L)
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = View.GONE
                    }
                })
        }

        /**
         * Set view's visibility to VISIBLE with cross fade animation
         */
        fun showView(view : View?) {
            view?.alpha = 0f
            view?.visibility = View.VISIBLE
            view?.animate()
                ?.alpha(1f)
                ?.setDuration(500L)
                ?.setListener(null)
        }

        fun showNotification(context: Context, title : String, text : String) {
            val builder = NotificationCompat.Builder(context, "notify_001")
                .setSmallIcon(R.drawable.logo1)
                .setContentTitle(title)
                .setContentText(text)

            val notification = builder.build()

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    "notify_001",
                    "Repeat reminder",
                    NotificationManager.IMPORTANCE_DEFAULT)

                nManager.createNotificationChannel(channel)
            }

            nManager.notify(1, notification)
        }
    }
}