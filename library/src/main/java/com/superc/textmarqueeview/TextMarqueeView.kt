package com.superc.textmarqueeview

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatTextView

/**
 * @author: SuperChen
 * @last-modifier: SuperChen
 * @version: 1.0
 * @create-date: 2021/1/26 10:34
 * @last-modify-date: 2021/1/26 10:34
 * @description:
 */
open class TextMarqueeView : AppCompatTextView {

    protected var data: List<*>? = null
    protected var lastX: Int = 0
    protected var lastY: Int = 0
    protected var space: CharSequence = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t"
    protected var span: SpannableStringBuilderForAllvers? = null
    protected var onMarqueeItemClickListener: OnMarqueeItemClickListener? = null
    protected var adapter: MarqueeAdapter? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        ellipsize = TextUtils.TruncateAt.MARQUEE
        isSingleLine = true
        marqueeRepeatLimit = -1
    }

    override fun isFocused(): Boolean {
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val superResult = super.onTouchEvent(event)
        if (event != null) {
            val buffer = span
            if (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_UP) {
                var x = event.x
                var y = event.y.toInt()
                lastX = x.toInt()
                lastY = y

                x -= totalPaddingLeft
                y -= totalPaddingTop

                val marqueeScroll = getMarqueeScroll()
                x += scrollX + marqueeScroll[0]
                if (x >= marqueeScroll[1]) {
                    x -= marqueeScroll[1]
                }
                y += scrollY

                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x)
                val link = buffer?.getSpans(off, off, ClickableSpan::class.java)
                if (link!!.isNotEmpty()) {
                    val span = link[0]
                    if (event.action == MotionEvent.ACTION_UP) {
                        span.onClick(this)
                    }
                    return true
                }
            }
        }
        return superResult
    }

    protected open fun getMarqueeScroll(): FloatArray {
        try {
            val field = this.javaClass.superclass.superclass.getDeclaredField("mMarquee")
            field.isAccessible = true
            val marquee = field[this]
            val method = marquee.javaClass.getDeclaredMethod("getScroll")
            method.isAccessible = true
            val scroll = method.invoke(marquee)
            val maxScrollField = marquee.javaClass.getDeclaredField("mMaxScroll")
            maxScrollField.isAccessible = true
            val maxScroll = maxScrollField[marquee]
            return floatArrayOf(scroll as Float, maxScroll as Float)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return floatArrayOf(0f, 0f)
    }

    protected open fun setText() {
        if (adapter == null) return
        val builder: StringBuilder = StringBuilder()
        val startList: MutableList<Int> = ArrayList()
        val endList: MutableList<Int> = ArrayList()
        for (i in getDatas().indices) {
            var te = getDatas()[i]?.toString()
            if (te == null) te = ""
            builder.append(te)
            if (i != getDatas().size - 1) {
                builder.append(space)
            }
            if (i == 0) {
                startList.add(0)
                endList.add(te.length)
            } else {
                val start = endList[i - 1] + space.length
                startList.add(start)
                endList.add(start + te.length)
            }
        }
        span = SpannableStringBuilderForAllvers(builder)
        for (i in startList.indices) {
            span?.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onMarqueeItemClickListener?.onMarqueeItemClick(this@TextMarqueeView, i, getDatas()[i])
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    adapter?.bind(ds, i, getDatas()[i])
                }
            }, startList[i], endList[i], Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        super.setText(span)
    }

    open fun getDatas(): List<*> {
        return (if (data == null) java.util.ArrayList<Any?>().also { data = it } else data)!!
    }

    open fun setDatas(datas: MutableList<*>): TextMarqueeView {
        this.data = datas
        setText()
        return this
    }

    open fun setSpace(space: CharSequence?): TextMarqueeView {
        this.space = space ?: ""
        return this;
    }

    open fun setMarqueeAdapter(marqueeAdapter: MarqueeAdapter): TextMarqueeView {
        this.adapter = marqueeAdapter
        return this
    }

    open fun setOnMarqueeItemClickListener(onMarqueeItemClickListener: OnMarqueeItemClickListener?): TextMarqueeView {
        this.onMarqueeItemClickListener = onMarqueeItemClickListener
        return this
    }
}


open class SpannableStringBuilderForAllvers : SpannableStringBuilder {
    constructor() : super("") {}
    constructor(text: CharSequence) : super(text, 0, text.length) {}
    constructor(text: CharSequence?, start: Int, end: Int) : super(text, start, end) {}

    override fun append(text: CharSequence?): SpannableStringBuilder {
        if (text == null) {
            return this
        }
        val length = length
        return replace(length, length, text, 0, text.length)
    }

    /**
     * 该方法在原API里面只支持API21或者以上，这里适应低版本
     */
    override fun append(text: CharSequence?, what: Any, flags: Int): SpannableStringBuilderForAllvers {
        if (text == null) {
            return this
        }
        val start = length
        append(text)
        setSpan(what, start, length, flags)
        return this
    }
}


interface MarqueeAdapter {
    fun bind(ds: TextPaint, position: Int, item: Any?)
}


interface OnMarqueeItemClickListener {
    fun onMarqueeItemClick(widget: View, position: Int, item: Any?)
}