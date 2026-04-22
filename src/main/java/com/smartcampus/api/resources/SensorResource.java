package com.smartcampus.api.resources;

import com.smartcampus.api.Sensor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/api/v1/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    // In-memory storage for sensors
    private static Map<String, Sensor> sensors = new HashMap<>();


    // Allow RoomResource to access sensors
    public static Map<String, Sensor> getSensors() {
        return sensors;
    }


    // -----------------------------
    // GET /sensors  (get all sensors)
    // -----------------------------
    @GET
    public Response getAllSensors() {
        return Response.ok(sensors.values()).build();
    }


    // -----------------------------------
    // GET /sensors/{id}  (get sensor by ID)
    // -----------------------------------
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensors.get(sensorId);

        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor not found");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }

        return Response.ok(sensor).build();
    }


    // -----------------------------
    // POST /sensors  (create a sensor)
    // -----------------------------
    @POST
    public Response createSensor(Sensor sensor) {

        if (sensors.containsKey(sensor.getId())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor already exists");
            return Response.status(Response.Status.CONFLICT)
                    .entity(error)
                    .build();
        }

        sensors.put(sensor.getId(), sensor);

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }


    // -----------------------------
    // DELETE /sensors/{id}
    // -----------------------------
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensors.get(sensorId);

        if (sensor == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Sensor not found");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }

        sensors.remove(sensorId);

        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Sensor deleted successfully");
        return Response.ok(msg).build();
    }
}
