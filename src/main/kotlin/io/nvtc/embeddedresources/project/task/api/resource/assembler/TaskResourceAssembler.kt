package io.nvtc.embeddedresources.project.task.api.resource.assembler

import io.nvtc.embeddedresources.project.task.api.resource.TaskResource
import io.nvtc.embeddedresources.project.task.model.Task
import org.springframework.stereotype.Component

@Component
class TaskResourceAssembler(private val taskResourceAssemblerHelper: TaskResourceAssemblerHelper) {

  fun assemble(task: Task): TaskResource =
      taskResourceAssemblerHelper.assemble(listOf(task)).first()
}
