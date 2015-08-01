package de.ad.sudoku;

import java.util.Random;

public class Generator {
  private Solver solver;

  public Generator() {
    this.solver = new Solver();
  }

  public Generator(Solver solver) {
    this.solver = solver;
  }

  public Grid generate() {
    Grid grid = Grid.emptyGrid();

    solver.solve(grid);

    return grid;
  }

  public Grid generate(int numberOfEmptyCells) {
    Grid grid = generate();

    Random random = new Random();
    for (int i = 0; i < numberOfEmptyCells; i++) {
      int randomRow = random.nextInt(9);
      int randomColumn = random.nextInt(9);

      Grid.Cell cell = grid.getCell(randomRow, randomColumn);
      if (!cell.isEmpty()) {
        cell.setValue(0);
      } else {
        i--;
      }
    }

    return grid;
  }
}
