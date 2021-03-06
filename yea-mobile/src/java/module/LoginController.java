package module;

import Utils.ErrorReporter;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@Named(value = "loginController")
@ManagedBean
@RequestScoped
public class LoginController {

    private String username;
    private String password;
    
    private final static String LOGGED_USER = "loggedUser";
    
    public String login() {
        SiteUser user = SiteUser.find(username, password);
        FacesContext context = FacesContext.getCurrentInstance();

        if (user == null) {
            ErrorReporter.addError("Unknown login, try again");
            username = null;
            password = null;
            return null;
        } else if (!user.isActive()) {
            ErrorReporter.addError("User is no longer active");
            return null;
        } else {
            
            context.getExternalContext().getSessionMap().put(LOGGED_USER, user);
            return "index?faces-redirect=true";
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index?faces-redirect=true";
    }

    public static boolean isUserLoggedIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getExternalContext().getSessionMap().containsKey(LOGGED_USER);
    }
    
    public static boolean isManagerLoggedIn() {
        FacesContext context = FacesContext.getCurrentInstance();
        return isUserLoggedIn() && 
                ((SiteUser)(context.getExternalContext().getSessionMap().get(LOGGED_USER))).isManager();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
