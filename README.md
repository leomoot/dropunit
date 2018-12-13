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
1) drop-unit (the package implementing dropunit), 
2) drop-unit (the simulator where you register the drops), 
3) engine-under-test (the rest proxy to be replaced with a real rest-service)and,
4) integrationtests (the integration tests that register their own dropunits and performs the test on the 'engine-under-test').


# DEVELOPMENT

DropUnit is based on the registration of a DropUnitDto that has the following properties:
- url: the URL-path for whcih this droppy will respond to
- method: the HTTP-method to which must be responded
- requestContentType: when provided the content-type of the request that needs to match
- requestBody: when provided the body of the request that needs to match
- responseCode: the response code to be returned when all request parameters are matched.
- responseContentType: when provided the content-type of the response to be returned when all request parameters are matched.
- responseBody: when provided the body of the response to be returned when all request parameters are matched.
- responseDelay: when provided the delay before the response will be returned. Useful to simulate slow servers of connection timeouts.


# DEVELOPMENT ENVIRONMENT

### Build the projects

	$ mvn -f pom.xml clean package
	
### Starting IT environment

    $ docker-compose -f _docker/application/docker-compose-it.yml up -d --build --renew-anon-volumes

Wait until all dockers and applications have started.

### Run the intetgrationtests

    $ mvn test -Dtest=*IT -DfailIfNoTests=false


### Stop integration-test environment

    $ docker-compose -f _docker/application/docker-compose-it.yml stop

