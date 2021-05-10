package io.nvtc.embeddedresources.common.api.resource

import io.nvtc.embeddedresources.common.db.AbstractEntity
import java.util.*

class ResourceReference(val identifier: UUID, val displayText: String) : AbstractResource() {
  companion object {
    fun of(entity: AbstractEntity<Long>) =
        ResourceReference(entity.identifier, entity.getDisplayText())
  }
}
