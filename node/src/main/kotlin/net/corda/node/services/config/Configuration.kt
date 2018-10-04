package net.corda.node.services.config

import com.uchuhimo.konf.Config
import java.io.InputStream
import java.io.Reader
import java.nio.file.Path
import java.util.*

interface Configuration {

    companion object {

        // TODO sollecitom perhaps try to use JvmStatic here
        val from: Builder.SourceSelector = Konfiguration.Builder.SourceSelector(Config.invoke().from)
    }

    fun mutable(): Configuration.Mutable

    operator fun <EXPECTED_VALUE> get(key: String): EXPECTED_VALUE

    interface Mutable : Configuration {

        operator fun set(key: String, value: Any?)

        override fun mutable() = this
    }

    interface Builder {

        val from: SourceSelector

        // TODO sollecitom perhaps add the Spec here as a mandatory param
        fun build(): Configuration

        interface SourceSelector {

            fun systemProperties(prefixFilter: String = ""): Configuration.Builder

            fun environment(prefixFilter: String = ""): Configuration.Builder

            fun properties(properties: Properties): Configuration.Builder

            fun map(map: Map<String, Any>): Configuration.Builder

            fun hierarchicalMap(map: Map<String, Any>): Configuration.Builder

            val hocon: SourceSelector.FormatAware

            val yaml: SourceSelector.FormatAware

            val xml: SourceSelector.FormatAware

            val json: SourceSelector.FormatAware

            interface FormatAware {

                fun file(path: Path): Configuration.Builder

                fun resource(resourceName: String): Configuration.Builder

                fun reader(reader: Reader): Configuration.Builder

                fun inputStream(stream: InputStream): Configuration.Builder

                fun string(rawFormat: String): Configuration.Builder

                fun bytes(bytes: ByteArray): Configuration.Builder
            }
        }
    }
}