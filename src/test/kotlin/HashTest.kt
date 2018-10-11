import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class HashTest {

    @Test
    fun simpleTest() {
        val map = HashMap<String, String>()
        for (i in 1..40) map["$i"] = (i * i).toString()
        for (i in 1..20) map.remove("$i")
        assertEquals(20, map.size)
        print(map["${33}"])
        for (i in 21..40) {
            assertEquals((i * i).toString(), map["$i"])
        }
    }
}