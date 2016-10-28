import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class ListMaker {
	public static void main(String[] args) {

		List<Game> games = new ArrayList<Game>();
		XStream xstream = new XStream(new StaxDriver());
		FileReader inpFile;
		/*
		 * try { inpFile = new FileReader("GamesWanted.xml");
		 * xstream.alias("game", Game.class); games = (List<Game>)
		 * xstream.fromXML(inpFile); xstream.processAnnotations(Game.class); }
		 * catch (FileNotFoundException e1) { System.out.println(
		 * "File handling error"); e1.printStackTrace(); }
		 */
		
		GameOnSaleFactory gameFactory = new GameOnSaleFactory();
		Game testGame = gameFactory.enlistGame("Moose Effect", 50.0, 0.1, 1);
		Game testGame2 = gameFactory.enlistGame("Diabloo", 20.0, .4, 2);
		games.add(testGame);
		games.add(testGame2);

		games.forEach(game -> System.out.println(game.toString()));
		System.out.println("");

		List<Game> results = scoreGames(games);
		int listNum = 1;
		for (Game game : results) {
			System.out.println(listNum + ": " + game.getTitle() + " | score: " + game.getScore());
			listNum++;
		}

	}

	public static List<Game> scoreGames(List<Game> games) {

		List<Game> gameResults = new ArrayList<Game>();

		int costRate = 40;
		int saleRate = 40;
		int priorityRate = 20;

		System.out
				.println("cost rate : " + costRate + ", sale Rate : " + saleRate + ", priority rate : " + priorityRate);

		gameResults = generateScores(games, costRate, saleRate, priorityRate);
		return gameResults;
	}

	private static List<Game> generateScores(List<Game> games, int costRate, int saleRate, int priorityRate) {

		int priceWeight;
		int priorityWeight;
		int saleWeight;
		List<Game> results = new ArrayList<Game>();

		double lowestPrice = getLowestCost(games);
		GameListingFactory gameListings = new GameListingFactory();

		for (Game game : games) {

			priceWeight = (int) (Math.sqrt(lowestPrice / game.getCost()) * costRate);
			saleWeight = (int) (game.getSale() * saleRate);
			priorityWeight = priorityRate / game.getPriority();

			int score = priceWeight + saleWeight + priorityWeight;
			Game gameToList = gameListings.enlistGame(game.getTitle(), game.getCost(), game.getSale(),
					game.getPriority());
			gameToList.setScore(score);
			results.add(gameToList);
		}
		results = results.stream().sorted((g1, g2) -> Integer.compare(g2.getScore(), g1.getScore()))
				.collect(Collectors.toList());
		return results;
	}

	private static double getLowestCost(List<Game> games) {
		double lowest = Integer.MAX_VALUE;
		for (Game game : games) {
			if (game.getCost() < lowest)
				lowest = game.getCost();
		}
		return lowest;

	}

	public static void printGame(Game game) {
		System.out.println(game.toString());
	}

}
