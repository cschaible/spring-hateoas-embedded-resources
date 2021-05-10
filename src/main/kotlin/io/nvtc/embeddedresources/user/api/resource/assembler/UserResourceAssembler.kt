package io.nvtc.embeddedresources.user.api.resource.assembler

import io.nvtc.embeddedresources.user.api.UserController
import io.nvtc.embeddedresources.user.api.resource.UserResource
import io.nvtc.embeddedresources.user.api.resource.UserResource.Companion.REL_USERS
import io.nvtc.embeddedresources.user.model.User
import org.springframework.data.domain.PageRequest
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class UserResourceAssembler(private val userResourceAssemblerHelper: UserResourceAssemblerHelper) {

  fun assemble(user: User): UserResource {
    val resource = userResourceAssemblerHelper.assemble(listOf(user)).first()

    // Users
    resource.add(
        linkTo(methodOn(UserController::class.java).findAll(PageRequest.of(0, 20)))
            .withRel(REL_USERS))

    return resource
  }
}
