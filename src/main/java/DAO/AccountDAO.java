package DAO;
import Util.ConnectionUtil;
import java.sql.*;
import Model.Account;

public class AccountDAO 
{
    public Account insertAccount(Account account)
    {
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            String checkForAccount = "SELECT * FROM account WHERE username = ?";
            PreparedStatement accountCheckStatement = connection.prepareStatement(checkForAccount);
            accountCheckStatement.setString(1, account.username);
            ResultSet rs = accountCheckStatement.executeQuery();
            if(rs.next()) return null;

            String sql = "INSERT INTO account (username, password) VALUES(?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, account.username);
            preparedStatement.setString(2, account.password);

            preparedStatement.executeUpdate();

            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_account_id, account.username, account.password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            String checkForAccount = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement accountCheckStatement = connection.prepareStatement(checkForAccount);
            accountCheckStatement.setString(1, account.username);
            accountCheckStatement.setString(2, account.password);
            ResultSet rs = accountCheckStatement.executeQuery();

            if(rs.next())
            {
                int generated_account_id = (int) rs.getLong(1);
                return new Account(generated_account_id, account.username, account.password);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
}
