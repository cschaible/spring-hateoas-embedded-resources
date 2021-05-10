package io.nvtc.embeddedresources.user.api.resource.assembler

import io.nvtc.embeddedresources.project.project.api.ProjectController
import io.nvtc.embeddedresources.user.api.UserController
import io.nvtc.embeddedresources.user.api.resource.UserResource
import io.nvtc.embeddedresources.user.api.resource.UserResource.Companion.REL_USER_PROJECTS
import io.nvtc.embeddedresources.user.model.User
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class UserResourceAssemblerHelper() {

  fun assemble(users: List<User>): List<UserResource> {
    if (users.isEmpty()) {
      return emptyList()
    }

    return users.map { user ->
      val resource = UserResource(user.identifier, user.version, user.name)

      // Self link
      resource.add(
          linkTo(methodOn(UserController::class.java).findOne(user.identifier)).withSelfRel())

      // User projects
      resource.add(
          linkTo(
                  methodOn(ProjectController::class.java)
                      .findAll(user.identifier, null, PageRequest.of(0, 20)))
              .withRel(REL_USER_PROJECTS))

      return@map resource
    }
  }
}
