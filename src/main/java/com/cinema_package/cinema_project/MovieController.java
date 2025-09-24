package com.cinema_package.cinema_project;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping//done
    public List<Movie> getMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String genre

    ) {
        return movieService.getAllMovies(title, date, location, genre);
    }

    @GetMapping("/{id}")//done
    public Movie getMovieById(@PathVariable("id") Integer id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/booking/history")//done
    public List<BookingHistory> getBookingHistory() {
        return movieService.getBookingHistory();
    }


    @PostMapping//done
    public void addMovie(@RequestBody CinemaProjectApplication.NewMovieRequest movie) {
        movieService.addMovie(movie);
    }

    @PostMapping("/booking/{movieId}/{quantity}/{totalPrice}")//done
    public void createBooking(
            @PathVariable("movieId") Integer id,
            @PathVariable("quantity") Integer quantity,
            @PathVariable("totalPrice") Integer totalPrice
    ) {
        movieService.bookTickets(id, quantity, totalPrice);
    }

    @DeleteMapping("/{movieId}")//done
    public void deleteMovie(@PathVariable("movieId") Integer id) {
        movieService.deleteMovie(id);
    }

    @PutMapping("/{movieId}")//done
    public void updateMovie(@PathVariable("movieId") Integer id, @RequestBody CinemaProjectApplication.NewMovieRequest movie) {
        movieService.updateMovie(id, movie);
    }


}