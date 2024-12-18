import java.util.Optional
import java.util.concurrent.atomic.AtomicReference

import java.util.Objects.requireNonNull
import java.util.Optional.empty
import java.util.Optional.of


class MSQueue<E> {
    private class Node<E> {
        var value: AtomicReference<E>
        var next: AtomicReference<Node<E>>

        constructor() {
            value = AtomicReference<E>()
            next = AtomicReference<Node<E>>()
        }

        constructor(value: E) {
            this.value = AtomicReference<E>(value)
            next = AtomicReference<Node<E>>()
        }
    }

    private val head: AtomicReference<Node<E>>
    private val tail: AtomicReference<Node<E>>

    init {
        val dummyNode = Node<E>()
        head = AtomicReference<Node<E>>(dummyNode)
        tail = AtomicReference<Node<E>>(dummyNode)
    }

    fun enqueue(element: E) {
        val newNode = Node<E>(requireNonNull<E>(element))
        while (true) {
            val prevTail: Node<E> = tail.get()
            if (prevTail.next.compareAndSet(null, newNode)) {
                tail.compareAndSet(prevTail, newNode)
                return
            } else {
                tail.compareAndSet(prevTail, prevTail.next.get())
            }
        }
    }

    fun dequeue(): Optional<E & Any> {
        while (true) {
            val prevHead: Node<E> = head.get()
            val prevTail: Node<E> = tail.get()
            val nextHead = head.get().next.get()
            if (prevHead == prevTail) {
                if (nextHead == null) {
                    return empty()
                } else {
                    tail.compareAndSet(prevTail, nextHead)
                }
            }
            else {
                if (head.compareAndSet(prevHead, nextHead)) {
                    val element: E = nextHead.value.get()
                    return of(element!!)
                }
            }
        }
    }
}