package com.group5.miniproject2.viewmodel;

import android.app.Application;
import androidx.lifecycle.*;
import com.group5.miniproject2.data.model.User;
import com.group5.miniproject2.data.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repo;

    public UserViewModel(Application app) {
        super(app);
        repo = new UserRepository(app);
    }

    public LiveData<User> login(String username, String password) {
        MutableLiveData<User> result = new MutableLiveData<>();
        new Thread(() -> {
            try { result.postValue(repo.login(username, password)); }
            catch (Exception e) { result.postValue(null); }
        }).start();
        return result;
    }

    public LiveData<Long> register(User user) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        new Thread(() -> {
            try {
                if (repo.usernameExists(user.getUsername())) { result.postValue(-1L); return; }
                result.postValue(repo.insert(user));
            } catch (Exception e) { result.postValue(0L); }
        }).start();
        return result;
    }

    public LiveData<User> getUserById(int id) { return repo.getUserById(id); }

    public void updateUser(User user) { repo.update(user); }
}