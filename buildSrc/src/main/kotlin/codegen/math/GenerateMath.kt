package codegen.math

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.KModifier.*
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFile
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@Suppress("UnstableApiUsage")
open class GenerateMath : DefaultTask() {

	@OutputDirectory
	val outputDir: DirectoryProperty = project.objects.directoryProperty()

	@get:OutputDirectory
	val commonDir: Provider<RegularFile> = outputDir.file("common")


	private val packageName = "com.kgl.math"

	private val typeList = listOf(
		BYTE to "0",
		SHORT to "0",
		INT to "0",
		LONG to "0L",
		U_BYTE to "0U",
		U_SHORT to "0U",
		U_INT to "0U",
		U_LONG to "0UL",
		FLOAT to "0f",
		DOUBLE to "0.0"
	)


	@TaskAction
	fun generate() {
		vectorTypes()
	}

	private val suppress = ClassName("kotlin", "Suppress")

	private val allComponentNames = listOf("x", "y", "z", "w")
	private val arithmetics = listOf("plus" to "+", "minus" to "-", "times" to "*", "div" to "/")

	private fun vectorTypes() {
		for (componentCount in 2..4) {
			val componentNames = allComponentNames.take(componentCount)

			typeList.forEach { (type, zero) ->
				val vectorType = ClassName(packageName, "${type.simpleName}Vector$componentCount")
				val mutableVectorType = ClassName(packageName, "Mutable${vectorType.simpleName}")

				buildFile(packageName, vectorType.simpleName) {
					indent("\t")

					annotation(suppress) {
						member("%S", "NOTHING_TO_INLINE")
					}

					when (type) {
						INT, LONG, FLOAT, DOUBLE -> import("kotlin.math", "abs")
					}
					import("kotlin.math", "sqrt")

					buildClass(vectorType) {
						modifiers(SEALED)

						componentNames.forEach {
							property(it, type, ABSTRACT)
						}

						function("get") {
							modifiers(OPERATOR)
							parameter("index", INT)
							controlFlow("when (index)") {
								componentNames.forEachIndexed { i, it ->
									statement("$i -> $it")
								}
								statement("else -> throw IndexOutOfBoundsException()")
							}
						}

						property("length", type) {
							getter {
								val code = when (type) {
									BYTE, SHORT, INT, LONG ->
										"return sqrt((this dot this).toDouble()).to${type.simpleName}()"
									U_BYTE, U_SHORT, U_INT, U_LONG ->
										"return sqrt((this dot this).toDouble()).to${type.simpleName.drop(1)}().to${type.simpleName}()"
									else ->
										"return sqrt(this dot this)"
								}
								statement(code)
							}
						}

						property("lengthSquared", type) {
							getter {
								statement("return this dot this")
							}
						}

						function("normalized") {
							returns(vectorType)
							statement("val length = length")
							val args = componentNames.joinToString {
								when (type) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "($it / length).to${type.simpleName}()"
									else -> "$it / length"
								}
							}
							statement("return %T($args)", vectorType)
						}

						for ((name, op) in arithmetics) {
							function(name) {
								modifiers(OPERATOR)
								parameter("other", vectorType)
								returns(vectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									when (type) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "($it $op other.$it).to${type.simpleName}()"
										else -> "$it $op other.$it"
									}
								}
								statement("return %T($args)", vectorType)
							}
						}
					}

					buildClass(mutableVectorType) {
						superclass(vectorType)

						primaryConstructor {
							componentNames.forEach { name ->
								parameter(name, type)
							}
							callSuperConstructor()
						}

						secondaryConstructor {
							parameter("scalar", type)
							callThisConstructor(*componentNames.map { "scalar" }.toTypedArray())
						}

						secondaryConstructor {
							callThisConstructor(*componentNames.map { zero }.toTypedArray())
						}

						componentNames.forEach { name ->
							mutableProperty(name, type, OVERRIDE) {
								initializer(name)
							}
						}

						function("set") {
							modifiers(OPERATOR)
							parameter("index", INT)
							parameter("value", type)
							controlFlow("when (index)") {
								componentNames.forEachIndexed { i, it ->
									statement("$i -> $it = value")
								}
								statement("else -> throw IndexOutOfBoundsException()")
							}
						}

						function("normalize") {
							statement("val length = length")
							componentNames.forEach {
								val code = when (type) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "$it = ($it / length).to${type.simpleName}()"
									else -> "$it /= length"
								}
								statement(code)
							}
						}

						for ((funName, op) in arithmetics) {
							function("${funName}Assign") {
								modifiers(OPERATOR)
								parameter("other", vectorType)
								componentNames.forEach {
									val code = when (type) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "$it = ($it $op other.$it).to${type.simpleName}()"
										else -> "$it $op= other.$it"
									}
									statement(code)
								}
							}
						}
					}

					// extensions

					function(vectorType.simpleName) {
						componentNames.forEach { name ->
							parameter(name, type)
						}
						returns(vectorType)
						statement("return %T(${componentNames.joinToString()})", mutableVectorType)
					}

					function(vectorType.simpleName) {
						parameter("scalar", type)
						returns(vectorType)
						statement("return %T(scalar)", mutableVectorType)
					}

					componentNames.forEachIndexed { i, name ->
						extensionFunction(vectorType, "component${i + 1}") {
							modifiers(INLINE, OPERATOR)
							returns(type)
							statement("return $name")
						}
					}

					extensionFunction(vectorType, "dot") {
						modifiers(INFIX)
						parameter("other", vectorType)
						returns(type)
						val result = componentNames.joinToString(" + ") { "$it * other.$it" }
							.let {
								when (type) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "($it).to${type.simpleName}()"
									else -> "\n\t($it)"
								}
							}
						statement("return $result")
					}

					if (componentCount == 3) {
						extensionFunction(vectorType, "cross") {
							modifiers(INFIX)
							parameter("other", vectorType)
							returns(vectorType)
							val args =
								componentNames
									.drop(1)
									.plus(componentNames.take(2))
									.zipWithNext()
									.joinToString(",\n\t", "\n\t", "\n") { (c1, c2) ->
										when (type) {
											BYTE, SHORT,
											U_BYTE, U_SHORT -> "($c1 * other.$c2 - other.$c1 * $c2).to${type.simpleName}()"
											else -> "$c1 * other.$c2 - other.$c1 * $c2"
										}
									}
							statement("return %T($args)", vectorType)
						}
					}
				}.apply { writeTo(commonDir.get().asFile) }
			}
		}
	}
}
