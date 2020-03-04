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


	data class TypeInfo(
		val type: ClassName,
		val zero: String,
		val baseType: TypeInfo? = null,
		val componentCount: Int? = null
	)


	private val packageName = "com.kgl.math"

	private val primitiveTypes = listOf(
		TypeInfo(BYTE, "0"),
		TypeInfo(SHORT, "0"),
		TypeInfo(INT, "0"),
		TypeInfo(LONG, "0L"),
		TypeInfo(U_BYTE, "0U"),
		TypeInfo(U_SHORT, "0U"),
		TypeInfo(U_INT, "0U"),
		TypeInfo(U_LONG, "0UL"),
		TypeInfo(FLOAT, "0f"),
		TypeInfo(DOUBLE, "0.0")
	)

	private val vectorTypes = listOf(
		TypeInfo(ClassName(packageName, "ByteVector2"), "", TypeInfo(BYTE, "0"), 2),
		TypeInfo(ClassName(packageName, "ByteVector3"), "", TypeInfo(BYTE, "0"), 3),
		TypeInfo(ClassName(packageName, "ByteVector4"), "", TypeInfo(BYTE, "0"), 4),
		TypeInfo(ClassName(packageName, "ShortVector2"), "", TypeInfo(SHORT, "0"), 2),
		TypeInfo(ClassName(packageName, "ShortVector3"), "", TypeInfo(SHORT, "0"), 3),
		TypeInfo(ClassName(packageName, "ShortVector4"), "", TypeInfo(SHORT, "0"), 4),
		TypeInfo(ClassName(packageName, "IntVector2"), "", TypeInfo(INT, "0"), 2),
		TypeInfo(ClassName(packageName, "IntVector3"), "", TypeInfo(INT, "0"), 3),
		TypeInfo(ClassName(packageName, "IntVector4"), "", TypeInfo(INT, "0"), 4),
		TypeInfo(ClassName(packageName, "LongVector2"), "", TypeInfo(LONG, "0L"), 2),
		TypeInfo(ClassName(packageName, "LongVector3"), "", TypeInfo(LONG, "0L"), 3),
		TypeInfo(ClassName(packageName, "LongVector4"), "", TypeInfo(LONG, "0L"), 4),
		TypeInfo(ClassName(packageName, "UByteVector2"), "", TypeInfo(U_BYTE, "0U"), 2),
		TypeInfo(ClassName(packageName, "UByteVector3"), "", TypeInfo(U_BYTE, "0U"), 3),
		TypeInfo(ClassName(packageName, "UByteVector4"), "", TypeInfo(U_BYTE, "0U"), 4),
		TypeInfo(ClassName(packageName, "UShortVector2"), "", TypeInfo(U_SHORT, "0U"), 2),
		TypeInfo(ClassName(packageName, "UShortVector3"), "", TypeInfo(U_SHORT, "0U"), 3),
		TypeInfo(ClassName(packageName, "UShortVector4"), "", TypeInfo(U_SHORT, "0U"), 4),
		TypeInfo(ClassName(packageName, "UIntVector2"), "", TypeInfo(U_INT, "0U"), 2),
		TypeInfo(ClassName(packageName, "UIntVector3"), "", TypeInfo(U_INT, "0U"), 3),
		TypeInfo(ClassName(packageName, "UIntVector4"), "", TypeInfo(U_INT, "0U"), 4),
		TypeInfo(ClassName(packageName, "ULongVector2"), "", TypeInfo(U_LONG, "0UL"), 2),
		TypeInfo(ClassName(packageName, "ULongVector3"), "", TypeInfo(U_LONG, "0UL"), 3),
		TypeInfo(ClassName(packageName, "ULongVector4"), "", TypeInfo(U_LONG, "0UL"), 4),
		TypeInfo(ClassName(packageName, "FloatVector2"), "", TypeInfo(FLOAT, "0f"), 2),
		TypeInfo(ClassName(packageName, "FloatVector3"), "", TypeInfo(FLOAT, "0f"), 3),
		TypeInfo(ClassName(packageName, "FloatVector4"), "", TypeInfo(FLOAT, "0f"), 4),
		TypeInfo(ClassName(packageName, "DoubleVector2"), "", TypeInfo(DOUBLE, "0.0"), 2),
		TypeInfo(ClassName(packageName, "DoubleVector3"), "", TypeInfo(DOUBLE, "0.0"), 3),
		TypeInfo(ClassName(packageName, "DoubleVector4"), "", TypeInfo(DOUBLE, "0.0"), 4)
	)

	private val allComponentNames = listOf("x", "y", "z", "w")
	private val arithmetics = listOf("plus" to "+", "minus" to "-", "times" to "*", "div" to "/")


	@TaskAction
	fun generate() {
		commonFunctions()
		geometricFunctions()
		vectorTypes()
	}

	private fun commonFunctions() {
		(primitiveTypes + vectorTypes).forEach { (type, zero, baseType, componentCount) ->
			val componentNames = allComponentNames.take(componentCount ?: 0)
			val mutableType = ClassName(packageName, "Mutable${type.simpleName}")

			buildFile(packageName, "Common${type.simpleName}") {
				indent("\t")

				import("kotlin.math", "abs")
				import("kotlin.math", "ceil")
				import("kotlin.math", "floor")
				import("kotlin.math", "round")
				import("kotlin.math", "sign")
				import("kotlin.math", "truncate")

				// abs

				when (type) {
					BYTE, SHORT -> {
						function("abs") {
							parameter("n", type)
							returns(type)
							statement("return abs(n.toInt()).to%T()", type)
						}
					}
				}

				when (baseType?.type) {
					BYTE, SHORT, INT, LONG,
					FLOAT, DOUBLE -> {
						extensionFunction(type, "abs") {
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "abs($it)" }
							statement("return %T($args)", type)
						}

						extensionFunction(mutableType, "absAssign") {
							componentNames.forEach {
								statement("$it = abs($it)")
							}
						}
					}
				}


				// sign

				when (type) {
					BYTE, SHORT -> {
						extensionProperty(type, "sign", type) {
							getter {
								statement("return toInt().sign.to%T()", type)
							}
						}
					}
				}

				when (baseType?.type) {
					BYTE, SHORT, INT, LONG,
					FLOAT, DOUBLE -> extensionProperty(type, "sign", type) {
						getter {
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
								"$it.sign${if (baseType.type == LONG) ".toLong()" else ""}"
							}
							statement("return %T($args)", type)
						}
					}
				}

				// floor

				when (baseType?.type) {
					FLOAT, DOUBLE -> {
						extensionFunction(type, "floor") {
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "floor($it)" }
							statement("return %T($args)", type)
						}

						extensionFunction(mutableType, "floorAssign") {
							componentNames.forEach {
								statement("$it = floor($it)")
							}
						}
					}
				}

				// trunc

				when (baseType?.type) {
					FLOAT, DOUBLE -> {
						extensionFunction(type, "truncate") {
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "truncate($it)" }
							statement("return %T($args)", type)
						}

						extensionFunction(mutableType, "truncateAssign") {
							componentNames.forEach {
								statement("$it = truncate($it)")
							}
						}
					}
				}

				// round

				when (baseType?.type) {
					FLOAT, DOUBLE -> {
						extensionFunction(type, "round") {
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "round($it)" }
							statement("return %T($args)", type)
						}

						extensionFunction(mutableType, "roundAssign") {
							componentNames.forEach {
								statement("$it = round($it)")
							}
						}
					}
				}

				// roundEven

				when (type) {
					FLOAT, DOUBLE -> {
						function("roundEven") {
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

				when (baseType?.type) {
					FLOAT, DOUBLE -> {
						extensionFunction(type, "roundEven") {
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "roundEven($it)" }
							statement("return %T($args)", type)
						}

						extensionFunction(mutableType, "roundEvenAssign") {
							componentNames.forEach {
								statement("$it = roundEven($it)")
							}
						}
					}
				}

				// ceil

				when (baseType?.type) {
					FLOAT, DOUBLE -> {
						extensionFunction(type, "ceil") {
							returns(type)
							val args = componentNames.joinToString { "ceil($it)" }
							statement("return %T($args)", type)
						}

						extensionFunction(mutableType, "ceilAssign") {
							componentNames.forEach {
								statement("$it = ceil($it)")
							}
						}
					}
				}

				// fract

				when (type) {
					FLOAT, DOUBLE -> {
						extensionProperty(type, "fraction", type) {
							getter {
								statement("return this %% 1")
							}
						}
					}
				}

				when (baseType?.type) {
					FLOAT, DOUBLE -> {
						extensionProperty(type, "fraction", type) {
							getter {
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.fraction" }
								statement("return %T($args)", type)
							}
						}
					}
				}

				// mod

				if (baseType != null) {
					extensionFunction(type, "rem") {
						modifiers(OPERATOR)
						parameter("scalar", baseType.type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							when (baseType.type) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "($it %% scalar).to${baseType.type.simpleName}()"
								else -> "$it %% scalar"
							}
						}
						statement("return %T($args)", type)
					}

					extensionFunction(type, "rem") {
						modifiers(OPERATOR)
						parameter("other", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							when (baseType.type) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "($it %% other.$it).to${baseType.type.simpleName}()"
								else -> "$it %% other.$it"
							}
						}
						statement("return %T($args)", type)
					}

					extensionFunction(mutableType, "remAssign") {
						modifiers(OPERATOR)
						parameter("scalar", baseType.type)
						componentNames.forEach {
							val code = when (baseType.type) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "$it = ($it %% scalar).to${baseType.type.simpleName}()"
								else -> "$it %%= scalar"
							}
							statement(code)
						}
					}

					extensionFunction(mutableType, "remAssign") {
						modifiers(OPERATOR)
						parameter("other", type)
						componentNames.forEach {
							val code = when (baseType.type) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "$it = ($it %% other.$it).to${baseType.type.simpleName}()"
								else -> "$it %%= other.$it"
							}
							statement(code)
						}
					}
				}

				// min

				if (baseType != null) {
					extensionFunction(type, "coerceAtMost") {
						parameter("maximumValue", baseType.type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceAtMost(maximumValue)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(type, "coerceAtMost") {
						parameter("maximumValue", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceAtMost(maximumValue.$it)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(mutableType, "coerceAssignAtMost") {
						parameter("maximumValue", baseType.type)
						componentNames.forEach {
							statement("$it = $it.coerceAtMost(maximumValue)")
						}
					}

					extensionFunction(mutableType, "coerceAssignAtMost") {
						parameter("maximumValue", type)
						componentNames.forEach {
							statement("$it = $it.coerceAtMost(maximumValue.$it)")
						}
					}
				}

				// max

				if (baseType != null) {
					extensionFunction(type, "coerceAtLeast") {
						parameter("minimumValue", baseType.type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceAtLeast(minimumValue)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(type, "coerceAtLeast") {
						parameter("minimumValue", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceAtLeast(minimumValue.$it)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(mutableType, "coerceAssignAtLeast") {
						parameter("minimumValue", baseType.type)
						componentNames.forEach {
							statement("$it = $it.coerceAtLeast(minimumValue)")
						}
					}

					extensionFunction(mutableType, "coerceAssignAtLeast") {
						parameter("minimumValue", type)
						componentNames.forEach {
							statement("$it = $it.coerceAtLeast(minimumValue.$it)")
						}
					}
				}

				// clamp

				if (baseType != null) {
					extensionFunction(type, "coerceIn") {
						parameter("minimumValue", baseType.type)
						parameter("maximumValue", baseType.type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceIn(minimumValue, maximumValue)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(type, "coerceIn") {
						parameter("minimumValue", type)
						parameter("maximumValue", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceIn(minimumValue.$it, maximumValue.$it)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(type, "coerceIn") {
						parameter("range", ClosedRange::class.asClassName().parameterizedBy(baseType.type))
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceIn(range)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(mutableType, "coerceAssignIn") {
						parameter("minimumValue", baseType.type)
						parameter("maximumValue", baseType.type)
						componentNames.forEach {
							statement("$it = $it.coerceIn(minimumValue, maximumValue)")
						}
					}

					extensionFunction(mutableType, "coerceAssignIn") {
						parameter("minimumValue", type)
						parameter("maximumValue", type)
						componentNames.forEach {
							statement("$it = $it.coerceIn(minimumValue.$it, maximumValue.$it)")
						}
					}

					extensionFunction(mutableType, "coerceAssignIn") {
						parameter("range", ClosedRange::class.asClassName().parameterizedBy(baseType.type))
						componentNames.forEach {
							statement("$it = $it.coerceIn(range)")
						}
					}
				}

				// mix

				function("mix") {
					parameter("x", type)
					parameter("y", type)
					parameter("a", BOOLEAN)
					returns(type)
					statement("return if (a) x else y")
				}

				when (type) {
					FLOAT, DOUBLE -> {
						function("mix") {
							parameter("x", type)
							parameter("y", type)
							parameter("a", type)
							returns(type)
							statement("return x * (1f - a) + y * a")
						}
					}
				}

				when (baseType?.type) {
					FLOAT, DOUBLE -> {
						function("mix") {
							parameter("x", type)
							parameter("y", type)
							parameter("a", baseType.type)
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "mix(x.$it, y.$it, a)" }
							statement("return %T($args)", type)
						}

						function("mix") {
							parameter("x", type)
							parameter("y", type)
							parameter("a", type)
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "mix(x.$it, y.$it, a.$it)" }
							statement("return %T($args)", type)
						}
					}
				}

				// step

				// smoothStep

				// isNaN

				// isInf

				// floatBitsToInt/floatBitsToUInt

				fun toBitsFunction(bitsType: ClassName, unsigned: Boolean) {
					ClassName(packageName, "${bitsType.simpleName}Vector$componentCount").let { bitsVectorType ->
						extensionFunction(type, "to${if (unsigned) "Unsigned" else ""}Bits") {
							returns(bitsVectorType)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
								"$it.toBits()${if (unsigned) ".to${bitsType.simpleName}()" else ""}"
							}
							statement("return %T($args)", bitsVectorType)
						}

						extensionFunction(type, "to${if (unsigned) "Unsigned" else ""}RawBits") {
							returns(bitsVectorType)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
								"$it.toRawBits()${if (unsigned) ".to${bitsType.simpleName}()" else ""}"
							}
							statement("return %T($args)", bitsVectorType)
						}
					}
				}

				when (baseType?.type) {
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
						extensionFunction(type.nestedClass("Companion"), "fromBits") {
							parameter("bits", bitsVectorType)
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
								val unsignedConversion = if (unsigned) ".to${bitsType.simpleName.drop(1)}()" else ""
								"${baseType!!.type.simpleName}.fromBits(bits.$it$unsignedConversion)"
							}
							statement("return %T($args)", type)
						}
					}
				}

				when (baseType?.type) {
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

			}.apply { writeTo(commonDir.get().asFile) }
		}
	}

	private fun geometricFunctions() {
		vectorTypes.forEach { (vectorType, _, baseType, componentCount) ->
			val componentNames = allComponentNames.take(componentCount ?: return@forEach)
			val (type) = baseType ?: return@forEach

			buildFile(packageName, "Geometric${vectorType.simpleName}") {
				indent("\t")

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
						val args = componentNames
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

	private fun vectorTypes() {
		vectorTypes.forEach { (vectorType, _, baseType, componentCount) ->
			val mutableVectorType = ClassName(packageName, "Mutable${vectorType.simpleName}")
			val componentNames = allComponentNames.take(componentCount ?: return@forEach)
			val (type, zero) = baseType ?: return@forEach

			buildFile(packageName, vectorType.simpleName) {
				indent("\t")

				import("kotlin.math", "sqrt")

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
			}.apply { writeTo(commonDir.get().asFile) }
		}
	}
}
