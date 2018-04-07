package fms.service;

import com.google.gson.Gson;

import org.junit.*;

import fms.dao.PersonsDao;
import fmshared.fmrequest.LoadRequest;
import fmshared.model.Persons;

import static org.junit.Assert.*;


public class LoadServiceTest {
    @After
    public void tearDown() {
        ClearService.clear();
    }

    @Test
    public void validLoad() {
        Gson gson = new Gson();
        Persons[] persons = null;

        boolean passed = true;

        try {
            LoadRequest loadReq = gson.fromJson("{\n" +
                    "  \"users\": [\n" +
                    "    {\n" +
                    "      \"userName\": \"sheila\",\n" +
                    "      \"password\": \"parker\",\n" +
                    "      \"email\": \"sheila@parker.com\",\n" +
                    "      \"firstName\": \"Sheila\",\n" +
                    "      \"lastName\": \"Parker\",\n" +
                    "      \"gender\": \"f\",\n" +
                    "      \"personID\": \"Sheila_Parker\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"persons\": [\n" +
                    "    {\n" +
                    "      \"firstName\": \"Sheila\",\n" +
                    "      \"lastName\": \"Parker\",\n" +
                    "      \"gender\": \"f\",\n" +
                    "      \"personID\": \"Sheila_Parker\",\n" +
                    "      \"father\": \"Patrick_Spencer\",\n" +
                    "      \"mother\": \"Im_really_good_at_names\",\n" +
                    "      \"descendant\": \"sheila\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"firstName\": \"Patrick\",\n" +
                    "      \"lastName\": \"Spencer\",\n" +
                    "      \"gender\": \"m\",\n" +
                    "      \"personID\":\"Patrick_Spencer\",\n" +
                    "      \"spouse\": \"Im_really_good_at_names\",\n" +
                    "      \"descendant\": \"sheila\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"firstName\": \"CS240\",\n" +
                    "      \"lastName\": \"JavaRocks\",\n" +
                    "      \"gender\": \"f\",\n" +
                    "      \"personID\": \"Im_really_good_at_names\",\n" +
                    "      \"spouse\": \"Patrick_Spencer\",\n" +
                    "      \"descendant\": \"sheila\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"events\": [\n" +
                    "    {\n" +
                    "      \"eventType\": \"started family map\",\n" +
                    "      \"personID\": \"Sheila_Parker\",\n" +
                    "      \"city\": \"Salt Lake City\",\n" +
                    "      \"country\": \"United States\",\n" +
                    "      \"latitude\": 40.7500,\n" +
                    "      \"longitude\": -110.1167,\n" +
                    "      \"year\": 2016,\n" +
                    "      \"eventID\": \"Sheila_Family_Map\",\n" +
                    "      \"descendant\":\"sheila\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"eventType\": \"fixed this thing\",\n" +
                    "      \"personID\": \"Patrick_Spencer\",\n" +
                    "      \"city\": \"Provo\",\n" +
                    "      \"country\": \"United States\",\n" +
                    "      \"latitude\": 40.2338,\n" +
                    "      \"longitude\": -111.6585,\n" +
                    "      \"year\": 2017,\n" +
                    "      \"eventID\": \"I_hate_formatting\",\n" +
                    "      \"descendant\": \"sheila\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n", LoadRequest.class);

            loadReq.populateValues();

            LoadService.load(loadReq);

            persons = PersonsDao.getAll("sheila");
        }
        catch (Exception e) {
            passed = false;
        }

        if (persons == null || persons.length == 0)
            passed = false;

        assertTrue(passed);
    }

    @Test
    public void invalidLoad() {
        Gson gson = new Gson();
        Persons[] persons = null;

        boolean passed = true;

        try {
            LoadRequest loadReq = gson.fromJson("{\n" +
                    "  \"users\": [\n" +
                    "    {\n" +
                    "      \"userName\": \"sheila\",\n" +
                    "      \"password\": \"parker\",\n" +
                    "      \"email\": \"sheila@parker.com\",\n" +
                    "      \"firstName\": \"Sheila\",\n" +
                    "      \"lastName\": \"Parker\",\n" +
                    "      \"gender\": \"fm\",\n" +
                    "      \"personID\": \"Sheila_Parker\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"persons\": [\n" +
                    "    {\n" +
                    "      \"firstName\": \"Sheila\",\n" +
                    "      \"lastName\": \"Parker\",\n" +
                    "      \"gender\": \"f\",\n" +
                    "      \"personID\": \"Sheila_Parker\",\n" +
                    "      \"father\": \"Patrick_Spencer\",\n" +
                    "      \"mother\": \"Im_really_good_at_names\",\n" +
                    "      \"descendant\": \"sheila\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"firstName\": \"Patrick\",\n" +
                    "      \"lastName\": \"Spencer\",\n" +
                    "      \"gender\": \"m\",\n" +
                    "      \"personID\":\"Patrick_Spencer\",\n" +
                    "      \"spouse\": \"Im_really_good_at_names\",\n" +
                    "      \"descendant\": \"sheila\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"firstName\": \"CS240\",\n" +
                    "      \"lastName\": \"JavaRocks\",\n" +
                    "      \"gender\": \"f\",\n" +
                    "      \"personID\": \"Im_really_good_at_names\",\n" +
                    "      \"spouse\": \"Patrick_Spencer\",\n" +
                    "      \"descendant\": \"sheila\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"events\": [\n" +
                    "    {\n" +
                    "      \"eventType\": \"started family map\",\n" +
                    "      \"personID\": \"Sheila_Parker\",\n" +
                    "      \"city\": \"Salt Lake City\",\n" +
                    "      \"country\": \"United States\",\n" +
                    "      \"latitude\": 40.7500,\n" +
                    "      \"longitude\": -110.1167,\n" +
                    "      \"year\": 2016,\n" +
                    "      \"eventID\": \"Sheila_Family_Map\",\n" +
                    "      \"descendant\":\"sheila\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"eventType\": \"fixed this thing\",\n" +
                    "      \"personID\": \"Patrick_Spencer\",\n" +
                    "      \"city\": \"Provo\",\n" +
                    "      \"country\": \"United States\",\n" +
                    "      \"latitude\": 40.2338,\n" +
                    "      \"longitude\": -111.6585,\n" +
                    "      \"year\": 2017,\n" +
                    "      \"eventID\": \"I_hate_formatting\",\n" +
                    "      \"descendant\": \"sheila\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}\n", LoadRequest.class);

            loadReq.populateValues();

            LoadService.load(loadReq);

            persons = PersonsDao.getAll("sheila");
        }
        catch (Exception e) {
            passed = false;
        }

        if (persons == null || persons.length == 0)
            passed = false;

        assertFalse(passed);
    }
}