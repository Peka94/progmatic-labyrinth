package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.CellType;
import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.exceptions.CellException;
import com.progmatic.labyrinthproject.exceptions.InvalidMoveException;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author pappgergely
 */
public class LabyrinthImpl implements Labyrinth {

    private CellType[][] labirynth;
    private Coordinate playerCoordinate;

    public LabyrinthImpl() {

    }

    @Override
    public void loadLabyrinthFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int width = Integer.parseInt(sc.nextLine());
            int height = Integer.parseInt(sc.nextLine());
            labirynth = new CellType[height][width];
            for (int hh = 0; hh < width; hh++) {
                String line = sc.nextLine();
                for (int ww = 0; ww < height; ww++) {
                    switch (line.charAt(ww)) {
                        case 'W':
                            labirynth[hh][ww] = CellType.WALL;
                            break;
                        case 'E':
                            labirynth[hh][ww] = CellType.END;
                            break;
                        case 'S':
                            labirynth[hh][ww] = CellType.START;
                            this.playerCoordinate = new Coordinate(ww, hh);
                            break;
                        case ' ':
                            labirynth[hh][ww] = CellType.EMPTY;
                            break;
                    }
                }
            }
        } catch (FileNotFoundException | NumberFormatException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public int getWidth() {
        if (this.labirynth == null) {
            return -1;
        } else {
            return this.labirynth[0].length;
        }
    }

    @Override
    public int getHeight() {
        if (this.labirynth == null) {
            return -1;
        } else {
            return this.labirynth.length;
        }
    }

    @Override
    public CellType getCellType(Coordinate c) throws CellException {
        if (c.getRow() < 0 || c.getCol() < 0 || getHeight() - 1 < c.getRow() || getWidth() - 1 < c.getCol()) {
            throw new CellException(c, "Nem megfelelő.");
        } else {
            return this.labirynth[c.getRow()][c.getCol()];
        }
    }

    @Override
    public void setSize(int width, int height) {
        if (width < 0 || height < 0) {
            try {
                throw new Exception();
            } catch (Exception ex) {
            }
        } else {
            this.labirynth = new CellType[height][width];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    labirynth[i][j] = CellType.EMPTY;
                }
            }
        }
    }

    @Override
    public void setCellType(Coordinate c, CellType type) throws CellException {
        if ((c.getRow() < 0 || c.getRow() >= labirynth.length)|| c.getCol() < 0 || c.getCol() >= labirynth[c.getRow()].length) {
            throw new CellException(c, "Coordinate is out of the labyrinth.");
        }
        if (CellType.START == type) {
            playerCoordinate = c;
        }
        labirynth[c.getRow()][c.getCol()] = type;
    }

    @Override
    public Coordinate getPlayerPosition() {
        return this.playerCoordinate;
    }

    @Override
    public boolean hasPlayerFinished() {
        return CellType.END == labirynth[playerCoordinate.getCol()][playerCoordinate.getRow()];
    }

    @Override
    public List<Direction> possibleMoves() {
        List<Direction> possibleMoves = new ArrayList<>();
        int pR = playerCoordinate.getRow();
        int pC = playerCoordinate.getCol();

        if (pR > 0 && labirynth[pR - 1][pC] == CellType.EMPTY) {
            possibleMoves.add(Direction.NORTH);
        } else if (labirynth[pR + 1][pC] == CellType.EMPTY) {
            possibleMoves.add(Direction.SOUTH);
        } else if (pC > 0 && labirynth[pR][pC - 1] == CellType.EMPTY) {
            possibleMoves.add(Direction.WEST);
        } else if (labirynth[pR][pC + 1] == CellType.EMPTY) {
            possibleMoves.add(Direction.EAST);
        }
        return possibleMoves;
    }

    @Override
    public void movePlayer(Direction direction) throws InvalidMoveException {
        int pR = playerCoordinate.getRow();
        int cR = playerCoordinate.getCol();
        switch (direction) {
            case NORTH:
                if (pR - 1 < 0 || labirynth[pR - 1][cR] == CellType.WALL) {
                    throw new InvalidMoveException();
                } else {
                    playerCoordinate = new Coordinate(cR, pR - 1);
                    break;
                }
            case SOUTH:
                if (pR + 1 >= labirynth.length || labirynth[pR + 1][cR] == CellType.WALL) {
                    throw new InvalidMoveException();
                } else {
                    playerCoordinate = new Coordinate(cR, pR + 1);
                    break;
                }
            case WEST:
                if (cR - 1 < 0 || labirynth[pR][cR - 1] == CellType.WALL) {
                    throw new InvalidMoveException();
                } else {
                    playerCoordinate = new Coordinate(cR - 1, pR);
                    break;
                }
            case EAST:
                if (cR + 1 >= labirynth[pR].length || labirynth[pR][cR + 1] == CellType.WALL) {
                    throw new InvalidMoveException();
                } else {
                    playerCoordinate = new Coordinate(cR + 1, pR);
                    break;
                }
        }
    }

}
