import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerException;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;


public class ListMaker {
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerException{
		
		File xmlFile = new File("GamesWanted.XML");
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(xmlFile);
		document.getDocumentElement().normalize();
		XPathFactory xpfactory = XPathFactory.newInstance();
		XPath path = xpfactory.newXPath();
		
		ArrayList<GameOnSale> games = new ArrayList<GameOnSale>();
		
		XStream gameStream = new XStream();
		
		String title = path.evaluate("games/game[1]/title", document);
		Double cost = Double.parseDouble(path.evaluate("/games/game[1]/cost", document));
		Double sale = Double.parseDouble(path.evaluate("/games/game[1]/sale", document));
		int priority = Integer.parseInt(path.evaluate("/games/game[1]/priority", document));
		GameOnSale game1 = new GameOnSale(title, cost, sale, priority);
		games.add(game1);
		
		title = path.evaluate("games/game[2]/title", document);
		cost = Double.parseDouble(path.evaluate("/games/game[2]/cost", document));
		sale = Double.parseDouble(path.evaluate("/games/game[2]/sale", document));
		priority = Integer.parseInt(path.evaluate("/games/game[2]/priority", document));
		GameOnSale game2 = new GameOnSale(title, cost, sale, priority);
		games.add(game2);
		
		title = path.evaluate("games/game[3]/title", document);
		cost = Double.parseDouble(path.evaluate("/games/game[3]/cost", document));
		sale = Double.parseDouble(path.evaluate("/games/game[3]/sale", document));
		priority = Integer.parseInt(path.evaluate("/games/game[3]/priority", document));
		GameOnSale game3 = new GameOnSale(title, cost, sale, priority);
		games.add(game3);

		title = path.evaluate("games/game[4]/title", document);
		cost = Double.parseDouble(path.evaluate("/games/game[4]/cost", document));
		sale = Double.parseDouble(path.evaluate("/games/game[4]/sale", document));
		priority = Integer.parseInt(path.evaluate("/games/game[4]/priority", document));
		GameOnSale game4 = new GameOnSale(title, cost, sale, priority);
		games.add(game4);
		
		title = path.evaluate("games/game[5]/title", document);
		cost = Double.parseDouble(path.evaluate("/games/game[5]/cost", document));
		sale = Double.parseDouble(path.evaluate("/games/game[5]/sale", document));
		priority = Integer.parseInt(path.evaluate("/games/game[5]/priority", document));
		GameOnSale game5 = new GameOnSale(title, cost, sale, priority);
		games.add(game5);
		
		title = path.evaluate("games/game[6]/title", document);
		cost = Double.parseDouble(path.evaluate("/games/game[6]/cost", document));
		sale = Double.parseDouble(path.evaluate("/games/game[6]/sale", document));
		priority = Integer.parseInt(path.evaluate("/games/game[6]/priority", document));
		GameOnSale game6 = new GameOnSale(title, cost, sale, priority);
		games.add(game6);
		
		title = path.evaluate("games/game[7]/title", document);
		cost = Double.parseDouble(path.evaluate("/games/game[7]/cost", document));
		sale = Double.parseDouble(path.evaluate("/games/game[7]/sale", document));
		priority = Integer.parseInt(path.evaluate("/games/game[7]/priority", document));
		GameOnSale game7 = new GameOnSale(title, cost, sale, priority);
		games.add(game7);
		
		
		for(GameOnSale game : games){
			System.out.println(game.getTitle() + ", cost: " + game.getCost() + ", sale: " + 
					game.getSale() + ", personal priority: " + game.getPriority());
		}
		
		System.out.println("");
		
		List<ListOfGames> results = scoreGames(games);
		int listNum = 1;
		for(ListOfGames game : results){
			System.out.println(listNum + ": " + game.getName() + ", score: " + game.getScore());
			listNum++;
		}
		
	}
	
		public static List<ListOfGames> scoreGames(ArrayList<GameOnSale> games){
			//make an empty list
			List<ListOfGames> gameResults = new ArrayList<ListOfGames>();
			
			//find the lowest price and best sale
			double lowestPrice = 100;
			//double bestSale = .1;
			for(GameOnSale game : games){
				if ( game.getCost() < lowestPrice ) lowestPrice = game.getCost();
				//if ( game.getSale() > bestSale) bestSale = game.getSale();
				
			}
			
			//generate scores
			int priceWeight;
			int priorityWeight;
			int saleWeight;
			
			int costRate = 40;
			int saleRate = 40;
			int priorityRate = 20;
			
			System.out.println("cost rate : " + costRate + ", sale Rate : " + 
					saleRate + ", priority rate : " + priorityRate);
			
			for(GameOnSale game : games){
				//generate weights
				
				priceWeight =  (int) (Math.sqrt(lowestPrice / game.getCost()) * costRate) ;
				saleWeight = (int) (game.getSale() * saleRate) ;
				priorityWeight = priorityRate / game.getPriority() ;
				
				//add score and name to list
				int score = priceWeight + saleWeight + priorityWeight;
				gameResults.add(new ListOfGames(score, game.getTitle()));
			}
			
			Collections.sort(gameResults , Collections.reverseOrder());
			return gameResults;
		}
}
