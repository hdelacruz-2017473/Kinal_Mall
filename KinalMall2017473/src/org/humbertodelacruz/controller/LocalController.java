
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
import org.humbertodelacruz.bean.Locales;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.system.Principal;


public class LocalController implements Initializable{

    private Principal escenarioPrincipal;
    private ObservableList <Locales> listaLocales;
    private enum operaciones{NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblLocal;
    @FXML private TableColumn colCodigoLocal;
    @FXML private TableColumn colSaldoFavor;
    @FXML private TableColumn colSaldoContra;
    @FXML private TableColumn colMesesPendientes;
    @FXML private TableColumn colDisponibilidad;
    @FXML private TableColumn colValorLocal;
    @FXML private TableColumn colValorAdministracion;
    @FXML private TextField txtCodigoLocal;
    @FXML private TextField txtSaldoFavor;
    @FXML private TextField txtSaldoContra;
    @FXML private TextField txtMesesPendientes;
    @FXML private TextField txtDisponibilidad;
    @FXML private TextField txtValorLocal;
    @FXML private TextField txtValorAdministracion;
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();;
    }
    
    public void cargarDatos(){
        tblLocal.setItems(getLocales());
        colCodigoLocal.setCellValueFactory(new PropertyValueFactory< Locales, Integer>("codigoLocal"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, Double>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, Double>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory( new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, Double>("valorLocal"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, Double>("valorAdministracion"));
        
        
    }
    
    public ObservableList<Locales> getLocales(){
        ArrayList<Locales> lista = new ArrayList<Locales>();
        try {
            PreparedStatement Procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarLocales()}");
            ResultSet resultado = Procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Locales(resultado.getInt("codigoLocal"), 
                        resultado.getDouble("saldoFavor"), 
                        resultado.getDouble("saldoContra"), 
                        resultado.getInt("mesesPendientes"), resultado.getBoolean("disponibilidad"), 
                        resultado.getDouble("valorLocal"), 
                        resultado.getDouble("valorAdministracion")));
            }

        } catch (Exception e) {
            e.printStackTrace();;
        }
        return  listaLocales = FXCollections.observableArrayList(lista);
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
    
    public void ventanaCuentaPorCobrar(){
        this.escenarioPrincipal.ventanaCuentaPorCobrar();
    }
    
/* *****************************************************************************/

    public void activarControles(){
        txtCodigoLocal.setEditable(false);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtMesesPendientes.setEditable(true);
        txtDisponibilidad.setEditable(true);
        txtValorLocal.setEditable(true);
        txtValorAdministracion.setEditable(true);
    }
    
    
    public void desactivarControles(){
        txtCodigoLocal.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtMesesPendientes.setEditable(false);
        txtDisponibilidad.setEditable(false);
        txtValorLocal.setEditable(false);
        txtValorAdministracion.setEditable(false);
    }
    
    public void limpiarControles(){
        txtCodigoLocal.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtMesesPendientes.clear();
        txtDisponibilidad.clear();
        txtValorLocal.clear();
        txtValorAdministracion.clear();
    }
    
/* *****************************************************************************/
    
    
    public void nuevo(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                limpiarControles();
                activarControles();
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
        //Creo una nueva instancia osea un nuevo objeto para almacenar el nuevo resgitrp
        Locales registro = new Locales();
        //Le seteo a cada atributo(columna) el valor que esta en los TextField
        registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
        registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
        registro.setDisponibilidad(Boolean.parseBoolean(txtDisponibilidad.getText()));
        registro.setValorLocal(Double.parseDouble(txtValorLocal.getText()));
        registro.setValorAdministracion(Double.parseDouble(txtValorAdministracion.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarLocal(?,?,?,?,?,?)}");
            //Le agrego a cada atributo su valor, esto sucede en MYSQL
            procedimiento.setDouble(1, registro.getSaldoFavor());
            procedimiento.setDouble(2, registro.getSaldoContra());
            procedimiento.setInt(3, registro.getMesesPendientes());
            procedimiento.setBoolean(4, registro.isDisponibilidad());
            procedimiento.setDouble(5, registro.getValorLocal());
            procedimiento.setDouble(6, registro.getValorAdministracion());
            procedimiento.execute();
            //Le agreo a cada atributo su valor, esto sucede en la Vista
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
                if(tblLocal.getSelectionModel().getSelectedItem() != null){
                    int resultado = JOptionPane.showConfirmDialog(null, "¿Está seguro de eleminar el elemento?", "Elimnar Local", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(resultado == JOptionPane.YES_OPTION){
                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarLocal(?)}");
                            procedimiento.setInt(1, ((Locales)tblLocal.getSelectionModel().getSelectedItem()).getCodigoLocal());
                            procedimiento.execute();
                            cargarDatos();
                            limpiarControles();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }                        
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
                
        }
    }
    
/* *****************************************************************************/
    
    public void seleccionarElemento(){
        if(tblLocal.getSelectionModel().getSelectedItem() != null){
            txtCodigoLocal.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getCodigoLocal()));
            txtSaldoFavor.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            txtSaldoContra.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getSaldoContra()));
            txtMesesPendientes.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getMesesPendientes()));
            txtDisponibilidad.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).isDisponibilidad()));
            txtValorLocal.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getValorLocal()));
            txtValorAdministracion.setText(String.valueOf(((Locales)tblLocal.getSelectionModel().getSelectedItem()).getValorAdministracion()));
        }else{
            JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
        }
    }
    
/* *****************************************************************************/
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblLocal.getSelectionModel().getSelectedItem() != null){
                    activarControles();
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccioanr un elemento");
                }break;
            
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
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
                
        }
    }
    
    public void actualizar(){
        //alamceno el registro seleccionado en una variable
        Locales registro = (Locales)tblLocal.getSelectionModel().getSelectedItem();
        //le seteo a cada atributo el nuevo valor, que agregamos en los TextField
        registro.setSaldoFavor(Double.parseDouble(txtSaldoFavor.getText()));
        registro.setSaldoContra(Double.parseDouble(txtSaldoContra.getText()));
        registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
        registro.setDisponibilidad(Boolean.parseBoolean(txtDisponibilidad.getText()));
        registro.setValorLocal(Double.parseDouble(txtValorLocal.getText()));
        registro.setValorAdministracion(Double.parseDouble(txtValorAdministracion.getText()));
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarLocal(?,?,?,?,?,?,?)}");
            //Le agreo a cada atributo su nuevo valor, esto sucede en MYSQL
            procedimiento.setInt(1, registro.getCodigoLocal());
            procedimiento.setDouble(2, registro.getSaldoFavor());
            procedimiento.setDouble(3, registro.getSaldoContra());
            procedimiento.setInt(4, registro.getMesesPendientes());
            procedimiento.setBoolean(5, registro.isDisponibilidad());
            procedimiento.setDouble(6, registro.getValorLocal());
            procedimiento.setDouble(7, registro.getValorAdministracion());
            procedimiento.execute();
            //Le agreo a cada atributo su nuevo valor, esto sucede en la Vista
            cargarDatos();;
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
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/humbertodelacruz/images/editar.png"));
                imgReporte.setImage(new Image("/org/humbertodelacruz/images/reporte.png"));
                tipoDeOperaciones = operaciones.NINGUNO;
                break;
        }
    }
}




