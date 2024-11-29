data class Profile (
    val username: String,
    val age: Int,
    val hobby: String,
    val favoriteHobbyPartner: Profile? = null
)

fun main() {
    val elodie = Profile("Elodie", 21, "Tennis", null)
    val eduardo = Profile("Eduardo", 22, "Tennis", elodie)

    listOf(elodie, eduardo).forEach{
        println("Name: ${it.username}")
        println("Age: ${it.age}")
        println("Hobby: ${it.hobby}")
        if (it.favoriteHobbyPartner != null) {
            print("With: ${it.favoriteHobbyPartner.username}")
        }
        println()
    }
}