package gamestudio;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import gamestudio.consoleUI.ConsoleGameUI;
import gamestudio.consoleUI.ConsoleMenu;
import gamestudio.game.minesweeper.consoleui.ConsoleUI;
import gamestudio.service.CommentService;
import gamestudio.service.FavoriteService;
import gamestudio.service.RatingService;
import gamestudio.service.ScoreService;
import gamestudio.service.impl.CommentServiceRestClient;
import gamestudio.service.impl.FavoriteServiceRestClient;
import gamestudio.service.impl.RatingServiceRestClient;
import gamestudio.service.impl.ScoreServiceRestClient;

@Configuration
@SpringBootApplication
@ComponentScan(basePackages = { "gamestudio" },
excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "gamestudio.server.*"))
public class SpringClient {

	public static void main(String[] args) throws Exception {
		//SpringApplication.run(SpringClient.class, args);
		new SpringApplicationBuilder(SpringClient.class).web(false).run(args);
	}
	
	@Bean
	public CommandLineRunner runner(ConsoleMenu menu) {
		return args -> menu.show();
	}
	
	@Bean
	public ConsoleMenu menu(ConsoleGameUI[] games) {
		return new ConsoleMenu(games);
	}
	
	@Bean
	public ConsoleGameUI consoleUIMines() {
		return new ConsoleUI();
	}
	
	@Bean
	public ConsoleGameUI consoleUIPuzzle() {
		return new gamestudio.game.puzzle.consoleui.ConsoleUI();
	}

	@Bean
	public ConsoleGameUI consoleUIGuessNumber() {
		return new gamestudio.game.guessnumber.consoleui.ConsoleUI();
	}
	
	@Bean
	public RatingService ratingService() {
		return new RatingServiceRestClient();
	}
	
	@Bean
	public ScoreService scoreService() {
		return new ScoreServiceRestClient();
	}
	
	@Bean
	public CommentService commentService() {
		return new CommentServiceRestClient();
	}
	
	@Bean
	public FavoriteService favoriteService() {
		return new FavoriteServiceRestClient();
	}
}
