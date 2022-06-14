/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.humbertodelacruz.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.humbertodelacruz.bean.CuentaPorCobrar;
import org.humbertodelacruz.controller.AdministracionController;
import org.humbertodelacruz.controller.CargoController;
import org.humbertodelacruz.controller.ClienteController;
import org.humbertodelacruz.controller.CuentaPorCobrarController;
import org.humbertodelacruz.controller.CuentaPorPagarController;
import org.humbertodelacruz.controller.DepartamentoController;
import org.humbertodelacruz.controller.EmpleadoController;
import org.humbertodelacruz.controller.HorarioController;
import org.humbertodelacruz.controller.LocalController;
import org.humbertodelacruz.controller.LoginController;
import org.humbertodelacruz.controller.MenuPrincipalController;
import org.humbertodelacruz.controller.ProgramadorController;
import org.humbertodelacruz.controller.ProveedorController;
import org.humbertodelacruz.controller.TipoClienteController;
import org.humbertodelacruz.controller.UsuarioController;

/**
 * Programador: Humberto Alexander de la Cruz Chanchavac
 * IN5AM
 * 2017473
 *      Fecha Creación:
 *          05-05-2021
 *      Modificaciones:
 *          05-05-2021
 *          06-05-2021
 *          12-05-2021
 *          13-05-2021
 */


public class Principal extends Application {
    private  final String paqueteVistas = "/org/humbertodelacruz/view/"; //ruta de la todas las vistas
    private Stage escenarioPrincipal;
    private Scene escena;
    
    @Override
    public void start(Stage escenarioPrincipal) throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Kinal Mall 2021");
        
        //Parent root= FXMLLoader.load(getClass().getResource("/org/humbertodelacruz/view/MenuPrincipalView.fxml"));
        //Scene escena = new Scene(root);
        //escenarioPrincipal.setScene(escena);
        
        ventanaLogin();
        escenarioPrincipal.show();
   
    }
    
    public void menuPrincipal(){
        try {
            MenuPrincipalController menu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 969,534);
            menu.setEscenarioPrincipal(this);
                 
            
        } catch (Exception e) {
                e.printStackTrace();
        }       
    }
    
    public void ventanaProgramador(){
        try {
            ProgramadorController programador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",969,534);
            programador.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaAdministracion(){
        try {
            AdministracionController administracion = (AdministracionController)cambiarEscena("AdministracionView.fxml", 969, 534);
            administracion.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoCliente(){
        try {
            TipoClienteController tipoCliente = (TipoClienteController)cambiarEscena("TipoClienteView.fxml", 969, 534);
            tipoCliente.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaDepartamento(){
        try {
            DepartamentoController departamento = (DepartamentoController)cambiarEscena("DepartamentoView.fxml", 969, 534);
            departamento.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void ventanaLocal(){
        try {
            LocalController local = (LocalController)cambiarEscena("LocalView.fxml", 1200, 534);
            local.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaCliente(){
        try {
            ClienteController cliente = (ClienteController) cambiarEscena("ClienteView.fxml", 1200, 534);
            cliente.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaProveedor(){
        try {
            ProveedorController proveedor = (ProveedorController) cambiarEscena("ProveedorView.fxml", 1200, 534);
            proveedor.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaCuentaPorPagar(){
        try {
            CuentaPorPagarController cuentaPorPagar = (CuentaPorPagarController) cambiarEscena("CuentaPorPagarView.fxml", 1200, 534);
            cuentaPorPagar.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaHorario(){
        try {
            HorarioController horario = (HorarioController) cambiarEscena("HorarioView.fxml", 1200, 534);
            horario.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void ventanaEmpleado(){
        try {
            EmpleadoController empleado = (EmpleadoController) cambiarEscena("EmpleadoView.fxml", 1200, 534);
            empleado.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaCargo(){
        try {
            CargoController cargo = (CargoController) cambiarEscena("CargoView.fxml", 969, 534);
            cargo.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void ventanaCuentaPorCobrar(){
        try {
            CuentaPorCobrarController cuentaPorCobrar = (CuentaPorCobrarController) cambiarEscena("CuentaPorCobrarView.fxml",1200, 534);
            cuentaPorCobrar.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void ventanaUsuario(){
        try {
            UsuarioController usuario = (UsuarioController) cambiarEscena("UsuarioView.fxml",969, 534);
            usuario.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try {
            LoginController login = (LoginController) cambiarEscena("LoginView.fxml", 997, 481);
            login.setEscenarioPrincipal(this);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws IOException{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new  FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(paqueteVistas + fxml); //Lectura (getResourceAsStream lee el archivo de la ruta que le damos)
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(paqueteVistas + fxml)); //Localizar (cargadorFXML necesita saber la localizacion la ruta)
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo), ancho, alto);
        
        escenarioPrincipal.setScene(escena);
        
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }

    
    
    
}
