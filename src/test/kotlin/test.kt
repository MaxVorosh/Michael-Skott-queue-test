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
    fun stressTest() = StressOptions().check(this::class)
    
}