# Task List

## Create an application that represents a Task List (TaskList) with the following features:

- [X] Each Task will be represented by a data class called Task, containing the following properties:

    - id (unique for each task, generated automatically).
    - title (String) - Task title.
    - description (String?) - Optional task description.
    - isCompleted (Boolean) - Indicates whether the task is completed.
    - createdAt (DateTime) - Task creation date.

- [X] Use a companion object to automatically generate unique IDs for each Task.

- [X] Implement a TaskManager class with:
    - A list of tasks (tasks), initialized as an empty list.
    - Methods to:
        - Add a new task.
        - List all tasks (destructuring title and isCompleted).
        - Search for a task by ID.
        - Update the isCompleted status of a specific task.
        - Delete a task by ID.
        - Filter completed or pending tasks using filter.

- [X] Use validation functions like require to ensure that:
    - The task title is not empty.
    - The task exists before performing operations such as updating or deleting.

- [X] Use sealed classes to represent operation results with the following states:
    - Success (with a custom message).
    - Error (with an error message).

- [X] Add extension functions to:
    - Convert Task into a formatted string.
    - Get the count of completed tasks directly from the task list.