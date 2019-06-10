package exe.weazy.extendenglish.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import exe.weazy.extendenglish.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, null)
    }

    override fun onStart() {
        super.onStart()

        switch_enable_dark_mode.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        button_enable_notifications.setOnClickListener {
            switch_enable_notifications.isChecked = !switch_enable_notifications.isChecked
        }

        button_enable_dark_mode.setOnClickListener {
            switch_enable_dark_mode.isChecked = !switch_enable_dark_mode.isChecked
        }

        switch_enable_dark_mode.setOnCheckedChangeListener { compoundButton, enabled ->
            if (enabled) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}