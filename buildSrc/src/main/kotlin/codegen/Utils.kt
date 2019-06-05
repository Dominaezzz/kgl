/**
 * Copyright [2019] [Dominic Fischer]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codegen

import org.w3c.dom.Element
import org.w3c.dom.Node

internal fun getTypeDeclRegex(type: String, name: String) : Regex {
	return Regex("""^(const )?(?:struct )?($type)\s*(\*(?:(?:\s?const)?\*)?)?\s*$name(?:\[([0-9]+|[A-Z_]+)])?$""")
}

internal fun toType(typeDecl: String, type: String, name: String): CTypeDecl {
	val matchResult = getTypeDeclRegex(type, name).matchEntire(typeDecl) ?: TODO("$typeDecl failed TypeDecl regex")
	val (const, actualType, stars, count) = matchResult.destructured
	return CTypeDecl(actualType, const.isNotBlank(), stars.count { it == '*' }, count)
}

internal fun Node.getChildren(name: String): Sequence<Element> {
	return childNodes.run { (0 until length).asSequence().map { item(it) } }
			.filter { it.nodeName == name }
			.filter { it.nodeType == Node.ELEMENT_NODE }
			.map { it as Element }
}

internal fun Node.getChild(name: String) = getChildren(name).single()
