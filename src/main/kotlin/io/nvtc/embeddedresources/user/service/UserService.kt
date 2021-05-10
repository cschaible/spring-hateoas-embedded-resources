package io.nvtc.embeddedresources.user.service

import io.nvtc.embeddedresources.user.repository.UserRepository
import java.util.*
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

  @Transactional(readOnly = true) fun findAll(pageable: Pageable) = userRepository.findAll(pageable)

  @Transactional(readOnly = true)
  fun findByIdentifier(identifier: UUID) = userRepository.findByIdentifier(identifier)
}
