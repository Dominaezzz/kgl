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
package com.kgl.glfw

import kotlin.test.*

class SampleTests {
	@Test
	@Ignore
	fun testMe() {
		assertEquals(Glfw.currentContext, null)
	}

	@Test
	@Ignore
	fun testBasicWindow() {
		Glfw.init()
		Glfw.windowHints.clientApi = ClientApi.None

		val window = Window(640, 480, "Test window")

		while (!window.shouldClose) {
			Glfw.pollEvents()
		}

		window.close()
		Glfw.terminate()
	}

	@Test
	@Ignore
	fun testPerformance() {
		Glfw.init()
		Glfw.windowHints.clientApi = ClientApi.None

		val window = Window(640, 480, "Test window", null, null)

		val start = Glfw.time

		repeat(10000) {
			window.shouldClose
			Glfw.pollEvents()
		}

		val end = Glfw.time

		println("Total ${end - start}")
		println("Per: ${(end - start) / 10000}")

		window.close()
		Glfw.terminate()
	}
}
