package codegen.math

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.KModifier.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

@Suppress("UnstableApiUsage")
open class GenerateMath : DefaultTask() {
	@OutputDirectory
	val outputDir = project.objects.directoryProperty()

	@get:OutputDirectory
	val commonDir = outputDir.file("common")


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

	private val allComponents = listOf(1 to "x", 2 to "y", 3 to "z", 4 to "w")
	private val arithmetics = listOf("plus" to "+", "minus" to "-", "times" to "*", "div" to "/")

	private fun vectorTypes() {
		for (componentCount in 2..4) {
			val components = allComponents.take(componentCount)

			typeList.forEach { (type, zero) ->
				val vectorType = ClassName(packageName, "${type.simpleName}Vector$componentCount")
				val mutableVectorType = ClassName(packageName, "Mutable${vectorType.simpleName}")

				buildFile(packageName, vectorType.simpleName) {
					import("kotlin.math", "abs")
					import("kotlin.math", "sqrt")

					buildClass(vectorType) {
						modifiers(SEALED)

						components.forEach { (_, name) ->
							property(name, type, ABSTRACT)
						}

						function("get") {
							modifiers(OPERATOR)
							parameter("index", INT)
							controlFlow("when (index)") {
								components.forEach { (i, name) ->
									statement("${i - 1} -> $name")
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
							val args = components.joinToString { (_, name) ->
								when (type) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "($name / length).to${type.simpleName}()"
									else -> "$name / length"
								}
							}
							statement("return %T($args)", vectorType)
						}

						for ((name, op) in arithmetics) {
							function(name) {
								modifiers(OPERATOR)
								parameter("other", vectorType)
								returns(vectorType)
								val args = components.joinToString { (_, name) ->
									when (type) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "($name $op other.$name).to${type.simpleName}()"
										else -> "$name $op other.$name"
									}
								}
								statement("return %T($args)", vectorType)
							}
						}
					}

					buildClass(mutableVectorType) {
						superclass(vectorType)

						primaryConstructor {
							components.forEach { (_, name) ->
								parameter(name, type)
							}
							callSuperConstructor()
						}

						secondaryConstructor {
							parameter("scalar", type)
							callThisConstructor(*components.map { "scalar" }.toTypedArray())
						}

						secondaryConstructor {
							callThisConstructor(*components.map { zero }.toTypedArray())
						}

						components.forEach { (_, name) ->
							mutableProperty(name, type, OVERRIDE) {
								initializer(name)
							}
						}

						function("set") {
							modifiers(OPERATOR)
							parameter("index", INT)
							parameter("value", type)
							controlFlow("when(index)") {
								components.forEach { (i, name) ->
									statement("${i - 1} -> $name = value")
								}
								statement("else -> throw IndexOutOfBoundsException()")
							}
						}

						function("normalize") {
							statement("val length = length")
							components.forEach { (_, name) ->
								val code = when (type) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "$name = ($name / length).to${type.simpleName}()"
									else -> "$name /= length"
								}
								statement(code)
							}
						}

						for ((funName, op) in arithmetics) {
							function("${funName}Assign") {
								modifiers(OPERATOR)
								parameter("other", vectorType)
								components.forEach { (_, name) ->
									val code = when (type) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "$name = ($name $op other.$name).to${type.simpleName}()"
										else -> "$name $op= other.$name"
									}
									statement(code)
								}
							}
						}
					}

					// extensions

					function(vectorType.simpleName) {
						components.forEach { (_, name) ->
							parameter(name, type)
						}
						returns(vectorType)
						statement("return %T(${components.joinToString { it.second }})", mutableVectorType)
					}

					function(vectorType.simpleName) {
						parameter("scalar", type)
						returns(vectorType)
						statement("return %T(scalar)", mutableVectorType)
					}

					components.forEach { (i, name) ->
						extensionFunction(vectorType, "component$i") {
							modifiers(INLINE, OPERATOR)
							returns(type)
							statement("return $name")
						}
					}

					extensionFunction(vectorType, "dot") {
						modifiers(INFIX)
						parameter("other", vectorType)
						returns(type)
						val result = components.joinToString(" + ") { (_, name) -> "$name * other.$name" }
							.let {
								when (type) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "($it).to${type.simpleName}()"
									else -> "($it)"
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
								components
									.drop(1)
									.plus(components.take(2))
									.zipWithNext()
									.joinToString { (c1, c2) ->
										val name1 = c1.second
										val name2 = c2.second
										when (type) {
											BYTE, SHORT,
											U_BYTE, U_SHORT -> "($name1 * other.$name2 - other.$name1 * $name2).to${type.simpleName}()"
											else -> "$name1 * other.$name2 - other.$name1 * $name2"
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
