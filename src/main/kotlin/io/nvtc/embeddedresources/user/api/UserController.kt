package io.nvtc.embeddedresources.user.api

import io.nvtc.embeddedresources.common.api.resource.PageResource
import io.nvtc.embeddedresources.user.api.resource.UserResource
import io.nvtc.embeddedresources.user.api.resource.assembler.UserPageResourceAssembler
import io.nvtc.embeddedresources.user.api.resource.assembler.UserResourceAssembler
import io.nvtc.embeddedresources.user.service.UserService
import java.util.*
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/users")
@RestController
class UserController(
    private val userService: UserService,
    private val userResourceAssembler: UserResourceAssembler,
    private val userPageResourceAssembler: UserPageResourceAssembler
) {

  @GetMapping
  fun findAll(pageable: Pageable): PageResource<UserResource> =
      userService.findAll(pageable).let { userPageResourceAssembler.assemble(it) }

  @GetMapping("/{id}")
  fun findOne(@PathVariable("id") identifier: UUID): ResponseEntity<UserResource> =
      userService.findByIdentifier(identifier).let {
        when (it == null) {
          true -> ResponseEntity.notFound().build()
          false -> ResponseEntity.ok(userResourceAssembler.assemble(it))
        }
      }
}
