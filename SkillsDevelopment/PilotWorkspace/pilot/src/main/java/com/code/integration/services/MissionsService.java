package com.code.integration.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.code.enums.MediaTypeConstants;

@Path("/missions")
@Consumes(MediaTypeConstants.APPLICATION_JSON)
@Produces(MediaTypeConstants.APPLICATION_JSON)
public class MissionsService {

}
