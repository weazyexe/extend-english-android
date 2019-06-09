package exe.weazy.extendenglish.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import exe.weazy.extendenglish.R
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, null)
    }

    override fun onStart() {
        super.onStart()

        button_enable_notifications.setOnClickListener {
            switch_enable_notifications.isChecked = !switch_enable_notifications.isChecked
        }

        button_enable_dark_mode.setOnClickListener {
            switch_enable_dark_mode.isChecked = !switch_enable_dark_mode.isChecked
        }
    }
}