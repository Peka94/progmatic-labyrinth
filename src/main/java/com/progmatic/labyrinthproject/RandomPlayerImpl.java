package com.progmatic.labyrinthproject;

import com.progmatic.labyrinthproject.enums.Direction;
import com.progmatic.labyrinthproject.interfaces.Labyrinth;
import com.progmatic.labyrinthproject.interfaces.Player;
import java.util.List;

public class RandomPlayerImpl implements Player{

    @Override
    public Direction nextMove(Labyrinth l) {
        List<Direction> pM = l.possibleMoves();
        return pM.get((int)(Math.random() * pM.size()));
    }

}
