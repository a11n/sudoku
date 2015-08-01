package de.ad.sudoku;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A Solver is capable of solving a given Sudoku {@link Grid}.
 */
public class Solver {
  private final int[] values;

  /**
   * Constructs a new Solver instance.
   */
  public Solver() {
    List<Integer> randomValues = Arrays.asList(1,2,3,4,5,6,7,8,9);
    Collections.shuffle(randomValues);
    this.values = randomValues.stream().mapToInt(i->i).toArray();
  }

  /**
   * Solves a given {@link Grid} using backtracking.
   * @param grid the {@link Grid} to solve
   * @throws IllegalStateException in case the provided {@link Grid} is invalid.
   */
  public void solve(Grid grid) {
    boolean solvable = solve(grid, grid.getFirstEmptyCell());

    if (!solvable) {
      throw new IllegalStateException("The provided grid is not solvable.");
    }
  }

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
}
