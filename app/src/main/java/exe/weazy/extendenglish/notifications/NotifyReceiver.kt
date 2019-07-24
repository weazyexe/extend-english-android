package exe.weazy.extendenglish.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotifyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        /*if (intent.action == "BOOT_COMPLETED") {
            val serviceIntent = Intent(context, NotifyService::class.java)
            serviceIntent.putExtra("timestamp", -1L)
            context.startService(serviceIntent)
        }
        if (intent.action == "ALARM_MANAGER_SET") {
            val serviceIntent = Intent(context, NotifyService::class.java)
            serviceIntent.putExtra("timestamp", intent.getLongExtra("timestamp", -1L))
            context.startService(serviceIntent)
        }*/
    }
}
