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
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete(("/messages/{message_id}"), this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByUserHandler);
        return app;
    }
    private void postAccountHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);

        if(addedAccount==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        }
    }
    private void postLoginHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loggedIn = accountService.loginAccount(account);

        if(loggedIn==null){
            ctx.status(401);
        }else{
            ctx.json(mapper.writeValueAsString(loggedIn));
            ctx.status(200);
        }
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        var deletedMessage = messageService.deleteMessageByID(Integer.parseInt(ctx.pathParam("message_id")));
        if(deletedMessage == null){
            ctx.status(200);
        }else{
            ctx.json(mapper.writeValueAsString(deletedMessage));
            ctx.status(200);
        }
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
        ctx.json(messageService.getAllMessagesByUser(Integer.parseInt(ctx.pathParam("account_id"))));
    }

    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message foundMessage = messageService.getMessageByID(Integer.parseInt(ctx.pathParam("message_id")));
        if(foundMessage == null){
            ctx.status(200);
        }else{
            ctx.json(mapper.writeValueAsString(foundMessage));
        }
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