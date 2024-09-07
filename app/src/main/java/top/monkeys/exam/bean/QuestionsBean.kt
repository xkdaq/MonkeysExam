package top.monkeys.exam.bean

data class QuestionsResponse(
    val total: Int,
    val data: List<QuestionsBean>?
)

data class QuestionsBean(
    val addTime: String,
    val answer: String,
    val car: Any,
    val chapter: Chapter,
    val chapterId: Int,
    val errorCount: Any,
    val itemA: String,
    val itemB: String,
    val itemC: String,
    val itemD: String,
    val knowId: Int,
    val knowPoints: KnowPoints,
    val modTime: String,
    val sCar: String,
    val sDisable: String,
    val sExplain: String,
    val sId: Int,
    val sImg: String,
    val sKm: String,
    val sQuestion: String,
    val sType: String,
    val successCount: Any,
    val userAnswer: String,
    val userFavor: Boolean
)

data class Chapter(
    val chapterCar: String,
    val chapterId: Any,
    val chapterName: String,
    val count: Int,
    val km: String,
    val subjects: Any
)

data class KnowPoints(
    val count: Int,
    val knowCar: Any,
    val knowId: Any,
    val knowKm: Any,
    val knowName: String
)
