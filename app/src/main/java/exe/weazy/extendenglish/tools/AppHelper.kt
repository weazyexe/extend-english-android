package exe.weazy.extendenglish.tools

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import exe.weazy.extendenglish.notifications.NotifyReceiver

class AppHelper {
    companion object {
        fun setupNotification(context: Context, time : Long) {
            val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(context.applicationContext, NotifyReceiver::class.java) // AlarmReceiver1 = broadcast receiver

            alarmIntent.putExtra("reason", "notification")
            alarmIntent.putExtra("timestamp", time)
            alarmIntent.action = "ALARM_MANAGER_SET"

            val pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
    }
}