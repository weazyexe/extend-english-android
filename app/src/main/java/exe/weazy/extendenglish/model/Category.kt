package exe.weazy.extendenglish.model

enum class Category(name : String) {
    BASICS("BASICS"), TIME("TIME"), HOUSE("HOUSE"), CITY("CITY"),
    ANIMALS("ANIMALS"), COMPUTER("COMPUTER"), CLOTHES("CLOTHES"),
    FAMILY("FAMILY"), CHARACTER("CHARACTER"), FOOD("FOOD"),
    BODY("BODY"), FEELINGS("FEELINGS"), EMOTIONS("EMOTIONS"),
    MONEY("MONEY"), COLORS("COLORS"), ACTIONS("ACTIONS"),
    DRINKS("DRINKS"), COOKING("COOKING"), FRUITS("FRUITS"),
    BERRIES("BERRIES"), VEGETABLES("VEGETABLES"), DISH("DISH"),
    KITCHEN("KITCHEN"), MATERIALS("MATERIALS"), TOOLS("TOOLS"),
    STATIONERY("STATIONERY"), SIZES("SIZES"), NATURE("NATURE"),
    WEATHER("WEATHER"), BIRDS("BIRDS"), INSECTS("INSECTS"),
    FLOWERS("FLOWERS"), TREES("TREES"), TRANSPORT("TRANSPORT"),
    SCHOOL("SCHOOL"), AIRPORT("AIRPORT"), HEALTH("HEALTH"),
    FABRICS("FABRICS"), GEOGRAPHY("GEOGRAPHY"), SPACE("SPACE"),
    SPORT("SPORT"), PROFESSIONS("PROFESSIONS");

    companion object {
        fun getCategoryByString(str : String) = valueOf(str)
    }
}