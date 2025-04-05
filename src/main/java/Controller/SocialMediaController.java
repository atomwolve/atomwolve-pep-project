package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Service.AccountService;
import Service.MessageService;;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;
    
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("localhost:8080/register", this::postAccountHandler);   //TODO: check if localhost is needed
        // app.put("localhost:8080/login", this::updateFlightHandler); //TODO: check if {login creds} is needed
        app.post("localhost:8080/messages", this::postMessageHandler);
        app.get("localhost:8080/messages", this::getAllMessagesHandler);
        app.get("localhost:8080/messages/{message_id}", this::getMessageByIDHandler);
        app.get("localhost:8080/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);

        app.patch("localhost:8080/messages/{message_id}", this::updateMessageHandler);
        app.delete(("localhost:8080/messages/{message_id}"), this::deleteMessageHandler);
        return app;
    }

    private void postAccountHandler(Context ctx) throws JsonProcessingException
    {

    }
    private void deleteMessageHandler(Context ctx){

    }
    private void updateMessageHandler(Context ctx){

    }
    private void getAllMessagesHandler(Context ctx){
        // ctx.json(messageService.getAllMessages());
    }
    private void getAllMessagesByUserHandler(Context ctx){
        // ctx.json(messageService.getAllMessages());
    }
    private void getMessageByIDHandler(Context ctx){
        // ctx.json(messageService.getAllMessages());
    }
    private void postMessageHandler(Context ctx){

    }
    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postFlightHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Flight flight = mapper.readValue(ctx.body(), Flight.class);
        Flight addedFlight = flightService.addFlight(flight);
        if(addedFlight==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedFlight));
        }
    }
    private void updateFlightHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Flight flight = mapper.readValue(ctx.body(), Flight.class);
        int flight_id = Integer.parseInt(ctx.pathParam("flight_id"));
        Flight updatedFlight = flightService.updateFlight(flight_id, flight);
        System.out.println(updatedFlight);
        if(updatedFlight == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedFlight));
        }

    }
    private void getAllFlightsHandler(Context ctx){
        ctx.json(flightService.getAllFlights());
    }
    private void getAllFlightsDepartingFromCityArrivingToCityHandler(Context ctx) {
        ctx.json(flightService.getAllFlightsFromCityToCity(ctx.pathParam("departure_city"),
                ctx.pathParam("arrival_city")));
    }
}