package com.group5.miniproject2.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.group5.miniproject2.data.dao.TicketDao;
import com.group5.miniproject2.data.database.AppDatabase;
import com.group5.miniproject2.data.model.Ticket;
import com.group5.miniproject2.data.model.TicketDetail;
import java.util.List;
import java.util.concurrent.Future;

public class TicketRepository {
    private final TicketDao ticketDao;

    public TicketRepository(Application app) {
        ticketDao = AppDatabase.getInstance(app).ticketDao();
    }

    public long insert(Ticket ticket) throws Exception {
        Future<Long> f = AppDatabase.databaseWriteExecutor.submit(() -> ticketDao.insert(ticket));
        return f.get();
    }

    public void update(Ticket ticket) {
        AppDatabase.databaseWriteExecutor.execute(() -> ticketDao.update(ticket));
    }

    public LiveData<List<TicketDetail>> getTicketsByUser(int userId) {
        return ticketDao.getTicketsByUser(userId);
    }
}