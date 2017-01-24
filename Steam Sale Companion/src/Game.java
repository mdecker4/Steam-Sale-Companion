

public abstract class Game {
	
	private String title;
	
	private Double cost;
	
	private Double sale;
	
	
	private Integer priority;
	
	private double score;
	
	public Game(String title, Double cost, Double sale, Integer priority){
		this.cost = cost;
		this.sale = sale;
		this.priority = priority;
		this.title = title;
		//this.score = score;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getSale() {
		return sale;
	}
	public void setSale(Double sale) {
		this.sale = sale;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
	
	public String toString(){
		String result = "";
		result += (this.title + ", $" + this.cost + ", %" + this.sale*100 + ", " + this.priority);
		return result;
	}
}
