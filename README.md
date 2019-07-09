# DROPUNIT

Drop unit is composed of DropWizard providing a simulator server and JUnit invoking tests.
Combined with Rocker containers it is to be used to perform integration-testing for services
that depend on other services. Dropunit provides a simulator for dependent services that can
be configured remotely or by configuration in order to react in a defined way on an incoming
request.
As such, Dropunit is there to support integration-testing with which it simulates dependent
services for a component (engine) under development that on its turn can be tested as a 
real-life environment. In order to realise even a more real-life environment Docker is used to
create the 'servers' with a network connecting the components. Docker is not mandatory to
use Dropunit, but is just an option. The following diagram depicts the usage and setup of
dropunit.

```
  +---------------+                                       +----------+
  | integration-  |                                       |          |
  | test          |                                       | dropunit |
  |               |                                       |          |
  | @Before       |                                       |          |   
  |  setup    ====|========== deliver drop unit =========>| register |<== configfile
  |               |                                       |          |
  |               |          +-------------+              |          |
  |               |          |engine under |              |          |
  |@Test          |          |development  |              |          |
  |  invoke       |          |             |              |          |
  |  request &   =|== req ==>| operation ==|== backend ==>| simulate |
  |  test         |          |             |   req        |          |
  |  response     |          |             |              |          |
  +---------------+          +-------------+              +----------+
```

Dropunit consistst of three components (from left to right):
1) integration-test are JUnit based tests that first configures the dropunit that
   is followed by the requests targetted to the engine-under-development,
2) engine-under-development the component that is to be tested,
3) dropunit which is the simulator for any dependent services for the 
   engine-under-development.

In this project it is implemented as multi-module project that serves part as the
development environment and example how you can use dropunit in your development.
This multi-module project is composed of the following modules: 
1) _docker provides, the setup with docker-compose;
2) _integration-test, where all the jUnit based integration tests are implemented;
3) drop-unit, the common module for client and server (simulator);
4) drop-unit-client, the client with which the dropunit-simulator can be configured as 
   well verified/asserted;
5) drop-unit-simulator, the base for the simlator;
6) engine-under-test, the rest proxy to be replaced with a real rest-service.

In short, if you use this project you would need the modules _docker (1), 
_integration-test (2), drop-unit-simulator (5), and engine-under-test (6).


# DROPUNIT DEVELOPMENT ENVIRONMENT

This section explains how to use the project for its own development purposes. 

### Build the projects

	$ mvn -f pom.xml clean package
	
### Starting IT environment

    $ docker-compose -f _docker/application/docker-compose-it.yml up -d --build --renew-anon-volumes

Wait until all dockers and applications have started.

### Run the intetgrationtests

    $ mvn test -Dtest=*IT -DfailIfNoTests=false


### Stop integration-test environment

    $ docker-compose -f _docker/application/docker-compose-it.yml stop


Given that this project is also setup as a possible example your own development
you can use the same structure and logic.


/* Copyright 2019 Harrie Hazewinkel. All rights reserved. */