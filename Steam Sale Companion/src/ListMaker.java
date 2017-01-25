import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.github.goive.steamapi.SteamApi;
import com.github.goive.steamapi.data.SteamApp;
import com.github.goive.steamapi.exceptions.SteamApiException;

public class ListMaker {
	final static Charset ENCODING = StandardCharsets.UTF_8;

	public static void main(String[] args) throws SteamApiException {

		SteamApi steam = new SteamApi("US");
		List<String> gameNames;
		List<SteamApp> steamGames;
		List<Game> results;

		try {
			gameNames = getGameNames("GameNames.txt");
			steamGames = new ArrayList<SteamApp>();
			for (String name : gameNames) {
				steamGames.add(steam.retrieve(name));
			}
			results = scoreGames(steamGames);

			for (Game game : results) {
				System.out.printf("%s | score: %.2f \n", game.getSteamGame().getName(), game.getScore());
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static List<Game> scoreGames(List<SteamApp> games) {

		List<Game> gameResults = new ArrayList<Game>();

		double costRate = 40;
		double saleRate = 20;
		double reviewRate = 40;

		System.out.println("cost rate : " + costRate + ", sale Rate : " + saleRate + ", review rate : " + reviewRate);

		gameResults = generateScores(games, costRate, saleRate, reviewRate);
		return gameResults;
	}

	private static List<Game> generateScores(List<SteamApp> games, double costRate, double saleRate, double reviewRate) {

		double priceWeight;
		double reviewWeight;
		double saleWeight;
		double lowestPrice = getLowestCost(games);
		List<Game> gameWithScores = new ArrayList<Game>();

		for (SteamApp game : games) {

			priceWeight = (Math.sqrt(lowestPrice / game.getPrice()) * costRate);
			saleWeight = (game.getPriceDiscountPercentage() / 100 * saleRate);

			if (game.getMetacriticScore() != null)
				reviewWeight = ((reviewRate / (double)game.getMetacriticScore())*100);
			else
				reviewWeight = 0;

			double score = priceWeight + saleWeight + reviewWeight;
			Game newGame = new Game(game, score);
			gameWithScores.add(newGame);
		}

		return gameWithScores;
	}

	public static List<SteamApp> sortGameList(List<Game> games) {
		throw new UnsupportedOperationException();
	}

	private static double getLowestCost(List<SteamApp> games) {
		double lowest = Integer.MAX_VALUE;
		for (SteamApp game : games) {
			if (game.getPrice() < lowest)
				lowest = game.getPrice();
		}
		return lowest;

	}

	public static List<String> getGameNames(String fileName) throws IOException {
		Path path = Paths.get(fileName);
		return Files.readAllLines(path, ENCODING);
	}

}
