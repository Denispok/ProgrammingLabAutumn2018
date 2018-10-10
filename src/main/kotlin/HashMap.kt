class HashMap<K, V> : MutableMap<K, V> {

    companion object {
        const val MIN_SIZE = 16
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> = HashEntrySet()
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        var array: Array<MutableMap.MutableEntry<K, V>?> = Array(MIN_SIZE) { null }

        override fun add(element: MutableMap.MutableEntry<K, V>): Boolean {
            val index = indexFor(element.key?.hashCode(), array.size)
            if (array[index] == null) array[index] = element
            else {
                var nextIndex: Int? = null
                for (i in index until array.size) {
                    val currentElement = array[i]
                    if (currentElement == null) nextIndex = i
                    else if (currentElement.key?.hashCode() == element.key?.hashCode() && currentElement.key?.equals(element.key) ?: (element.key == null))
                        return false
                }
                if (nextIndex == null)
                    for (i in 0 until index) {
                        val currentElement = array[i]
                        if (currentElement == null) nextIndex = i
                        else if (currentElement.key?.hashCode() == element.key?.hashCode() && currentElement.key?.equals(element.key) ?: (element.key == null))
                            return false
                    }
                array[nextIndex!!] = element
            }
            if (++size == array.size) increaseArray()
            return true
        }

        private fun increaseArray() {
            val oldArray = array
            array = Array(array.size * 2) { null }
            size = 0
            oldArray.forEach { add(it!!) }
        }

        override fun iterator(): HashMutableIterator = HashMutableIterator()

        inner class HashMutableIterator : MutableIterator<MutableMap.MutableEntry<K, V>> {
            var pointer: Int = -1

            override fun hasNext(): Boolean {
                val nextPointer = pointer + 1
                if (nextPointer < array.size)
                    for (i in nextPointer until array.size)
                        if (array[i] != null) return true
                return false
            }

            override fun next(): MutableMap.MutableEntry<K, V> {
                for (i in pointer + 1 until array.size) {
                    if (array[i] != null) {
                        pointer = i
                        return array[i]!!
                    }
                }
                throw ArrayIndexOutOfBoundsException()
            }

            override fun remove() {
                array[pointer] = null
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