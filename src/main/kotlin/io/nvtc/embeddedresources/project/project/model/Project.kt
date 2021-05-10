package io.nvtc.embeddedresources.project.project.model

import io.nvtc.embeddedresources.common.db.AbstractEntity
import io.nvtc.embeddedresources.user.model.User
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.ManyToOne
import javax.validation.constraints.Size

@Entity
data class Project(

    // Name
    @field:Size(min = 1, max = MAX_NAME_LENGTH)
    @Column(nullable = false, length = MAX_NAME_LENGTH)
    var name: String,

    // Creator
    @ManyToOne(fetch = LAZY) var creator: User
) : AbstractEntity<Long>() {

  override fun getDisplayText(): String = name

  companion object {
    const val MAX_NAME_LENGTH = 100
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    if (!super.equals(other)) return false

    other as Project

    if (name != other.name) return false

    return true
  }

  override fun hashCode(): Int {
    var result = super.hashCode()
    result = 31 * result + name.hashCode()
    return result
  }
}
