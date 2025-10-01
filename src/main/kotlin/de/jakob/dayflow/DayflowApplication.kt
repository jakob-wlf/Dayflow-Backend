package de.jakob.dayflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DayflowApplication

fun main(args: Array<String>) {
	runApplication<DayflowApplication>(*args)
}
