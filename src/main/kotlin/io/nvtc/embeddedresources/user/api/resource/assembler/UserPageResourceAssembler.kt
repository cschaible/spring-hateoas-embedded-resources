package io.nvtc.embeddedresources.user.api.resource.assembler

import io.nvtc.embeddedresources.common.api.resource.PageResource
import io.nvtc.embeddedresources.project.project.api.ProjectController
import io.nvtc.embeddedresources.user.api.UserController
import io.nvtc.embeddedresources.user.api.resource.UserResource
import io.nvtc.embeddedresources.user.api.resource.UserResource.Companion.REL_PROJECTS
import io.nvtc.embeddedresources.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class UserPageResourceAssembler(
    private val userResourceAssemblerHelper: UserResourceAssemblerHelper
) {

  fun assemble(page: Page<User>): PageResource<UserResource> {
    val resource =
        PageResource(
            userResourceAssemblerHelper.assemble(page.content),
            page.number,
            page.size,
            page.totalPages,
            page.totalElements)

    resource.add(
        linkTo(
                methodOn(UserController::class.java)
                    .findAll(PageRequest.of(page.number, page.size, page.sort)))
            .withSelfRel())

    // Projects
    resource.add(
        linkTo(methodOn(ProjectController::class.java).findAll(null, null, PageRequest.of(0, 20)))
            .withRel(REL_PROJECTS))

    return resource
  }
}
