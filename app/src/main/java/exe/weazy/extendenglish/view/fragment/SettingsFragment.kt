package exe.weazy.extendenglish.view.fragment

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.view.activity.AboutActivity
import exe.weazy.extendenglish.view.dialog.ThemeDialog
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, null)
    }

    override fun onStart() {
        super.onStart()

        button_dark_theme.setOnClickListener {
            val dialog = ThemeDialog()
            dialog.show(activity!!.supportFragmentManager, "Theme Switch")
        }

        button_about.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(activity as Activity,
                Pair.create<View, String>(about_card, "trtext_about"))

            startActivity(intent, options.toBundle())
        }
    }
}