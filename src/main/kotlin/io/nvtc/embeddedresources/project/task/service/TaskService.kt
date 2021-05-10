package io.nvtc.embeddedresources.project.task.service

import io.nvtc.embeddedresources.project.task.model.TaskStatusEnum
import io.nvtc.embeddedresources.project.task.repository.TaskRepository
import java.util.*
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(private val taskRepository: TaskRepository) {

  @Transactional(readOnly = true)
  fun findAllByProjectIdentifier(projectId: UUID, pageable: Pageable) =
      taskRepository.findAllByProjectIdentifier(projectId, pageable)

  @Transactional(readOnly = true)
  fun findAllByProjectIdentifierIn(projectIdentifiers: List<UUID>) =
      taskRepository.findAllByProjectIdentifierIn(projectIdentifiers)

  @Transactional(readOnly = true)
  fun findAll(pageable: Pageable) = taskRepository.findAllWithDetailsBy(pageable)

  @Transactional(readOnly = true)
  fun findOneWithDetailsByIdentifier(identifier: UUID) =
      taskRepository.findOneWithDetailsByIdentifier(identifier)

  @Transactional
  fun updateStatus(identifier: UUID, status: TaskStatusEnum) =
      taskRepository.findOneWithDetailsByIdentifier(identifier)?.let {
        it.status = status
        taskRepository.save(it)
      }
}
