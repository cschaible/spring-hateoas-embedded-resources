package io.nvtc.embeddedresources.project.task.api.resource

import io.nvtc.embeddedresources.common.api.resource.AbstractResource
import io.nvtc.embeddedresources.common.api.resource.ResourceReference
import io.nvtc.embeddedresources.project.task.model.TaskStatusEnum
import java.util.*

class TaskResource(
    val identifier: UUID,
    val version: Long?,
    val name: String,
    val description: String?,
    val status: TaskStatusEnum,
    val project: ResourceReference
) : AbstractResource()
