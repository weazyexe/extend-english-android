package exe.weazy.extendenglish.entity

enum class Level(name : String) {
    NEWBIE("Newbie"), ELEMENTARY("Elementary"), INTERMEDIATE("Intermediate"),
    UPPER_INTERMEDIATE("Upper Intermediate"), ADVANCED("Advanced"), PROFICIENCY("Proficiency");

    companion object {
        fun getLevelByString(str : String) = Level.valueOf(str)
    }
}