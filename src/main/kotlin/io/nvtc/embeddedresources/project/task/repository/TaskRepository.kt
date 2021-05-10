package io.nvtc.embeddedresources.project.task.repository

import io.nvtc.embeddedresources.project.task.model.Task
import java.util.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long> {

  @EntityGraph(attributePaths = ["project"])
  fun findAllByProjectIdentifier(projectId: UUID, pageable: Pageable): Page<Task>

  @EntityGraph(attributePaths = ["project"])
  fun findAllByProjectIdentifierIn(projectIdentifiers: List<UUID>): List<Task>

  @EntityGraph(attributePaths = ["project"])
  fun findAllWithDetailsBy(pageable: Pageable): Page<Task>

  @EntityGraph(attributePaths = ["project"])
  fun findOneWithDetailsByIdentifier(identifier: UUID): Task?
}
