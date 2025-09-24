package com.cinema_package.cinema_project;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> filterMovies(String title, LocalDate date, String location, String genre) {
        List<Movie> movies = movieRepository.findAll();
        List<Movie> filteredMovies = new ArrayList<>();
        for (Movie movie : movies) {
            boolean match = true;
            if (title != null && !movie.getTitle().toLowerCase().contains(title.toLowerCase())) match = false;
            if (date != null && !movie.getDate().isEqual(date)) match = false;
            if (location != null && !movie.getLocation().toLowerCase().contains(location.toLowerCase())) match = false;
            if (genre != null && !movie.getGenre().toLowerCase().contains(genre.toLowerCase())) match = false;
            if (match) filteredMovies.add(movie);
        }
        return filteredMovies;
    }

    public List<Movie> getAllMovies(String title, LocalDate date, String location, String genre) {
        if (title == null && date == null && location == null && genre == null)
            return movieRepository.findAll();
        else
            return filterMovies(title, date, location, genre);
    }

    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + id));
    }

    public List<BookingHistory> getBookingHistory() {
        List<Movie> movies = movieRepository.findAll();
        List<BookingHistory> bookingHistory = new ArrayList<>();
        for (Movie movie : movies) {
            int bookedTickets = movie.getTotalSeats() - movie.getAvailableSeats();
            if (bookedTickets > 0) {
                int totalPrice = bookedTickets * movie.getPrice();
                BookingHistory booking = new BookingHistory(
                        movie.getId(), movie.getTitle(), movie.getDirector(),
                        movie.getDescription(), movie.getGenre(), movie.getDate(),
                        movie.getLocation(), bookedTickets, totalPrice
                );
                bookingHistory.add(booking);
            }
        }
        return bookingHistory;
    }

    public void bookTickets(Integer id, Integer tickets, Integer payment) {
        Movie movie = getMovieById(id);
        int availableSeats = movie.getAvailableSeats();
        if (tickets > availableSeats) throw new IllegalArgumentException("No seats available at this time.");
        int calculatedTotalPrice = tickets * movie.getPrice();
        if (!payment.equals(calculatedTotalPrice)) throw new IllegalArgumentException("Invalid total price.");
        movie.setAvailableSeats(availableSeats - tickets);
        movieRepository.save(movie);
    }

    public void addMovie(CinemaProjectApplication.NewMovieRequest request) {
        Movie movie = new Movie();
        movie.setDescription(request.description());
        movie.setDirector(request.director());
        movie.setGenre(request.genre());
        movie.setTitle(request.title());
        movie.setDate(request.date());
        movie.setLocation(request.location());
        movie.setTotalSeats(request.totalSeats());
        movie.setAvailableSeats(request.availableSeats());
        movie.setPrice(request.price());
        movieRepository.save(movie);
    }

    public void deleteMovie(Integer id) {
        Movie movie = getMovieById(id);
        movieRepository.delete(movie);
    }

    public void updateMovie(Integer id, CinemaProjectApplication.NewMovieRequest request) {
        Movie movie = getMovieById(id);
        movie.setDescription(request.description());
        movie.setDirector(request.director());
        movie.setGenre(request.genre());
        movie.setTitle(request.title());
        movie.setDate(request.date());
        movie.setLocation(request.location());
        movie.setTotalSeats(request.totalSeats());
        movie.setAvailableSeats(request.availableSeats());
        movie.setPrice(request.price());
        movieRepository.save(movie);
    }
}
