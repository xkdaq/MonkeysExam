package top.monkeys.exam.activity

import android.annotation.SuppressLint
import androidx.viewpager2.widget.ViewPager2
import top.monkeys.exam.adapter.QuizPagerAdapter
import top.monkeys.exam.base.BaseActivity
import top.monkeys.exam.bean.QuizQuestion
import top.monkeys.exam.databinding.ActivityMainBinding
import top.monkeys.exam.ext.parseQuizQuestionsFromJson


/**
 * 需求：
 * 1.假设有100道题，一个题目集合后台下发给我，其中题目包含单选题和多选题，我要实现可以左右滑动刷题的效果。
 * 2.每个页面显示一道题目，默认显示题干和4个选项，用户选择完之后显示正确答案和所选答案，以及题目解析。
 * 3.如果题目为单选题，用户选择之后，如果正确就显示正确选项颜色为绿色，如果错误，所选选项显示为红色，正确选项显示为绿色
 * 4.如果题目为多选题，用户选择完选项之后，点击一个确认按钮，同样的如果正确就显示全部正确选项颜色为绿色，如果错选，错误选项标为红色，正确选项标为绿色，漏选选项标为灰色。
 */


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var quizPagerAdapter: QuizPagerAdapter

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    var quizQuestions: MutableList<QuizQuestion> = mutableListOf()

    override fun initView() {
        val list: List<QuizQuestion> = parseQuizQuestionsFromJson(this, "questions.json")
        quizQuestions.clear()
        quizQuestions.addAll(list)
        // 初始化 QuizPagerAdapter
        quizPagerAdapter = QuizPagerAdapter(quizQuestions)
        // 设置适配器给 ViewPager
        binding.viewPager.adapter = quizPagerAdapter
        binding.viewPager.offscreenPageLimit = quizQuestions.size
        // 设置页面变化的回调
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 这里可以获取到当前页面的索引
                handlePageSelected(position)
            }
        })

    }

    @SuppressLint("SetTextI18n")
    private fun handlePageSelected(position: Int) {
        // 处理当前页面被选中的逻辑
        binding.nowPositionNum.text = "${(position + 1)}"
        binding.nowTotalNum.text = "${(quizQuestions.size)}"
    }

}
