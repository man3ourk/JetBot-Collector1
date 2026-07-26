package com.man3ourk.jetbot

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class MyAccessibilityService : AccessibilityService() {

    companion object {
        private const val TAG = "JetBotAccessibility"
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Accessibility Service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        val packageName = event.packageName?.toString() ?: ""
        val eventType = event.eventType
        val className = event.className?.toString() ?: ""

        Log.d(
            TAG,
            "Event received -> package: $packageName, class: $className, type: $eventType"
        )

        val rootNode = rootInActiveWindow ?: return

        // جمع‌آوری متن‌های قابل مشاهده برای توسعه بعدی
        val visibleTexts = mutableListOf<String>()
        collectTexts(rootNode, visibleTexts)

        if (visibleTexts.isNotEmpty()) {
            Log.d(TAG, "Visible texts: ${visibleTexts.joinToString(" | ")}")
        }

        // اینجا بعداً می‌توانی منطق پردازش UI را اضافه کنی
        // فعلاً فقط ساختار سرویس پایدار و قابل بیلد است
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility Service interrupted")
    }

    private fun collectTexts(node: AccessibilityNodeInfo?, result: MutableList<String>) {
        if (node == null) return

        val text = node.text?.toString()?.trim()
        val contentDesc = node.contentDescription?.toString()?.trim()

        if (!text.isNullOrEmpty()) {
            result.add(text)
        }

        if (!contentDesc.isNullOrEmpty()) {
            result.add(contentDesc)
        }

        for (i in 0 until node.childCount) {
            collectTexts(node.getChild(i), result)
        }
    }
}
