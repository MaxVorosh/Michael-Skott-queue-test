import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Test

class MSQueueTest {
    val q = MSQueue<Int>()

    @Operation
    fun enqueue(x: Int) {
        q.enqueue(x)
    }

    @Operation
    fun dequeue() {
        q.dequeue()
    }

    @Test
    fun stressTest() = StressOptions()
        .threads(1).actorsPerThread(10).iterations(100)
        .check(this::class)

    @Test
    fun stressParallelTest() = StressOptions()
        .actorsBefore(10)
        .threads(2).actorsPerThread(10).iterations(10)
        .actorsAfter(10)
        .check(this::class)

    @Test
    fun stressMultiParallelTest() = StressOptions()
        .actorsBefore(10)
        .threads(10).actorsPerThread(1).iterations(5)
        .actorsAfter(10)
        .check(this::class)

    @Test
    fun stressEmptyQueueParallelTest() = StressOptions()
        .threads(3).actorsPerThread(3).iterations(1)
        .check(this::class)
}