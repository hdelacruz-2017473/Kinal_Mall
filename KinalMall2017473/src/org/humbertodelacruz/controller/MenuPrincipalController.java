
package org.humbertodelacruz.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.humbertodelacruz.system.Principal;

//LA INTERFAZ "INICIALIZABE" INICIA LOS CONTROLADORES DE CADA VISTA
public class MenuPrincipalController implements Initializable{
    
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {       
        
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaProgramador(){
        this.escenarioPrincipal.ventanaProgramador();
    }
    
    public void ventanaAdministracion(){
         this.escenarioPrincipal.ventanaAdministracion();
    }
    
    public void ventanaTipoCliente(){
        this.escenarioPrincipal.ventanaTipoCliente();
    }
    
    public void ventanaDepartamento(){
        this.escenarioPrincipal.ventanaDepartamento();
    }
    
    public void ventanaLocal(){
        this.escenarioPrincipal.ventanaLocal();
    }
    
    public void ventanaCliente(){
        this.escenarioPrincipal.ventanaCliente();
    }
    
    public void ventanaProveedor(){
        this.escenarioPrincipal.ventanaProveedor();
    }
    
    public void ventanaCuentaPorPagar(){
        this.escenarioPrincipal.ventanaCuentaPorPagar();
    }
    
    public void ventanaHorario(){
        this.escenarioPrincipal.ventanaHorario();
    }
    
    public void ventanaEmpleado(){
        this.escenarioPrincipal.ventanaEmpleado();
        
    }
    
    public void ventanaCargo(){
        this.escenarioPrincipal.ventanaCargo();
        
    }
    
    public void ventanaCuentaPorCobrar(){
        this.escenarioPrincipal.ventanaCuentaPorCobrar();
    }
    
    public void ventanaLogin(){
        this.escenarioPrincipal.ventanaLogin();
    }

}
