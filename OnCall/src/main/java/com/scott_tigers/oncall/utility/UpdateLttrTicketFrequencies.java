package com.scott_tigers.oncall.utility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.scott_tigers.oncall.bean.LTTRTicket;
import com.scott_tigers.oncall.shared.EngineerFiles;
import com.scott_tigers.oncall.shared.Properties;

public abstract class UpdateLttrTicketFrequencies extends Utility {

    private Map<String, LTTRTicket> lttrMap;

    protected void run() {

	lttrMap = LTTRPage.TOP.getMap();

	List<LTTRTicket> lttrPlanTickets = getQuipLttrTicketStream()
		.peek(ticket -> ticket.update(lttrMap))
		.collect(Collectors.toList());

	EngineerFiles.UPDATED_QUIP_LTTR_PLAN
		.write(w -> w.CSV(lttrPlanTickets, Properties.TICKET, Properties.TICKETS_PER_WEEK,
			Properties.CURRENT_TICKETS_PER_WEEK));
    }

    protected abstract Stream<LTTRTicket> getQuipLttrTicketStream();

}
