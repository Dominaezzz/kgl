package codegen.math

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.DOUBLE
import com.squareup.kotlinpoet.FLOAT
import com.squareup.kotlinpoet.INT
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
		// BYTE to "0", // i
		// SHORT to "0",
		// INT to "0",
		// LONG to "0L",
		FLOAT to "0f",
		DOUBLE to "0.0"
	)


	@TaskAction
	fun generate() {
		generateVector2()
		generateVectors()
	}

	private fun generateVector2() {
		buildFile(packageName, "Vector2") {
			import("kotlin.math", "sqrt")

			for ((componentType, zero) in typeList) {
				val vectorType = ClassName(packageName, "${componentType.simpleName}Vector2")
				val mutableVectorType = ClassName(packageName, "Mutable${componentType.simpleName}Vector2")
				val arithmetics = listOf("plus" to "+", "minus" to "-", "times" to "*", "div" to "/")

				buildClass(vectorType) {
					modifiers(SEALED)

					property("x", componentType, ABSTRACT)

					property("y", componentType, ABSTRACT)

					function("get") {
						modifiers(OPERATOR)
						parameter("index", INT)
						controlFlow("when (index)") {
							statement("0 -> x; 1 -> y")
							statement("else -> throw IndexOutOfBoundsException()")
						}
					}

					function("component1") {
						modifiers(OPERATOR)
						returns(componentType)
						statement("return x")
					}

					function("component2") {
						modifiers(OPERATOR)
						returns(componentType)
						statement("return y")
					}

					property("length", componentType) {
						getter {
							statement("return sqrt(this dot this)")
						}
					}

					property("lengthSquared", componentType) {
						getter {
							statement("return this dot this")
						}
					}

					function("normalized") {
						returns(vectorType)
						statement("val length = length")
						statement("return %T(x / length, y / length)", vectorType)
					}

					function("dot") {
						modifiers(INFIX)
						parameter("other", vectorType)
						returns(componentType)
						statement("return x * other.x + y * other.y")
					}

					for ((name, op) in arithmetics) {
						function(name) {
							modifiers(OPERATOR)
							parameter("other", vectorType)
							returns(vectorType)
							statement("return %T(x $op other.x, y $op other.y)", vectorType)
						}
					}
				}

				function(vectorType.simpleName) {
					parameter("x", componentType)
					parameter("y", componentType)
					returns(vectorType)
					statement("return %T(x, y)", mutableVectorType)
				}

				buildClass(mutableVectorType) {
					superclass(vectorType)

					primaryConstructor {
						parameter("x", componentType)
						parameter("y", componentType)
						callSuperConstructor()
					}

					secondaryConstructor {
						parameter("scalar", componentType) {
							defaultValue(zero)
						}
						callThisConstructor("scalar", "scalar")
					}

					mutableProperty("x", componentType, OVERRIDE) {
						initializer("x")
					}

					mutableProperty("y", componentType, OVERRIDE) {
						initializer("y")
					}

					function("set") {
						modifiers(OPERATOR)
						parameter("index", INT)
						parameter("value", componentType)
						controlFlow("when(index)") {
							statement("0 -> x = value; 1 -> y = value")
							statement("else -> throw IndexOutOfBoundsException()")
						}
					}

					function("normalize") {
						statement("val length = length")
						statement("x /= length; y /= length")
					}

					for ((name, op) in arithmetics) {
						function("${name}Assign") {
							modifiers(OPERATOR)
							parameter("other", vectorType)
							statement("x $op= other.x; y $op= other.y")
						}
					}
				}
			}
		}.apply { writeTo(commonDir.get().asFile) }
	}

	private val allComponents = listOf(1 to "x", 2 to "y", 3 to "z", 4 to "w")
	private val arithmetics = listOf("plus" to "+", "minus" to "-", "times" to "*", "div" to "/")

	private fun generateVectors() {
		for (componentCount in 2..4) {
			val components = allComponents.take(componentCount)

			buildFile(packageName, "Vector$componentCount") {
				import("kotlin.math", "sqrt")

				for ((componentType, zero) in typeList) {
					val vectorType = ClassName(packageName, "${componentType.simpleName}Vector$componentCount")
					val mutableVectorType =
						ClassName(packageName, "Mutable${componentType.simpleName}Vector$componentCount")

					buildClass(vectorType) {
						modifiers(SEALED)

						components.forEach { (_, name) ->
							property(name, componentType, ABSTRACT)
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

						components.forEach { (i, name) ->
							function("component$i") {
								modifiers(OPERATOR)
								returns(componentType)
								statement("return $name")
							}
						}

						property("length", componentType) {
							getter {
								statement("return sqrt(this dot this)")
							}
						}

						property("lengthSquared", componentType) {
							getter {
								statement("return this dot this")
							}
						}

						function("normalized") {
							returns(vectorType)
							statement("val length = length")
							val args = buildString {
								components.forEach { (i, name) ->
									append("$name / length")
									if (i < componentCount) append(", ")
								}
							}
							statement("return %T($args)", vectorType)
						}

						function("dot") {
							modifiers(INFIX)
							parameter("other", vectorType)
							returns(componentType)
							val result = buildString {
								components.forEach { (i, name) ->
									append("$name * other.$name")
									if (i < componentCount) append(" + ")
								}
							}
							statement("return $result")
						}

						if (componentCount == 3) {
							function("cross") {
								modifiers(INFIX)
								parameter("other", vectorType)
								returns(vectorType)
								val args = buildString {
									components
										.drop(1)
										.plus(components.take(2))
										.zipWithNext()
										.forEachIndexed { i, (c1, c2) ->
											val name1 = c1.second
											val name2 = c2.second
											append("$name1 * other.$name2 - other.$name1 * $name2")
											if (i < componentCount - 1) append(", ")
										}
								}
								statement("return %T($args)", vectorType)
							}
						}

						for ((name, op) in arithmetics) {
							function(name) {
								modifiers(OPERATOR)
								parameter("other", vectorType)
								returns(vectorType)
								val args = buildString {
									components.forEach { (i, name) ->
										append("$name $op other.$name")
										if (i < componentCount) append(", ")
									}
								}
								statement("return %T($args)", vectorType)
							}
						}
					}

					function(vectorType.simpleName) {
						components.forEach { (_, name) ->
							parameter(name, componentType)
						}
						returns(vectorType)
						statement("return %T(${components.joinToString { it.second }})", mutableVectorType)
					}

					buildClass(mutableVectorType) {
						superclass(vectorType)

						primaryConstructor {
							components.forEach { (_, name) ->
								parameter(name, componentType)
							}
							callSuperConstructor()
						}

						secondaryConstructor {
							parameter("scalar", componentType) {
								defaultValue(zero)
							}
							callThisConstructor(*components.map { "scalar" }.toTypedArray())
						}

						components.forEach { (_, name) ->
							mutableProperty(name, componentType, OVERRIDE) {
								initializer(name)
							}
						}

						function("set") {
							modifiers(OPERATOR)
							parameter("index", INT)
							parameter("value", componentType)
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
								statement("$name /= length")
							}
						}

						for ((funName, op) in arithmetics) {
							function("${funName}Assign") {
								modifiers(OPERATOR)
								parameter("other", vectorType)
								components.forEach { (_, name) ->
									statement("$name $op= other.$name")
								}
							}
						}
					}
				}
			}.apply { writeTo(commonDir.get().asFile) }
		}
	}
}
