package io.nvtc.embeddedresources.project.task.api.resource.assembler

import io.nvtc.embeddedresources.common.api.resource.ResourceReference
import io.nvtc.embeddedresources.project.task.api.TaskController
import io.nvtc.embeddedresources.project.task.api.resource.TaskResource
import io.nvtc.embeddedresources.project.task.model.Task
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class TaskResourceAssemblerHelper {

  fun assemble(tasks: List<Task>): List<TaskResource> {
    if (tasks.isEmpty()) {
      return emptyList()
    }

    return tasks.map { task ->
      val resource =
          TaskResource(
              task.identifier,
              task.version,
              task.name,
              task.description,
              task.status,
              ResourceReference.of(task.project))

      // Self link
      resource.add(
          linkTo(methodOn(TaskController::class.java).findById(task.identifier)).withSelfRel())

      return@map resource
    }
  }
}
