package io.nvtc.embeddedresources.project.task.api

import io.nvtc.embeddedresources.common.api.resource.PageResource
import io.nvtc.embeddedresources.project.task.api.resource.TaskResource
import io.nvtc.embeddedresources.project.task.api.resource.assembler.TaskPageResourceAssembler
import io.nvtc.embeddedresources.project.task.api.resource.assembler.TaskResourceAssembler
import io.nvtc.embeddedresources.project.task.model.Task
import io.nvtc.embeddedresources.project.task.model.TaskStatusEnum.*
import io.nvtc.embeddedresources.project.task.service.TaskService
import java.util.*
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/projects")
@RestController
class TaskController(
    private val taskService: TaskService,
    private val taskResourceAssembler: TaskResourceAssembler,
    private val taskPageResourceAssembler: TaskPageResourceAssembler
) {

  @GetMapping("/{projectId}/tasks")
  fun findByProjectId(
      @PathVariable("projectId") identifier: UUID,
      pageable: Pageable
  ): PageResource<TaskResource> =
      taskService.findAllByProjectIdentifier(identifier, pageable).let {
        taskPageResourceAssembler.assemble(identifier, it)
      }

  @GetMapping("tasks")
  fun findAll(pageable: Pageable): PageResource<TaskResource> =
      taskService.findAll(pageable).let { taskPageResourceAssembler.assemble(null, it) }

  @GetMapping("tasks/{id}")
  fun findById(@PathVariable("id") identifier: UUID): ResponseEntity<TaskResource> =
      assembleResource(taskService.findOneWithDetailsByIdentifier(identifier))

  @PostMapping("/tasks/{id}/start")
  fun startTask(@PathVariable("id") identifier: UUID): ResponseEntity<TaskResource> =
      assembleResource(taskService.updateStatus(identifier, IN_PROGRESS))

  @PostMapping("/tasks/{id}/finish")
  fun finishTask(@PathVariable("id") identifier: UUID): ResponseEntity<TaskResource> =
      assembleResource(taskService.updateStatus(identifier, DONE))

  @PostMapping("/tasks/{id}/reset")
  fun resetTask(@PathVariable("id") identifier: UUID): ResponseEntity<TaskResource> =
      assembleResource(taskService.updateStatus(identifier, TODO))

  private fun assembleResource(task: Task?): ResponseEntity<TaskResource> =
      when (task == null) {
        true -> ResponseEntity.notFound().build()
        false -> ResponseEntity.ok(taskResourceAssembler.assemble(task))
      }
}
