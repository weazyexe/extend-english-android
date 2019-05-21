package exe.weazy.extendenglish.tools

import com.google.common.base.CaseFormat

class StringHelper {
    companion object {
        fun upperSnakeToLowerCamel(string : String) = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, string)
        fun lowerCamelToUpperSnake(string : String) = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, string)
    }
}