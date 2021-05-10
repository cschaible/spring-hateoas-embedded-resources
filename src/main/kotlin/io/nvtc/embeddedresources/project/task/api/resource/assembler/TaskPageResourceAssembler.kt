package io.nvtc.embeddedresources.project.task.api.resource.assembler

import io.nvtc.embeddedresources.common.api.resource.PageResource
import io.nvtc.embeddedresources.project.task.api.TaskController
import io.nvtc.embeddedresources.project.task.api.resource.TaskResource
import io.nvtc.embeddedresources.project.task.model.Task
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class TaskPageResourceAssembler(
    private val taskResourceAssemblerHelper: TaskResourceAssemblerHelper
) {

  fun assemble(projectId: UUID?, page: Page<Task>): PageResource<TaskResource> {
    val resource =
        PageResource(
            taskResourceAssemblerHelper.assemble(page.content),
            page.number,
            page.size,
            page.totalPages,
            page.totalElements)

    // Self link
    if (projectId == null) {
      resource.add(
          linkTo(methodOn(TaskController::class.java).findAll(PageRequest.of(0, 20))).withSelfRel())
    } else {
      resource.add(
          linkTo(
                  methodOn(TaskController::class.java)
                      .findByProjectId(projectId, PageRequest.of(0, 20)))
              .withSelfRel())
    }

    return resource
  }
}
