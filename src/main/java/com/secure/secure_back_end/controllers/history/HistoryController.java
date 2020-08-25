package com.secure.secure_back_end.controllers.history;

import com.secure.secure_back_end.dto.history.view.HistoryViewModel;
import com.secure.secure_back_end.services.implementations.HistoryServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class HistoryController
{
    private final HistoryServiceImpl historyService;

    public HistoryController(HistoryServiceImpl historyService)
    {
        this.historyService = historyService;
    }

    @GetMapping("/tickets/history/{ticketId}")
    public List<HistoryViewModel> getHistoryForTicket(@PathVariable("ticketId") @Min(1) Long ticketId)
    {
        return this.historyService.getHistoryForTicket(ticketId);
    }
}