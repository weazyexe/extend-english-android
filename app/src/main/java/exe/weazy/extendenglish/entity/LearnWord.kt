package exe.weazy.extendenglish.entity

class LearnWord {
    lateinit var word: String
    lateinit var translate: String
    lateinit var category: Category

    constructor()

    constructor(word : String, translate : String, category: Category) {
        this.word = word
        this.translate = translate
        this.category = category
    }
}