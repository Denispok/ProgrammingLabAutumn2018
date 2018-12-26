class HashMap<K, V> : MutableMap<K, V> {

    companion object {
        const val INITIAL_SIZE = 16
        const val DELETED = true
    }

    override val size: Int
        get() = entries.size

    override fun containsKey(key: K): Boolean = keys.contains(key)

    override fun containsValue(value: V): Boolean = values.contains(value)

    override fun get(key: K): V? {
        val entry = entries.array[entries.findEntryIndexByKey(key)]
        if (entry == null || entry == DELETED) return null
        return (entry as MutableMap.MutableEntry<K, V>).value
    }

    override fun isEmpty(): Boolean = size == 0

    override val entries: HashEntrySet = HashEntrySet()
    override val keys: MutableSet<K> = HashKeysSet()
    override val values: MutableCollection<V> = HashValuesCollection()

    override fun clear() {
        entries.clear()
    }

    override fun put(key: K, value: V): V? {
        val previous = get(key)
        entries.add(HashEntry(key, value))
        return previous
    }

    override fun putAll(from: Map<out K, V>) {
        entries.increaseArrayTo(from.size)
        from.entries.forEach {
            put(it.key, it.value)
        }
    }

    override fun remove(key: K): V? {
        val index = entries.findEntryIndexByKey(key)
        if (entries.array[index] == null || entries.array[index] == DELETED) return null
        val previous = (entries.array[index] as MutableMap.MutableEntry<K, V>).value
        entries.array[index] = DELETED
        entries.size--
        return previous
    }

    inner class HashEntrySet : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
        override var size: Int = 0
        var array: Array<Any?> = Array(INITIAL_SIZE) { null }

        override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
            val index = findEntryIndexByKey(element.key)
            if (array[index] != null && array[index] != DELETED) {
                if ((array[index] as MutableMap.MutableEntry<K, V>).value == element.value) return false
            } else size++
            array[index] = element
            if (size == array.size) increaseArray(2)
            return true
        }

        fun findEntryIndexByKey(key: K): Int {
            val firstIndex = indexFor(key?.hashCode())
            var i = firstIndex
            var firstDeleted: Int? = null
            do {
                if (array[i] == null) {
                    if (firstDeleted != null) i = firstDeleted
                    break
                }
                if (array[i] == DELETED) {
                    if (firstDeleted == null) firstDeleted = i
                    if (i + 1 == array.size) i = 0 else i++
                    continue
                }
                val currentElement = array[i] as MutableMap.MutableEntry<K, V>
                if (currentElement.key?.equals(key) ?: (key == null)) break
                if (i + 1 == array.size) i = 0 else i++
            } while (i != firstIndex)
            return i
        }

        override fun clear() {
            array = Array(INITIAL_SIZE) { null }
            size = 0
        }

        private fun indexFor(hash: Int?): Int = if (hash == null) 0 else hash and (array.size - 1)

        private fun increaseArray(multiplier: Int) {
            val oldArray = array
            array = Array(array.size * multiplier) { null }
            size = 0
            oldArray.forEach { if (it != null && it != DELETED) add(it as MutableMap.MutableEntry<K, V>) }
        }

        fun increaseArrayTo(size: Int) {
            if (size > array.size) {
                var newSize = INITIAL_SIZE
                while (newSize < size) newSize *= 2
                increaseArray(newSize / array.size)
            }
        }

        override fun iterator(): HashMutableIterator = HashMutableIterator()

        inner class HashMutableIterator : MutableIterator<MutableMap.MutableEntry<K, V>> {
            private var pointer: Int = -1

            override fun hasNext(): Boolean {
                val nextPointer = pointer + 1
                for (i in nextPointer until array.size)
                    if (array[i] != null && array[i] != DELETED) return true
                return false
            }

            override fun next(): MutableMap.MutableEntry<K, V> {
                for (i in pointer + 1 until array.size) {
                    if (array[i] != null && array[i] != DELETED) {
                        pointer = i
                        return array[i] as MutableMap.MutableEntry<K, V>
                    }
                }
                throw ArrayIndexOutOfBoundsException()
            }

            override fun remove() {
                array[pointer] = DELETED
                size--
            }
        }
    }

    inner class HashEntry<K, V>(override val key: K, override var value: V) : MutableMap.MutableEntry<K, V> {
        override fun setValue(newValue: V): V {
            val oldValue = value
            value = newValue
            return oldValue
        }
    }

    inner class HashKeysSet : AbstractMutableSet<K>() {
        override val size: Int
            get() = entries.size

        override fun add(element: K): Boolean {
            throw UnsupportedOperationException()
        }

        override fun iterator(): MutableIterator<K> = object : MutableIterator<K> {
            val entrySetIterator = entries.iterator()

            override fun hasNext(): Boolean = entrySetIterator.hasNext()

            override fun next(): K = entrySetIterator.next().key

            override fun remove() = entrySetIterator.remove()
        }
    }

    inner class HashValuesCollection : AbstractMutableCollection<V>() {
        override val size: Int
            get() = entries.size

        override fun add(element: V): Boolean {
            throw UnsupportedOperationException()
        }

        override fun iterator(): MutableIterator<V> = object : MutableIterator<V> {
            val entrySetIterator = entries.iterator()

            override fun hasNext(): Boolean = entrySetIterator.hasNext()

            override fun next(): V = entrySetIterator.next().value

            override fun remove() = entrySetIterator.remove()
        }
    }
}