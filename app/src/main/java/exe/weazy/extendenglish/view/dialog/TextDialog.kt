package exe.weazy.extendenglish.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import exe.weazy.extendenglish.R

class TextDialog(var type : String) : AppCompatDialogFragment() {

    private lateinit var text : String
    private lateinit var listener : TextDialogListener
    private lateinit var editText : TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_change, null)

        when (type) {
            "Email" -> {
                builder.setView(view).setTitle(R.string.change_email)
                    .setPositiveButton(R.string.save) { _, _ ->
                        text = editText.text.toString()
                        listener.applyText(text)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->

                    }
                val editTextLayout = view?.findViewById<TextInputLayout>(R.id.layout_text)
                editTextLayout?.hint = getString(R.string.new_email)
            }
            else -> {
                builder.setView(view).setTitle(R.string.change_username)
                    .setPositiveButton(R.string.save) { _, _ ->
                        text = editText.text.toString()
                        listener.applyText(text)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->

                    }
                val editTextLayout = view?.findViewById<TextInputLayout>(R.id.layout_text)
                editTextLayout?.hint = getString(R.string.new_username)
            }
        }

        editText = view!!.findViewById(R.id.edittext_text)

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as TextDialogListener
    }

    interface TextDialogListener {
        fun applyText(text : String)
    }
}