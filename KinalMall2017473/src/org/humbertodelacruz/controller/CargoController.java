
package org.humbertodelacruz.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.humbertodelacruz.bean.Cargo;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.system.Principal;


public class CargoController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList <Cargo> listaCargos;
    
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private  ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblCargo;
    @FXML private TableColumn colCodigoCargo;
    @FXML private TableColumn colNombreCargo;
    @FXML private TextField txtCodigoCargo;
    @FXML private TextField txtNombreCargo;
 
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();

    }
    
    public void cargarDatos(){
        tblCargo.setItems(getCargos());
        colCodigoCargo.setCellValueFactory(new PropertyValueFactory<Cargo, Integer> ("codigoCargo"));
        colNombreCargo.setCellValueFactory(new PropertyValueFactory<Cargo, String> ("nombreCargo"));
        
    }
    
    public ObservableList <Cargo> getCargos(){
        ArrayList <Cargo> lista = new ArrayList<Cargo>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCargos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cargo(resultado.getInt("codigoCargo"), 
                                    resultado.getString("nombreCargo")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCargos = FXCollections.observableArrayList(lista);
    }
    
       
    
/* *****************************************************************************/

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        this.escenarioPrincipal.menuPrincipal();
    }
    
    public void ventanaEmpleado(){
        this.escenarioPrincipal.ventanaEmpleado();
    }

/* *****************************************************************************/    
    
    public void activarControles(){
        txtCodigoCargo.setEditable(false);
        txtNombreCargo.setEditable(true);
    }
    
    
    public void desactivarControles(){
        txtCodigoCargo.setEditable(false);
        txtNombreCargo.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCargo.clear();
        txtNombreCargo.clear();
        
    }
    
/* *****************************************************************************/   
    
    
        public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/guardar.png"));
                imgEliminar.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                tipoDeOperaciones = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
    
    
    
    
    public void guardar(){
        Cargo registro = new Cargo();
        registro.setNombreCargo(txtNombreCargo.getText());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCargo(?)}");
            procedimiento.setString(1, registro.getNombreCargo());
            
            procedimiento.execute();
            cargarDatos();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
/* *****************************************************************************/
    
    
        public void eliminar(){
        switch(tipoDeOperaciones){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
            default:
                if(tblCargo.getSelectionModel().getSelectedItem() != null){
                    int resultado = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el Elemento?", "Elminar Cargo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resultado == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCargo(?)}");
                            procedimiento.setInt(1, ((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargo());
                            
                            procedimiento.execute();
                            cargarDatos();
                            limpiarControles();
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        } 
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un Elemento");
                }
            break;
        }        
    }
        
/* *****************************************************************************/
        
    public void seleccionarElemento(){
        if(tblCargo.getSelectionModel().getSelectedItem() != null){
            txtCodigoCargo.setText(String.valueOf(((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getCodigoCargo()));
            txtNombreCargo.setText(((Cargo)tblCargo.getSelectionModel().getSelectedItem()).getNombreCargo());
            
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }    
/* *****************************************************************************/        
        
        
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblCargo.getSelectionModel().getSelectedItem()!= null){
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar el elemento");
                }
            break;
            
            
            case ACTUALIZAR:
                actualizar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
            break;
        }
    }
    
    
    public void actualizar(){
        Cargo registro = ((Cargo)tblCargo.getSelectionModel().getSelectedItem());
        registro.setNombreCargo(txtNombreCargo.getText());
        
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCargo(?,?)}");
            procedimiento.setInt(1, registro.getCodigoCargo());
            procedimiento.setString(2, registro.getNombreCargo());
            
            
            procedimiento.execute();
            cargarDatos();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
/* *****************************************************************************/    
        public void reporte(){
           switch(tipoDeOperaciones){
               case ACTUALIZAR:
                   desactivarControles();
                   limpiarControles();
                   btnEditar.setText("Editar");
                   btnReporte.setText("Reporte");
                   imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                   imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                   btnNuevo.setDisable(false);
                   btnEliminar.setDisable(false);
                   tipoDeOperaciones = operaciones.NINGUNO;
                   break;
                   
           }
        }   
}
