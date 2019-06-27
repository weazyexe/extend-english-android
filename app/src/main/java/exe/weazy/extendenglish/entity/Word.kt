package exe.weazy.extendenglish.entity

class Word {
    lateinit var word: String
    lateinit var transcription : String
    lateinit var translate: String
    lateinit var category: Category

    constructor()

    constructor(word : String, translate : String, transcription : String, category: Category) {
        this.word = word
        this.translate = translate
        this.transcription = transcription
        this.category = category
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Word) return false

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