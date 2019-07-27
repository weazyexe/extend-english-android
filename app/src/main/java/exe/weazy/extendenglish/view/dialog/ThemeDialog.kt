package exe.weazy.extendenglish.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.textfield.TextInputLayout
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.entity.AppSettings
import exe.weazy.extendenglish.entity.Theme

class ThemeDialog : AppCompatDialogFragment() {

    private lateinit var enableRadio : RadioButton
    private lateinit var disableRadio : RadioButton
    private lateinit var autoRadio : RadioButton
    private lateinit var themeRadioGroup : RadioGroup

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_theme, null)

        builder.setView(view).setTitle(R.string.setup_dark_theme)
            /*.setPositiveButton(R.string.save) { _, _ ->
                when {
                    enableRadio.isChecked -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        AppSettings.theme = Theme.NIGHT
                    }

                    disableRadio.isChecked -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        AppSettings.theme = Theme.DAY
                    }

                    autoRadio.isChecked -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        AppSettings.theme = Theme.AUTO
                    }
                }

                dismiss()
            }
            .setNegativeButton(R.string.cancel) { _, _ ->

            }*/
        val editTextLayout = view?.findViewById<TextInputLayout>(R.id.layout_text)
        editTextLayout?.hint = getString(R.string.new_email)

        enableRadio = view!!.findViewById(R.id.radio_enable)
        disableRadio = view.findViewById(R.id.radio_disable)
        autoRadio = view.findViewById(R.id.radio_auto)
        themeRadioGroup = view.findViewById(R.id.radio_group_theme)

        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                enableRadio.isChecked = true
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                disableRadio.isChecked = true
            }

            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> {
                autoRadio.isChecked = true
            }

            else -> {

            }
        }

        themeRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            val checkedRadioButton = radioGroup.findViewById<RadioButton>(i)

            when (checkedRadioButton.tag) {
                "enable" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    AppSettings.theme = Theme.NIGHT
                }

                "disable" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    AppSettings.theme = Theme.DAY
                }

                "auto" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    AppSettings.theme = Theme.AUTO
                }
            }
        }

        return builder.create()
    }
}