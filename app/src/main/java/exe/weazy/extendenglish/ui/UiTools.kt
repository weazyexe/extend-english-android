package exe.weazy.extendenglish.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class UiTools {
    companion object {
        fun hideKeyboard(view: View?, context: Context?) {
            val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        fun hideView(view : View?) {
            view?.alpha = 1f
            view?.animate()
                ?.alpha(0f)
                ?.setDuration(500L)
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        view.visibility = View.GONE
                    }
                })
        }

        fun showView(view : View?) {
            view?.alpha = 0f
            view?.visibility = View.VISIBLE
            view?.animate()
                ?.alpha(1f)
                ?.setDuration(500L)
                ?.setListener(null)
        }
    }
}