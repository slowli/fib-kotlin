package me.ostrovski.fib.tasks

import me.ostrovski.fib.logic.FibonacciService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CacheSyncTask(val fibonacciService: FibonacciService) {
    private val logger: Logger = LoggerFactory.getLogger(CacheSyncTask::class.java)

    @Scheduled(fixedRate = 2000)
    fun syncCache() {
        logger.trace("Syncing powers cache to Redis...")
        val syncedCount = fibonacciService.dumpToRepository()
        if (syncedCount > 0) {
            logger.info("Synced $syncedCount power(s) to Redis cache")
        }
    }
}
