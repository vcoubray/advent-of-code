import java.io.IOException
import java.net.URL
import java.time.LocalDate

repositories {
    mavenCentral()
}

interface AocExtension {
    val year: Property<Int>
    val dest: Property<String>
    val session: Property<String>
}

class AocPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create<AocExtension>("aoc")
        project.task("loadInputs") {
            doLast {
                val dest = File(extension.dest.get())

                // Create inputs folder if not already exists
                dest.mkdirs()

                val maxDate = listOf(LocalDate.now(),
                    LocalDate.of(extension.year.get(), 12, 25))
                    .minOrNull()!!
                println(maxDate)
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
                    if(e.message?.startsWith("Server returned HTTP response code: 400") == true){
                        System.err.println("The session is probably not valid. Try to get a new one.")
                    }
                    throw e
                }

            }
        }
    }
}

val aocSession: String by project

subprojects {
    apply<AocPlugin>()
    //Configure the extension using a DSL block
    configure<AocExtension> {
        year.set(2023)
        dest.set("${project.projectDir}/src/main/resources/inputs")
        session.set(aocSession)
    }
}
