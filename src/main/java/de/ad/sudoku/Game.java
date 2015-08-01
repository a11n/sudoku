package de.ad.sudoku;

public class Game {

  public static void main(String... args) {
    Grid grid = new Generator().generate(37);

    Printer printer = new Printer();
    Solver solver = new Solver();

    System.out.println("Grid to solve:");
    printer.print(grid);

    solver.solve(grid);

    System.out.println("Solved grid:");
    printer.print(grid);
  }
}
