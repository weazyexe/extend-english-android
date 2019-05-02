package exe.weazy.extendenglish.entity

enum class Category(name : String) {
    BASICS("BASICS"), TIME("TIME"), HOUSE("HOUSE"), CITY("CITY"),
    ANIMALS("ANIMALS"), COMPUTER("COMPUTER"), CLOTHES("CLOTHES"),
    FAMILY("FAMILY"), CHARACTER("CHARACTER"), FOOD("FOOD");

    companion object {
        fun getCategoryByString(str : String) = valueOf(str)
    }
}