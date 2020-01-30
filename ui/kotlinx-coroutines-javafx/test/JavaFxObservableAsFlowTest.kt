package kotlinx.coroutines.javafx

import javafx.beans.property.SimpleIntegerProperty
import kotlinx.coroutines.TestBase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class JavaFxObservableAsFlowTest : TestBase() {

    @Before
    fun setup() {
        ignoreLostThreads("JavaFX Application Thread", "Thread-", "QuantumRenderer-", "InvokeLaterDispatcher")
    }

    @Test
    fun testFlowOrder() = runTest {
        if (!initPlatform()) {
            println("Skipping JavaFxTest in headless environment")
            return@runTest // ignore test in headless environments
        }

        val integerProperty = SimpleIntegerProperty(0)
        val n = 10000 * stressTestMultiplier
        val flow = integerProperty.asFlow().takeWhile { j -> j != n }
        newSingleThreadContext("setter").use { pool ->
            launch(pool) {
                for (i in 1..n) {
                    launch(Dispatchers.JavaFx) {
                        integerProperty.set(i)
                    }
                }
            }
            var i = -1
            flow.collect { j ->
                // elements are neither repeated nor shuffled
                assertTrue(i < (j as Int))
                i = j
            }
            // at least one element is present
            assertTrue(i != -1)
        }
    }

    @Test
    fun testConflation() = runTest {
        if (!initPlatform()) {
            println("Skipping JavaFxTest in headless environment")
            return@runTest // ignore test in headless environments
        }

        val END_MARKER = -1
        val integerProperty = SimpleIntegerProperty(0)
        val flow = integerProperty.asFlow().takeWhile { j -> j != END_MARKER }
        launch(start = CoroutineStart.UNDISPATCHED) {
            withContext(Dispatchers.JavaFx) {
                integerProperty.set(1)
            }
            withContext(Dispatchers.JavaFx) {
                integerProperty.set(-2) // should be skipped
                integerProperty.set(2)
            }
            withContext(Dispatchers.JavaFx) {
                integerProperty.set(END_MARKER)
            }
        }

        flow.collect { i ->
            assertTrue(i == 1 || i == 2)
        }
    }

    @Test
    fun cancellationRaceStressTest() = runTest {
        if (!initPlatform()) {
            println("Skipping JavaFxTest in headless environment")
            return@runTest // ignore test in headless environments
        }

        val integerProperty = SimpleIntegerProperty(0)
        val flow = integerProperty.asFlow()
        var i = 1
        val n = 1000 * stressTestMultiplier
        newSingleThreadContext("collector").use { pool ->
            repeat (n) {
                launch(pool) {
                    flow.first()
                }
                withContext(Dispatchers.JavaFx) {
                    integerProperty.set(i)
                }
                i += 1
            }
        }
    }
}
