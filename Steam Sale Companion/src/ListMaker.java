import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import com.github.goive.steamapi.SteamApi;
import com.github.goive.steamapi.data.SteamApp;
import com.github.goive.steamapi.exceptions.SteamApiException;

public class ListMaker {
	final static Charset ENCODING = StandardCharsets.UTF_8;
	
	public static void main(String[] args) throws SteamApiException {
		
		
		SteamApi steam = new SteamApi("US");
		List<String> gameNames;
		List<Game> games = new ArrayList<Game>();
		List<SteamApp> steamGames;
		
		try {
			gameNames = getGameNames("GameNames.txt");
			steamGames = new ArrayList<SteamApp>();
			for(String name : gameNames){
				steamGames.add(steam.retrieve(name));
			}
			steamGames.forEach(game -> System.out.println(game.getName()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("end API test");
		
	
		
		
		
		
		GameOnSaleFactory gameFactory = new GameOnSaleFactory();
		
		Game testGame = gameFactory.enlistGame("Moose Effect", 50.0, 0.1, 2);
		Game testGame2 = gameFactory.enlistGame("Diabloo", 20.0, .4, 3);
		Game testGame3 = gameFactory.enlistGame("Moon Coaster", 30.0, 0.5, 1);
		games.add(testGame);
		games.add(testGame2);
		games.add(testGame3);
		
		games.forEach(game -> System.out.println(game.toString()));
		System.out.println("");

		List<Game> results = scoreGames(games);
		int listNum = 1;
		for (Game game : results) {
			System.out.printf("%d: %s | score: %.2f %n", listNum, game.getTitle(), game.getScore());
			listNum++;
		}

	}

	public static List<Game> scoreGames(List<Game> games) {

		List<Game> gameResults = new ArrayList<Game>();

		int costRate = 40;
		int saleRate = 30;
		int priorityRate = 10;
		
		System.out
				.println("cost rate : " + costRate + ", sale Rate : " + saleRate + ", priority rate : " + priorityRate);

		gameResults = generateScores(games, costRate, saleRate, priorityRate);
		return gameResults;
	}

	private static List<Game> generateScores(List<Game> games, int costRate, int saleRate, int priorityRate) {

		double priceWeight;
		double priorityWeight;
		double saleWeight;
		List<Game> results = new ArrayList<Game>();
		GameListingFactory gameListings = new GameListingFactory();
		double lowestPrice = getLowestCost(games);

		for (Game game : games) {

			priceWeight =  (Math.sqrt(lowestPrice / game.getCost()) * costRate);
			saleWeight =  (game.getSale() * saleRate);
			priorityWeight =  (priorityRate / game.getPriority());
			double score = priceWeight + saleWeight + priorityWeight;

			Game gameToList = gameListings.enlistGame(game.getTitle(), game.getCost(), game.getSale(),
					game.getPriority());
			gameToList.setScore(score);

			results.add(gameToList);
		}
		results = sortGameList(results);
		return results;
	}

	
	public static List<Game> sortGameList(List<Game> games) {
		return games.parallelStream().sorted((g1, g2) -> Double.compare(g2.getScore(), g1.getScore()))
				.collect(Collectors.toList());
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
	
	public static List<String> getGameNames(String fileName) throws IOException{
		Path path = Paths.get(fileName);
		return Files.readAllLines(path, ENCODING);
	}

}
