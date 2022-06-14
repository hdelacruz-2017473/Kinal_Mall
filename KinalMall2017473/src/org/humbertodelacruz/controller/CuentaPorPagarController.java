
package org.humbertodelacruz.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.humbertodelacruz.bean.Administracion;
import org.humbertodelacruz.bean.CuentaPorPagar;
import org.humbertodelacruz.bean.Proveedor;
import org.humbertodelacruz.db.Conexion;
import org.humbertodelacruz.system.Principal;


public class CuentaPorPagarController implements Initializable{

    private Principal escenarioPrincipal;
    private enum operaciones {NINGUNO, GUARDAR, ACTUALIZAR};
    private operaciones tipoDeOperaciones = operaciones.NINGUNO;
    private ObservableList<CuentaPorPagar> listaCuentaPorPagar;
    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Proveedor> listaProveedor;
    private DatePicker fechaLimite;
    
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private  ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoCuentasPorPagar;
    @FXML private TextField txtNumeroFactura;
    @FXML private TextField txtEstadoPago;
    @FXML private TextField txtValorNetoPago;
    @FXML private ComboBox cmbCodigoAdministracion;
    @FXML private  ComboBox cmbCodigoProveedor;
    @FXML private GridPane grpFechaLimitePago;
    @FXML private TableView tblCuentaPorPagar;
    @FXML private TableColumn colCodigoCuentasPorPagar;
    @FXML private TableColumn colNumeroFactura;
    @FXML private TableColumn colFechaLimitePago;
    @FXML private TableColumn colEstadoPago;
    @FXML private TableColumn colValorNetoPago;
    @FXML private TableColumn colCodigoAdministracion;
    @FXML private TableColumn colCodigoProveedor;
    
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        //CREAMOS EL OBJETO "DATE PICKER" EN IDIOMA INGLES
        fechaLimite = new DatePicker(Locale.ENGLISH);
        //PONEMOS EN FORMATO INGLES EL CALENDARIO
        fechaLimite.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        //AQUÍ ESTABLECE LA FECHA EN LA QUE SE EJECUTA
        fechaLimite.getCalendarView().todayButtonTextProperty().set("Today");
        //AQUÍ OCULTAMOS LAS SEMANAS(ES OPCIONAL)
        fechaLimite.getCalendarView().setShowWeeks(false);
        //AQUÍ ASIGNAMOS EL DATEPICKER EN LA VISTA
        grpFechaLimitePago.add(fechaLimite, 5, 0);
        //LE ASIGANMOS UN ESTILO CSS AL OBJETO(DATE PICKER)
        fechaLimite.getStylesheets().add("/org/humbertodelacruz/resource/DatePicker.css");
        desactivarControles();
    }
    
    public void cargarDatos(){
        tblCuentaPorPagar.setItems(getCuentasPorPagar());
        colCodigoCuentasPorPagar.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Integer>("codigoCuentasPorPagar"));
        colNumeroFactura.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, String>("numeroFactura"));
        colFechaLimitePago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Date>("fechaLimitePago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, String>("estadoPago"));
        colValorNetoPago.setCellValueFactory(new PropertyValueFactory<CuentaPorPagar, Double>("valorNetoPago"));
        colCodigoAdministracion.setCellValueFactory(new PropertyValueFactory<Administracion, Integer>("codigoAdministracion"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<Proveedor, Integer>("codigoProveedor"));
        cmbCodigoAdministracion.setItems(getAdministracion());
        cmbCodigoProveedor.setItems(getProveedores());
        
        
        
    }
    
    public ObservableList<CuentaPorPagar> getCuentasPorPagar (){
        ArrayList<CuentaPorPagar> lista = new ArrayList<CuentaPorPagar>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarCuentasPorPagar()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new CuentaPorPagar(resultado.getInt("codigoCuentasPorPagar"), 
                                            resultado.getString("numeroFactura"), 
                                            resultado.getDate("fechaLimitePago"), 
                                            resultado.getString("estadoPago"), 
                                            resultado.getDouble("valorNetoPago"), 
                                            resultado.getInt("codigoAdministracion"), 
                                            resultado.getInt("codigoProveedor")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaCuentaPorPagar = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList <Administracion> getAdministracion(){
            ArrayList<Administracion> lista=new ArrayList<Administracion>();
            try {
                PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarAdministracion()}"); //prepareCall hcae la llamda del elemnto de Mysql (procedimiento almacenado)
                ResultSet resultado = procedimiento.executeQuery();
                //resultado.next = true - ese es su valor por defecto
                while (resultado.next()) {
                    lista.add(new Administracion(resultado.getInt("codigoAdministracion"),
                                                    resultado.getString("direccion"),
                                                    resultado.getString("telefono")));
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Aquí se realizó el casteo, lo que estaba alamcenadao en ArrayList ahora esta en el ObservableList
            return listaAdministracion = FXCollections.observableArrayList(lista);
        }
    
    
    public ObservableList<Proveedor> getProveedores(){
        ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProveedores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Proveedor(resultado.getInt("codigoProveedor"), 
                                        resultado.getString("NITProveedor"),   
                                        resultado.getString("servicioPrestado"), 
                                        resultado.getString("telefonoProveedor"), 
                                        resultado.getString("direccionProveedor"), 
                                        resultado.getDouble("saldoFavor"), 
                                        resultado.getDouble("saldoContra"), 
                                        resultado.getInt("codigoAdministracion")));
            }
                      
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listaProveedor = FXCollections.observableArrayList(lista);
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
    
/* *****************************************************************************/
    
    
    public void activarControles(){
        txtCodigoCuentasPorPagar.setEditable(false);
        txtNumeroFactura.setEditable(true);
        fechaLimite.setDisable(false);
        txtEstadoPago.setEditable(true);
        txtValorNetoPago.setEditable(true);
        cmbCodigoAdministracion.setDisable(false);
        cmbCodigoProveedor.setDisable(false);
    }
    
        public void activarControles2(){
        txtCodigoCuentasPorPagar.setEditable(false);
        txtNumeroFactura.setEditable(true);
        fechaLimite.setDisable(false);
        txtEstadoPago.setEditable(true);
        txtValorNetoPago.setEditable(true);
        cmbCodigoAdministracion.setDisable(true);
        cmbCodigoProveedor.setDisable(true);
    }
    
    public void desactivarControles(){
        txtCodigoCuentasPorPagar.setEditable(false);
        txtNumeroFactura.setEditable(false);
        fechaLimite.setDisable(true);
        txtEstadoPago.setEditable(false);
        txtValorNetoPago.setEditable(false);
        cmbCodigoAdministracion.setDisable(true);
        cmbCodigoProveedor.setDisable(true);
        
    }
    
    public void limpiarControles(){
        txtCodigoCuentasPorPagar.clear();
        txtNumeroFactura.clear();
        fechaLimite.setPromptText("");
        txtEstadoPago.clear();
        txtValorNetoPago.clear();
        cmbCodigoAdministracion.setValue(null);
        cmbCodigoProveedor.setValue(null);
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
        CuentaPorPagar registro= new CuentaPorPagar();
        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(fechaLimite.getSelectedDate());
        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
        registro.setCodigoProveedor(((Proveedor)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarCuentaPorPagar(?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNumeroFactura());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaLimitePago().getTime()));
            procedimiento.setString(3, registro.getEstadoPago());
            procedimiento.setDouble(4, registro.getValorNetoPago());
            procedimiento.setInt(5, registro.getCodigoAdministracion());
            procedimiento.setInt(6, registro.getCodigoProveedor());
            
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
                if (tblCuentaPorPagar.getSelectionModel().getSelectedItem() != null) {
                    int resultado = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar el elemento?", "Elminar Cuenta Por Pagar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resultado == JOptionPane.YES_OPTION) {

                        try {
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarCuentaPorPagar(?)}");
                            procedimiento.setInt(1, ((CuentaPorPagar) tblCuentaPorPagar.getSelectionModel().getSelectedItem()).getCodigoCuentasPorPagar());

                            procedimiento.execute();
                            cargarDatos();
                            limpiarControles();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }else {
                    JOptionPane.showMessageDialog(null, "Debe selecionar un elemento");
                }
                break;
        }
    }

    
/* *****************************************************************************/
    
    public void seleccionarElemento(){
        if(tblCuentaPorPagar.getSelectionModel().getSelectedItem() != null){
            txtCodigoCuentasPorPagar.setText(String.valueOf(((CuentaPorPagar)tblCuentaPorPagar.getSelectionModel().getSelectedItem()).getCodigoCuentasPorPagar()));
            txtNumeroFactura.setText(((CuentaPorPagar)tblCuentaPorPagar.getSelectionModel().getSelectedItem()).getNumeroFactura());
            fechaLimite.selectedDateProperty().set(((CuentaPorPagar)tblCuentaPorPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago());
            txtEstadoPago.setText(((CuentaPorPagar)tblCuentaPorPagar.getSelectionModel().getSelectedItem()).getEstadoPago());
            txtValorNetoPago.setText(String.valueOf(((CuentaPorPagar)tblCuentaPorPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            cmbCodigoAdministracion.getSelectionModel().select(buscarAdministracion(((CuentaPorPagar)tblCuentaPorPagar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbCodigoProveedor.getSelectionModel().select(buscarProveedor(((CuentaPorPagar)tblCuentaPorPagar.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        }else{
            JOptionPane.showMessageDialog(null,  "Debe selecionar un elemento");
        }
        
    }
    
    public Administracion buscarAdministracion(int codigoAdministracion){
        Administracion resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarAdministracion(?)}");
            procedimiento.setInt(1, codigoAdministracion);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Administracion(registro.getInt("codigoAdministracion"),
                                                registro.getString("direccion"), 
                                                registro.getString("telefono"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado; 
    }
    
    
    public Proveedor buscarProveedor(int codigoProveedor){
        Proveedor resultado = null;
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProveedor(?)}");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Proveedor(registro.getInt("codigoProveedor"), 
                                            registro.getString("NITProveedor"), 
                                            registro.getString("servicioPrestado"), 
                                            registro.getString("telefonoProveedor"), 
                                            registro.getString("direccionProveedor"), 
                                            registro.getDouble("saldoFavor"), 
                                            registro.getDouble("SaldoContra"), 
                                            registro.getInt("codigoAdministracion"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }
    
/* *****************************************************************************/
    
    public void editar(){
        switch(tipoDeOperaciones){
            case NINGUNO:
                if(tblCuentaPorPagar.getSelectionModel().getSelectedItem() != null){
                    activarControles2();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    imgEditar.setImage(new Image("/org/humbertodelacruz/images/actualizar.png"));
                    imgReporte.setImage(new Image("/org/humbertodelacruz/images/cancelar.png"));
                    tipoDeOperaciones = operaciones.ACTUALIZAR;
                    
                }else{
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                }
                break;
                
                
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
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
        CuentaPorPagar registro = (CuentaPorPagar)tblCuentaPorPagar.getSelectionModel().getSelectedItem();
        registro.setNumeroFactura(txtNumeroFactura.getText());
        registro.setFechaLimitePago(fechaLimite.getSelectedDate());
        registro.setEstadoPago(txtEstadoPago.getText());
        registro.setValorNetoPago(Double.parseDouble(txtValorNetoPago.getText()));
//        registro.setCodigoAdministracion(((Administracion)cmbCodigoAdministracion.getSelectionModel().getSelectedItem()).getCodigoAdministracion());
//        registro.setCodigoProveedor(((Proveedor)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarCuentaPorPagar(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCuentasPorPagar());
            procedimiento.setString(2, registro.getNumeroFactura());
            procedimiento.setDate(3, new java.sql.Date(registro.getFechaLimitePago().getTime()));
            procedimiento.setString(4, registro.getEstadoPago());
            procedimiento.setDouble(5, registro.getValorNetoPago());
            
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
                limpiarControles();
                desactivarControles();
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
    
}
