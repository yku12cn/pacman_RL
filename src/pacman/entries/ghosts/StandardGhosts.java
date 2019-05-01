package pacman.entries.ghosts;

import java.util.EnumMap;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Usually chase when non-edible and run away when edible.
 * Have some probability of moving randomly in both cases.
 */
public class StandardGhosts extends Controller<EnumMap<GHOST,MOVE>>
{
	private Random rng = new Random();
	private static final double CONSISTENCY = 0.8;
	
	private EnumMap<GHOST,MOVE> Moves = new EnumMap<>(GHOST.class);

	/** Fill all ghost moves. */
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
				
		Moves.clear();
		for (GHOST ghost : GHOST.values())
			if (game.doesGhostRequireAction(ghost))
				Moves.put(ghost,getMove(ghost, game));

		return Moves;
	}
	
	/** Fill one ghost move. */
	private MOVE getMove(GHOST ghost, Game game) {
		
		DM metric = DM.PATH;
		MOVE[] allMoves = MOVE.values();
		
		MOVE lastMove = game.getGhostLastMoveMade(ghost);
		MOVE nextMove = allMoves[rng.nextInt(allMoves.length)];
		
		if (rng.nextDouble() < CONSISTENCY) {
			
			int sourceNode = game.getGhostCurrentNodeIndex(ghost);
			int targetNode = game.getPacmanCurrentNodeIndex();
			
			if (game.getGhostEdibleTime(ghost) > 0)
			    // run away
				nextMove = game.getApproximateNextMoveAwayFromTarget(sourceNode,targetNode,lastMove,metric);
			else
			    // chase the pacman
				nextMove = game.getApproximateNextMoveTowardsTarget(sourceNode,targetNode,lastMove,metric);
		}
		
		return nextMove;
	}
}
