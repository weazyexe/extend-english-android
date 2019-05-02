package exe.weazy.extendenglish.entity

enum class LearnProgress(name : String) {
    REPEAT_LONG("REPEAT_LONG"), REPEAT_FOUR_DAYS("REPEAT_FOUR_DAYS"),
    REPEAT_THREE_DAYS("REPEAT_THREE_DAYS"), REPEAT_TWO_DAYS("REPEAT_TWO_DAYS"),
    REPEAT_YESTERDAY("REPEAT_YESTERDAY"), LEARN_TODAY("LEARN_TODAY"), LEARNED("LEARNED");

    companion object {
        fun getLearnProgressByString(str : String) = LearnProgress.valueOf(str)
    }
}