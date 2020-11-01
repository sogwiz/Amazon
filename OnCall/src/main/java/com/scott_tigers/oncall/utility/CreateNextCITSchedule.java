package com.scott_tigers.oncall.utility;

import java.io.IOException;

import com.scott_tigers.oncall.schedule.ScheduleCreator;
import com.scott_tigers.oncall.shared.URL;

@SuppressWarnings("unchecked")
public class CreateNextCITSchedule extends Utility {

    public static void main(String[] args) throws IOException {
	new CreateNextCITSchedule().run();
    }

    private void run() throws IOException {
	new ScheduleCreator()
		.startDate("2020-11-09")
//		.endDate("2020-12-31")
//		.endAfterMonths(6)
		.shifts(12)
		.shiftSize(9)
		.maximumShiftFrequency(4)
		.daysBetweenShifts(7)
//		.iterations(10000)
		.timeLimit(20)
		.create();

	runCommands(
		CreateCITOnlineSchedule.class,
		CreateCSVSchedule.class);

	launchUrl(URL.CIT_SCHEDULE);
    }
}
