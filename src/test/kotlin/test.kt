import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.annotations.*
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.ModelCheckingOptions
import org.jetbrains.kotlinx.lincheck.strategy.stress.StressOptions
import org.junit.jupiter.api.Test
import java.util.*
import java.util.Optional.empty
import java.util.Optional.of

class SequentialQueue {
    private val s = LinkedList<Int>()

    fun enqueue(x: Int) { s.add(x) }
    fun dequeue(): Optional<Int> {
        if (s.size == 0) {
            return empty<Int>()
        }
        val res: Int = s.poll()
        return of<Int>(res)
    }
}

class MSQueueTest {
    val q = MSQueue<Int>()

    @Operation
    fun enqueue(x: Int) {
        q.enqueue(x)
    }

    @Operation
    fun dequeue(): Optional<Int> {
        return q.dequeue()
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

    @Test
    fun modelCheckingTest() = ModelCheckingOptions()
        .actorsBefore(10)
        .threads(3).actorsPerThread(3).iterations(1)
        .actorsAfter(10)
        .check(this::class)

    @Test
    fun stressSequentialSpecTest() = StressOptions()
        .actorsBefore(10)
        .threads(3).actorsPerThread(3).iterations(3)
        .actorsAfter(10)
        .sequentialSpecification(SequentialQueue::class.java)
        .check(this::class)
}