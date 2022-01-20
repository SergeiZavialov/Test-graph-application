package com.example.test_graph_application.utils

import com.example.test_graph_application.api.Dataset
import kotlin.math.hypot

object DouglasPeukerSimplification {

    private fun perpendicularDistance(pt: Dataset, lineStart: Dataset, lineEnd: Dataset): Double {
        var dx = lineEnd.time - lineStart.time
        var dy = lineEnd.cpu - lineStart.cpu

        // Normalize
        val mag = hypot(dx, dy)
        if (mag > 0.0) {
            dx /= mag; dy /= mag
        }
        val pvx = pt.time - lineStart.time
        val pvy = pt.cpu - lineStart.cpu

        // Get dot product (project pv onto normalized direction)
        val pvdot = dx * pvx + dy * pvy

        // Scale line direction vector and substract it from pv
        val ax = pvx - pvdot * dx
        val ay = pvy - pvdot * dy

        return hypot(ax, ay)
    }

    fun ramerDouglasPeucker(pointList: List<Dataset>, epsilon: Double, out: MutableList<Dataset>) {
        if (pointList.size < 2) return

        // Find the point with the maximum distance from line between start and end
        var dmax = 0.0
        var index = 0
        val end = pointList.size - 1
        for (i in 1 until end) {
            val d = perpendicularDistance(pointList[i], pointList[0], pointList[end])
            if (d > dmax) {
                index = i; dmax = d
            }
        }

        // If max distance is greater than epsilon, recursively simplify
        if (dmax > epsilon) {
            val recResults1 = mutableListOf<Dataset>()
            val recResults2 = mutableListOf<Dataset>()
            val firstLine = pointList.take(index + 1)
            val lastLine = pointList.drop(index)
            ramerDouglasPeucker(firstLine, epsilon, recResults1)
            ramerDouglasPeucker(lastLine, epsilon, recResults2)

            // build the result list
            out.addAll(recResults1.take(recResults1.size - 1))
            out.addAll(recResults2)
            if (out.size < 2) throw RuntimeException("Problem assembling output")
        } else {
            // Just return start and end points
            out.clear()
            out.add(pointList.first())
            out.add(pointList.last())
        }
    }
}