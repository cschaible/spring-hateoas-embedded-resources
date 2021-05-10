package io.nvtc.embeddedresources.project.project.repository

import io.nvtc.embeddedresources.project.project.model.Project
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository : JpaRepository<Project, Long> {
  @EntityGraph(attributePaths = ["creator"])
  fun findOneWithDetailsByIdentifier(identifier: UUID): Project?

  @EntityGraph(attributePaths = ["creator"])
  fun findAllWithDetailsByCreatorIdentifier(
      creatorIdentifier: UUID,
      pageable: Pageable
  ): Page<Project>

  @EntityGraph(attributePaths = ["creator"])
  fun findAllWithDetailsBy(pageable: Pageable): Page<Project>
}
