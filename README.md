# Embedded resources and action endpoints with Spring HATEOAS

This repository showcases different approaches how to embed nested resources in response entities. Additionally, the
concept of action endpoints is shown.

## Embedded resources
In software projects, it is often necessary to find the right measure and procedure of how to transfer large amounts of
data in a way that is appropriate to the situation and circumstances between a client and server.  
In the subsequent sections different approaches using REST and HATEOAS are described.

### Direct embedded resources
One way to embed resources directly is to put data directly into place where it is needed.  

The following example shows a task resource with an embedded project resource.

```json
{ 
  "identifier":"a963f78f-b357-4073-845d-d2fcf7c5655c", 
  "version":0, 
  "name":"task1 of project 1", 
  "description":"description", 
  "status":"TODO", 
  "project":{
    "identifier":"c002f1d1-9005-4c52-8de3-7bee5461f99e",
    "version":0,
    "name":"project 1 of user 1",
    "creator":{
      "identifier":"066ed7df-7e78-476f-a05c-190490fe8cf0",
      "displayText":"user1"
    },
    "_links":{
      "self":{
        "href":"http://localhost:8080/projects/c002f1d1-9005-4c52-8de3-7bee5461f99e{?embedTasks}",
        "templated":true
      },
      "tasks":{
        "href":"http://localhost:8080/projects/c002f1d1-9005-4c52-8de3-7bee5461f99e/tasks"
      }
    }
  }, 
  "_links":{ 
    "self":{ 
      "href":"http://localhost:8080/projects/tasks/a963f78f-b357-4073-845d-d2fcf7c5655c"
    }
  }
}
```

In some situation this approach make sense. As shown in the example above though, this approach often comes with 
further questions to answer, for example how to deal with nested resources in nested resources (the `creator` object 
in the embedded `project` resource) or circular dependencies.  
The advantage of this approach is that it is quite simple. Additional resources are added on-demand as additional
attributes.
The disadvantage is that it is often not as easy to figure out what a nested resources is and what's not (especially
if the application isn't using HATEOAS where the links give insights about the resources attributes).

### Resource References
The idea of embedding resources often is based on the idea of reducing the amount of round trips from the client to the
server. Depending on the amount of data to load, especially if lots of nested resources have to be embedded, this can
result in a bad API performance - not only because of the effort to retrieve the data from the database and constructing
response resources but also sending the amount of data back to the client (over slow mobile internet connections).   
If embedded data is requested repeatedly without a sensible reason, this is even worse.

One good approach is to provide dedicated endpoints for different resources (e.g. user, project, task) to load them
once and use resource references instead of full-blown resources to show the relations between the resources.

The following example shows a task resource with an embedded project resource reference.

```json
{
  "identifier":"a963f78f-b357-4073-845d-d2fcf7c5655c",
  "version":0,
  "name":"task1 of project 1",
  "description":"description",
  "status":"TODO",
  "project":{
    "identifier":"c002f1d1-9005-4c52-8de3-7bee5461f99e",
    "displayText":"project 1 of user 1"
  },
  "_links":{
    "self":{
      "href":"http://localhost:8080/projects/tasks/a963f78f-b357-4073-845d-d2fcf7c5655c"
    }
  }
}
```

The response of this approach is significantly shorter than the direct embedding approach and still provides the 
most relevant data of the project resource. Often an identifier and a display text is sufficient for the clients.

### Embedded resources list
In certain scenarios it's required or desired to load lots of data at once, even if the required data is only loosely
coupled to the requested data.  
In such scenarios the approach of a generic `_embedded` attribute can be useful. Under the `_embedded` attribute 
additional data can be added in a "standardized" fashion.  

The following example shows a project resource that was requested to be returned with a list of embedded tasks.
The task resources itself use resource references back to the project.

```json
{
  "identifier":"c002f1d1-9005-4c52-8de3-7bee5461f99e",
  "version":0,
  "name":"project 1 of user 1",
  "creator":{
    "identifier":"066ed7df-7e78-476f-a05c-190490fe8cf0",
    "displayText":"user1"
  },
  "_links":{
    "self":{
      "href":"http://localhost:8080/projects/c002f1d1-9005-4c52-8de3-7bee5461f99e?embedTasks=true"
    }
  },
  "_embedded":{
    "tasks":{
      "items":[
        {
          "identifier":"a963f78f-b357-4073-845d-d2fcf7c5655c",
          "version":0,
          "name":"task1 of project 1",
          "description":"description",
          "status":"TODO",
          "project": {
            "identifier":"c002f1d1-9005-4c52-8de3-7bee5461f99e",
            "displayText":"project 1 of user 1"
          },
          "_links":{
            "self":{
              "href":"http://localhost:8080/projects/tasks/a963f78f-b357-4073-845d-d2fcf7c5655c"
            }
          }
        },
        {
          "identifier":"8565e574-4955-4f45-8584-7ca627439052",
          "version":0,
          "name":"task2 of project 1",
          "description":"description",
          "status":"IN_PROGRESS",
          "project":{
            "identifier":"c002f1d1-9005-4c52-8de3-7bee5461f99e",
            "displayText":"project 1 of user 1"
          },
          "_links":{
            "self":{
              "href":"http://localhost:8080/projects/tasks/8565e574-4955-4f45-8584-7ca627439052"
            }
          }
        }
      ]
    }
  }
}
```

## Action endpoints
REST as the name implies is a "state transfer" that is proved easy to be used in most scenarios. 
Sending a whole object to toggle a flag although is an edge-case that feels like using a sledge-hammer to crack a nut.  
Sending useless data and the effort to write the logic to figure out what has been changed by the user and trigger 
derived actions based on the changes may not be worth insisting on the strict adherence of the REST concept. 

One approach to simplify such use-cases are action endpoints. Action endpoints are REST endpoints which handle `POST`
requests of requests without a body, mapped to a URL that has the action-name defined as suffixes in the URL.

The example below shows an action endpoint that switches the state of a task from `TODO` into `IN_PROGRESS`.

```kotlin
@PostMapping("/tasks/{id}/start")
fun startTask(@PathVariable("id") identifier: UUID) = ...
```
As explained above it would be possible to send the whole task object with all of its attributes to update the state 
from `TODO` to `IN_PROGRESS`. Sometimes although this is misleading, especially when the state itself is a real 
application state and not an attribute that can edited by users in the traditional sense of "editing" a text box in 
a UI dialog.  
Action endpoints should only be used consciously in situations where it makes sense. They are not intended to be used
to update single attributes of a resource (for single attribute changes, the HTTP `PATCH` method can be used).

## Sample Code
This repository contains sample code that showcases the above explained approaches to embed resources and action 
endpoints with Spring WebMvc and HATEOAS.

The code is divided into two contexts - `user` and `project`. The project context is sub-structured for the entities 
`Project` and `Task`. The context/entity packages are structured into the layers: api, service, repository and model.

The code where nested task resources are embedded into the project resources can be found in the
[ProjectResourceAssemblerHelper](src/main/kotlin/io/nvtc/embeddedresources/project/project/api/resource/assembler/ProjectResourceAssemblerHelper.kt).

The action endpoints can be found in the
[TaskController](src/main/kotlin/io/nvtc/embeddedresources/project/task/api/TaskController.kt).

There are more interesting concepts to be explored by clicking through the source code.

## License
The source code to showcase embedded resources and action endpoints with Spring HATEOAS in this repository is licensed
under the MIT-License ([LICENSE](LICENSE) or [http://opensource.org/licenses/MIT]).