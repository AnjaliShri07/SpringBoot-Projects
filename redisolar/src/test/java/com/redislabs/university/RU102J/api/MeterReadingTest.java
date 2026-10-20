package com.redislabs.university.RU102J.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MeterReadingTest {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();
    private MeterReading reading;

    @BeforeEach
    public void setUp() {
        this.reading = new MeterReading(1L,
                ZonedDateTime.parse("2020-01-01T00:00:00+00:00",
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                2.5,
                3.0,
                22.0);
    }

    @Test
    public void testSerializeMap() {
        Map<String, String> map = reading.toMap();
        assertThat(new MeterReading(map), is(reading));
    }

    @Test
    public void serializesToJSON() throws Exception {
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/meterReading.json"), MeterReading.class));

        assertThat(MAPPER.writeValueAsString(reading), is(expected));
    }

    @Test
    @Disabled
    public void deserializesFromJSON() throws Exception {
        assertThat(MAPPER.readValue(fixture("fixtures/meterReading.json"), MeterReading.class),
                is(reading));
    }

    private static String fixture(String path) throws IOException {
        try (InputStream in = MeterReadingTest.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IOException("Fixture not found on classpath: " + path);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
