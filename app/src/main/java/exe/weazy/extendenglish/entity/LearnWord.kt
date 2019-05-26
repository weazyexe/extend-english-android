package exe.weazy.extendenglish.entity

import android.os.Parcel
import android.os.Parcelable

class LearnWord : Parcelable {
    lateinit var word: String
    lateinit var transcription : String
    lateinit var translate: String
    lateinit var category: Category

    constructor(parcel: Parcel) : this() {
        word = parcel.readString()
        translate = parcel.readString()
        transcription = parcel.readString()
    }

    constructor()

    constructor(word : String, translate : String, transcription : String, category: Category) {
        this.word = word
        this.translate = translate
        this.transcription = transcription
        this.category = category
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(word)
        parcel.writeString(translate)
        parcel.writeString(transcription)
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

    override fun equals(other: Any?): Boolean {
        if (other !is LearnWord) return false

        if (this.word == other.word && this.translate == other.translate && this.transcription == other.transcription && this.category == other.category) return true

        return false
    }

    override fun toString(): String {
        return "${this.word} | ${this.transcription} | ${this.translate} | ${this.category}"
    }

    override fun hashCode(): Int {
        var result = word.hashCode()
        result = 31 * result + transcription.hashCode()
        result = 31 * result + translate.hashCode()
        result = 31 * result + category.hashCode()
        return result
    }
}