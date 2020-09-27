package com.scott_tigers.oncall.utility;

import java.io.IOException;

import com.scott_tigers.oncall.newschedule.ScheduleCreator;

public class CreateNextCITSchedule extends Utility {

    public static void main(String[] args) throws IOException {
	new CreateNextCITSchedule().run();
    }

    private void run() throws IOException {
	new ScheduleCreator()
		.startDate("2020-10-12")
//		.endDate("2020-12-31")
		.endAfterMonths(6)
//		.shifts(24)
		.shiftSize(7)
		.maximumShiftFrequency(3)
		.daysBetweenShifts(7)
//		.iterations(10000)
		.timeLimit(10)
		.create();

	createCSVCITSchedule();
    }
}