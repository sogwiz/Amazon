package com.scott_tigers.oncall.utility;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.scott_tigers.oncall.bean.TT;
import com.scott_tigers.oncall.shared.Dates;

public class TicketFlowAggregator {
    private HashMap<String, TicketMetric> metricMap = new HashMap<String, TicketMetric>();

    public void newTicket(TT tt) {
	Stream.of("CreateDate", "ResolvedDate")
		.forEach(dateType -> addMetric(tt, dateType));
    }

    private void addMetric(TT tt, String dateType) {
	try {
	    Optional
		    .of((String) TT.class.getMethod("get" + dateType).invoke(tt))
		    .filter(date -> !date.isEmpty())
		    .map(date -> date.substring(0, 10))
		    .map(this::firstDayOfWeek)
		    .ifPresentOrElse(date -> {
			TicketMetric metric = metricMap.get(date);

			if (metric == null) {
			    metric = new TicketMetric(date);
			    metricMap.put(date, metric);
			}

			metric.addDataPoint(dateType);

		    }, () -> System.out.println("No Create Date"));
	} catch (Exception e) {
	    e.printStackTrace();
	    System.exit(1);
	}
    }

    private String firstDayOfWeek(String date) {
	return Dates.SORTABLE
		.getDateFromString(date)
		.toInstant()
		.atZone(ZoneId.systemDefault())
		.toLocalDate()
		.with(DayOfWeek.MONDAY)
		.toString();
    }

    public List<TicketMetric> getMetrics() {
	Collection<TicketMetric> v1 = metricMap.values();
	Stream<TicketMetric> v2 = v1.stream();
	Stream<TicketMetric> v3 = v2.sorted();
	List<TicketMetric> v4 = v3.collect(Collectors.toList());
	return v4;
    }

}
