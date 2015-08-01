package de.ad.sudoku;

public class Printer {
  public void print(Grid grid) {
    int size = grid.getSize();

    printTopBorder();
    for (int row = 0; row < size; row++) {
      printRowBorder();
      for (int column = 0; column < size; column++) {
        printValue(grid, row, column);
        printRightColumnBorder(column + 1, size);
      }
      printRowBorder();
      System.out.println();
      printBottomRowBorder(row + 1, size);
    }
    printBottomBorder();
  }

  private void printTopBorder() {
    System.out.println("╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗");
  }

  private void printRowBorder() {
    System.out.print("║");
  }

  private void printValue(Grid grid, int row, int column) {
    int value = grid.getCell(row, column).getValue();
    String output = value != 0 ? String.valueOf(value) : " ";
    System.out.print(" " + output + " ");
  }

  private void printRightColumnBorder(int column, int size) {
    if (column == size) {
      return;
    }

    if (column % 3 == 0) {
      System.out.print("║");
    } else {
      System.out.print("│");
    }
  }

  private void printBottomRowBorder(int row, int size) {
    if (row == size) {
      return;
    }

    if (row % 3 == 0) {
      System.out.println("╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣");
    } else {
      System.out.println("╟───┼───┼───╫───┼───┼───╫───┼───┼───╢");
    }
  }

  private void printBottomBorder() {
    System.out.println("╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝");
  }
}
