package com.group5.miniproject2.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.group5.miniproject2.data.dao.*;
import com.group5.miniproject2.data.model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {User.class, Movie.class, Theater.class, Showtime.class, Ticket.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract MovieDao movieDao();
    public abstract TheaterDao theaterDao();
    public abstract ShowtimeDao showtimeDao();
    public abstract TicketDao ticketDao();

    private static volatile AppDatabase INSTANCE;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "movie_ticket_db"
                            )
                            .addCallback(seedCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // ── Seed Data ───────────────────────────────────────────────────────────
    private static final Callback seedCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                AppDatabase database = INSTANCE;
                seedMovies(database);
                seedTheaters(database);
                seedShowtimes(database);
                seedUsers(database);
            });
        }
    };

    private static void seedUsers(AppDatabase db) {
        User u1 = new User("admin", "123456", "admin@cinema.vn", "0901234567", "Admin User");
        User u2 = new User("john", "123456", "john@gmail.com", "0912345678", "John Doe");
        db.userDao().insert(u1);
        db.userDao().insert(u2);
    }

    private static void seedMovies(AppDatabase db) {
        String[] titles   = {"Avengers: Endgame", "Inception", "Interstellar",
                "The Dark Knight", "Spider-Man: No Way Home",
                "Avatar: The Way of Water", "Top Gun: Maverick"};
        String[] genres   = {"Action/Sci-Fi", "Sci-Fi/Thriller", "Sci-Fi/Drama",
                "Action/Crime", "Action/Adventure",
                "Action/Sci-Fi", "Action/Drama"};
        String[] directors = {"Russo Brothers", "Christopher Nolan", "Christopher Nolan",
                "Christopher Nolan", "Jon Watts", "James Cameron", "Joseph Kosinski"};
        String[] casts    = {"Robert Downey Jr., Chris Evans", "Leonardo DiCaprio, Ellen Page",
                "Matthew McConaughey, Anne Hathaway", "Christian Bale, Heath Ledger",
                "Tom Holland, Zendaya", "Sam Worthington, Zoe Saldana",
                "Tom Cruise, Miles Teller"};
        int[]   durations = {181, 148, 169, 152, 148, 192, 137};
        float[] ratings   = {8.4f, 8.8f, 8.6f, 9.0f, 8.2f, 7.6f, 8.3f};
        String[] ages     = {"T13", "T13", "PG", "T13", "PG", "PG", "PG"};
        String[] dates    = {"2019-04-26", "2010-07-16", "2014-11-07",
                "2008-07-18", "2021-12-17", "2022-12-16", "2022-05-27"};
        String[] posters  = {
                "https://upload.wikimedia.org/wikipedia/en/0/0d/Avengers_Endgame_poster.jpg",
                "https://upload.wikimedia.org/wikipedia/en/2/2e/Inception_%282010%29_theatrical_poster.jpg",
                "https://upload.wikimedia.org/wikipedia/en/b/bc/Interstellar_film_poster.jpg",
                "https://upload.wikimedia.org/wikipedia/en/1/1c/The_Dark_Knight_%282008_film%29.jpg",
                "https://upload.wikimedia.org/wikipedia/en/3/37/Spider-Man_No_Way_Home_poster.jpg",
                "https://upload.wikimedia.org/wikipedia/en/5/54/Avatar_The_Way_of_Water_poster.jpg",
                "https://upload.wikimedia.org/wikipedia/en/b/b8/Top_Gun_Maverick_Poster.jpg"
        };

        Movie[] movies = new Movie[titles.length];
        for (int i = 0; i < titles.length; i++) {
            Movie m = new Movie();
            m.setTitle(titles[i]);
            m.setGenre(genres[i]);
            m.setDirector(directors[i]);
            m.setCast(casts[i]);
            m.setDuration(durations[i]);
            m.setRating(ratings[i]);
            m.setAgeRating(ages[i]);
            m.setReleaseDate(dates[i]);
            m.setPosterUrl(posters[i]);
            m.setNowShowing(i < 5);
            m.setComingSoon(i >= 5);
            m.setDescription("Bộ phim " + titles[i] + " - một tác phẩm điện ảnh xuất sắc thuộc thể loại " + genres[i] + ". Đạo diễn: " + directors[i] + ". Diễn viên: " + casts[i]);
            m.setLanguage("Tiếng Anh - Phụ đề Việt");
            movies[i] = m;
        }
        db.movieDao().insertAll(movies);
    }

    private static void seedTheaters(AppDatabase db) {
        Theater t1 = new Theater();
        t1.setName("CGV Vincom Hải Phòng");
        t1.setAddress("Tầng 5, Vincom Plaza, 1 Lê Thánh Tông, Ngô Quyền");
        t1.setCity("Hải Phòng");
        t1.setPhone("1900 6017");
        t1.setRating(4.5f);
        t1.setTotalSeats(120);

        Theater t2 = new Theater();
        t2.setName("Lotte Cinema Hải Phòng");
        t2.setAddress("Tầng 5, Lotte Mart, 6 Trần Phú, Hồng Bàng");
        t2.setCity("Hải Phòng");
        t2.setPhone("1900 6099");
        t2.setRating(4.3f);
        t2.setTotalSeats(100);

        Theater t3 = new Theater();
        t3.setName("Galaxy Cinema Hải Phòng");
        t3.setAddress("Đường Tô Hiệu, Lê Chân, Hải Phòng");
        t3.setCity("Hải Phòng");
        t3.setPhone("028 3823 8888");
        t3.setRating(4.2f);
        t3.setTotalSeats(90);

        db.theaterDao().insertAll(t1, t2, t3);
    }

    private static void seedShowtimes(AppDatabase db) {
        // Lấy ngày hôm nay và ngày mai
        java.time.LocalDate today = java.time.LocalDate.now();
        String[] dates = {
                today.toString(),
                today.plusDays(1).toString(),
                today.plusDays(2).toString()
        };
        String[] times = {"09:00", "11:30", "14:00", "16:30", "19:00", "21:30"};
        String[] types = {"2D", "3D", "IMAX"};
        String[] rooms = {"P1", "P2", "P3"};
        float[] prices = {80000f, 90000f, 120000f};

        // Tạo suất chiếu cho 5 phim × 3 rạp × 3 ngày × 2 ca
        for (int movieId = 1; movieId <= 5; movieId++) {
            for (int theaterId = 1; theaterId <= 3; theaterId++) {
                for (String date : dates) {
                    // 2 suất mỗi combo
                    for (int t = 0; t < 2; t++) {
                        int ti = (movieId + theaterId + t) % times.length;
                        int typeIdx = (movieId - 1) % 3;
                        Showtime s = new Showtime();
                        s.setMovieId(movieId);
                        s.setTheaterId(theaterId);
                        s.setShowDate(date);
                        s.setShowTime(times[ti]);
                        s.setRoomNumber(rooms[theaterId - 1]);
                        s.setScreenType(types[typeIdx]);
                        s.setPrice(prices[typeIdx]);
                        s.setTotalSeats(48);
                        s.setAvailableSeats(48);
                        s.setBookedSeats("");
                        db.showtimeDao().insertAll(s);
                    }
                }
            }
        }
    }
}