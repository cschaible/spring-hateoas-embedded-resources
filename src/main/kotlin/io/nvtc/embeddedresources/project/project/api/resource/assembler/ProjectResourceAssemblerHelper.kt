package io.nvtc.embeddedresources.project.project.api.resource.assembler

import io.nvtc.embeddedresources.common.api.resource.ListResource
import io.nvtc.embeddedresources.common.api.resource.ResourceReference
import io.nvtc.embeddedresources.project.project.api.ProjectController
import io.nvtc.embeddedresources.project.project.api.resource.ProjectResource
import io.nvtc.embeddedresources.project.project.api.resource.ProjectResource.Companion.REL_TASKS
import io.nvtc.embeddedresources.project.project.model.Project
import io.nvtc.embeddedresources.project.task.api.TaskController
import io.nvtc.embeddedresources.project.task.api.resource.TaskResource
import io.nvtc.embeddedresources.project.task.api.resource.assembler.TaskResourceAssemblerHelper
import io.nvtc.embeddedresources.project.task.repository.TaskRepository
import io.nvtc.embeddedresources.project.task.service.TaskService
import java.util.*
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class ProjectResourceAssemblerHelper(
    private val taskService: TaskService,
    private val taskResourceAssemblerHelper: TaskResourceAssemblerHelper
) {

  fun assemble(projects: List<Project>, embedTasks: Boolean?): List<ProjectResource> {
    if (projects.isEmpty()) {
      return emptyList()
    }

    val embedNestedResources = embedTasks != null && embedTasks

    var taskResources: Map<UUID, List<TaskResource>> = HashMap()
    if (embedNestedResources) {
      taskResources =
          taskResourceAssemblerHelper.assemble(
            taskService.findAllByProjectIdentifierIn(projects.map { it.identifier }))
              .groupBy { it.project.identifier }
    }

    return projects.map { project ->
      val resource =
          ProjectResource(
              project.identifier,
              project.version,
              project.name,
              ResourceReference.of(project.creator))

      // Self link
      resource.add(
          linkTo(methodOn(ProjectController::class.java).findOne(project.identifier, embedTasks))
              .withSelfRel())

      // Embed tasks or add a link
      if (embedNestedResources) {
        val tasks = taskResources[project.identifier]
        if (tasks != null) {
          resource.addResource("tasks", ListResource(tasks))
        } else {
          resource.addResource("tasks", ListResource<TaskResource>(emptyList()))
        }
      } else {
        resource.add(
            linkTo(
                    methodOn(TaskController::class.java)
                        .findByProjectId(project.identifier, PageRequest.of(0, 20)))
                .withRel(REL_TASKS))
      }

      return@map resource
    }
  }
}
