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
		TypeInfo(BOOLEAN),
		TypeInfo(INT),
		TypeInfo(LONG),
		TypeInfo(U_INT),
		TypeInfo(U_LONG),
		TypeInfo(FLOAT),
		TypeInfo(DOUBLE)
	)

	private val vectorTypes = primitiveTypes.flatMap { (type: ClassName) ->
		(2..4).map { c ->
			TypeInfo(ClassName(packageName, "${type.simpleName}Vector$c"), type, c)
		}
	}

	private val allComponentNames = listOf("x", "y", "z", "w")
	private val arithmetics = listOf("plus" to "+", "minus" to "-", "times" to "*", "div" to "/")


	@TaskAction
	fun generate() {
		commonFunctions()
		geometricFunctions()
		vectorTypes()
		vectorRelationalFunctions()
	}

	private fun commonFunctions() {
		(primitiveTypes + vectorTypes).forEach { (type, baseType, componentCount) ->
			val componentNames = allComponentNames.take(componentCount ?: 0)
			val mutableType = ClassName(packageName, "Mutable${type.simpleName}")

			val boolVectorType = vectorTypes.find { it.baseType == BOOLEAN && it.componentCount == componentCount }

			val (zero, vectorZero) = literal("0", type, baseType)
			val (one, vectorOne) = literal("1", type, baseType)
			val (two, vectorTwo) = literal("2", type, baseType)
			val (three, vectorThree) = literal("3", type, baseType)

			buildFile(packageName, "${type.simpleName}Common") {
				indent("\t")

				// abs

				when (baseType) {
					INT, LONG,
					FLOAT, DOUBLE -> {
						import("kotlin.math", "abs")

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

				when (baseType) {
					INT, LONG,
					FLOAT, DOUBLE -> {
						import("kotlin.math", "sign")

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
						import("kotlin.math", "floor")

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
						import("kotlin.math", "truncate")

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
						import("kotlin.math", "roundToInt")
						import("kotlin.math", "roundToLong")

						val intVectorType = vectorTypes.find { it.baseType == INT && it.componentCount == componentCount }!!.type
						val longVectorType = vectorTypes.find { it.baseType == LONG && it.componentCount == componentCount }!!.type

						extensionFunction(type, "roundToInt") {
							kdoc(
								"""
								Rounds each component `x` to the nearest integer and stores the result in [${intVectorType.simpleName}].
								Ties are rounded towards positive infinity.
								
								Special cases:
								- `x.roundToInt() == Int.MAX_VALUE` when `x > Int.MAX_VALUE`
								- `x.roundToInt() == Int.MIN_VALUE` when `x < Int.MIN_VALUE`
								
								@throws IllegalArgumentException when this value is `NaN`
								""".trimIndent()
							)
							returns(intVectorType)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.roundToInt()" }
							statement("return %T($args)", intVectorType)
						}

						extensionFunction(type, "roundToLong") {
							kdoc(
								"""
								Rounds each component `x` to the nearest integer and stores the result to [${longVectorType.simpleName}].
								Ties are rounded towards positive infinity.
								
								Special cases:
								- `x.roundToLong() == Long.MAX_VALUE` when `x > Long.MAX_VALUE`
								- `x.roundToLong() == Long.MIN_VALUE` when `x < Long.MIN_VALUE`
								
								@throws IllegalArgumentException when this value is `NaN`
								""".trimIndent()
							)
							returns(longVectorType)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.roundToLong()" }
							statement("return %T($args)", longVectorType)
						}
					}
				}

				// roundEven

				when (baseType) {
					FLOAT, DOUBLE -> {
						import("kotlin.math", "round")

						extensionFunction(type, "round") {
							kdoc(
								"""
								Rounds each component `x` to the closest integer with ties rounded towards even integers.
								
								Special cases:
								- `round(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer. 
								""".trimIndent()
							)
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "round($it)" }
							statement("return %T($args)", type)
						}

						extensionFunction(mutableType, "roundAssign") {
							kdoc(
								"""
								Rounds each component to the closest integer with ties rounded towards even integers.
								
								Special cases:
								- `round(x)` is `x` where `x` is `NaN` or `+Inf` or `-Inf` or already a mathematical integer. 
								""".trimIndent()
							)

							componentNames.forEach {
								statement("$it = round($it)")
							}
						}
					}
				}

				// ceil

				when (baseType) {
					FLOAT, DOUBLE -> {
						import("kotlin.math", "ceil")

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
								statement("return this - this.toLong()")
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

				if (baseType != null && baseType != BOOLEAN) {
					extensionFunction(type, "rem") {
						modifiers(OPERATOR)
						parameter("scalar", baseType)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it %% scalar" }
						statement("return %T($args)", type)
					}

					extensionFunction(type, "rem") {
						modifiers(OPERATOR)
						parameter("other", type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it %% other.$it" }
						statement("return %T($args)", type)
					}

					extensionFunction(mutableType, "remAssign") {
						modifiers(OPERATOR)
						parameter("scalar", baseType)
						componentNames.forEach {
							statement("$it %%= scalar")
						}
					}

					extensionFunction(mutableType, "remAssign") {
						modifiers(OPERATOR)
						parameter("other", type)
						componentNames.forEach {
							statement("$it %%= other.$it")
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

				if (baseType != null) {
					function("mix") {
						parameter("x", type)
						parameter("y", type)
						parameter("a", boolVectorType!!.type)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "mix(x.$it, y.$it, a.$it)" }
						statement("return %T($args)", type)
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

				when (baseType) {
					// is primitive type
					null -> {
						function("step") {
							parameter("edge", type)
							parameter("x", type)
							returns(type)
							statement("return mix($one, $zero, x < edge)")
						}
					}

					// is vector type
					else -> {
						function("step") {
							parameter("edge", type)
							parameter("x", type)
							returns(type)
							statement("return mix(\n\t$vectorOne,\n\t$vectorZero,\n\tx.lessThan(edge)\n)")
						}

						function("step") {
							parameter("edge", baseType)
							parameter("x", type)
							returns(type)
							statement("return step(%T(edge), x)", type)
						}
					}
				}

				// smoothStep

				when (type) {
					FLOAT, DOUBLE -> {
						function("smoothStep") {
							parameter("edge0", type)
							parameter("edge1", type)
							parameter("x", type)
							returns(type)
							statement("val tmp = ((x - edge0) / (edge1 - edge0)).coerceIn($zero, $one)")
							statement("return tmp * tmp * ($three - $two * tmp)")
						}
					}

					// is vector type
					else -> when (baseType) {
						FLOAT, DOUBLE -> {
							function("smoothStep") {
								parameter("edge0", type)
								parameter("edge1", type)
								parameter("x", type)
								returns(type)
								statement("val tmp = ((x - edge0) / (edge1 - edge0)).coerceIn($vectorZero, $vectorOne)")
								statement("return tmp * tmp * ($vectorThree - $vectorTwo * tmp)")
							}

							function("smoothStep") {
								parameter("edge0", baseType)
								parameter("edge1", baseType)
								parameter("x", type)
								returns(type)
								statement("return smoothStep(%T(edge0), %T(edge1), x)", type, type)
							}
						}
					}
				}

				// isNaN

				when (baseType) {
					FLOAT, DOUBLE -> {
						extensionFunction(type, "isNaN") {
							returns(boolVectorType!!.type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.isNaN()" }
							statement("return %T($args)", boolVectorType.type)
						}
					}
				}

				// isInf

				when (baseType) {
					FLOAT, DOUBLE -> {
						extensionFunction(type, "isInfinite") {
							returns(boolVectorType!!.type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.isInfinite()" }
							statement("return %T($args)", boolVectorType.type)
						}
					}
				}

				// isFinite (from kotlin stdlib)

				when (baseType) {
					FLOAT, DOUBLE -> {
						extensionFunction(type, "isFinite") {
							returns(boolVectorType!!.type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it.isFinite()" }
							statement("return %T($args)", boolVectorType.type)
						}
					}
				}

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

						extensionFunction(mutableType.nestedClass("Companion"), "fromBits") {
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

				// frexp

				when (type) {
					FLOAT -> {
						extensionFunction(type, "toFractionAndExponent") {
							returns(Pair::class.asClassName().parameterizedBy(type, INT))
							code(
								"""
								// implementation from glibc math
								var fraction = this
								var hx = fraction.toRawBits()
								var ix = hx and 0x7FFF_FFFF
								var exponent = 0
								if (ix >= 0x7F80_0000 || ix == 0) {
									return fraction + fraction to exponent
								}
								if (ix < 0x0080_0000) {
									fraction *= 2e25f
									hx = fraction.toRawBits()
									ix = hx and 0x7FFF_FFFF
									exponent = -25
								}
								exponent += (ix shr 23) - 126
								hx = (hx and 0x807F_FFFF.toInt()) or 0x3F00_0000
								fraction = Float.fromBits(hx)
								return fraction to exponent
								""".trimIndent()
							)
						}
					}
					DOUBLE -> {
						extensionFunction(type, "toFractionAndExponent") {
							returns(Pair::class.asClassName().parameterizedBy(type, INT))
							code(
								"""
								// implementation from glibc math
								var fraction = this
								var ix = fraction.toRawBits()
								var ex = 0x7FF and (ix shr 52).toInt()
								var exponent = 0

								if (ex != 0x7FF && fraction != 0.0) {
									exponent = ex - 1022
									if (ex == 0) {
										fraction *= 2e54
										ix = fraction.toRawBits()
										ex = 0x7FF and (ix shr 52).toInt()
										exponent = ex - 1022 - 54
									}
									ix = (ix and 0x800F_FFFF_FFFF_FFFFu.toLong()) or 0x3FE0_0000_0000_0000
									fraction = Double.fromBits(ix)
								} else {
									fraction += fraction
								}
								return fraction to exponent
								""".trimIndent()
							)
						}
					}
				}

				when (baseType) {
					FLOAT, DOUBLE -> {
						val intVectorType = vectorTypes.find { it.baseType == INT && it.componentCount == componentCount }!!.type
						extensionFunction(type, "toFractionAndExponent") {
							returns(Pair::class.asClassName().parameterizedBy(type, intVectorType))
							val (fargs, iargs) = componentNames.map {
								statement("val (f$it, i$it) = $it.toFractionAndExponent()")
								"f$it" to "i$it"
							}.toMap().run { keys.joinToString() to values.joinToString() }
							statement("return %T($fargs) to %T($iargs)", type, intVectorType)
						}
					}
				}

				// ldexp

				when (type) {
					FLOAT, DOUBLE -> {
						import("kotlin.math", "pow")

						extensionFunction(type.nestedClass("Companion"), "fromFractionAndExponent") {
							parameter("value", type)
							parameter("exp", INT)
							returns(type)
							code(
								"""
								if (value.isInfinite() || value == $zero) return value
								val result = value * $two.pow(exp)
								if (result.isInfinite() || value == $zero) error("value out of range!")
								return result
								""".trimIndent()
							)
						}
					}
				}

				when (baseType) {
					FLOAT, DOUBLE -> {
						val intVectorType = vectorTypes.find { it.baseType == INT && it.componentCount == componentCount }!!.type
						extensionFunction(type.nestedClass("Companion"), "fromFractionAndExponent") {
							parameter("value", type)
							parameter("exp", intVectorType)
							returns(type)
							val args = componentNames.joinToString(",\n\t", "\n\t", "\n") {
								"${baseType.simpleName}.fromFractionAndExponent(value.$it, exp.$it)"
							}
							statement("return %T($args)", type)
						}
					}
				}
			}.writeTo(commonDir.get().asFile)
		}
	}

	private fun geometricFunctions() {
		vectorTypes.forEach { (type, baseType, componentCount) ->
			if (baseType == BOOLEAN) return@forEach

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
					statement("return \n\t($result)")
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
								"$c1 * other.$c2 - other.$c1 * $c2"
							}
						statement("return %T($args)", type)
					}
				}
			}.writeTo(commonDir.get().asFile)
		}
	}

	private fun vectorTypes() {
		vectorTypes.forEach { (type, baseType, componentCount) ->
			val mutableVectorType = ClassName(packageName, "Mutable${type.simpleName}")
			val componentNames = allComponentNames.take(componentCount!!)
			baseType ?: error("null base type invalid for vectors")

			val (zero) = literal("0", baseType, null)
			val (one) = literal("1", baseType, null)

			buildFile(packageName, type.simpleName) {
				indent("\t")

				import("kotlin.math", "sqrt")

				buildClass(type) {
					modifiers(SEALED)

					companionObject {
						property("ZERO", type) {
							initializer("%T($zero)", type)
						}
						property("ONE", type) {
							initializer("%T($one)", type)
						}
						componentNames.forEach { outer ->
							property(outer.toUpperCase(), type) {
								val args = componentNames.joinToString { inner ->
									if (outer == inner) one else zero
								}
								initializer("%T($args)", type)
							}
						}
					}

					componentNames.forEach {
						property(it, baseType, ABSTRACT)
					}

					function("get") {
						modifiers(OPERATOR)
						parameter("index", INT)
						returns(baseType)
						controlFlow("return when (index)") {
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
									INT, LONG ->
										"return sqrt((this dot this).toDouble()).to${baseType.simpleName}()"
									U_INT, U_LONG ->
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
							val args = componentNames.joinToString { "$it / length" }
							statement("return %T($args)", type)
						}

						for ((name, op) in arithmetics) {
							function(name) {
								modifiers(OPERATOR)
								parameter("scalar", baseType)
								returns(type)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it $op scalar" }
								statement("return %T($args)", type)
							}

							function(name) {
								modifiers(OPERATOR)
								parameter("other", type)
								returns(type)
								val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it $op other.$it" }
								statement("return %T($args)", type)
							}
						}
					}
				}

				buildClass(mutableVectorType) {
					superclass(type)

					companionObject()

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
								statement("$it /= length")
							}
						}

						for ((funName, op) in arithmetics) {
							function("${funName}Assign") {
								modifiers(OPERATOR)
								parameter("scalar", baseType)
								componentNames.forEach {
									statement("$it $op= scalar")
								}
							}

							function("${funName}Assign") {
								modifiers(OPERATOR)
								parameter("other", type)
								componentNames.forEach {
									statement("$it $op= other.$it")
								}
							}
						}
					}

					function("equals") {
						modifiers(OVERRIDE)
						parameter("other", ANY.copy(nullable = true))
						returns(BOOLEAN)
						statement("if (other === this) return true")
						statement("if (other == null) return false")
						statement("other as ${type.simpleName}")
						componentNames.forEach {
							statement("if ($it != other.$it) return false")
						}
						statement("return true")
					}

					function("hashCode") {
						modifiers(OVERRIDE)
						returns(INT)
						componentNames.forEachIndexed { i, it ->
							statement(
								if (i == 0) "var result = $it.hashCode()" else "result = 31 * result + $it.hashCode()"
							)
						}
						statement("return result")
					}

					function("toString") {
						modifiers(OVERRIDE)
						returns(STRING)
						statement("return %P", componentNames.joinToString(", ", "[", "]") { "$$it" })
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
					parameter("scalar", baseType) {
						defaultValue(zero)
					}
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
			}.writeTo(commonDir.get().asFile)
		}
	}

	private fun vectorRelationalFunctions() {
		vectorTypes.forEach { (type, baseType, componentCount) ->
			val componentNames = allComponentNames.take(componentCount!!)
			baseType!!

			val boolVectorType = vectorTypes.find { it.baseType == BOOLEAN && it.componentCount == componentCount }!!

			buildFile(packageName, "${type.simpleName}Relational") {

				// lessThan

				extensionFunction(type, "lessThan") {
					modifiers(INFIX)
					parameter("other", type)
					returns(boolVectorType.type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it < other.$it" }
					statement("return %T($args)", boolVectorType.type)
				}

				// lessThanEqual

				extensionFunction(type, "lessThanEqual") {
					modifiers(INFIX)
					parameter("other", type)
					returns(boolVectorType.type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it <= other.$it" }
					statement("return %T($args)", boolVectorType.type)
				}

				// greaterThan

				extensionFunction(type, "greaterThan") {
					modifiers(INFIX)
					parameter("other", type)
					returns(boolVectorType.type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it > other.$it" }
					statement("return %T($args)", boolVectorType.type)
				}

				// greaterThanEqual

				extensionFunction(type, "greaterThanEqual") {
					modifiers(INFIX)
					parameter("other", type)
					returns(boolVectorType.type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it >= other.$it" }
					statement("return %T($args)", boolVectorType.type)
				}

				// equal

				extensionFunction(type, "equal") {
					modifiers(INFIX)
					parameter("other", type)
					returns(boolVectorType.type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it == other.$it" }
					statement("return %T($args)", boolVectorType.type)
				}

				// notEqual

				extensionFunction(type, "notEqual") {
					modifiers(INFIX)
					parameter("other", type)
					returns(boolVectorType.type)
					val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "$it != other.$it" }
					statement("return %T($args)", boolVectorType.type)
				}

				if (baseType == BOOLEAN) {
					// should the `any` and `all` functionality for collections from stdlib be generalized to all vector types?

					// any

					extensionFunction(type, "any") {
						returns(BOOLEAN)
						val result = componentNames.joinToString(" || ") { it }
						statement("return $result")
					}

					// all

					extensionFunction(type, "all") {
						returns(BOOLEAN)
						val result = componentNames.joinToString(" && ") { it }
						statement("return $result")
					}

					// not_

					extensionFunction(type, "not") {
						modifiers(OPERATOR)
						returns(type)
						val args = componentNames.joinToString(",\n\t", "\n\t", "\n") { "!$it" }
						statement("return %T($args)", type)
					}
				}
			}.writeTo(commonDir.get().asFile)
		}
	}


	private fun literal(literal: String, type: ClassName, baseType: ClassName?): Pair<String, String?> {
		return when (baseType) {
			null -> when (type) {
				BOOLEAN -> if (literal == "0") "false" to null else "true" to null
				INT -> literal to null
				LONG -> "${literal}L" to null
				U_INT -> "${literal}U" to null
				U_LONG -> "${literal}UL" to null
				FLOAT -> "${literal}f" to null
				DOUBLE -> "${literal}.0" to null
				else -> error("invalid type: $type")
			}
			else -> {
				val (one) = literal(literal, baseType, null)
				one to "${type.simpleName}($one)"
			}
		}
	}
}
