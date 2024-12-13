package nl.joozd.days

import nl.joozd.days.day13.Machine

class Day13(isTest: Boolean = false): Day(13, isTest) {
    private val inputs = input.split("\n\n")

    override fun first(): Long {
        val machines = inputs.map {Machine.ofInput(it)}
        val costs = machines.mapNotNull { it.lowestCost() }
        return costs.sum()
    }

    override fun second(): Long {
        val machines = inputs.map {Machine.ofInput(it, addOffset = true)}
        val costs = machines.mapNotNull { it.lowestCost() }
        return costs.sum()
    }
}