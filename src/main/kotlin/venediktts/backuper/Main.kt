package venediktts.backuper

import mu.KotlinLogging
import venediktts.backuper.config.Config
import venediktts.backuper.config.ServiceConfigurer
import java.lang.Exception

fun main(args: Array<String>) {
    val log = KotlinLogging.logger("Main")
    try {
        val config = Config(args[0])
        ServiceConfigurer.initServices(config)
    } catch (e: Exception) {
        log.error(e.message)
    }
}