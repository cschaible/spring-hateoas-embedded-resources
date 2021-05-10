package io.nvtc.embeddedresources

import io.nvtc.embeddedresources.common.db.AbstractEntity
import io.nvtc.embeddedresources.project.project.model.Project
import io.nvtc.embeddedresources.project.project.repository.ProjectRepository
import io.nvtc.embeddedresources.project.task.model.Task
import io.nvtc.embeddedresources.project.task.model.TaskStatusEnum.IN_PROGRESS
import io.nvtc.embeddedresources.project.task.model.TaskStatusEnum.TODO
import io.nvtc.embeddedresources.project.task.repository.TaskRepository
import io.nvtc.embeddedresources.user.model.User
import io.nvtc.embeddedresources.user.repository.UserRepository
import java.util.UUID.randomUUID
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationListener

@SpringBootApplication
class EmbeddedResourcesApplication(
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository
) : ApplicationListener<ApplicationStartedEvent> {
  override fun onApplicationEvent(event: ApplicationStartedEvent) {

    // users
    val user1 = userRepository.save(setIdentifier(User("user1", true)))
    val user2 = userRepository.save(setIdentifier(User("user2", false)))
    userRepository.save(setIdentifier(User("user3", false)))

    // projects
    val user1Project1 = projectRepository.save(setIdentifier(Project("project 1 of user 1", user1)))
    val user2Project1 = projectRepository.save(setIdentifier(Project("project 1 of user 2", user2)))

    // tasks
    taskRepository.save(
        setIdentifier(Task("task1 of project 1", "description", TODO, user1Project1)))
    taskRepository.save(
        setIdentifier(Task("task2 of project 1", "description", IN_PROGRESS, user1Project1)))
    taskRepository.save(
        setIdentifier(Task("task1 of project 1", "description", TODO, user2Project1)))
  }

  companion object {
    fun <T : AbstractEntity<Long>> setIdentifier(entity: T): T {
      entity.apply { identifier = randomUUID() }
      return entity
    }
  }
}

fun main(args: Array<String>) {
  runApplication<EmbeddedResourcesApplication>(*args)
}
