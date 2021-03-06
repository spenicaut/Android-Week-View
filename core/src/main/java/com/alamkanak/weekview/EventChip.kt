package com.alamkanak.weekview

import android.graphics.RectF
import android.view.MotionEvent

/**
 * This class encapsulates a [WeekViewEvent] and its visual representation, a [RectF]
 * which is eventually drawn to the screen.
 *
 * There may be more than one [EventChip] for any even (think multi-day events). In that case,
 * multiple [EventChip]s will be used for a single [WeekViewEvent].
 *
 * The original [WeekViewEvent] is accessible via [originalEvent]. The [WeekViewEvent] that
 * corresponds to the drawn rectangle is accessible via [event].
 */
internal data class EventChip<T>(
    /**
     * The [WeekViewEvent] corresponding to the drawn rectangle. It might differ from
     * [originalEvent], which may be a multi-day event.
     */
    val event: WeekViewEvent<T>,
    /**
     * The original [WeekViewEvent], which may be a multi-day event.
     */
    val originalEvent: WeekViewEvent<T>
) {

    /**
     * The rectangle in which the [WeekViewEvent] will be drawn.
     */
    var rect: RectF? = null

    /**
     * The relative start position of the [EventChip].
     *
     * For instance, if there are four columns of events, possible values are 0.0, 0.25, 0.5 and
     * 0.75.
     */
    var relativeStart: Float = 0f

    /**
     * The relative width of the [EventChip].
     *
     * For instance, if there are four columns of events, possible values are:
     * - 0.25: spanning a single column
     * - 0.50: spanning two columns
     * - 0.75: spanning three columns
     * - 1.00: spanning all four columns
     */
    var relativeWidth: Float = 0f

    var minutesFromStartHour: Int = 0

    private var availableWidthCache: Int = 0
    private var availableHeightCache: Int = 0

    fun didAvailableAreaChange(
        area: RectF,
        horizontalPadding: Int,
        verticalPadding: Int
    ): Boolean {
        val availableWidth = (area.right - area.left - horizontalPadding).toInt()
        val availableHeight = (area.bottom - area.top - verticalPadding).toInt()
        return availableWidth != availableWidthCache || availableHeight != availableHeightCache
    }

    fun updateAvailableArea(width: Int, height: Int) {
        availableWidthCache = width
        availableHeightCache = height
    }

    fun clearCache() {
        rect = null
        availableWidthCache = 0
        availableHeightCache = 0
    }

    fun isHit(e: MotionEvent): Boolean {
        return rect?.let {
            e.x > it.left && e.x < it.right && e.y > it.top && e.y < it.bottom
        } ?: false
    }
}
