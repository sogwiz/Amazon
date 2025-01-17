package com.scott_tigers.oncall.utility;

public class PrepareForNextCITSchedule extends Utility {

    public static void main(String[] args) {
	new PrepareForNextCITSchedule().run();
    }

    @SuppressWarnings("unchecked")
    private void run() {
	runCommands(
		CreateTicketClosureReport.class,
		UpdateMasterListWithTicketsClosed.class,
		UpdateUnavailabilityFromQuip.class);
    }

}
