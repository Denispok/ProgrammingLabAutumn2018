import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HashTest {

    @Test
    fun simpleTest() {
        val map = HashMap<String, String>()
        for (i in 1..40) map["$i"] = (i * i).toString()
        for (i in 1..20) map.remove("$i")
        assertEquals(20, map.size)
        for (i in 21..40) assertEquals((i * i).toString(), map["$i"])
        map.clear()
        assertTrue(map.isEmpty())
        for (i in 1..40) map["$i"] = (i * i).toString()
        for (i in 1..40) assertEquals((i * i).toString(), map["$i"])
    }

    @Test
    fun putGetTest() {
        val map = HashMap<Int, String>()
        map[16] = "sixteen"
        map[17] = "seventeen"
        map[1] = "one"
        map.remove(1)
        assertEquals("seventeen", map.remove(17))
        assertEquals(null, map[1])
        map[1] = "one"
        assertEquals("one", map.put(1, "another one"))
        assertEquals("another one", map[1])
        map.clear()

        for (i in 0..63) {
            map[i] = "$i"
        }
        for (i in 0..63) {
            assertEquals("$i", map[i])
        }
        assertEquals(64, map.size)
        map.clear()

        val map2 = mutableMapOf<Int, String>()
        for (i in 0..63) {
            map2[i] = "$i"
        }
        map.putAll(map2.toMap())
        assertEquals(64, map.size)
        for (i in 0..63) {
            assertEquals("$i", map[i])
        }
    }

    @Test
    fun containsTest() {
        val map = HashMap<Int?, Int?>()
        assertEquals(false, map.containsKey(null))
        map.put(null, 10)
        assertEquals(true, map.containsKey(null))
        assertEquals(false, map.containsValue(null))
        map.put(null, null)
        assertEquals(true, map.containsValue(null))
        map.remove(null)
        assertEquals(false, map.containsKey(null))

        val map2 = HashMap<Int, Int>()
        for (i in 0..63) {
            assertFalse(map2.contains(i))
            assertFalse(map2.containsKey(i))
            assertFalse(map2.containsValue(i))
        }
        for (i in 0..63) {
            map2[i] = i
        }
        for (i in 0..63) {
            assertTrue(map2.contains(i))
            assertTrue(map2.containsKey(i))
            assertTrue(map2.containsValue(i))
        }
    }

    @Test
    fun iteratorTest() {
        val map = HashMap<Int, String>()
        for (i in 0..63) {
            map[i] = "$i"
        }

        val set = mutableSetOf<String>()
        for (i in map) {
            set.add(i.value)
        }
        assertEquals(64, set.size)
        for (i in 0..63) {
            assertTrue(set.contains("$i"))
        }

        val iterator = map.iterator()
        for (i in 0..63) {
            assertTrue(iterator.hasNext())
            assertTrue(set.contains(iterator.next().value))
        }
        assertFalse(iterator.hasNext())
        assertThrows(ArrayIndexOutOfBoundsException().javaClass) { iterator.next() }
    }

    @Test
    fun iteratorRemoveTest() {
        val map = HashMap<Int, String>()
        val set1 = mutableSetOf<String>()
        val set2 = mutableSetOf<String>()
        for (i in 0..63) {
            if (i % 2 == 0) set2.add("$i")
            else set1.add("$i")
            map[i] = "$i"
        }

        val iterator = map.iterator()
        for (i in 0..63) {
            assertTrue(iterator.hasNext())
            if (set2.contains(iterator.next().value)) iterator.remove()
        }
        assertFalse(iterator.hasNext())
        for (i in set1) assertTrue(map.containsValue(i))
        for (i in set2) assertFalse(map.containsValue(i))
    }

    @Test
    fun keysValuesTest() {
        val map = HashMap<Int, Int>()
        val set1 = mutableSetOf<Int>()
        val set2 = mutableSetOf<Int>()
        for (i in 0..63) {
            if (i % 2 == 0) set2.add(i)
            else set1.add(i)
            map[i] = i
        }

        val keys = map.keys
        assertEquals(64, keys.size)
        assertThrows(UnsupportedOperationException().javaClass) { keys.add(0) }
        val keysIterator = keys.iterator()
        for (i in 0..63) {
            assertTrue(keysIterator.hasNext())
            if (set2.contains(keysIterator.next())) keysIterator.remove()
        }
        assertFalse(keysIterator.hasNext())
        for (i in set1) assertTrue(keys.contains(i))
        for (i in set2) assertFalse(keys.contains(i))

        val values = map.values
        assertEquals(32, values.size)
        assertThrows(UnsupportedOperationException().javaClass) { values.add(0) }
        val valuesIterator = values.iterator()
        for (i in 0..31) {
            assertTrue(valuesIterator.hasNext())
            if (set1.contains(valuesIterator.next())) valuesIterator.remove()
        }
        assertFalse(valuesIterator.hasNext())
        for (i in set1) assertFalse(values.contains(i))
        for (i in set2) assertFalse(values.contains(i))
    }

    @Test
    fun bigTest() {
        val map = HashMap<Int, String>()
        val mapStd = mutableMapOf<Int, String>()
        for (i in 0..1_000_000) {
            map[i] = "$i"
            mapStd[i] = "${i * i}"
        }
        for (i in 0..1_000_000) {
            assertEquals("$i", map[i])
        }
        map.putAll(mapStd)
        for (i in 0..1_000_000) {
            assertEquals("${i * i}", map[i])
        }
    }
}