import java.util.*

fun main() {
    val taskStorage = TaskStorage()
    val taskManager = TaskManager(taskStorage)

    val ui = UI(taskManager)
    ui.start()
}

data class Task(
    val id: Int,
    var title: String,
    var description: String,
    var isCompleted: Boolean = false,
    val createdAt: Date = Date()
) {
    companion object {
        private var lastId = 0

        fun generateNewId(): Int {
            return ++this.lastId
        }
    }
}

interface Manager<T> {
    fun insert(item: T)
    fun delete(id: Int): Boolean
    fun update(item: T): Boolean
    fun search(id: Int): T?
    fun list(): List<T>
}

class TaskStorage() : Manager<Task> {
    private val taskList = mutableListOf<Task>()
    val count: Int
        get() = taskList.count()

    override fun insert(item: Task) {
        taskList.add(item)
    }

    override fun delete(id: Int): Boolean {
        return taskList.removeIf { it.id == id }
    }

    override fun update(item: Task): Boolean {
        val currentTask = taskList.indexOfFirst { it.id == item.id }

        if (currentTask > 0) {
            taskList[currentTask] = item
        }

        return currentTask > 0
    }

    override fun search(id: Int): Task? {
        return taskList.find { it.id == id }
    }

    override fun list(): List<Task> {
        return taskList.toList()
    }

    private fun MutableList<Task>.count(): Int {
        return taskList.count { it.isCompleted }
    }
}

class TaskManager(private val taskStorage: TaskStorage) {
    fun add(title: String, description: String): Result {
        try {
            require(title.isNotEmpty() && description.isNotEmpty())

            val newTask = Task(
                id = Task.generateNewId(),
                title = title,
                description = description
            )

            taskStorage.insert(newTask)
            return Result.Success("New task created successfully")

        } catch (error: IllegalArgumentException) {
            return Result.Error("Title and Description required")
        } catch (error: Exception) {
            return Result.Error("Something got wrong try again")
        }
    }

    fun list(): Result {
        val taskList = taskStorage.list()

        if (taskList.isEmpty()) {
            return Result.Success("EMPTY LIST")
        }

        return Result.Success(taskList.joinToString(separator = "\n") { task -> toUI(task) })
    }

    fun search(id: Int): Result {
        val task = taskStorage.search(id) ?: return Result.Error("Resource not found")

        return Result.Success(toUI(task))
    }

    fun updateStatus(id: Int): Result {
        val task = taskStorage.search(id) ?: return Result.Error("Resource not found")

        task.isCompleted = !task.isCompleted
        taskStorage.update(task)
        return Result.Success("Task updated successfully")
    }

    fun remove(id: Int): Result {
        val task = taskStorage.search(id) ?: return Result.Error("Resource not found")

        taskStorage.delete(task.id)
        return Result.Success("Task removed successfully")
    }

    fun fetchByStatus(isCompleted: Boolean): Result {
        val tasks = taskStorage.list().filter { it.isCompleted == isCompleted }
        return Result.Success(tasks.joinToString(separator = "\n") { task -> toUI(task) })
    }

    fun count(): Array<Int> {
        return arrayOf(taskStorage.list().size, taskStorage.count)
    }

    private fun toUI(task: Task): String {
        return """
        TASK_${task.id}: ${task.title.uppercase()}
        created at ${task.createdAt.toString()}
        status: ${if (task.isCompleted) "Completed" else "Pending"}
        description: ${task.description}
        
        """.trimIndent()
    }
}

class UI(private val taskManager: TaskManager) {
    fun start() {
        var action: Int? = null

        while (action != TaskActions.EXIT.ordinal) {
            renderTasksPreview()
            renderOptions()

            action = InputHelper.requestInt()

            when (action) {
                TaskActions.ADD.ordinal -> handleAdd()
                TaskActions.UPDATE.ordinal -> handleUpdate()
                TaskActions.REMOVE.ordinal -> handleRemove()
                TaskActions.SEARCH.ordinal -> handleSearch()
                TaskActions.FETCH_BY_STATUS.ordinal -> handleFetchByStatus()
                TaskActions.EXIT.ordinal -> println("Thank you. Come back soon!")
                else -> println("Unknown action, try again!")
            }
        }
    }

    private fun renderTasksPreview() {
        val tasks = taskManager.list()
        val (amount, completed) = taskManager.count()

        println(
            """
------ CURRENT TASKS ------
AMOUNT: $amount
COMPLETED: $completed
---------------------------

${handleResult(tasks)}
---------------------------
        """.trimIndent()
        )
    }

    private fun renderOptions() {
        println(
            """
                   TASK MANAGEMENT  
             --------------------------- 
              1. ADD           
              2. UPDATE STATUS       
              3. REMOVE        
              4. SEARCH        
              5. FETCH BY STATUS 
              0. EXIT          
            """.trimIndent()
        )
    }

    private fun handleAdd() {
        println("Please enter the task title:")
        val title = InputHelper.requestString()

        println("Please enter the task description:")
        val description = InputHelper.requestString()

        val result = taskManager.add(title, description)
        println(handleResult(result))
    }

    private fun handleUpdate() {
        println("Please enter the task ID to update status")
        val id = InputHelper.requestInt()

        val result = taskManager.updateStatus(id)
        println(handleResult(result))
    }

    private fun handleRemove() {
        println("Please enter the task ID to remove")
        val id = InputHelper.requestInt()

        val result = taskManager.remove(id)
        println(handleResult(result))
    }

    private fun handleSearch() {
        println("Please enter the task ID to search")
        val id = InputHelper.requestInt()

        val result = taskManager.search(id)
        println(handleResult(result))
    }

    private fun handleFetchByStatus() {
        var input: Int? = null
        while (input == null) {
            println(
                """
                -- PLEASE ENTER THE TASK STATUS CODE --
                    COMPLETED ONLY: 1
                    PENDING ONLY: 2
            """.trimIndent()
            )

            input = InputHelper.requestInt()
            input = if (input in 1..2) input else null
        }

        val result = taskManager.fetchByStatus(input == 1)
        println(handleResult(result))
    }

    private fun handleResult(result: Result): String {
        return when (result) {
            is Result.Success -> result.message
            is Result.Error -> "Error: ${result.message}"
        }
    }
}

sealed class Result {
    class Success(val message: String) : Result()
    class Error(val message: String) : Result()
}

enum class TaskActions {
    EXIT,
    ADD,
    UPDATE,
    REMOVE,
    SEARCH,
    FETCH_BY_STATUS,
}

object InputHelper {
    fun requestInt(): Int {
        val value = requestValue().toIntOrNull()

        if (value == null) {
            println("Please enter a valid integer")
            return requestInt()
        }

        return value
    }

    fun requestString(): String = this.requestValue()

    private fun requestValue(): String {
        print("-> ")
        val value = readlnOrNull()?.ifEmpty { null }

        if (value == null) {
            println("Please enter a value")
            return requestValue()
        }
        return value
    }
}