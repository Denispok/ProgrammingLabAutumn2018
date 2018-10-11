import org.junit.jupiter.api.Test

class HashTest {

    @Test
    fun simpleTest() {
        val map = HashMap<String, String>()
        for (i in 1..40) {
            map.put("$i", "$(i*i)")
        }
        for (i in 1..40) {
            println(map.get("$i"))
        }
    }
}