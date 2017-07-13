package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URL;

@RestController
@RequestMapping("/api")
public class CalendarResource {
    // Not used yet
    /*
    @Value("${akl.calendar.id:''}")
    private String calendarId;


    @RequestMapping(value = "/calendar", method = RequestMethod.GET)
    @Timed
    @Cacheable(value = "calendar")
    ComponentList getCalendar() throws IOException, ParserException {
        URL url = new URL("https://calendar.google.com/calendar/ical/" + calendarId + "%40group.calendar.google.com/public/basic.ics");
        InputStream is = url.openStream();
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(is);
        is.close();

        ComponentList events = calendar.getComponents("VEVENT");
        for (int i = 0; i < events.size(); i++) {
            CalendarComponent component = (CalendarComponent) events.get(i);
            component.getName();
            component.getProperties().stream().forEach(a -> a.getName());
        }

        return events;
    }
    */
}
