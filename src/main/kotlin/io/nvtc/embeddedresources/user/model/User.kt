package io.nvtc.embeddedresources.user.model

import io.nvtc.embeddedresources.common.db.AbstractEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Index
import javax.persistence.Table
import javax.validation.constraints.Size

@Entity
@Table(indexes = [Index(name = "UK_User_Identifier", columnList = "identifier", unique = true)])
data class User(

    // Name
    @field:Size(min = 1, max = MAX_NAME_LENGTH)
    @Column(nullable = false, length = MAX_NAME_LENGTH)
    var name: String,

    // Is admin
    @Column(nullable = false) var admin: Boolean,
) : AbstractEntity<Long>() {

  override fun getDisplayText(): String = name

  companion object {
    const val MAX_NAME_LENGTH = 100
  }
}
