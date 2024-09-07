package top.monkeys.exam.activity

import android.content.Intent
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import top.monkeys.exam.R
import top.monkeys.exam.base.BaseActivity
import top.monkeys.exam.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    private var animationTime: Long = 2000

    override fun initViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initView() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.ff5)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnimation.duration = animationTime
        rotateAnimation.fillAfter = true
        val scaleAnimation = ScaleAnimation(
            0f, 1f, 0f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = animationTime
        scaleAnimation.fillAfter = true
        val alphaAnimation = AlphaAnimation(0f, 1f)
        alphaAnimation.duration = animationTime
        alphaAnimation.fillAfter = true
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(alphaAnimation)
        binding.logo.startAnimation(animationSet)
        animationSet.setAnimationListener(animationListener)
    }


    private val animationListener = object : Animation.AnimationListener {
        /**
         * onAnimationStart
         * @param animation
         */
        override fun onAnimationStart(animation: Animation) {}

        /**
         * onAnimationEnd
         * @param animation
         */
        override fun onAnimationEnd(animation: Animation) {
            //跳转到登陆界面
            jumpMain()
        }

        /**
         * onAnimationRepeat
         * @param animation
         */
        override fun onAnimationRepeat(animation: Animation) {}
    }

    private fun jumpMain() {
        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()
    }

}
