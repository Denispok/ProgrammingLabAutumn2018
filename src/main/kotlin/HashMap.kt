class HashMap<K, V> : MutableMap<K, V> {

    companion object {
        const val MIN_SIZE = 16
    }

    override val size: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

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

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val keys: MutableSet<K>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val values: MutableCollection<V>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

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

    inner class HashEntrySet : AbstractMutableSet<HashEntry<K, V>>() {
        override var size: Int = MIN_SIZE
        var array: Array<HashEntry<K, V>?> = Array(size) { null }


        override fun add(element: HashEntry<K, V>): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun iterator(): HashMutableIterator = HashMutableIterator()

        inner class HashMutableIterator : MutableIterator<HashEntry<K, V>> {
            var pointer: Int = -1

            override fun hasNext(): Boolean {
                val nextPointer = pointer + 1
                if (nextPointer < array.size)
                    for (i in nextPointer until array.size)
                        if (array[i] != null) return true
                return false
            }

            override fun next(): HashEntry<K, V> {
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
        val hash = key?.hashCode() ?: 0
        override fun setValue(newValue: V): V {
            val oldValue = value
            value = newValue
            return oldValue
        }
    }
}