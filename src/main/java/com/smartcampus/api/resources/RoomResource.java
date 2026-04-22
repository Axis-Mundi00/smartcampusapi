package com.smartcampus.api.resources;

import com.smartcampus.api.Room;
import com.smartcampus.api.Sensor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Path("/api/v1/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    // In-memory storage for rooms
    private static Map<String, Room> rooms = new HashMap<>();


    // -----------------------------
    // GET /rooms  (get all rooms)
    // -----------------------------
    @GET
    public Response getAllRooms() {
        return Response.ok(rooms.values()).build();
    }


    // -----------------------------------
    // GET /rooms/{id}  (get room by ID)
    // -----------------------------------
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);

        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room not found");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }

        return Response.ok(room).build();
    }


    // -----------------------------
    // POST /rooms  (create a room)
    // -----------------------------
    @POST
    public Response createRoom(Room room) {

        if (rooms.containsKey(room.getId())) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room already exists");
            return Response.status(Response.Status.CONFLICT)
                    .entity(error)
                    .build();
        }

        rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }


    // -----------------------------
    // DELETE /rooms/{id}
    // -----------------------------
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);

        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room not found");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }

        if (!room.getSensorIds().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Cannot delete room with attached sensors");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        rooms.remove(roomId);

        Map<String, String> msg = new HashMap<>();
        msg.put("message", "Room deleted successfully");
        return Response.ok(msg).build();
    }


    // -------------------------------------------------------
    // GET /rooms/{id}/sensors  (get all sensors in a room)
    // -------------------------------------------------------
    @GET
    @Path("/{roomId}/sensors")
    public Response getSensorsForRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);

        if (room == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Room not found");
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(error)
                    .build();
        }

        // Get all sensors from SensorResource
        Map<String, Sensor> allSensors = SensorResource.getSensors();
        ArrayList<Sensor> sensorsInRoom = new ArrayList<>();

        for (Sensor s : allSensors.values()) {
            if (roomId.equals(s.getRoomId())) {
                sensorsInRoom.add(s);
            }
        }

        return Response.ok(sensorsInRoom).build();
    }
}
