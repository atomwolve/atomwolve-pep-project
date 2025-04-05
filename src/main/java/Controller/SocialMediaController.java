package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Service.*;
import Model.*;
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
        // app.put("localhost:8080/login", this::updateAccountHandler); //TODO: check if {login creds} is needed
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
        ctx.json(messageService.deleteMessageByID(Integer.parseInt(ctx.pathParam("message_id"))));
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessage(message_id, message);
        System.out.println(updatedMessage);
        if(updatedMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    private void getAllMessagesHandler(Context ctx){
        ctx.json(messageService.getAllMessages());
    }

    private void getAllMessagesByUserHandler(Context ctx){
        // ctx.json(messageService.getAllMessages());
    }

    private void getMessageByIDHandler(Context ctx){
        ctx.json(messageService.getMessageByID(Integer.parseInt(ctx.pathParam("message_id"))));
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedMessage));
            ctx.status(200);
        }
    }
}