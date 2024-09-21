package top.monkeys.exam.activity

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import top.monkeys.exam.base.BaseActivity
import top.monkeys.exam.databinding.ActivityMainBinding
import top.monkeys.exam.ext.tolog
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class AppListActivity : BaseActivity<ActivityMainBinding>() {

    override fun initViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initView() {
        //方法一：getInstalledPackages
        getInstalledPackagesApp(this)
        //方法二：getInstalledApplications
        getInstalledApplicationsApp()
        //方法三：queryIntentActivities
        queryIntentActivitiesApp(this)
        //方法四：pmListPackage
        pmListPackageApp()
    }


    fun getInstalledPackagesApp(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val pm = context.packageManager
            val installedPackages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)
            val jsonArray = JSONArray()
            installedPackages.forEach {
                val jsonObject = JSONObject()
                jsonObject["package"] = "" + it.packageName
                jsonArray.add(jsonObject)
            }
            withContext(Dispatchers.Main) {
                "方法一：getInstalledPackages size======${jsonArray.size}".tolog()
            }

        }
    }


    fun getInstalledApplicationsApp() {
        // 在后台线程执行耗时操作
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val pm: PackageManager = packageManager
                val installedApps: List<ApplicationInfo> =
                    pm.getInstalledApplications(PackageManager.GET_META_DATA)
                val jsonArray = mutableListOf<JSONObject>()

                for (appInfo in installedApps) {
                    val jsonObject = JSONObject()
                    val appName = pm.getApplicationLabel(appInfo).toString() // 获取应用名
                    val packageName = appInfo.packageName // 获取包名

                    jsonObject["app_name"] = appName
                    jsonObject["package"] = packageName
                    jsonArray.add(jsonObject)
                }

                // 切换到主线程更新UI或处理结果
                withContext(Dispatchers.Main) {
                    "方法二：getInstalledApplications size======${jsonArray.size}".tolog()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //-----------


    fun queryIntentActivitiesApp(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val pm = context.packageManager
            val intent = Intent(Intent.ACTION_MAIN)
            //intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val installedPackages = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            val jsonArray = JSONArray()
            installedPackages.forEach {
                val jsonObject = JSONObject()
                jsonObject["package"] = "" + it.activityInfo.packageName
                jsonArray.add(jsonObject)
            }

            withContext(Dispatchers.Main) {
                "方法三：queryIntentActivities size======${installedPackages.size}".tolog()
            }

        }
    }


    fun pmListPackageApp() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val pm = packageManager
                val jsonArray = mutableListOf<JSONObject>()

                val process = Runtime.getRuntime().exec("pm list package")
                val bis = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?

                while (bis.readLine().also { line = it } != null) {
                    val jsonObject = JSONObject()
                    val packageName = line?.replace("package:", "")

                    try {
                        val appInfo = pm.getApplicationInfo(packageName!!, 0)
                        val appName = pm.getApplicationLabel(appInfo).toString()
                        jsonObject["app_name"] = appName
                        jsonObject["package"] = packageName
                        jsonArray.add(jsonObject)
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                }
                withContext(Dispatchers.Main) {
                    "方法四：pmListPackage size======${jsonArray.size}".tolog()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}