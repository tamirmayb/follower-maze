# Follower Maze Code Challenge

#### Prerequisite
* JDK 1.8
* Maven 3.x
* Java > 15.0
* Intellij
* Junit

The [instructions.md](instructions.md) contains all the details for the task.

## Part 1: Follower Maze - Refactor
* This part was refactored using Java 15 & Maven. 
* It supports all functionality as described in the instructions with an improved code structure.

#### How to run
* Clone this repository
* Build the project and get the project's jar file in target folder:
```
mvn clean package
```
* Run the application
```
mvn exec:java
```
#### The application should begin and await events.
#### You can use the provided tester folder to send events:
```
  ./tester/runall.sh
```
OR 
```
  ./tester/runfast.sh
```

## Part 2: Follower Maze - Extension (DLQ)
* A Special type of event was added named DeadEvent, this is the event which would be triggered by any kind of error in the process (i.e. wrong types, malformed input etc.).
* You can find a todo related to this case where a new handler needs to be added which would handle the dead events.
  * This handler would send the error event to a different queue where the data would be written to AWS S3.
* Note: as instructed this part was not developed. 