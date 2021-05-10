package io.nvtc.embeddedresources.project.project.api

import io.nvtc.embeddedresources.common.api.resource.PageResource
import io.nvtc.embeddedresources.project.project.api.resource.ProjectResource
import io.nvtc.embeddedresources.project.project.api.resource.assembler.ProjectPageResourceAssembler
import io.nvtc.embeddedresources.project.project.api.resource.assembler.ProjectResourceAssembler
import io.nvtc.embeddedresources.project.project.service.ProjectService
import java.util.*
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/projects")
@RestController
class ProjectController(
    private val projectService: ProjectService,
    private val projectResourceAssembler: ProjectResourceAssembler,
    private val projectPageResourceAssembler: ProjectPageResourceAssembler
) {

  @GetMapping
  fun findAll(
      @RequestParam("userId", required = false) userId: UUID?,
      @RequestParam("embedTasks", required = false) embedTasks: Boolean?,
      pageable: Pageable
  ): PageResource<ProjectResource> =
      when (userId == null) {
        true ->
            projectService.findAllWithDetails(pageable).let {
              projectPageResourceAssembler.assemble(userId, embedTasks, it)
            }
        false ->
            projectService.findAllWithDetailsByCreatorIdentifier(userId, pageable).let {
              projectPageResourceAssembler.assemble(userId, embedTasks, it)
            }
      }

  @GetMapping("/{id}")
  fun findOne(
      @PathVariable("id") identifier: UUID,
      @RequestParam("embedTasks", required = false) embedTasks: Boolean?,
  ): ResponseEntity<ProjectResource> =
      projectService.findOneWithDetailsByIdentifier(identifier).let {
        when (it == null) {
          true -> ResponseEntity.notFound().build()
          false -> ResponseEntity.ok(projectResourceAssembler.assemble(it, embedTasks))
        }
      }
}
