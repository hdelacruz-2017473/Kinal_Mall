
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
import org.humbertodelacruz.bean.TipoCliente;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.system.Principal;

public class TipoClienteController implements Initializable{

    
    private Principal escenarioPrincipal;
    private ObservableList <TipoCliente> listaTipoCliente;
    private enum operaciones {NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblTipoCliente;
    @FXML private TableColumn colCodigoTipoCliente;
    @FXML private TableColumn colDescripcion;
    @FXML private TextField txtCodigoTipoCliente;
    @FXML private TextField txtDescripcion;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CargarDatos();
    }
    
    public void CargarDatos(){
        tblTipoCliente.setItems(getTipoCliente());
        colCodigoTipoCliente.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("codigoTipoCliente"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoCliente, String>("descripcion"));
    }
    
    public ObservableList<TipoCliente> getTipoCliente(){
        ArrayList<TipoCliente> lista = new ArrayList<TipoCliente>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoClientes()}");
            ResultSet resultado = procedimiento.executeQuery(); //ALMACENA Y EJECUTA
            while(resultado.next()){
                lista.add(new TipoCliente(resultado.getInt("codigoTipoCliente"),
                                            resultado.getString("descripcion")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoCliente = FXCollections.observableArrayList(lista);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        this.escenarioPrincipal.menuPrincipal();
    } 
    
    public void ventanaCliente(){
        this.escenarioPrincipal.ventanaCliente();
    }
    
    
   /* *****************************************************************************/
        public void activarControles(){
        txtCodigoTipoCliente.setEditable(false);
        txtDescripcion.setEditable(true);
    }  
        
        public void desactivarControles(){
            txtCodigoTipoCliente.setEditable(false);
            txtDescripcion.setEditable(false);
        }
        
        public void limpiarControles(){
            txtCodigoTipoCliente.clear();
            txtDescripcion.clear();
            
        }

 /* *****************************************************************************/

        
        public void Nuevo(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    activarControles();
                    limpiarControles();
                    btnNuevo.setText("Guardar");
                    btnEliminar.setText("Cancelar");
                    btnEditar.setDisable(true);
                    btnReporte.setDisable(true);
                    imgNuevo.setImage(new Image("/org/humbertodelacruz/images/guardar.png"));
                    imgEliminar.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                    tipoDeOperacion = operaciones.GUARDAR;
                    break;
                
                case GUARDAR:
                    guardar();
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                    imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
            }
            
        }
        
        public void guardar(){
            TipoCliente registro = new TipoCliente();
            registro.setDescripcion(txtDescripcion.getText());
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoCliente(?)}");
                procedimiento.setString(1, registro.getDescripcion());
                procedimiento.execute();
                //TODO LO QUE HICIMOS ANTERIORMENTE PASA EN MYSQL, ENTONCES CON ESTE CODIGO SUCEDE TAMBIEN EN LA TABLA
                //listaTipoCliente.add(registro);
                CargarDatos();
               
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
 /* *****************************************************************************/
        
        public void eliminar(){
            switch(tipoDeOperacion){
                case GUARDAR:
                    desactivarControles();
                    limpiarControles();
                    btnNuevo.setText("Nuevo");
                    btnEliminar.setText("Eliminar");
                    btnEditar.setDisable(false);
                    btnReporte.setDisable(false);
                    imgNuevo.setImage(new Image("/org/humbertodelacruz/images/nuevo.png"));
                    imgEliminar.setImage(new Image("/org/humbertodelacruz/images/eliminar.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
                    
                default:
                    if(tblTipoCliente.getSelectionModel().getSelectedItem() != null){
                        int respuesta =JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este elemento?", "Eliminar Tipo Cliente",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if(respuesta == JOptionPane.YES_OPTION){
                            try {
                                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoCliente(?)}");
                                //Establecer el parámetro
                                procedimiento.setInt(1, ((TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente());
                                procedimiento.execute();
                                //TODO LO QUE HICIMOS ANTERIORMENTE PASA EN MYSQL, ENTONCES CON ESTE CODIGO SUCEDE TAMBIEN EN LA TABLA
                                listaTipoCliente.remove(tblTipoCliente.getSelectionModel().getSelectedItem());
                                //CargarDatos();
                                limpiarControles();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleciooanr un elemento");
                    }
                    break;
            }
        }
        
        public void seleccionarElementos(){
            if(tblTipoCliente.getSelectionModel().getSelectedItem() != null){
                txtCodigoTipoCliente.setText(String.valueOf(((TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
                txtDescripcion.setText(((TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
            }else{
                JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
            }
        }
        
/* *****************************************************************************/

        
        public void editar(){
            switch(tipoDeOperacion){
                case NINGUNO:
                    if(tblTipoCliente.getSelectionModel().getSelectedItem() !=null){
                        activarControles();
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        imgEditar.setImage(new Image("/org/humbertodelacruz/images/actualizar.png"));
                        imgReporte.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else{
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                    }
                    break;
                
                case ACTUALIZAR:
                    actualizar();
                    desactivarControles();
                    limpiarControles();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
     
            }
        }
        
        public void actualizar(){
            try {
                PreparedStatement procedimiento =Conexion.getInstance().getConexion().prepareCall("{call sp_EditarTipoCliente(?,?)}");
                TipoCliente registro = (TipoCliente)tblTipoCliente.getSelectionModel().getSelectedItem();
                registro.setDescripcion(txtDescripcion.getText());
                procedimiento.setInt(1, registro.getCodigoTipoCliente());
                procedimiento.setString(2, registro.getDescripcion());
                procedimiento.execute();
                CargarDatos();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

/* *****************************************************************************/

        
        public void reporte(){
            switch(tipoDeOperacion){
                case ACTUALIZAR:
                    limpiarControles();
                    desactivarControles();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                    tipoDeOperacion = operaciones.NINGUNO;
                    break;
            }
        }
      
        
}






