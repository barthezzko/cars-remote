## Remote cars

Simple application to calculate position of a vehicle based on the commands set sent to unit. 
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

## Simple testing cases
Input | Expected
------------ | -------------
5,5:RFLFRFLF | 7,7
6,6:FFLFFLFFLFF | 6,6
5,5:FLFLFFRFFF | 1,4


