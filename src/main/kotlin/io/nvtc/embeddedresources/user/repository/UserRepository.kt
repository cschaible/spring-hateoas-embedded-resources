package io.nvtc.embeddedresources.user.repository

import io.nvtc.embeddedresources.user.model.User
import java.util.*
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
  fun findByIdentifier(identifier: UUID): User?
}
