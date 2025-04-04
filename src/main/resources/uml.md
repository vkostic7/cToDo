````plantuml
+class User {
  -username
  -password
}

+class Task {
  -taskName
  -description
  -dueDate
  -status
}

+class UserTask {
  -userId
  -taskId
}

+enum TaskDifficulty {
  WEAK
  NORMAL
  HARD
}

+enum TaskStatus {
  OPEN
  IN_PROGRESS
  CLOSED
}

User "1" -- "0..*" UserTask
Task "1" -- "0..*" UserTask

TaskDifficulty o-- Task
TaskStatus o-- Task


````
