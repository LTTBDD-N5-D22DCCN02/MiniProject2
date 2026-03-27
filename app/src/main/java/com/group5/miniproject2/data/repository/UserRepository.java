package com.group5.miniproject2.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.group5.movieticket.data.dao.UserDao;
import com.group5.movieticket.data.database.AppDatabase;
import com.group5.movieticket.data.model.User;
import java.util.concurrent.Future;

public class UserRepository {
    private final UserDao userDao;

    public UserRepository(Application app) {
        userDao = AppDatabase.getInstance(app).userDao();
    }

    public long insert(User user) throws Exception {
        Future<Long> future = AppDatabase.databaseWriteExecutor.submit(() -> userDao.insert(user));
        return future.get();
    }

    public User login(String username, String password) throws Exception {
        Future<User> future = AppDatabase.databaseWriteExecutor.submit(
                () -> userDao.login(username, password));
        return future.get();
    }

    public boolean usernameExists(String username) throws Exception {
        Future<Integer> f = AppDatabase.databaseWriteExecutor.submit(
                () -> userDao.usernameExists(username));
        return f.get() > 0;
    }

    public void update(User user) {
        AppDatabase.databaseWriteExecutor.execute(() -> userDao.update(user));
    }

    public LiveData<User> getUserById(int id) {
        return userDao.getUserById(id);
    }
}