class HashMap<K, V> : MutableMap<K, V> {

    companion object {
        const val MIN_SIZE = 16
        const val DELETED = true
    }

    override val size: Int
        get() = entries.size

    override fun containsKey(key: K): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun containsValue(value: V): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun get(key: K): V? {
        val entry = entries.array[entries.findEntryIndexByKey(key)]
        if (entry == null || entry == DELETED) return null
        return (entry as MutableMap.MutableEntry<K, V>).value
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val entries: HashEntrySet = HashEntrySet()
    override val keys: MutableSet<K>
        get() {
            val set = mutableSetOf<K>()
            for (i in entries) set.add(i.key)
            return set
        }
    override val values: MutableCollection<V>
        get() {
            val set = mutableSetOf<V>()
            for (i in entries) set.add(i.value)
            return set
        }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun put(key: K, value: V): V? {
        val previous = get(key)
        entries.add(HashEntry(key, value))
        return previous
    }

    override fun putAll(from: Map<out K, V>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(key: K): V? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun indexFor(hash: Int?, size: Int): Int = if (hash == null) 0 else hash and (size - 1)

    inner class HashEntrySet : AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
        override var size: Int = 0
        var array: Array<Any?> = Array(MIN_SIZE) { null }

        override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
            val index = findEntryIndexByKey(element.key)
            if (array[index] != null && array[index] != DELETED) {
                if ((array[index] as MutableMap.MutableEntry<K, V>).value == element.value) return false
            } else size++
            array[index] = element
            if (size == array.size) increaseArray()
            return true
        }

        fun findEntryIndexByKey(key: K): Int {
            var i = indexFor(key?.hashCode(), size)
            var firstDeleted: Int? = null
            while (true) {
                if (array[i] == null) {
                    if (firstDeleted != null) i = firstDeleted
                    break
                }
                if (array[i] == DELETED) {
                    if (firstDeleted == null) firstDeleted = i
                    continue
                }
                val currentElement = array[i] as MutableMap.MutableEntry<K, V>
                if (currentElement.key?.equals(key) ?: (key == null)) break
                i++
            }
            return i
        }

        private fun increaseArray() {
            val oldArray = array
            array = Array(array.size * 2) { null }
            size = 0
            oldArray.forEach { add(it as MutableMap.MutableEntry<K, V>) }
        }

        override fun iterator(): HashMutableIterator = HashMutableIterator()

        inner class HashMutableIterator : MutableIterator<MutableMap.MutableEntry<K, V>> {
            private var pointer: Int = -1

            override fun hasNext(): Boolean {
                val nextPointer = pointer + 1
                if (nextPointer < array.size)
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
}