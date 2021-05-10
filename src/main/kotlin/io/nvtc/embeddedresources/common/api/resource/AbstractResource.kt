package io.nvtc.embeddedresources.common.api.resource

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.hateoas.RepresentationModel

abstract class AbstractResource : RepresentationModel<AbstractResource>() {
  private val embeddedResources: HashMap<String, RepresentationModel<AbstractResource>> = HashMap()

  fun addResource(name: String, resource: RepresentationModel<AbstractResource>?) =
      resource?.let { embeddedResources.put(name, resource) }

  @JsonProperty("_embedded")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  fun getEmbeddedResources() = embeddedResources
}
