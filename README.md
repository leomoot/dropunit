# DROPUNIT

Drop unit is a simulator that can be configured remotely in order to support integration tests. 
As one would like to perform integration tests for a specific component in your infrastructure
it is mostly common to 'place' it in the final infrastructure while all the dependent services 
are the 'real' components. Dropunit is a simulator that you can configure on how to respond
from your integration tests allowing you to invoke operations on the 'engine under test'. 
The 'engine under test' must then be configured that it performs its own remote backend calls 
on the drop unit. 

The following diagram depicts the usage of dropunit.
```
  +------------------+                                           +----------+
  | integration-test |                                           | dropunit | 
  |                  |                                           |          |
  | @Before          |                                           |          |   
  |  setup    =======|============== deliver drop unit =========>| register |   
  |                  |                                           |          |   
  |                  |          +-----------------+              |          |   
  |                  |          |engine under test|              |          |   
  |@Test             |          |                 |              |          |
  |  invoke request =|== req ==>| operation ======|== backend ==>| simulate |   
  |  test response   |          |                 |   req        |          |
  +------------------+          +-----------------+              +----------+ 
```

Dropunit is a name composed of DropWizard that ius used and JUnit that invokes tests.

The project consistst of three modules: 
1) dropunit (the simulator), 
2) integrationtests and,
3) 'engine under test'.
 
# DEVELOPEMENT ENVIRONMENT

### Build the projects

	$ mvn -f pom.xml clean package
	
### Starting IT environment

    $ docker-compose -f _docker/application/docker-compose-it.yml up -d --build --renew-anon-volumes

Wait until all dockers and applications have started.

### Run the intetgrationtests

    $ mvn test -Dtest=*IT -DfailIfNoTests=false


### Stop integration-test environment

    $ docker-compose -f _docker/application/docker-compose-it.yml stop

