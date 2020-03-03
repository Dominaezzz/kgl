package codegen.math

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.KModifier.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
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
		commonFunctions()
		vectorTypes()
	}

	private fun commonFunctions() {
		buildFile(packageName, "Common") {
			indent("\t")

			import("kotlin.math", "abs")
			import("kotlin.math", "round")
			import("kotlin.math", "sign")
			import("kotlin.math", "truncate")

			typeList.forEach { (type, zero) ->
				// abs

				if (type in arrayOf(BYTE, SHORT)) {
					function("abs") {
						modifiers(INLINE)
						parameter("n", type)
						returns(type)
						statement("return abs(n.toInt()).to%T()", type)
					}
				}

				// sign

				if (type in arrayOf(BYTE, SHORT)) {
					extensionProperty(type, "sign", type) {
						getter {
							modifiers(INLINE)
							statement("return toInt().sign.to%T()", type)
						}
					}
				}

				// fract

				if (type in arrayOf(FLOAT, DOUBLE)) {
					extensionProperty(type, "fraction", type) {
						getter {
							modifiers(INLINE)
							statement("return this %% 1")
						}
					}
				}

				// mix

				function("mix") {
					modifiers(INLINE)
					parameter("x", type)
					parameter("y", type)
					parameter("a", BOOLEAN)
					returns(type)
					statement("return if (a) x else y")
				}

				if (type in arrayOf(FLOAT, DOUBLE)) {
					function("mix") {
						modifiers(INLINE)
						parameter("x", type)
						parameter("y", type)
						parameter("a", type)
						returns(type)
						statement("return x * (1f - a) + y * a")
					}
				}

				// roundEven

				if (type in arrayOf(FLOAT, DOUBLE)) {
					function("roundEven") {
						modifiers(INLINE)
						parameter("n", type)
						returns(type)
						statement("val i = truncate(n)")
						statement("val f = n - i")
						controlFlow("return when") {
							statement("f > 0.5f || f < 0.5f -> round(n)")
							statement("i %% 2 == $zero -> i")
							statement("else -> mix(i - 1f, i + 1f, n <= 0)")
						}
					}
				}
			}
		}.apply { writeTo(commonDir.get().asFile) }
	}

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

					import("kotlin.math", "sqrt")

					// Import for floating point numbers
					if (type in arrayOf(FLOAT, DOUBLE)) {
						import("kotlin.math", "ceil")
						import("kotlin.math", "floor")
						import("kotlin.math", "round")
						import("kotlin.math", "truncate")
					}

					// Imports for signed types larger than Byte and Short
					if (type in arrayOf(INT, LONG, FLOAT, DOUBLE)) {
						import("kotlin.math", "abs")
						import("kotlin.math", "sign")
					}

					buildClass(vectorType) {
						modifiers(SEALED)

						companionObject()

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
								parameter("scalar", type)
								returns(vectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									when (type) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "($it $op scalar).to${type.simpleName}()"
										else -> "$it $op scalar"
									}
								}
								statement("return %T($args)", vectorType)
							}

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
								parameter("scalar", type)
								componentNames.forEach {
									val code = when (type) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "$it = ($it $op scalar).to${type.simpleName}()"
										else -> "$it $op= scalar"
									}
									statement(code)
								}
							}

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

					// region Extension Functions

					// Immutable Constructors

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

					// Destructuring Operators

					componentNames.forEachIndexed { i, name ->
						extensionFunction(vectorType, "component${i + 1}") {
							modifiers(INLINE, OPERATOR)
							returns(type)
							statement("return $name")
						}
					}

					// Dot Product

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

					// Cross Product

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

					// endregion

					// region Common Functions

					// abs

					when (type) {
						BYTE, SHORT, INT, LONG,
						FLOAT, DOUBLE -> {
							extensionFunction(vectorType, "abs") {
								returns(vectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "abs($it)" }
								statement("return %T($args)", vectorType)
							}

							extensionFunction(mutableVectorType, "absAssign") {
								componentNames.forEach {
									statement("$it = abs($it)")
								}
							}
						}
					}

					// sign

					when (type) {
						BYTE, SHORT, INT, LONG,
						FLOAT, DOUBLE -> extensionProperty(vectorType, "sign", vectorType) {
							getter {
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									"$it.sign${if (type == LONG) ".toLong()" else ""}"
								}
								statement("return %T($args)", vectorType)
							}
						}
					}

					// floor

					when (type) {
						FLOAT, DOUBLE -> {
							extensionFunction(vectorType, "floor") {
								returns(vectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "floor($it)" }
								statement("return %T($args)", vectorType)
							}

							extensionFunction(mutableVectorType, "floorAssign") {
								componentNames.forEach {
									statement("$it = floor($it)")
								}
							}
						}
					}

					// trunc

					when (type) {
						FLOAT, DOUBLE -> {
							extensionFunction(vectorType, "truncate") {
								returns(vectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "truncate($it)" }
								statement("return %T($args)", vectorType)
							}

							extensionFunction(mutableVectorType, "truncateAssign") {
								componentNames.forEach {
									statement("$it = truncate($it)")
								}
							}
						}
					}

					// round

					when (type) {
						FLOAT, DOUBLE -> {
							extensionFunction(vectorType, "round") {
								returns(vectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "round($it)" }
								statement("return %T($args)", vectorType)
							}

							extensionFunction(mutableVectorType, "roundAssign") {
								componentNames.forEach {
									statement("$it = round($it)")
								}
							}
						}
					}

					// roundEven

					when (type) {
						FLOAT, DOUBLE -> {
							extensionFunction(vectorType, "roundEven") {
								returns(vectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "roundEven($it)" }
								statement("return %T($args)", vectorType)
							}

							extensionFunction(mutableVectorType, "roundEvenAssign") {
								componentNames.forEach {
									statement("$it = roundEven($it)")
								}
							}
						}
					}

					// ceil

					when (type) {
						FLOAT, DOUBLE -> {
							extensionFunction(vectorType, "ceil") {
								returns(vectorType)
								val args = componentNames.joinToString { "ceil($it)" }
								statement("return %T($args)", vectorType)
							}

							extensionFunction(mutableVectorType, "ceilAssign") {
								componentNames.forEach {
									statement("$it = ceil($it)")
								}
							}
						}
					}

					// fract

					when (type) {
						FLOAT, DOUBLE -> {
							extensionProperty(vectorType, "fraction", vectorType) {
								getter {
									val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.fraction" }
									statement("return %T($args)", vectorType)
								}
							}
						}
					}

					// mod

					extensionFunction(vectorType, "rem") {
						modifiers(OPERATOR)
						parameter("other", vectorType)
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							when (type) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "($it %% other.$it).to${type.simpleName}()"
								else -> "$it %% other.$it"
							}
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(vectorType, "rem") {
						modifiers(OPERATOR)
						parameter("scalar", type)
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							when (type) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "($it %% scalar).to${type.simpleName}()"
								else -> "$it %% scalar"
							}
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(mutableVectorType, "remAssign") {
						modifiers(OPERATOR)
						parameter("other", vectorType)
						componentNames.forEach {
							val code = when (type) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "$it = ($it %% other.$it).to${type.simpleName}()"
								else -> "$it %%= other.$it"
							}
							statement(code)
						}
					}

					extensionFunction(mutableVectorType, "remAssign") {
						modifiers(OPERATOR)
						parameter("scalar", type)
						componentNames.forEach {
							val code = when (type) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "$it = ($it %% scalar).to${type.simpleName}()"
								else -> "$it %%= scalar"
							}
							statement(code)
						}
					}

					// min

					extensionFunction(vectorType, "coerceAtMost") {
						parameter("maximumValue", type)
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceAtMost(maximumValue)"
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(vectorType, "coerceAtMost") {
						parameter("maximumValue", vectorType)
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceAtMost(maximumValue.$it)"
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(mutableVectorType, "coerceAssignAtMost") {
						parameter("maximumValue", type)
						componentNames.forEach {
							statement("$it = $it.coerceAtMost(maximumValue)")
						}
					}

					extensionFunction(mutableVectorType, "coerceAssignAtMost") {
						parameter("maximumValue", vectorType)
						componentNames.forEach {
							statement("$it = $it.coerceAtMost(maximumValue.$it)")
						}
					}

					// max

					extensionFunction(vectorType, "coerceAtLeast") {
						parameter("minimumValue", type)
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceAtLeast(minimumValue)"
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(vectorType, "coerceAtLeast") {
						parameter("minimumValue", vectorType)
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceAtLeast(minimumValue.$it)"
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(mutableVectorType, "coerceAssignAtLeast") {
						parameter("minimumValue", type)
						componentNames.forEach {
							statement("$it = $it.coerceAtLeast(minimumValue)")
						}
					}

					extensionFunction(mutableVectorType, "coerceAssignAtLeast") {
						parameter("minimumValue", vectorType)
						componentNames.forEach {
							statement("$it = $it.coerceAtLeast(minimumValue.$it)")
						}
					}

					// clamp

					extensionFunction(vectorType, "coerceIn") {
						parameter("minimumValue", type)
						parameter("maximumValue", type)
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceIn(minimumValue, maximumValue)"
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(vectorType, "coerceIn") {
						parameter("minimumValue", vectorType)
						parameter("maximumValue", vectorType)
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceIn(minimumValue.$it, maximumValue.$it)"
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(vectorType, "coerceIn") {
						parameter("range", ClosedRange::class.asClassName().parameterizedBy(type))
						returns(vectorType)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceIn(range)"
						}
						statement("return %T($args)", vectorType)
					}

					extensionFunction(mutableVectorType, "coerceAssignIn") {
						parameter("minimumValue", type)
						parameter("maximumValue", type)
						componentNames.forEach {
							statement("$it = $it.coerceIn(minimumValue, maximumValue)")
						}
					}

					extensionFunction(mutableVectorType, "coerceAssignIn") {
						parameter("minimumValue", vectorType)
						parameter("maximumValue", vectorType)
						componentNames.forEach {
							statement("$it = $it.coerceIn(minimumValue.$it, maximumValue.$it)")
						}
					}

					extensionFunction(mutableVectorType, "coerceAssignIn") {
						parameter("range", ClosedRange::class.asClassName().parameterizedBy(type))
						componentNames.forEach {
							statement("$it = $it.coerceIn(range)")
						}
					}

					// mix

					// step

					// smoothStep

					// isNaN

					// isInf

					// floatBitsToInt/floatBitsToUInt

					fun toBitsFunction(bitsType: ClassName, unsigned: Boolean) {
						ClassName(packageName, "${bitsType.simpleName}Vector$componentCount").let { bitsVectorType ->
							extensionFunction(vectorType, "to${if (unsigned) "Unsigned" else ""}Bits") {
								returns(bitsVectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									"$it.toBits()${if (unsigned) ".to${bitsType.simpleName}()" else ""}"
								}
								statement("return %T($args)", bitsVectorType)
							}

							extensionFunction(vectorType, "to${if (unsigned) "Unsigned" else ""}RawBits") {
								returns(bitsVectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									"$it.toRawBits()${if (unsigned) ".to${bitsType.simpleName}()" else ""}"
								}
								statement("return %T($args)", bitsVectorType)
							}
						}
					}

					when (type) {
						FLOAT -> {
							toBitsFunction(INT, unsigned = false)
							toBitsFunction(U_INT, unsigned = true)
						}
						DOUBLE -> {
							toBitsFunction(LONG, unsigned = false)
							toBitsFunction(U_LONG, unsigned = true)
						}
					}

					// intBitsToFloat/uintBitsToFloat

					fun fromBitsFunction(bitsType: ClassName, unsigned: Boolean) {
						ClassName(packageName, "${bitsType.simpleName}Vector$componentCount").let { bitsVectorType ->
							extensionFunction(vectorType.nestedClass("Companion"), "fromBits") {
								parameter("bits", bitsVectorType)
								returns(vectorType)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									val unsignedConversion = if (unsigned) ".to${bitsType.simpleName.drop(1)}()" else ""
									"${type.simpleName}.fromBits(bits.$it$unsignedConversion)"
								}
								statement("return %T($args)", vectorType)
							}
						}
					}

					when (type) {
						FLOAT -> {
							fromBitsFunction(INT, false)
							fromBitsFunction(U_INT, true)
						}
						DOUBLE -> {
							fromBitsFunction(LONG, false)
							fromBitsFunction(U_LONG, true)
						}
					}

					// fma

					// frexp

					// ldexp

					// endregion
				}.apply { writeTo(commonDir.get().asFile) }
			}
		}
	}
}
