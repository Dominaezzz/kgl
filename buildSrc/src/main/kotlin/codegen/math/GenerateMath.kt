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
		val baseType: ClassName? = null,
		val componentCount: Int? = null
	)


	private val packageName = "com.kgl.math"

	private val primitiveTypes = listOf(
		TypeInfo(BYTE),
		TypeInfo(SHORT),
		TypeInfo(INT),
		TypeInfo(LONG),
		TypeInfo(U_BYTE),
		TypeInfo(U_SHORT),
		TypeInfo(U_INT),
		TypeInfo(U_LONG),
		TypeInfo(FLOAT),
		TypeInfo(DOUBLE)
	)

	private val boolVectorTypes = listOf(
		TypeInfo(ClassName(packageName, "BooleanVector2"), BOOLEAN, 2),
		TypeInfo(ClassName(packageName, "BooleanVector3"), BOOLEAN, 3),
		TypeInfo(ClassName(packageName, "BooleanVector4"), BOOLEAN, 4)
	)

	private val vectorTypes = listOf(
		TypeInfo(ClassName(packageName, "ByteVector2"), BYTE, 2),
		TypeInfo(ClassName(packageName, "ByteVector3"), BYTE, 3),
		TypeInfo(ClassName(packageName, "ByteVector4"), BYTE, 4),
		TypeInfo(ClassName(packageName, "ShortVector2"), SHORT, 2),
		TypeInfo(ClassName(packageName, "ShortVector3"), SHORT, 3),
		TypeInfo(ClassName(packageName, "ShortVector4"), SHORT, 4),
		TypeInfo(ClassName(packageName, "IntVector2"), INT, 2),
		TypeInfo(ClassName(packageName, "IntVector3"), INT, 3),
		TypeInfo(ClassName(packageName, "IntVector4"), INT, 4),
		TypeInfo(ClassName(packageName, "LongVector2"), LONG, 2),
		TypeInfo(ClassName(packageName, "LongVector3"), LONG, 3),
		TypeInfo(ClassName(packageName, "LongVector4"), LONG, 4),
		TypeInfo(ClassName(packageName, "UByteVector2"), U_BYTE, 2),
		TypeInfo(ClassName(packageName, "UByteVector3"), U_BYTE, 3),
		TypeInfo(ClassName(packageName, "UByteVector4"), U_BYTE, 4),
		TypeInfo(ClassName(packageName, "UShortVector2"), U_SHORT, 2),
		TypeInfo(ClassName(packageName, "UShortVector3"), U_SHORT, 3),
		TypeInfo(ClassName(packageName, "UShortVector4"), U_SHORT, 4),
		TypeInfo(ClassName(packageName, "UIntVector2"), U_INT, 2),
		TypeInfo(ClassName(packageName, "UIntVector3"), U_INT, 3),
		TypeInfo(ClassName(packageName, "UIntVector4"), U_INT, 4),
		TypeInfo(ClassName(packageName, "ULongVector2"), U_LONG, 2),
		TypeInfo(ClassName(packageName, "ULongVector3"), U_LONG, 3),
		TypeInfo(ClassName(packageName, "ULongVector4"), U_LONG, 4),
		TypeInfo(ClassName(packageName, "FloatVector2"), FLOAT, 2),
		TypeInfo(ClassName(packageName, "FloatVector3"), FLOAT, 3),
		TypeInfo(ClassName(packageName, "FloatVector4"), FLOAT, 4),
		TypeInfo(ClassName(packageName, "DoubleVector2"), DOUBLE, 2),
		TypeInfo(ClassName(packageName, "DoubleVector3"), DOUBLE, 3),
		TypeInfo(ClassName(packageName, "DoubleVector4"), DOUBLE, 4)
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
		(primitiveTypes + vectorTypes).forEach { (type, baseType, componentCount) ->
			val componentNames = allComponentNames.take(componentCount ?: 0)
			val mutableType = ClassName(packageName, "Mutable${type.simpleName}")

			buildFile(packageName, "${type.simpleName}Common") {
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

				when (baseType) {
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

				when (baseType) {
					BYTE, SHORT, INT, LONG,
					FLOAT, DOUBLE -> {
						extensionProperty(type, "sign", type) {
							getter {
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									"$it.sign${if (baseType == LONG) ".toLong()" else ""}"
								}
								statement("return %T($args)", type)
							}
						}
					}
				}

				// floor

				when (baseType) {
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

				when (baseType) {
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

				when (baseType) {
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
								statement("i %% 2 == ${if (type == FLOAT) "0f" else "0.0"} -> i")
								statement("else -> mix(i - 1f, i + 1f, n <= 0)")
							}
						}
					}
				}

				when (baseType) {
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

				when (baseType) {
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

				when (baseType) {
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
						parameter("scalar", baseType)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							when (baseType) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "($it %% scalar).to${baseType.simpleName}()"
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
							when (baseType) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "($it %% other.$it).to${baseType.simpleName}()"
								else -> "$it %% other.$it"
							}
						}
						statement("return %T($args)", type)
					}

					extensionFunction(mutableType, "remAssign") {
						modifiers(OPERATOR)
						parameter("scalar", baseType)
						componentNames.forEach {
							val code = when (baseType) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "$it = ($it %% scalar).to${baseType.simpleName}()"
								else -> "$it %%= scalar"
							}
							statement(code)
						}
					}

					extensionFunction(mutableType, "remAssign") {
						modifiers(OPERATOR)
						parameter("other", type)
						componentNames.forEach {
							val code = when (baseType) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "$it = ($it %% other.$it).to${baseType.simpleName}()"
								else -> "$it %%= other.$it"
							}
							statement(code)
						}
					}
				}

				// min

				if (baseType != null) {
					extensionFunction(type, "coerceAtMost") {
						parameter("maximumValue", baseType)
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
						parameter("maximumValue", baseType)
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
						parameter("minimumValue", baseType)
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
						parameter("minimumValue", baseType)
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
						parameter("minimumValue", baseType)
						parameter("maximumValue", baseType)
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
						parameter("range", ClosedRange::class.asClassName().parameterizedBy(baseType))
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
							"$it.coerceIn(range)"
						}
						statement("return %T($args)", type)
					}

					extensionFunction(mutableType, "coerceAssignIn") {
						parameter("minimumValue", baseType)
						parameter("maximumValue", baseType)
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
						parameter("range", ClosedRange::class.asClassName().parameterizedBy(baseType))
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

				when (baseType) {
					FLOAT, DOUBLE -> {
						function("mix") {
							parameter("x", type)
							parameter("y", type)
							parameter("a", baseType)
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

				fun oneAndZero(type: ClassName, baseType: ClassName?): Pair<String, String> {
					return when (type) {
						BYTE -> "1.toByte()" to "0.toByte()"
						SHORT -> "1.toShort()" to "0.toShort()"
						INT -> "1" to "0"
						LONG -> "1L" to "0L"
						U_BYTE -> "1U.toUByte()" to "0U.toUByte()"
						U_SHORT -> "1U.toUShort()" to "0U.toUShort()"
						U_INT -> "1U" to "0U"
						U_LONG -> "1UL" to "0UL"
						FLOAT -> "1f" to "0f"
						DOUBLE -> "1.0" to "0.0"
						else -> if (baseType != null) {
							val (one, zero) = oneAndZero(baseType, null)
							"${type.simpleName}($one)" to "${type.simpleName}($zero)"
						} else error("")
					}
				}

				val (one, zero) = oneAndZero(type, baseType)

				// TODO implement relational functions before step can work
				//function("step") {
				//	parameter("edge", type)
				//	parameter("x", type)
				//	returns(type)
				//	statement("return mix($one, $zero, x < edge)")
				//}

				// is vector type
				//else -> {
				//	function("step") {
				//		parameter("edge", type)
				//		parameter("x", type)
				//		returns(type)
				//		val args = componentNames.joinToString {  }
				//		statement()
				//	}
				//}

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

				when (baseType) {
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
								"${baseType!!.simpleName}.fromBits(bits.$it$unsignedConversion)"
							}
							statement("return %T($args)", type)
						}
					}
				}

				when (baseType) {
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
		vectorTypes.forEach { (type, baseType, componentCount) ->
			val componentNames = allComponentNames.take(componentCount!!)
			baseType ?: error("null base type invalid for vector types")

			buildFile(packageName, "${type.simpleName}Geometric") {
				indent("\t")

				// Dot Product

				extensionFunction(type, "dot") {
					modifiers(INFIX)
					parameter("other", type)
					returns(baseType)
					val result = componentNames.joinToString(" + ") { "$it * other.$it" }
						.let {
							when (baseType) {
								BYTE, SHORT,
								U_BYTE, U_SHORT -> "($it).to${baseType.simpleName}()"
								else -> "\n\t($it)"
							}
						}
					statement("return $result")
				}

				// Cross Product

				if (componentCount == 3) {
					extensionFunction(type, "cross") {
						modifiers(INFIX)
						parameter("other", type)
						returns(type)
						val args = componentNames
							.drop(1)
							.plus(componentNames.take(2))
							.zipWithNext()
							.joinToString(",\n\t", "\n\t", "\n") { (c1, c2) ->
								when (baseType) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "($c1 * other.$c2 - other.$c1 * $c2).to${baseType.simpleName}()"
									else -> "$c1 * other.$c2 - other.$c1 * $c2"
								}
							}
						statement("return %T($args)", type)
					}
				}
			}.apply { writeTo(commonDir.get().asFile) }
		}
	}

	private fun vectorTypes() {
		(boolVectorTypes + vectorTypes).forEach { (type, baseType, componentCount) ->
			val mutableVectorType = ClassName(packageName, "Mutable${type.simpleName}")
			val componentNames = allComponentNames.take(componentCount!!)

			val zero = when (baseType) {
				BOOLEAN -> "false"
				BYTE,
				SHORT,
				INT -> "0"
				LONG -> "0L"
				U_BYTE,
				U_SHORT,
				U_INT -> "0U"
				U_LONG -> "0UL"
				FLOAT -> "0f"
				DOUBLE -> "0.0"
				null -> error("null base type invalid for vector types")
				else -> error("invalid base type: $baseType")
			}

			buildFile(packageName, type.simpleName) {
				indent("\t")

				import("kotlin.math", "sqrt")

				buildClass(type) {
					modifiers(SEALED)

					companionObject()

					componentNames.forEach {
						property(it, baseType, ABSTRACT)
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

					if (baseType != BOOLEAN) {
						property("length", baseType) {
							getter {
								val code = when (baseType) {
									BYTE, SHORT, INT, LONG ->
										"return sqrt((this dot this).toDouble()).to${baseType.simpleName}()"
									U_BYTE, U_SHORT, U_INT, U_LONG ->
										"return sqrt((this dot this).toDouble()).to${baseType.simpleName.drop(1)}().to${baseType.simpleName}()"
									else ->
										"return sqrt(this dot this)"
								}
								statement(code)
							}
						}

						property("lengthSquared", baseType) {
							getter {
								statement("return this dot this")
							}
						}

						function("normalized") {
							returns(type)
							statement("val length = length")
							val args = componentNames.joinToString {
								when (baseType) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "($it / length).to${baseType.simpleName}()"
									else -> "$it / length"
								}
							}
							statement("return %T($args)", type)
						}

						for ((name, op) in arithmetics) {
							function(name) {
								modifiers(OPERATOR)
								parameter("scalar", baseType)
								returns(type)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									when (baseType) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "($it $op scalar).to${baseType.simpleName}()"
										else -> "$it $op scalar"
									}
								}
								statement("return %T($args)", type)
							}

							function(name) {
								modifiers(OPERATOR)
								parameter("other", type)
								returns(type)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
									when (baseType) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "($it $op other.$it).to${baseType.simpleName}()"
										else -> "$it $op other.$it"
									}
								}
								statement("return %T($args)", type)
							}
						}
					}
				}

				buildClass(mutableVectorType) {
					superclass(type)

					primaryConstructor {
						componentNames.forEach { name ->
							parameter(name, baseType)
						}
						callSuperConstructor()
					}

					secondaryConstructor {
						parameter("scalar", baseType)
						callThisConstructor(*componentNames.map { "scalar" }.toTypedArray())
					}

					secondaryConstructor {
						callThisConstructor(*componentNames.map { zero }.toTypedArray())
					}

					componentNames.forEach { name ->
						mutableProperty(name, baseType, OVERRIDE) {
							initializer(name)
						}
					}

					function("set") {
						modifiers(OPERATOR)
						parameter("index", INT)
						parameter("value", baseType)
						controlFlow("when (index)") {
							componentNames.forEachIndexed { i, it ->
								statement("$i -> $it = value")
							}
							statement("else -> throw IndexOutOfBoundsException()")
						}
					}

					if (baseType != BOOLEAN) {
						function("normalize") {
							statement("val length = length")
							componentNames.forEach {
								val code = when (baseType) {
									BYTE, SHORT,
									U_BYTE, U_SHORT -> "$it = ($it / length).to${baseType.simpleName}()"
									else -> "$it /= length"
								}
								statement(code)
							}
						}

						for ((funName, op) in arithmetics) {
							function("${funName}Assign") {
								modifiers(OPERATOR)
								parameter("scalar", baseType)
								componentNames.forEach {
									val code = when (baseType) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "$it = ($it $op scalar).to${baseType.simpleName}()"
										else -> "$it $op= scalar"
									}
									statement(code)
								}
							}

							function("${funName}Assign") {
								modifiers(OPERATOR)
								parameter("other", type)
								componentNames.forEach {
									val code = when (baseType) {
										BYTE, SHORT,
										U_BYTE, U_SHORT -> "$it = ($it $op other.$it).to${baseType.simpleName}()"
										else -> "$it $op= other.$it"
									}
									statement(code)
								}
							}
						}
					}
				}

				// Immutable Constructors

				function(type.simpleName) {
					componentNames.forEach { name ->
						parameter(name, baseType)
					}
					returns(type)
					statement("return %T(${componentNames.joinToString()})", mutableVectorType)
				}

				function(type.simpleName) {
					parameter("scalar", baseType)
					returns(type)
					statement("return %T(scalar)", mutableVectorType)
				}

				// Destructuring Operators

				componentNames.forEachIndexed { i, name ->
					extensionFunction(type, "component${i + 1}") {
						modifiers(INLINE, OPERATOR)
						returns(baseType)
						statement("return $name")
					}
				}
			}.apply { writeTo(commonDir.get().asFile) }
		}
	}
}
