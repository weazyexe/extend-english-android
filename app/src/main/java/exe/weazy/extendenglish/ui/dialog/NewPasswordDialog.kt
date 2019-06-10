package exe.weazy.extendenglish.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import exe.weazy.extendenglish.R
import kotlinx.android.synthetic.main.activity_user.*

class NewPasswordDialog : AppCompatDialogFragment() {

    private val auth = FirebaseAuth.getInstance()

    private lateinit var new : TextInputEditText
    private lateinit var repeat : TextInputEditText

    private lateinit var newString : String
    private lateinit var repeatString : String

    private lateinit var listener: NewPasswordDialogListener


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        val view = inflater?.inflate(R.layout.dialog_change_password, null)

        builder.setView(view).setTitle(R.string.change_password)
            .setPositiveButton(R.string.save) { _, _ ->
                newString = new.text.toString()
                repeatString = repeat.text.toString()

                listener.applyPassword(newString, repeatString)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->

            }

        new = view!!.findViewById(R.id.edittext_new)
        repeat = view.findViewById(R.id.edittext_repeat)

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as NewPasswordDialogListener
    }

    interface NewPasswordDialogListener {
        fun applyPassword(password : String, repeat : String)
    }
}