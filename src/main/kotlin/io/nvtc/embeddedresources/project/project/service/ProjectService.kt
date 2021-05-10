package io.nvtc.embeddedresources.project.project.service

import io.nvtc.embeddedresources.project.project.repository.ProjectRepository
import java.util.*
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService(private val projectRepository: ProjectRepository) {

  @Transactional(readOnly = true)
  fun findOneWithDetailsByIdentifier(identifier: UUID) =
      projectRepository.findOneWithDetailsByIdentifier(identifier)

  @Transactional(readOnly = true)
  fun findAllWithDetailsByCreatorIdentifier(creatorIdentifier: UUID, pageable: Pageable) =
      projectRepository.findAllWithDetailsByCreatorIdentifier(creatorIdentifier, pageable)

  @Transactional(readOnly = true)
  fun findAllWithDetails(pageable: Pageable) = projectRepository.findAllWithDetailsBy(pageable)
}
