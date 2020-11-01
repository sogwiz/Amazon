package com.scott_tigers.oncall.utility;

import com.scott_tigers.oncall.shared.EngineerFiles;
import com.scott_tigers.oncall.shared.URL;

public class LauchCITMidweekDocuments extends Utility implements Command {

    public static void main(String[] args) throws Exception {
	new LauchCITMidweekDocuments().run();
    }

    @Override
    public void run() throws Exception {
	EngineerFiles.CIT_WEEK_WELCOME.launch();
	EngineerFiles.CIT_LAST_DAY_EMAIL.launch();
	launchUrl(URL.CIT_ON_CALL_SCHEDULE);
	launchUrl(URL.CIT_SCHEDULE);
    }

}
