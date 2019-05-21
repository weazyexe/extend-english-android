package exe.weazy.extendenglish.entity

enum class LearnProgress(name : String) {
    REPEAT_LONG("repeatLong"), REPEAT_FOUR_DAYS("repeatFourDays"),
    REPEAT_THREE_DAYS("repeatThreeDays"), REPEAT_TWO_DAYS("repeatTwoDays"),
    REPEAT_YESTERDAY("repeatYesterday"), LEARN_TODAY("learnToday"), LEARNED("learned");

    companion object {
        fun getLearnProgressByString(str : String) = LearnProgress.values().find { it.name == str }
    }
}