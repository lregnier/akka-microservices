# Akka Microservices

The aim of this project is to show how a microservices architecture can be laid out using the [Akka] stack as its foundation.


## Microservices projects

The repository consists of the following individual projects:

### user-service
It contains all concrete code related to the User bounding context. That is all of the User domain operations as well as all of the necessary interfaces to interact with it.

Both `common` and `testkit` are dependencies of this project.

### common
It contains all common abstractions which are not specific to any subdomain in particular. Those are for instance base and marker traits for domain objects and components such as _Entities_, _Services_ and _Repositories_. It also houses any common and technical facilities which are wanted to be available across different subdomains.

This project should be autonomous and not depend on any other. No subdomain specific code should be included here.

### testkit
It contains all common abstractations used for writing _Unit_ and _Integration_ specifications accross different microservices.

This project should depend only on `common`.

## Hexagonal Architecture
An [Hexagonal Architecture] has been chosen to structure each microservice project. The Hexagonal Architecture reinforces the separation of business logic from technical concerns which is a key aspect in a microservices environment where different communication technologies and protocols may take place (such as REST, GraphQL, gRPC, messaging queues, etc).

The Hexagonal Architecture defines mainly two distinct layers: the _inside_ and the _outside_. The former holds all of the domain objects and components while the latter houses all of the technical components which facilitate access from and to the inner layer. Those are named _domain_ and _infrastructure_ respectively in this project:

```
microservice
├── domain
│   ├── model
│   └── services
├── infrastrucuture
│   ├── rest
│   ├── grpc
│   ├── messaging
│   └── persistence
└── Main
```


## Running locally
All microservices can be run individually in a local machine using `sbt` commands. In order to do so, `common` and `testkit` projects should be first published locally using below commands:

```sh
cd common/ | sbt publishLocal
```

```sh
cd testkit/ | sbt publishLocal
```

[Akka]: <http://akka.io/>
[Hexagonal Architecture]: https://web.archive.org/web/20180822100852/http://alistair.cockburn.us/Hexagonal+architecture