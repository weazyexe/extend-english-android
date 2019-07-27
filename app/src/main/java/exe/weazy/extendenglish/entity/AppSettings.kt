package exe.weazy.extendenglish.entity

import android.content.SharedPreferences

class AppSettings {
    companion object {
        private lateinit var pref: SharedPreferences
        var theme : Theme = Theme.DAY
            set(value) {
                field = value
                pref.edit().putString("theme", theme.name).apply()
            }


        fun load(pref : SharedPreferences) {
            this.pref = pref
            theme = Theme.valueOf(pref.getString("theme", "DAY")!!)
        }

    }
}