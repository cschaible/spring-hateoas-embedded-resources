package io.nvtc.embeddedresources.project.project.api.resource

import io.nvtc.embeddedresources.common.api.resource.AbstractResource
import io.nvtc.embeddedresources.common.api.resource.ResourceReference
import java.util.*

class ProjectResource(
    val identifier: UUID,
    val version: Long?,
    val name: String,
    val creator: ResourceReference
) : AbstractResource() {

  companion object {
    const val REL_PROJECTS = "projects"
    const val REL_TASKS = "tasks"
  }
}
