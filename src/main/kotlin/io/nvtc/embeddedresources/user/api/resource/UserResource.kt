package io.nvtc.embeddedresources.user.api.resource

import io.nvtc.embeddedresources.common.api.resource.AbstractResource
import java.util.*

class UserResource(val identifier: UUID, val version: Long?, val name: String) :
    AbstractResource() {

  companion object {
    const val REL_PROJECTS = "projects"
    const val REL_USER_PROJECTS = "user_projects"
    const val REL_USERS = "users"
  }
}
