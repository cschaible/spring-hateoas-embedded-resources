package io.nvtc.embeddedresources.common.api.resource

open class PageResource<T : AbstractResource>(
    items: Collection<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val totalElements: Long
) : ListResource<T>(items)
