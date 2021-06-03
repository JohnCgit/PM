package com.webAppEmergency.Assignation;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.webAppEmergency.Assignation.Assignation;

@Service
public class AssignationService {

	MainRunnable dRunnable;
	private Thread displayThread;
}
