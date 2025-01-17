package com.scott_tigers.oncall.utility;

import java.util.stream.Stream;

import com.scott_tigers.oncall.bean.LTTRTicket;

public class UpdateLttrPlanFrequencies extends UpdateLttrTicketFrequencies {

    public static void main(String[] args) {
	new UpdateLttrPlanFrequencies().run();
    }

    @Override
    protected void run() {
	super.run();
    }

    @Override
    protected Stream<LTTRTicket> getQuipLttrTicketStream() {
        return getLttrQuipPlan();
    }
}
