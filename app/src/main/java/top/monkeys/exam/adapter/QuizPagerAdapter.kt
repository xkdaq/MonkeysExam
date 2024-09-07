package top.monkeys.exam.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import top.monkeys.exam.R
import top.monkeys.exam.bean.QuizQuestion
import top.monkeys.exam.ext.indicesToAnswer

class QuizPagerAdapter(
    private val questions: List<QuizQuestion>
) : RecyclerView.Adapter<QuizPagerAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_quiz_page, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        val question = questions[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int = questions.size

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val questionText: TextView = itemView.findViewById(R.id.questionText)
        private val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroup)
        private val confirmButton: Button = itemView.findViewById(R.id.confirmButton)

        private val answerView: LinearLayoutCompat = itemView.findViewById(R.id.answerView)
        private val rightAnswer: TextView = itemView.findViewById(R.id.rightAnswer)
        private val yourAnswer: TextView = itemView.findViewById(R.id.yourAnswer)
        private val explanationView: LinearLayoutCompat =
            itemView.findViewById(R.id.explanationView)
        private val explanationText: TextView = itemView.findViewById(R.id.explanationText)

        @SuppressLint("SetTextI18n")
        fun bind(question: QuizQuestion) {
            //题目
            var multipleText = if (question.isMultipleChoice) "多选题" else "单选题"
            val fullText = "<font color='#87CEFA'>($multipleText)</font>" + question.questionText
            questionText.text = Html.fromHtml(fullText, Html.FROM_HTML_MODE_LEGACY)
            //多选确认按钮
            confirmButton.visibility = if (question.isMultipleChoice) View.VISIBLE else View.GONE
            //答案和解释
            answerView.visibility = View.GONE
            explanationView.visibility = View.GONE

            // 动态创建选项按钮
            radioGroup.removeAllViews()
            for ((index, option) in question.options.withIndex()) {
                val radioButton = RadioButton(itemView.context).apply {
                    id = index
                    text = option
                    textSize = 16f
                    setTextColor(ContextCompat.getColor(context, R.color.color_333))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 7, 0, 7)
                    }
                }
                radioGroup.addView(radioButton)
            }

            if (question.isMultipleChoice) {
                confirmButton.setOnClickListener {
                    val selectedOptions = mutableListOf<Int>()
                    for (i in 0 until radioGroup.childCount) {
                        val checkBox = radioGroup.getChildAt(i) as CheckBox
                        if (checkBox.isChecked) {
                            selectedOptions.add(i)
                        }
                    }
                    displayResults(itemView.context, question, selectedOptions)
                }
            } else {
                // 单选题
                radioGroup.setOnCheckedChangeListener { _, checkedId ->
                    displayResults(itemView.context, question, listOf(checkedId))
                }
            }
        }

        private fun displayResults(
            context: Context,
            question: QuizQuestion,
            selectedOptions: List<Int>
        ) {
            // 禁用所有选项防止重复选择
            for (i in 0 until radioGroup.childCount) {
                val view = radioGroup.getChildAt(i)
                view.isEnabled = false
            }

            // 处理每个选项的颜色显示
            for (i in 0 until radioGroup.childCount) {
                val view = radioGroup.getChildAt(i) as TextView
                when {
                    i in question.correctAnswers && i in selectedOptions -> {
                        // 正确选中的选项
                        view.setTextColor(ContextCompat.getColor(context, R.color.color_green))
                    }

                    i !in question.correctAnswers && i in selectedOptions -> {
                        // 错误选中的选项
                        view.setTextColor(ContextCompat.getColor(context, R.color.color_red))
                    }

                    i in question.correctAnswers && i !in selectedOptions -> {
                        // 正确未选中的选项
                        if (question.isMultipleChoice) {
                            //多选
                            view.setTextColor(ContextCompat.getColor(context, R.color.color_888))
                        } else {
                            //单选
                            view.setTextColor(ContextCompat.getColor(context, R.color.color_green))
                        }
                    }
                }
            }
            //显示答案
            answerView.visibility = View.VISIBLE
            rightAnswer.text = indicesToAnswer(question.correctAnswers)
            yourAnswer.text = indicesToAnswer(selectedOptions)

            // 显示解释文本
            explanationView.visibility = View.VISIBLE
            explanationText.text = question.explanation
        }
    }
}
