package com.yourname.jetbetcollector

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class JetBetCollectorService : AccessibilityService() {

    // لیستی برای ذخیره اعداد استخراج شده (برای تحلیل الگو)
    private val numberHistory = mutableListOf<Int>()

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val rootNode = rootInActiveWindow ?: return

        // جستجو در تمام عناصر صفحه برای یافتن اعداد
        findNumbersInNodes(rootNode)
        
        rootNode.recycle()
    }

    private fun findNumbersInNodes(node: AccessibilityNodeInfo?) {
        if (node == null) return

        // اگر عنصر دارای متن باشد، چک می‌کنیم آیا عدد است یا خیر
        node.text?.let { text ->
            val textString = text.toString()
            // بررسی اینکه آیا متن شامل یک عدد بین 0 تا 99 است
            val number = textString.filter { it.isDigit() }.toIntOrNull()
            
            if (number != null && number in 0..99) {
                processNewNumber(number)
            }
        }

        // بررسی فرزندان این عنصر (عمق جستجو در صفحه)
        for (i in 0 until node.childCount) {
            findNumbersInNodes(node.getChild(i))
        }
    }

    private fun processNewNumber(number: Int) {
        // جلوگیری از ثبت اعداد تکراری پشت سر هم (اگر عدد ثابت مانده باشد)
        if (numberHistory.isEmpty() || numberHistory.last() != number) {
            numberHistory.add(number)
            
            // محدود کردن لیست به مثلاً ۱۰۰ عدد آخر برای جلوگیری از پر شدن حافظه
            if (numberHistory.size > 100) {
                numberHistory.removeAt(0)
            }

            Log.d("JetBetBot", "عدد جدید شناسایی شد: $number")
            Log.d("JetBetBot", "تاریخچه اعداد: $numberHistory")
            
            // در اینجا در آینده می‌توانیم تابع تحلیل الگو را صدا بزنیم
            analyzePattern()
        }
    }

    private fun analyzePattern() {
        if (numberHistory.size < 3) return
        
        // یک تحلیل ساده برای تست: بررسی اینکه آیا اعداد در حال افزایش هستند یا خیر
        val lastThree = numberHistory.takeLast(3)
        if (lastThree[0] < lastThree[1] && lastThree[1] < lastThree[2]) {
            Log.d("JetBetBot", "الگوی صعودی شناسایی شد!")
        }
    }

    override fun onInterrupt() {
        // این متد وقتی سرویس متوقف می‌شود فراخوانی می‌شود
    }
}
