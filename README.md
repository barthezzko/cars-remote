## Remote cars
Simple MVC Java 8 application to calculate position of a vehicle based on the commands set sent to unit. 

## Technologies/frameworks used:
Framework | Purpose | URL
------------ | ------------- | -------------
Guice | Bean wiring | https://github.com/google/guice/wiki/GettingStarted
Spark | Content in the second column | http://sparkjava.com
Log4j | Logging | https://logging.apache.org/log4j/2.x/
GSON | JSON marshalling | https://github.com/google/gson
Jade | JAde template Engine for MVC | https://github.com/perwendel/spark-template-engines/tree/master/spark-template-jade
BootstrapJS+Jquery | Rich JS UI Components | http://getbootstrap.com/

## Running the application
` java com.barthezzko.web.Server `
When started: http://localhost:9000/

## Configuration
`
server.port = 9000
gridSize = 15

`
## REST Methods
RequestType | Method | Params 
------------ | ------------ | ------------- 
GET | /config | NONE
POST | /calc | String instruction
GET | / | NONE -- not REST actually, returns HTML page body

## Rules
### Format
for the given ` 5,5:RFLFRFLF `
* ` 5,5 ` - initial position coordinates delimited by comma. Should be inside the grid, obviously. Otherwise - throw exception
* ` : ` - delimiter between initial position and a set of instructions
* ` RFLFRFLF ` - set of instructions executed sequentially. If the vehicle goes out of the grid - throw exception
### Command types
Code | Behavior
------------ | -------------
R | rotate clockwise (right)
F | move in forward according to current direction on teh grid
L | rotate counterClockwise (left)

## Simple testing cases (check tests for more)
Input | Expected
------------ | -------------
5,5:RFLFRFLF | 7,7
6,6:FFLFFLFFLFF | 6,6
5,5:FLFLFFRFFF | 1,4

## Testing approach
Unit tests:
* DirectionTest
* CarPositionTest

IntegrationTest
* RestAPIIntegrationTest

