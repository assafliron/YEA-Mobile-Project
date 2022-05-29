/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ErrorReporter {

    public static void addError(String err) {
        FacesContext context = FacesContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage(err);
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        context.addMessage(null, msg);
    }
}
