package Service;
import DAO.MessageDAO;
import Model.Message;
import java.util.List;

public class MessageService {

    MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        if(message.message_text.length() > 255 || message.message_text.isBlank()) return null;
        return messageDAO.insertMessage(message);
    }

    public Message updateMessage(int message_id, Message message){
        if(message.message_text.length() > 255 || message.message_text.isBlank()) return null;
        if(messageDAO.getMessageByID(message_id) == null) return null;

        messageDAO.updateMessageByID(message_id, message.message_text);
        return(messageDAO.getMessageByID(message_id));
    }

    public Message deleteMessageByID(int message_id){
        Message messageToDelete = messageDAO.getMessageByID(message_id);
        if(messageToDelete == null) return null;

        messageDAO.deleteMessageByID(message_id);
        return(messageToDelete);
    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesByUser(int account_id){
        return messageDAO.getAllMessagesByAccount(account_id);
    }

    public Message getMessageByID(int message_id){
        return messageDAO.getMessageByID(message_id);
    }
}
