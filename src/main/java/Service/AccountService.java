package Service;
import DAO.AccountDAO;
import Model.Account;

public class AccountService 
{
    AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    
    public Account addAccount(Account account){
        if(account.username.isBlank()) return null;
        if(account.password.length() < 4) return null;
        return accountDAO.insertAccount(account);
    }

    public Account loginAccount(Account account){
        return accountDAO.getAccount(account);
    }
}
