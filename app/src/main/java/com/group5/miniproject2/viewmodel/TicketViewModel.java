package com.group5.miniproject2.viewmodel;

import android.app.Application;
import androidx.lifecycle.*;
import com.group5.miniproject2.data.model.Ticket;
import com.group5.miniproject2.data.model.TicketDetail;
import com.group5.miniproject2.data.repository.TicketRepository;
import java.util.List;

public class TicketViewModel extends AndroidViewModel {
    private final TicketRepository repo;

    public TicketViewModel(Application app) {
        super(app);
        repo = new TicketRepository(app);
    }

    public LiveData<Long> bookTicket(Ticket ticket) {
        MutableLiveData<Long> result = new MutableLiveData<>();
        new Thread(() -> {
            try { result.postValue(repo.insert(ticket)); }
            catch (Exception e) { result.postValue(-1L); }
        }).start();
        return result;
    }

    public LiveData<List<TicketDetail>> getMyTickets(int userId) {
        return repo.getTicketsByUser(userId);
    }

    public void cancelTicket(Ticket ticket) {
        ticket.setStatus(com.group5.movieticket.utils.Constants.STATUS_CANCELLED);
        repo.update(ticket);
    }
}