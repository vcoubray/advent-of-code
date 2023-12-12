import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.*
import java.io.File
import java.io.IOException
import java.net.URL
import java.time.LocalDate

interface AocExtension {
    val year: Property<Int>
    val dest: Property<String>
    val session: Property<String>
}

class AocPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<AocExtension>("aoc-inputs")
        project.extensions.add("aocInputs", extension)
        project.task("loadAocInputs") {
            doLast {
                val dest = File(extension.dest.get())

                // Create inputs folder if not already exists
                dest.mkdirs()

                val maxDate = listOf(
                    LocalDate.now(),
                    LocalDate.of(extension.year.get(), 12, 25)
                )
                    .minOrNull()!!
                val maxDay = maxDate.dayOfMonth

                try {
                    (1..maxDay).forEach { day ->
                        val con =
                            URL("https://adventofcode.com/${extension.year.get()}/day/$day/input").openConnection()
                        con.setRequestProperty("Cookie", "session=${extension.session.get()}")
                        con.inputStream.bufferedReader().use { reader ->
                            val dayName = day.toString().padStart(2, '0')
                            val inputFile = File("${dest.absolutePath}/Day${dayName}.txt")
                            inputFile.writeText(reader.readText())
                        }
                    }
                } catch (e: IOException) {
                    if (e.message?.startsWith("Server returned HTTP response code: 400") == true) {
                        println("The session is probably not valid. Try to get a new one.")
                    }
                    throw e
                }

            }
        }
    }
}


