package exe.weazy.extendenglish.entity

import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName

enum class Category(name : String) {
    BASICS("BASICS"), TIME("TIME"), HOUSE("HOUSE"), CITY("CITY"),
    ANIMALS("ANIMALS"), COMPUTER("COMPUTER"), CLOTHES("CLOTHES"),
    FAMILY("FAMILY"), CHARACTER("CHARACTER");

    companion object {
        fun getCategoryByString(str : String) = valueOf(str.toUpperCase())
    }
}