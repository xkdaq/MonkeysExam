package top.monkeys.exam.ext

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import top.monkeys.exam.BuildConfig
import top.monkeys.exam.bean.QuestionsResponse
import top.monkeys.exam.bean.QuizQuestion
import java.io.IOException
import java.io.InputStreamReader


fun getAnswerIndices(answer: String): List<Int> {
    if (answer.isEmpty()) return emptyList()
    val answerMap = mapOf(
        'A' to 0,
        'B' to 1,
        'C' to 2,
        'D' to 3
    )
    // 将答案字符串转换为字符集合，以处理类似“AD”和“DA”的情况
    val uniqueChars = answer.toUpperCase().toSet()
    // 将每个字符映射到其对应的索引
    return uniqueChars.mapNotNull { answerMap[it] }.sorted()
}

fun indicesToAnswer(indices: List<Int>): String {
    val indexMap = mapOf(
        0 to 'A',
        1 to 'B',
        2 to 'C',
        3 to 'D'
    )
    return indices
        .mapNotNull { indexMap[it] }
        .sorted()
        .joinToString("")
}

fun parseQuizQuestionsFromJson(context: Context, fileName: String): List<QuizQuestion> {

    val simpleQuestionsList = mutableListOf<QuizQuestion>()

    val gson = Gson()
    val type = object : TypeToken<QuestionsResponse>() {}.type
    val jsonString = loadJsonFromAssets(context, fileName)
    val questionsResponse: QuestionsResponse = gson.fromJson(jsonString, type)

    if (!questionsResponse.data.isNullOrEmpty()) {
        for (item in questionsResponse.data) {
            val simpleQuestion = QuizQuestion(
                id = item.sId,
                questionText = item.sQuestion,
                options = listOf(
                    "A." + item.itemA,
                    "B." + item.itemB,
                    "C." + item.itemC,
                    "D." + item.itemD
                ),
                correctAnswers = getAnswerIndices(item.answer),
                isMultipleChoice = false,
                explanation = item.sExplain
            )
            simpleQuestionsList.add(simpleQuestion)
        }
        return simpleQuestionsList
    }
    return listOf()
}

private fun loadJsonFromAssets(context: Context, fileName: String): String? {
    return try {
        context.assets.open(fileName).use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                reader.readText()
            }
        }
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}

fun String.tolog() {
    if (BuildConfig.DEBUG) {
        Log.e("xxxxxx", "=====$this")
    }
}
