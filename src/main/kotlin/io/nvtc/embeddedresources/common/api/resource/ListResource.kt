package io.nvtc.embeddedresources.common.api.resource

import org.springframework.hateoas.RepresentationModel

open class ListResource<T : AbstractResource>(val items: Collection<T>) :
    RepresentationModel<AbstractResource>()
