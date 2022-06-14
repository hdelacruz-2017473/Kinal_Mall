
package org.humbertodelacruz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.humbertodelacruz.bean.Usuario;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.system.Principal;


public class UsuarioController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO,GUARDAR, ELIMINAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<Usuario> listaUsuarios;
    
    
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuarioLogin;
    @FXML private TextField txtPassword;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
   
    }
    
    

    
 /* *****************************************************************************/    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    
 /* *****************************************************************************/    
    
    public void activarControles(){
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuarioLogin.setEditable(true);
        txtPassword.setEditable(true);
    }
    
    
    public void desactivarControles(){
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuarioLogin.setEditable(false);
        txtPassword.setEditable(false);
    }
    
    public void limpiarControles(){
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuarioLogin.clear();
        txtPassword.clear();
    }
    
    
 /* *****************************************************************************/ 
    
    
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/guardar.png"));
                tipoDeOperaciones = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                   
        }
    }
    
    
    public void guardar(){
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setApellidoUsuario(txtApellidoUsuario.getText());
        registro.setUsuarioLogin(txtUsuarioLogin.getText());
        registro.setContrasena(txtPassword.getText());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getApellidoUsuario());
            procedimiento.setString(3, registro.getUsuarioLogin());
            procedimiento.setString(4, registro.getContrasena());
            
            procedimiento.execute();
            ventanaLogin();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public void ventanaLogin(){
        this.escenarioPrincipal.ventanaLogin();
    }
/******************************************************************************/ 
    
    public void eliminar(){
        
        switch(tipoDeOperaciones){
                
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
               
                break;
        }
    }
    
    
}
