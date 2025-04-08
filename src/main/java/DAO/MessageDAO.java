package DAO;
import Model.*;
import Util.ConnectionUtil;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class MessageDAO 
{    
    /**
    * @return the inserted message
    */
    public Message insertMessage(Message message)
    {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String checkForAccount = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement accountCheckStatement = connection.prepareStatement(checkForAccount);
            accountCheckStatement.setInt(1, message.posted_by);
            ResultSet rs = accountCheckStatement.executeQuery();

            if(!rs.next()) return null;

            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.posted_by);
            preparedStatement.setString(2, message.message_text);
            preparedStatement.setLong(3, message.time_posted_epoch);

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getLong(1);
                return new Message(generated_message_id, message.posted_by, message.message_text, message.time_posted_epoch);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
    * @return the deleted message
    */
    public Message deleteMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        try {
            var message = getMessageByID(message_id);
            if(message == null) return null;

            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, message_id);
            preparedStatement.executeUpdate();

            return message;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
    * @return the updated message
    */
    public Message updateMessageByID(int message_id, String message_text)
    {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, message_text);
            preparedStatement.setInt(2, message_id);

            preparedStatement.executeUpdate();

            return getMessageByID(message_id);  //calculated again to account for the updated text
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
    * @return all messages.
    */
    public List<Message> getAllMessages()
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    /**
    * @return message by message_id
    */
    public Message getMessageByID(int message_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, message_id);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                System.out.println(message);
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
    * @return list of messages by account id
    */
    public List<Message> getAllMessagesByAccount(int account_id)
    {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next())
            {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}