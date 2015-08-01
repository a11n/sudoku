package de.ad.sudoku;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class represents a Sudoku Grid consisting of a 9x9 matrix containing nine 3x3 sub-grids
 * of {@link Cell}s.
 */
public class Grid {

  private final Cell[][] grid;

  private Grid(Cell[][] grid) {
    this.grid = grid;
  }

  /**
   * A static factory method which returns a Grid of a given two-dimensional array of integers.
   * 
   * @param grid a two-dimensional int-array representation of a Grid
   * @return a Grid instance corresponding to the provided two-dimensional int-array
   */
  public static Grid of(int[][] grid) {
    //verifyGrid(grid);

    Cell[][] cells = new Cell[9][9];
    List<List<Cell>> rows = new ArrayList<>();
    List<List<Cell>> columns = new ArrayList<>();
    List<List<Cell>> boxes = new ArrayList<>();

    for (int i = 0; i < 9; i++) {
      rows.add(new ArrayList<>());
      columns.add(new ArrayList<>());
      boxes.add(new ArrayList<>());
    }

    Cell lastCell = null;
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        Cell cell = new Cell(grid[row][column]);
        cells[row][column] = cell;

        rows.get(row).add(cell);
        columns.get(column).add(cell);
        boxes.get((row / 3) * 3 + column / 3).add(cell);

        if (lastCell != null) {
          lastCell.setNextCell(cell);
        }

        lastCell = cell;
      }
    }

    for (int i = 0; i < 9; i++) {
      List<Cell> row = rows.get(i);
      for (Cell cell : row) {
        List<Cell> rowNeighbors = new ArrayList<>(row);
        rowNeighbors.remove(cell);

        cell.setRowNeighbors(rowNeighbors);
      }

      List<Cell> column = columns.get(i);
      for (Cell cell : column) {
        List<Cell> columnNeighbors = new ArrayList<>(column);
        columnNeighbors.remove(cell);

        cell.setColumnNeighbors(columnNeighbors);
      }

      List<Cell> box = boxes.get(i);
      for (Cell cell : box) {
        List<Cell> boxNeighbors = new ArrayList<>(box);
        boxNeighbors.remove(cell);

        cell.setBoxNeighbors(boxNeighbors);
      }
    }

    return new Grid(cells);
  }

  /**
   * A static factory method which returns an empty Grid.
   * 
   * @return an empty Grid
   */
  public static Grid emptyGrid() {
    int[][] emptyGrid = new int[9][9];
    return Grid.of(emptyGrid);
  }

  /**
   * Returns the size of this Grid. This method is useful if you want to iterate over all {@link Cell}s.
   * <br><br>
   * To access one cell use {@link #getCell(int,int)}.
   * <br><br>
   * Note: This is the size of one dimension. This Grid contains size x size {@link Cell}s.
   * @return the size of this Grid
   */
  public int getSize() {
    return grid.length;
  }

  /**
   * Returns the {@link Cell} at the given position within the Grid.
   * <br><br>
   * This Grid has 0 to {@link #getSize()} rows and 0 to {@link #getSize()} columns.
   * @param row the row which contains the {@link Cell}
   * @param column the column which contains the {@link Cell}
   * @return the {@link Cell} at the given position
   */
  public Cell getCell(int row, int column) {
    return grid[row][column];
  }

  /**
   * Checks if a given value is valid for a certain {@link Cell}.
   * <br><br>
   * A value is valid if it does not already exist in the same row, column and box.
   * @param cell the {@link Cell} to check
   * @param value the value to validate
   * @return true if the given value is valid or false otherwise
   */
  public boolean isValidValueForCell(Cell cell, int value) {
    return isValidInRow(cell, value) && isValidInColumn(cell, value) && isValidInBox(cell, value);
  }

  private boolean isValidInRow(Cell cell, int value) {
    return !getRowValuesOf(cell).contains(value);
  }

  private boolean isValidInColumn(Cell cell, int value) {
    return !getColumnValuesOf(cell).contains(value);
  }

  private boolean isValidInBox(Cell cell, int value) {
    return !getBoxValuesOf(cell).contains(value);
  }

  private Collection<Integer> getRowValuesOf(Cell cell) {
    return cell.getRowNeighbors().stream().map(Cell::getValue).collect(Collectors.toList());
  }

  private Collection<Integer> getColumnValuesOf(Cell cell) {
    return cell.getColumnNeighbors().stream().map(Cell::getValue).collect(Collectors.toList());
  }

  private Collection<Integer> getBoxValuesOf(Cell cell) {
    return cell.getBoxNeighbors().stream().map(Cell::getValue).collect(Collectors.toList());
  }

  /**
   * Returns the first empty {@link Cell} of this Grid.
   * <br><br>
   * Note: The result is wrapped by an {@link Optional}.
   * @return a non-null value containing the first empty {@link Cell} if present
   */
  public Optional<Cell> getFirstEmptyCell() {
    Cell firstCell = grid[0][0];
    if (firstCell.isEmpty()) {
      return Optional.of(firstCell);
    }

    return getNextEmptyCellOf(firstCell);
  }

  /**
   * Returns the next empty {@link Cell} consecutively to the given {@link Cell} in this Grid.
   * <br><br>
   * Note: The result is wrapped by an {@link Optional}.
   * @param cell the {@link Cell} of which the next empty {@link Cell} should be obtained
   * @return a non-null value containing the next empty {@link Cell} if present
   */
  public Optional<Cell> getNextEmptyCellOf(Cell cell) {
    Cell nextEmptyCell = null;

    while ((cell = cell.getNextCell()) != null) {
      if (!cell.isEmpty()) {
        continue;
      }

      nextEmptyCell = cell;
      break;
    }

    return Optional.ofNullable(nextEmptyCell);
  }

  /**
   * Returns a {@link String} representation of this Grid.
   * @return a {@link String} representation of this Grid.
   */
  @Override public String toString() {
    return StringConverter.toString(this);
  }

  /**
   * This class represents a Cell within a Sudoku {@link Grid}.
   */
  public static class Cell {
    private int value;
    private Collection<Cell> rowNeighbors;
    private Collection<Cell> columnNeighbors;
    private Collection<Cell> boxNeighbors;
    private Cell nextCell;

    public Cell(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public boolean isEmpty() {
      return value == 0;
    }

    public void setValue(int value) {
      this.value = value;
    }

    public Collection<Cell> getRowNeighbors() {
      return rowNeighbors;
    }

    public void setRowNeighbors(Collection<Cell> rowNeighbors) {
      this.rowNeighbors = rowNeighbors;
    }

    public Collection<Cell> getColumnNeighbors() {
      return columnNeighbors;
    }

    public void setColumnNeighbors(Collection<Cell> columnNeighbors) {
      this.columnNeighbors = columnNeighbors;
    }

    public Collection<Cell> getBoxNeighbors() {
      return boxNeighbors;
    }

    public void setBoxNeighbors(Collection<Cell> boxNeighbors) {
      this.boxNeighbors = boxNeighbors;
    }

    public Cell getNextCell() {
      return nextCell;
    }

    public void setNextCell(Cell nextCell) {
      this.nextCell = nextCell;
    }
  }

  private static class StringConverter {
    public static String toString(Grid grid) {
      StringBuilder builder = new StringBuilder();
      int size = grid.getSize();

      printTopBorder(builder);
      for (int row = 0; row < size; row++) {
        printRowBorder(builder);
        for (int column = 0; column < size; column++) {
          printValue(builder, grid, row, column);
          printRightColumnBorder(builder, column + 1, size);
        }
        printRowBorder(builder);
        System.out.println();
        printBottomRowBorder(builder, row + 1, size);
      }
      printBottomBorder(builder);

      return builder.toString();
    }

    private static void printTopBorder(StringBuilder builder) {
      builder.append("╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗\n");
    }

    private static void printRowBorder(StringBuilder builder) {
      builder.append("║\n");
    }

    private static void printValue(StringBuilder builder, Grid grid, int row, int column) {
      int value = grid.getCell(row, column).getValue();
      String output = value != 0 ? String.valueOf(value) : " ";
      builder.append(" " + output + " ");
    }

    private static void printRightColumnBorder(StringBuilder builder, int column, int size) {
      if (column == size) {
        return;
      }

      if (column % 3 == 0) {
        builder.append("║");
      } else {
        builder.append("│");
      }
    }

    private static void printBottomRowBorder(StringBuilder builder, int row, int size) {
      if (row == size) {
        return;
      }

      if (row % 3 == 0) {
        builder.append("╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣\n");
      } else {
        builder.append("╟───┼───┼───╫───┼───┼───╫───┼───┼───╢\n");
      }
    }

    private static void printBottomBorder(StringBuilder builder) {
      builder.append("╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝\n");
    }
  }
}
