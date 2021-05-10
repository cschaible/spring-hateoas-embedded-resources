package io.nvtc.embeddedresources.project.project.api.resource.assembler

import io.nvtc.embeddedresources.project.project.api.ProjectController
import io.nvtc.embeddedresources.project.project.api.resource.ProjectResource
import io.nvtc.embeddedresources.project.project.api.resource.ProjectResource.Companion.REL_PROJECTS
import io.nvtc.embeddedresources.project.project.model.Project
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Component

@Component
class ProjectResourceAssembler(
    private val projectResourceAssemblerHelper: ProjectResourceAssemblerHelper
) {

  fun assemble(project: Project, embedTasks: Boolean?): ProjectResource {
    val resource = projectResourceAssemblerHelper.assemble(listOf(project), embedTasks).first()

    resource.add(
        linkTo(
                WebMvcLinkBuilder.methodOn(ProjectController::class.java)
                    .findAll(null, embedTasks, PageRequest.of(0, 20)))
            .withRel(REL_PROJECTS))

    return resource
  }
}
