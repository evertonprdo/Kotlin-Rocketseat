package challenges

object InputHelper {
    fun requestInt(): Int {
        val value = requestValue().toIntOrNull()

        if (value == null) {
            println("Please enter a valid integer")
            return requestInt()
        }

        return value
    }

    fun requestDouble(): Double {
        val value = requestValue().toDoubleOrNull()

        if (value == null) {
            println("Please enter a valid double")
            return requestDouble()
        }

        return value
    }

    fun requestString(): String = this.requestValue()

    private fun requestValue(): String {
        print("-> ")
        val value = readlnOrNull()

        if (value == null) {
            println("Please enter a value")
            return requestValue()
        }
        return value
    }

    fun requestNullableInt(): Int? {
        val value = this.requestNullableValue()
        if (value == null) return value

        val integer = value.toIntOrNull()
        if (integer == null) {
            println("Please enter a valid integer")
            return requestNullableInt()
        }

        return integer
    }

    fun requestNullableDouble(): Double? {
        val value = this.requestNullableValue()
        if (value == null) return value

        val integer = value.toDoubleOrNull()
        if (integer == null) {
            println("Please enter a valid double")
            return requestNullableDouble()
        }

        return integer
    }

    fun requestNullableString(): String? = this.requestNullableValue()

    private fun requestNullableValue(): String? {
        print("-> ")
        return readlnOrNull()?.ifEmpty { null }
    }
}

data class Product(
    var id: Int,
    var name: String,
    var price: Double,
    var amount: Int
) {
    fun updateFields() {
        println("Enter the Product name: (Press Enter to skip)")
        val uptName = InputHelper.requestNullableString()

        println("Enter the Product price: (Press Enter to skip)")
        val uptPrice = InputHelper.requestNullableDouble()

        println("Enter the Product amount: (Press Enter to skip)")
        val uptAmount = InputHelper.requestNullableInt()

        name = uptName ?: name
        price = uptPrice ?: price
        amount = uptAmount ?: amount
    }

    companion object {
        fun makeProduct(): Product {
            println("Enter the Product id:")
            val id = InputHelper.requestInt()

            println("Enter the Product name:")
            val name = InputHelper.requestString()

            println("Enter the Product price:")
            val price = InputHelper.requestDouble()

            println("Enter the Product amount:")
            val amount = InputHelper.requestInt()

            return Product(id, name, price, amount)
        }
    }
}

interface Storage<T> {
    fun insert(item: T)
    fun update(item: T): Boolean
    fun remove(id: Int): Boolean
    fun search(id: Int): T?
    fun getAll(): List<T>
}

class ProductStorage : Storage<Product> {
    private val productList = mutableListOf<Product>()

    override fun insert(item: Product) {
        productList.add(item)
    }

    override fun update(item: Product): Boolean {
        if (productList.removeIf { it.id == item.id }) {
            productList.add(item)
            return true
        }
        return false
    }

    override fun remove(id: Int): Boolean {
        return productList.removeIf { it.id == id }
    }

    override fun search(id: Int): Product? {
        return productList.find { it.id == id }
    }

    override fun getAll(): List<Product> {
        return productList.toList()
    }
}

enum class MenuActions {
    ADD_PRODUCT,
    UPDATE_PRODUCT,
    DELETE_PRODUCT,
    SEARCH_PRODUCT,
    EXIT
}

class StorageManagement(private val productStorage: ProductStorage) {
    fun start() {
        var action: Int? = null

        while (action != MenuActions.EXIT.ordinal) {
            renderOptions()
            action = InputHelper.requestInt()

            when (action) {
                MenuActions.ADD_PRODUCT.ordinal -> addNewProduct()
                MenuActions.UPDATE_PRODUCT.ordinal -> updateProduct()
                MenuActions.DELETE_PRODUCT.ordinal -> removeProduct()
                MenuActions.SEARCH_PRODUCT.ordinal -> searchProduct()
                MenuActions.EXIT.ordinal -> println("Thank you. Come back soon!")
                else -> println("Invalid option! Try again")
            }
        }
    }

    private fun renderOptions() {
        println(
            """
            +---------------------------------+
            |  STORAGE MANAGEMENT - PRODUCTS  |
            +---------------------------------+
            |  1 - Add                        |
            |  2 - Update                     |
            |  3 - Delete                     |
            |  4 - Search                     |
            |  5 - Exit                       |
            +---------------------------------+
            """
        )
        println("CURRENT PRODUCT LIST IN STORAGE")
        println(
            productStorage.getAll().joinToString(separator = "\n")
                .ifEmpty { "Any product was added to the storage" })
    }

    private fun addNewProduct() {
        println("Creating new product...")
        productStorage.insert(Product.makeProduct())
        println("Product added successfully")
    }

    private fun updateProduct() {
        println("Enter the product ID")
        val productToUpdate = productStorage.search(InputHelper.requestInt())

        if (productToUpdate != null) {
            println("Updating product ${productToUpdate.name}...")
            productToUpdate.updateFields()
            productStorage.update(productToUpdate)

            return println("Product updated successfully")
        }

        println("Resource not found")
    }

    private fun removeProduct() {
        println("Enter the product ID")
        println(
            if (productStorage.remove(InputHelper.requestInt())) "Product removed"
            else "Resource not found"
        )
    }

    private fun searchProduct() {
        println("Enter the product ID")
        println(productStorage.search(InputHelper.requestInt()))
    }
}

fun main() {
    val productStorage = ProductStorage()
    val storageManagement = StorageManagement(productStorage)

    storageManagement.start()
}