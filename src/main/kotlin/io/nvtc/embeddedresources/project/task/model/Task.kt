package io.nvtc.embeddedresources.project.task.model

import io.nvtc.embeddedresources.common.db.AbstractEntity
import io.nvtc.embeddedresources.project.project.model.Project
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne
import javax.validation.constraints.Size

@Entity
data class Task(

    // Name
    @field:Size(min = 1, max = MAX_NAME_LENGTH) var name: String,

    // Description
    @Column(nullable = true) var description: String?,

    // Status
    var status: TaskStatusEnum,

    // Project
    @ManyToOne(fetch = LAZY) val project: Project
) : AbstractEntity<Long>() {

  override fun getDisplayText(): String = name

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    if (!super.equals(other)) return false

    other as Task

    if (name != other.name) return false
    if (description != other.description) return false
    if (status != other.status) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + name.hashCode()
    result = 31 * result + (description?.hashCode() ?: 0)
    result = 31 * result + (status.hashCode())
    return result
  }

  companion object {
    const val MAX_NAME_LENGTH = 100
  }
}
