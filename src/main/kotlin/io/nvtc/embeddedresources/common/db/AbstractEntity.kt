package io.nvtc.embeddedresources.common.db

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.MappedSuperclass
import javax.persistence.Version
import javax.validation.constraints.NotNull
import org.springframework.data.jpa.domain.AbstractPersistable

@MappedSuperclass
abstract class AbstractEntity<T : Serializable> : AbstractPersistable<T>() {

  @Suppress("unused") @Version @field:NotNull @Column(nullable = false) val version: Long? = null

  @field:NotNull @Column(nullable = false) lateinit var identifier: UUID

  abstract fun getDisplayText(): String
}
