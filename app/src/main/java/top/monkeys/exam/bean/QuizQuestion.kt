package top.monkeys.exam.bean

data class QuizQuestion(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswers: List<Int>,
    val isMultipleChoice: Boolean,
    val explanation: String
)
