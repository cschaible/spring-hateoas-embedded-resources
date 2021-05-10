package io.nvtc.embeddedresources.project.project.api.resource.assembler

import io.nvtc.embeddedresources.common.api.resource.PageResource
import io.nvtc.embeddedresources.project.project.api.ProjectController
import io.nvtc.embeddedresources.project.project.api.resource.ProjectResource
import io.nvtc.embeddedresources.project.project.model.Project
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class ProjectPageResourceAssembler(
    private val projectResourceAssemblerHelper: ProjectResourceAssemblerHelper
) {

  fun assemble(
      userId: UUID?,
      embedTasks: Boolean?,
      page: Page<Project>
  ): PageResource<ProjectResource> {
    val resource =
        PageResource(
            projectResourceAssemblerHelper.assemble(page.content, embedTasks),
            page.number,
            page.size,
            page.totalPages,
            page.totalElements)

    // Self link
    resource.add(
        linkTo(
                methodOn(ProjectController::class.java)
                    .findAll(userId, embedTasks, PageRequest.of(page.number, page.size, page.sort)))
            .withSelfRel())

    return resource
  }
}
