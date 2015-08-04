#Sudoku

A Sudoku library for Java and Android.

It features a `Generator` to generate random Sudoku `Grid`s of various complexity as well as a `Solver` to solve any provided `Grid` using backtracking.

##What is Sudoku?
>Sudoku is a logic-based, combinatorial number-placement puzzle. The objective is to fill a 9×9 grid with digits so that each column, each row, and each of the nine 3×3 sub-grids that compose the grid (also called "boxes") contains all of the digits from 1 to 9.
*(Source [Wikipedia](https://en.wikipedia.org/wiki/Sudoku))*

###Example
```
╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗
║   │   │   ║ 6 │   │ 2 ║   │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │ 9 │ 2 ║   │   │   ║ 3 │ 1 │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 8 │   │ 5 ║   │ 1 │ 4 ║ 6 │ 9 │   ║
╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
║ 1 │ 3 │   ║ 4 │   │   ║ 7 │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 4 │   │ 9 ║   │   │ 8 ║ 1 │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │ 5 │   ║ 1 │ 3 │ 6 ║ 4 │   │ 9 ║
╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
║   │   │   ║   │   │ 1 ║ 5 │ 8 │ 7 ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║ 2 │   │ 1 ║ 5 │   │   ║   │   │   ║
╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
║   │ 8 │ 7 ║ 9 │ 4 │ 3 ║ 2 │   │   ║
╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝
```

##API
This library basically features three classes
* `Generator`: Generates random Sudoku `Grid`s of various complexity.
* `Grid`: Represents a Sudoku `Grid` and features a variety of convenient methods.
* `Solver`: Solves any provided `Grid` using backtracking.

For detailed information check the [javadoc](http://a11n.github.io/sudoku/).

###How to use `Generator`?
In order to generate a new, random Sudoku `Grid` the `Generator` class features a straightforward `generate()` method.
This method takes the `numberOfEmptyCells` as parameter. This parameter controls the complexity of the resulting `Grid` for human players.
The complexity increases with a higher amount of empty cells. The example `Grid` shown above contains 42 empty cells.

```java
Generator generator = new Generator();
Grid grid = generator.generate(42);
```

###How to use `Grid`?
In order to display the `Grid` in your application you have to iterate over the `Grid` utilizing its `getSize()` and `getCell()` functions.
For console-based application you may also use its convenient `toString()` method which produces a textual representation as shown in the example grid above.
```java
int size = grid.getSize();
for (int row = 0; row < size; row++) {
  for (int column = 0; column < size; column++) {
    Cell cell = grid.getCell(row, column);
    //do something with cell
  }
}
```
In order to create a `Grid` using standard Java data types you can use a two-dimensional array of integers and `Grid`'s static factory method `of`.
```java
//9x9
int[][] rawGrid = new int[][]{
  {0, 0, 0, 6, 0, 2, 0, 0, 0},
  ...
  {0, 8, 7, 9, 4, 3, 2, 0, 0}
};

Grid grid = Grid.of(rawGrid);
```

###How to use `Solver`?
The solver solves any valid Grid using backtracking.
```java
Solver solver = new Solver();
Grid grid = ...

solver.solve(grid);
```

####Backtracking

#####Motivation
The reason I created this library was that I wanted to understand how to solve a Sudoku puzzle programmatically.

I was afraid that a sort of *"brute force"* technique will take too long. Thus, my first solution followed the concept of *"candidate search"*. This lead to many lines of code and only worked for simple - medium level Sudokus.
At a certain point I needed to make assumptions anyway, so I started to use backtracking. Very quickly I've thrown away my solution and started to use backtracking completely, which simplified the algorithm's code a lot.

#####Algorithm
Backtracking is brute force. The algorithm takes the first valid value and goes on with this approach until the puzzle is solved or there is no more valid value left. If there is no more valid value left (but the puzzle is not solved yet) it tracks back to the last known valid value and tries another value.

That way the algorithm will always find a valid solution unless the initially provided puzzle is invalid! It will even fill an empty puzzle (with no pre-filled cells at all) which makes it also ideal to be used to generate new puzzles.

However, backtracking is a perfect example for recursion. Below is all the code it takes so solve any Sudoku puzzle.

```java
private final int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

private boolean solve(Grid grid, Optional<Grid.Cell> cell) {
    if (!cell.isPresent()) {
      return true;
    }

    for (int value : values) {
      if (grid.isValidValueForCell(cell.get(), value)) {
        cell.get().setValue(value);
        if (solve(grid, grid.getNextEmptyCellOf(cell.get()))) return true;
        cell.get().setValue(0);
      }
    }

    return false;
  }
```
