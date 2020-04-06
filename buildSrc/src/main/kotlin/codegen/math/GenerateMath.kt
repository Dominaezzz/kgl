package codegen.math

import com.squareup.kotlinpoet.*
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

internal data class TypeInfo(
	val type: ClassName,
	val baseType: ClassName? = null,
	val componentCount: Int? = null
)

internal const val packageName = "com.kgl.math"

internal lateinit var commonOutDir: File

@Suppress("UnstableApiUsage")
open class GenerateMath : DefaultTask() {

	@OutputDirectory
	val outputDir: DirectoryProperty = project.objects.directoryProperty()

	@get:OutputDirectory
	val commonDir: Provider<RegularFile> = outputDir.file("common")


	@TaskAction
	fun generate() {
		commonOutDir = commonDir.get().asFile
		coreFeatures()
		stableExtensions()
		experimentalExtensions()
	}
}
