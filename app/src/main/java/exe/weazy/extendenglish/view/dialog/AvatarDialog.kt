package exe.weazy.extendenglish.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialogFragment
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import exe.weazy.extendenglish.R
import exe.weazy.extendenglish.tools.GlideApp

class AvatarDialog : AppCompatDialogFragment() {

    private var storage = FirebaseStorage.getInstance()
    private lateinit var listener : AvatarDialogListener

    private val PLACEHOLDER = "default_avatars/placeholder.png"
    private val ASSASSIN = "default_avatars/assassin.jpg"
    private val BOX = "default_avatars/box.jpg"
    private val KOLXOZ = "default_avatars/kolxoz.jpg"
    private val OLEG = "default_avatars/oleg.jpg"
    private val HAT = "default_avatars/hat.jpg"


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater

        val view = inflater?.inflate(R.layout.dialog_avatars, null)

        builder.setView(view).setTitle(R.string.change_avatar)

        initListeners(view!!)

        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as AvatarDialogListener
    }

    private fun initListeners(view : View) {
        val avatar1 = view.findViewById<CircleImageView>(R.id.imageview_avatar1)
        val avatar2 = view.findViewById<CircleImageView>(R.id.imageview_avatar2)
        val avatar3 = view.findViewById<CircleImageView>(R.id.imageview_avatar3)
        val avatar4 = view.findViewById<CircleImageView>(R.id.imageview_avatar4)
        val avatar5 = view.findViewById<CircleImageView>(R.id.imageview_avatar5)
        val avatar6 = view.findViewById<CircleImageView>(R.id.imageview_avatar6)


        var ref = storage.getReference(PLACEHOLDER)

        GlideApp.with(this)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(avatar1)

        ref = storage.getReference(ASSASSIN)

        GlideApp.with(this)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(avatar2)

        ref = storage.getReference(KOLXOZ)

        GlideApp.with(this)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(avatar3)

        ref = storage.getReference(OLEG)

        GlideApp.with(this)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(avatar4)

        ref = storage.getReference(BOX)

        GlideApp.with(this)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(avatar5)

        ref = storage.getReference(HAT)

        GlideApp.with(this)
            .load(ref)
            .placeholder(R.drawable.avatar_placeholder)
            .into(avatar6)



        avatar1.setOnClickListener {
            listener.applyAvatar(PLACEHOLDER)
            dismiss()
        }

        avatar2.setOnClickListener {
            listener.applyAvatar(ASSASSIN)
            dismiss()
        }

        avatar3.setOnClickListener {
            listener.applyAvatar(KOLXOZ)
            dismiss()
        }

        avatar4.setOnClickListener {
            listener.applyAvatar(OLEG)
            dismiss()
        }

        avatar5.setOnClickListener {
            listener.applyAvatar(BOX)
            dismiss()
        }

        avatar6.setOnClickListener {
            listener.applyAvatar(HAT)
            dismiss()
        }
    }

    interface AvatarDialogListener {
        fun applyAvatar(chosen : String)
    }
}