package exe.weazy.extendenglish.entity

import android.os.Parcel
import android.os.Parcelable

class LearnWord : Parcelable {
    lateinit var word: String
    lateinit var translate: String
    lateinit var category: Category

    constructor(parcel: Parcel) : this() {
        word = parcel.readString()
        translate = parcel.readString()
    }

    constructor()

    constructor(word : String, translate : String, category: Category) {
        this.word = word
        this.translate = translate
        this.category = category
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(word)
        parcel.writeString(translate)
        parcel.writeString(category.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LearnWord> {
        override fun createFromParcel(parcel: Parcel): LearnWord {
            return LearnWord(parcel)
        }

        override fun newArray(size: Int): Array<LearnWord?> {
            return arrayOfNulls(size)
        }
    }
}