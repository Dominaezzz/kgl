package opengl

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.INT
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import org.w3c.dom.Document
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

object OpenGLGenerator {
    fun generate(outputDir: File) {
        val mingwDir = outputDir.resolve("mingw")

        val xmlDoc: Document = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse("https://raw.githubusercontent.com/KhronosGroup/OpenGL-Registry/master/xml/gl.xml").apply { documentElement.normalize() }

        val registry = Registry(xmlDoc)

        val coreCommands = mutableSetOf<String>()
        val coreEnums = mutableSetOf<String>()

        for (feature in registry.features.filter { it.api == "gl" }) {
            coreCommands += feature.requires.filter { it.profile != "compatibility" }.flatMap { it.commands }
            coreCommands -= feature.removes.flatMap { it.commands }

            coreEnums += feature.requires.filter { it.profile != "compatibility" }.flatMap { it.enums }
            coreEnums -= feature.removes.flatMap { it.enums }
        }

        val availableEnumEntries = registry.features.filter {
            it.name == "GL_VERSION_1_0" || it.name == "GL_VERSION_1_1"
        }.asSequence().flatMap { it.requires.asSequence() }
            .flatMap { it.enums.asSequence() }.toSet()

        for (group in registry.groups) {
            val enumFile = FileSpec.builder("com.kgl.opengl.enums", group.name)

            val enumBuilder = TypeSpec.enumBuilder(group.name)

            enumBuilder.primaryConstructor(
                FunSpec.constructorBuilder().addParameter("value", INT).build()
            ).addProperty(
                PropertySpec.builder("value", INT).initializer("value").build()
            )

            for (entry in group.enums) {
                if (entry !in coreEnums) continue
                if (entry.startsWith("GL_ALL_") && entry.endsWith("_BITS")) continue

                enumBuilder.addEnumConstant(
                    entry.removePrefix("GL_"),
                    TypeSpec.anonymousClassBuilder()
                        .addSuperclassConstructorParameter(entry)
                        .build()
                )

                if (entry in availableEnumEntries) {
                    enumFile.addImport("platform.opengl32", entry)
                } else {
                    enumFile.addImport("copengl", entry)
                }
            }

            enumFile.addType(enumBuilder.build())
            enumFile.build().writeTo(mingwDir)
        }

        val availableCommands = registry.features.filter {
            it.name == "GL_VERSION_1_0" || it.name == "GL_VERSION_1_1"
        }.asSequence().flatMap { it.requires.asSequence() }
            .flatMap { it.commands.asSequence() }.toSet()

        val glFile = FileSpec.builder("com.kgl.opengl", "GL")
            //.addImport("kotlinx.cinterop", "invoke")
            .addImport("kotlinx.cinterop", "reinterpret")
            .addImport("com.kgl.opengl.utils", "Loader")

        for (command in registry.commands) {
            if (command.name !in coreCommands) continue

            val pfnType = "PFN${(command.alias ?: command.name).toUpperCase()}PROC"

            glFile.addProperty(
                PropertySpec.builder(
                    "kgl_${command.name}",
                    ClassName(
                        "copengl",
//									if (command.name in availableCommands) "platform.opengl32" else "copengl",
                        pfnType
                    ).copy(nullable = true),
                    KModifier.INTERNAL
                ).initializer("Loader.kglGetProcAddress(%S)?.reinterpret()", command.name).build()
            )
        }

        glFile.build().writeTo(mingwDir)
    }
}