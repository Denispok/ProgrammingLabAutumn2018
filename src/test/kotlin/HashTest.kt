import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HashTest {

    @Test
    fun simpleTest() {
        val map = HashMap<String, String>()
        for (i in 1..40) map["$i"] = (i * i).toString()
        for (i in 1..20) map.remove("$i")
        assertEquals(20, map.size)
        for (i in 21..40) {
            assertEquals((i * i).toString(), map["$i"])
        }
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