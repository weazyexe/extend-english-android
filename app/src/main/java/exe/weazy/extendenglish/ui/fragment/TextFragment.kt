package exe.weazy.extendenglish.ui.fragment

import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import exe.weazy.extendenglish.R
import kotlinx.android.synthetic.main.fragment_text.*

class TextFragment : Fragment {

    private lateinit var type : String
    private lateinit var message : String

    constructor()

    constructor(type : String) {
        this.type = type
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_text, null)
    }

    override fun onStart() {
        super.onStart()
        when (type) {
            "E-mail" -> {
                message = getString(R.string.input_email)
                edittext_text.hint = type
                edittext_text.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                text_desc.text = message
            }

            "Password" -> {
                message = getString(R.string.input_password)
                edittext_text.hint = type
//                edittext_text.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                text_desc.text = message
            }

            "Confirm Password" -> {
                message = getString(R.string.confirm_password)
                edittext_text.hint = type
//                edittext_text.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                text_desc.text = message
            }
        }
    }


    fun getText() = edittext_text.text.toString()

    fun getEditText() = edittext_text
}